package net.iryndin.computetree.api;


import com.google.common.base.Optional;

/**
 * @author iryndin
 * @since 20/01/17
 */
public interface INode {
    /**
     * Get node ID
     * @return node ID
     */
    String getId();

    /**
     * Set node ID
     * @param nodeId node ID
     */
    void setId(String nodeId);

    /**
     * Node computation is made here.
     * @param treeResult reference to tree result object. This tree result object can be modified during
     *                   node computation.
     * @return result of node computation
     */
    INodeResult compute(ITreeResult treeResult);
}
