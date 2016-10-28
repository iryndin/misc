package com.jetlore.socialnewsreader.formatter.model;

/**
 * @author iryndin
 * @since 28/10/16
 */
public class TwitterUsernameObjectEntity extends ObjectEntity {

    protected TwitterUsernameObjectEntity(int startPosition, int endPosition) {
        super(ObjectEntityType.USERNAME_TWITTER, startPosition, endPosition);
    }

    @Override
    public String format(String text) {
        String s = text.substring(getStartPosition(), getEndPosition());
        return String.format("<a href=\"http://twitter.com/%s\">%s</a>", s,s);
    }
}
