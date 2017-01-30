package net.iryndin.computetree.node;

import net.iryndin.computetree.api.INodeResult;
import net.iryndin.computetree.api.ITreeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iryndin
 * @since 30/01/17
 */
public class CheckValidityNode extends AbstractNode {

    final static Logger log = LoggerFactory.getLogger(CheckValidityNode.class);

    @Override
    public INodeResult compute(ITreeResult treeResult) {
        String validityCheckResult = "valid";
        log.debug("Start checking validity...");
        try {
            Thread.sleep(3000);
            treeResult.put("valid", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("Result of validity check: {}", validityCheckResult);
        return INodeResult.createEmptyNodeResult(getId(), validityCheckResult);
    }
}
