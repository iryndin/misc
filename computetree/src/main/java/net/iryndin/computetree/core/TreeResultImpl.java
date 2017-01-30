package net.iryndin.computetree.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import net.iryndin.computetree.api.ITreeResult;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Should be thread-safe
 *
 * @author iryndin
 * @since 30/01/17
 */
public class TreeResultImpl implements ITreeResult {

    private final Map<String, Object> map = Maps.newConcurrentMap();

    @Override
    public void put(String id, Object data) {
        map.put(id, data);
    }

    @Override
    public Object get(String id) {
        return map.get(id);
    }

    @Override
    public Set<String> keys() {
        return map.keySet();
    }

    @Override
    public String toString() {
        return "TreeResult:  \n   "+ Joiner.on("\n   ")
                .join(map.entrySet()
                        .stream()
                        .sorted((n1,n2) -> n1.getKey().compareTo(n2.getKey()))
                        .collect(Collectors.toList()));
    }
}
