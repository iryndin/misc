package net.iryndin.computetree.api;

import com.google.common.base.Optional;

import java.util.Map;

/**
 * @author iryndin
 * @since 20/01/17
 */
public interface INodeResult {
    String getNodeId();
    String getValue();
    Optional<Map<Object, Object>> getData();

    static INodeResult createEmptyNodeResult(String nodeId, String result) {
        return new INodeResult() {

            @Override
            public String getNodeId() {
                return nodeId;
            }

            @Override
            public String getValue() {
                return result;
            }

            @Override
            public Optional<Map<Object, Object>> getData() {
                return Optional.absent();
            }
        };
    }
}
