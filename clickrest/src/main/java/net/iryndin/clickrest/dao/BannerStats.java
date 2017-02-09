package net.iryndin.clickrest.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author iryndin
 * @since 09/02/17
 */
@Document(collection = "bannercost")
public class BannerStats {

    @Id
    private String id;
    private Long cost;

    public BannerStats() {
    }

    public BannerStats(String id, Long cost) {
        this.id = id;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "BannerStats{" +
                "id='" + id + '\'' +
                ", cost=" + cost +
                '}';
    }
}
