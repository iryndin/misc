package net.iryndin.computetree.node;

import net.iryndin.computetree.api.INode;

/**
 * @author iryndin
 * @since 30/01/17
 */
public abstract class AbstractNode implements INode {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String nodeId) {
        this.id = nodeId;
    }

    @Override
    public String toString() {
        return getClass().getName()+": {id="+getId()+"}";
    }
}
