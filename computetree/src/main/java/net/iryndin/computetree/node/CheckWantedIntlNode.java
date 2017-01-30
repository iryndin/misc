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
public class CheckWantedIntlNode extends AbstractNode {

    final static Logger log = LoggerFactory.getLogger(CheckWantedIntlNode.class);

    @Override
    public INodeResult compute(ITreeResult treeResult) {
        boolean checkResult = false;
        log.debug("Start checking WANTED international database...");
        try {
            long timeoutMs = 1000 + new Random().nextInt(4000);
            Thread.sleep(timeoutMs);
            treeResult.put("wantedIntl", checkResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("Result of wantedIntl check: {}", checkResult);
        return INodeResult.createEmptyNodeResult(getId(), Boolean.toString(checkResult));
    }
}
