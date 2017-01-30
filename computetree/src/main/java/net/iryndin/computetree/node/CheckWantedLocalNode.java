package net.iryndin.computetree.node;

import net.iryndin.computetree.api.INodeResult;
import net.iryndin.computetree.api.ITreeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author iryndin
 * @since 30/01/17
 */
public class CheckWantedLocalNode extends AbstractNode {

    final static Logger log = LoggerFactory.getLogger(CheckWantedLocalNode.class);

    @Override
    public INodeResult compute(ITreeResult treeResult) {
        boolean checkResult = false;
        log.debug("Start checking WANTED domestic database...");
        try {
            long timeoutMs = 1000 + new Random().nextInt(4000);
            Thread.sleep(timeoutMs);
            treeResult.put("wantedLocal", checkResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("Result of wantedLocal check: {}", checkResult);
        return INodeResult.createEmptyNodeResult(getId(), Boolean.toString(checkResult));
    }
}