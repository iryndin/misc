package com.jetlore.socialnewsreader.formatter.model;

/**
 * @author iryndin
 * @since 28/10/16
 */
public class LinkObjectEntity extends ObjectEntity {

    protected LinkObjectEntity(int startPosition, int endPosition) {
        super(ObjectEntityType.LINK, startPosition, endPosition);
    }

    @Override
    public String format(String text) {
        String s = text.substring(getStartPosition(), getEndPosition());
        return String.format("<a href=\"%s\">%s</a>", s,s);
    }
}
