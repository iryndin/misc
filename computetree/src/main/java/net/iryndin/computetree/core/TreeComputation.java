package net.iryndin.computetree.core;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.iryndin.computetree.api.INode;
import net.iryndin.computetree.api.INodeRegistry;
import net.iryndin.computetree.api.INodeResult;
import net.iryndin.computetree.api.ITreeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author iryndin
 * @since 20/01/17
 */
public class TreeComputation implements AutoCloseable {

    final static Logger log = LoggerFactory.getLogger(TreeComputation.class);

    private final INodeRegistry registry;
    private final ExecutorService executorService;

    public TreeComputation(INodeRegistry registry) {
        this(registry, Executors.newCachedThreadPool());
    }

    public TreeComputation(INodeRegistry registry, ExecutorService executorService) {
        this.registry = registry;
        this.executorService = executorService;
    }

    @Override
    public void close() throws Exception {
        log.info("Closing TreeComputation...");
        executorService.shutdown();
        log.info("TreeComputation closed");
    }

    public Optional<ITreeResult> compute(String start) throws Exception {
        log.info("Start tree computation, start node ID: '{}'", start);
        // 1. get configured and created node by node ID
        Optional<INode> node = registry.getNode(start);
        if (node.isPresent()) {
            // 2. Create empty TreeResult
            ITreeResult treeResult = new TreeResultImpl();
            log.debug("Start node: {}", node.get());
            // 3. Start tree computation
            computeRecursive(Collections.singletonList(node.get()), treeResult);
            return Optional.of(treeResult);
        } else {
            log.info("Cannot find node with ID: '{}'", start);
        }
        return Optional.absent();
    }

    private void computeRecursive(List<INode> nodes, ITreeResult treeResult) throws ExecutionException, InterruptedException {
        if (nodes.isEmpty()) {
            log.debug("Empty list of nodes passed");
            return;
        }
        log.debug("Going to compute following nodes: '{}'",
                Joiner.on(",").join(nodes.stream().map(n -> n.getId()).collect(Collectors.toList())));
        List<NodeComputationCallable> callables = nodes.stream()
                .map(node -> new NodeComputationCallable(node, treeResult))
                .collect(Collectors.toList());

        List<Future<INodeResult>> futures = executorService.invokeAll(callables);

        List<INodeResult> results = Lists.newArrayListWithCapacity(futures.size());
        for (Future<INodeResult> f : futures) {
            results.add(f.get());
        }

        List<INode> treeNodes = results.stream()
                .map(res -> {
                    Optional<List<String>> nodeResults = registry.getNodeResults(res.getNodeId(), res.getValue());
                    if (nodeResults.isPresent()) {
                        log.debug("Results from registry by nodeID '{}' and result '{}': '{}'",
                                res.getNodeId(), res.getValue(), Joiner.on(",").join(nodeResults.get()));
                    } else {
                        log.debug("No results from registry by nodeID '{}' and result '{}'", res.getNodeId(), res.getValue());
                    }
                    return nodeResults.orNull();
                })
                .filter(x -> x != null)
                .flatMap(x -> x.stream())
                .map(nodeId -> {
                    log.debug("Going to search node by id: {}", nodeId);
                    return registry.getNode(nodeId).orNull();
                })
                .filter(node -> node != null)
                .collect(Collectors.toList());

        if (!treeNodes.isEmpty()) {
            computeRecursive(treeNodes, treeResult);
        } else {
            log.debug("Empty list of nodes, do not pass it to computation");
        }
    }

    /**
     * This class wraps node computation call
     */
    static class NodeComputationCallable implements Callable<INodeResult> {

        private final INode node;
        private final ITreeResult treeResult;

        public NodeComputationCallable(INode node, ITreeResult treeResult) {
            this.node = node;
            this.treeResult = treeResult;
        }

        @Override
        public INodeResult call() throws Exception {
            return node.compute(treeResult);
        }
    }
}
