package net.iryndin.computetree.node;

import net.iryndin.computetree.api.INodeResult;
import net.iryndin.computetree.api.ITreeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iryndin
 * @since 30/01/17
 */
public class DebugNode extends AbstractNode {

    final static Logger log = LoggerFactory.getLogger(DebugNode.class);

    private final String result;
    private final long timeoutMillis;

    public DebugNode(String nodeId) {
        this(nodeId, "success", 5000);
    }

    public DebugNode(String nodeId, String result, long timeoutMillis) {
        this.setId(nodeId);
        this.result = result;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public INodeResult compute(ITreeResult treeResult) {
        log.debug("Starting computation of node: '{}'", getId());
        try {
            Thread.sleep(timeoutMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("Complete computation of node: '{}'", getId());
        return INodeResult.createEmptyNodeResult(getId(), "success");
    }
}
