package net.iryndin.computetree.node;

import net.iryndin.computetree.api.INodeResult;
import net.iryndin.computetree.api.ITreeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iryndin
 * @since 29/01/17
 */
public class TrivialSuccessNode extends AbstractNode {

    final static Logger log = LoggerFactory.getLogger(TrivialSuccessNode.class);

    @Override
    public INodeResult compute(ITreeResult treeResult) {
        log.debug("Return success");
        return INodeResult.createEmptyNodeResult(getId(), "success");
    }
}
