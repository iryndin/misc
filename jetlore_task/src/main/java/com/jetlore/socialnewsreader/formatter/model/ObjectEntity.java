package com.jetlore.socialnewsreader.formatter.model;

/**
 * @author iryndin
 * @since 27/10/16
 */
public abstract class ObjectEntity {
    private final ObjectEntityType type;
    private final int startPosition;
    private final int endPosition;

    protected ObjectEntity(ObjectEntityType type, int startPosition, int endPosition) {
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public abstract String format(String text);

    @Override
    public String toString() {
        return "ObjectEntity{" +
                "type=" + type +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectEntity that = (ObjectEntity) o;

        return startPosition == that.startPosition &&
                endPosition == that.endPosition &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + startPosition;
        result = 31 * result + endPosition;
        return result;
    }
}
