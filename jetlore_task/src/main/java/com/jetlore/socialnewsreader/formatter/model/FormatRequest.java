package com.jetlore.socialnewsreader.formatter.model;

import java.util.List;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class FormatRequest {
    private final String text;
    private final List<ObjectEntity> entities;

    public FormatRequest(String text, List<ObjectEntity> entities) {
        this.text = text;
        this.entities = entities;
    }

    public String getText() {
        return text;
    }

    public List<ObjectEntity> getEntities() {
        return entities;
    }
}
