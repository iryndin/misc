package com.jetlore.socialnewsreader.formatter.model;

import static java.lang.String.format;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class ObjectEntityFactory {

    public static ObjectEntity createEntity(int start, int end) {
        return create(ObjectEntityType.ENTITY, start, end);
    }

    public static ObjectEntity createTwitterUsername(int start, int end) {
        return create(ObjectEntityType.USERNAME_TWITTER, start, end);
    }

    public static ObjectEntity createLink(int start, int end) {
        return create(ObjectEntityType.LINK, start, end);
    }

    private static ObjectEntity create(ObjectEntityType type, int start, int end) {
        checkPositions(start, end);
        return new ObjectEntity(type, start, end);
    }

    private static void checkPositions(int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException(format("Entity start position is negative (%d)!", start));
        }
        if (end <= start) {
            throw new IllegalArgumentException(format("Entity end position is <= than start position (%d <= %d)!", end, start));
        }
    }

    public static void main(String[] args) {
        checkPositions(1,1);
    }
}
