package net.iryndin.clickrest.service;

import java.util.Optional;

/**
 * @author iryndin
 * @since 09/02/17
 */
public interface IClickService {
    void registerClick(String bannerId, int cost);
    Optional<Long> getStats(String bannerId);
}
