package net.iryndin.computetree.api;

import com.google.common.base.Optional;

import java.util.List;

/**
 * @author iryndin
 * @since 20/01/17
 */
public interface INodeRegistry {
    Optional<INode> getNode(String id);
    Optional<List<String>> getNodeResults(String id, String result);
}
