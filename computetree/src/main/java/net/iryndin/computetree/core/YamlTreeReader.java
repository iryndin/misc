package net.iryndin.computetree.core;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import net.iryndin.computetree.api.INode;
import net.iryndin.computetree.api.INodeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Read tree configuration from YAML file. Also creates INodeRegistry, used for tree nodes access.
 *
 * @author iryndin
 * @since 30/01/17
 */
public class YamlTreeReader {

    final static Logger log = LoggerFactory.getLogger(YamlTreeReader.class);

    public static class IllegalYamlDocumentException extends Exception {

        public IllegalYamlDocumentException(String s) {
            super(s);
        }
    }

    private static class NodeData {
        private final String id;
        private final String nodeClass;
        private final Map<String, List<String>> results;

        public NodeData(String id, String nodeClass, Map<String, List<String>> results) {
            this.id = id;
            this.nodeClass = nodeClass;
            this.results = results;
        }

        public String getId() {
            return id;
        }

        public String getNodeClass() {
            return nodeClass;
        }

        public Map<String, List<String>> getResults() {
            return results;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("id", id)
                    .add("nodeClass", nodeClass)
                    .add("results", results)
                    .toString();
        }
    }

    private final String yamlText;
    private String startNodeId;
    private final Map<String, NodeData> nodesMap = new ConcurrentHashMap<>();

    public YamlTreeReader(String yamlText) throws IllegalYamlDocumentException {
        this.yamlText = yamlText;
        read(this.yamlText);
    }

    public String getStartNodeId() {
        return startNodeId;
    }

    public INodeRegistry createNodeRegistry() {
        return new INodeRegistry() {
            @Override
            public Optional<INode> getNode(String id) {
                return createNodeById(id);
            }

            @Override
            public Optional<List<String>> getNodeResults(String id, String result) {
                return createNodeResults(id, result);
            }
        };
    }

    private Optional<List<String>> createNodeResults(String id, String result) {
        log.debug("Create node results, nodeId='{}', result='{}'", id, result);
        NodeData nd = nodesMap.get(id);
        if (nd != null && nd.getResults() != null) {
            return Optional.fromNullable(nd.getResults().get(result));
        }
        return Optional.absent();
    }

    private Optional<INode> createNodeById(String id) {
        NodeData nd = nodesMap.get(id);
        if (nd == null) {
            return Optional.absent();
        } else {
            return createNode(nd);
        }
    }

    private Optional<INode> createNode(NodeData nd) {
        log.debug("Start creating tree node by id: '{}'", nd.getId());
        String className = nd.getNodeClass();
        log.debug("Loading node class '{}'", className);
        INode node = instantiateNodeClass(className);
        log.debug("Node class loaded OK: '{}'", className);
        node.setId(nd.getId());
        log.debug("Created node: {}", node);
        return Optional.of(node);
    }

    private INode instantiateNodeClass(final String className){
        try{
            return (INode)Class.forName(className).newInstance();
        } catch(final InstantiationException e){
            log.error("Error when creating node with class '{}'", className, e);
            throw new IllegalStateException(e);
        } catch(final IllegalAccessException e){
            log.error("Error when creating node with class '{}'", className, e);
            throw new IllegalStateException(e);
        } catch(final ClassNotFoundException e){
            log.error("Error when creating node with class '{}'", className, e);
            throw new IllegalStateException(e);
        }
    }

    private void read(String yamlText) throws IllegalYamlDocumentException {
        Map<String, Object> root = (Map<String, Object>)new Yaml().load(yamlText);
        Map<String, Object> tree = (Map<String, Object>)root.get("tree");
        if (tree == null) {
            throw new IllegalYamlDocumentException("Tree YAML document should have 'tree' root element");
        }
        Integer version = (Integer)tree.get("version");
        if (version == null) {
            throw new IllegalYamlDocumentException("Tree YAML document should have 'version' element");
        } else if (version == 1) {
            readVersion1(tree);
        } else {
            throw new IllegalYamlDocumentException("Tree YAML document should have 'version' equals to '1'");
        }
    }

    private void readVersion1(Map<String, Object> tree) throws IllegalYamlDocumentException {
        this.startNodeId = (String)tree.get("start");
        if (this.startNodeId == null) {
            throw new IllegalYamlDocumentException("Tree YAML document should have 'start' element");
        }
        List<Map<String, Object>> nodes = (List<Map<String, Object>>)tree.get("nodes");
        for (Map<String, Object> n : nodes) {
            readNode(n);
        }
        log.debug("Tree YAML successfully read, nodes:\n   {}", Joiner.on("\n   ")
                .join(nodesMap.values().stream().sorted((n1,n2) -> n1.getId().compareTo(n2.getId())).collect(Collectors.toList())));
    }

    private void readNode(Map<String, Object> n) throws IllegalYamlDocumentException {
        String nodeId = (String)n.get("id");
        if (nodeId == null) {
            throw new IllegalYamlDocumentException("Missing 'id' element in tree node");
        }
        String nodeClass = (String)n.get("class");
        if (nodeId == null) {
            throw new IllegalYamlDocumentException("Missing 'class' element in tree node");
        }
        Map<String, List<String>> nodeResults = (Map<String, List<String>>)n.get("results");

        NodeData nd = new NodeData(nodeId, nodeClass, nodeResults);
        nodesMap.put(nodeId, nd);
    }
}
