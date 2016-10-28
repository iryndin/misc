package com.jetlore.socialnewsreader.formatter.model;

/**
 * @author iryndin
 * @since 28/10/16
 */
public class RegularObjectEntity extends ObjectEntity {

    protected RegularObjectEntity(int startPosition, int endPosition) {
        super(ObjectEntityType.ENTITY, startPosition, endPosition);
    }

    @Override
    public String format(String text) {
        String s = text.substring(getStartPosition(), getEndPosition());
        return "<strong>" + s + "</strong>";
    }
}
