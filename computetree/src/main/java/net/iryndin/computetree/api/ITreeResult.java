package net.iryndin.computetree.api;

import java.util.Set;

/**
 * @author iryndin
 * @since 20/01/17
 */
public interface ITreeResult {
    void put(String id, Object data);
    Object get(String id);
    Set<String> keys();
}
