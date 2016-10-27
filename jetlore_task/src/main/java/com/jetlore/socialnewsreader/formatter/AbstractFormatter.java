package com.jetlore.socialnewsreader.formatter;

import com.google.common.collect.Lists;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author iryndin
 * @since 27/10/16
 */
public abstract class AbstractFormatter implements IFormatter {

    protected List<ObjectEntity> sortObjectEntitiesByStartPos(List<ObjectEntity> list) {
        List<ObjectEntity> result = Lists.newArrayList(list);
        Collections.sort(result, (o1, o2) -> o1.getStartPosition() - o2.getStartPosition());
        return result;
    }

    protected void checkOverlappingEntitiesInSortedList(List<ObjectEntity> list) throws FormatterException {
        ObjectEntity prev = null;
        for (ObjectEntity e : list) {
            if (prev != null) {
                if (prev.getEndPosition() >= e.getStartPosition()) {
                    throw new FormatterException(String.format("Overlapping entities found: '%s' and '%s'", prev, e));
                }
            }
            prev = e;
        }
    }

    protected void checkEntityPositionsAreWithinText(int textLength, List<ObjectEntity> list) throws FormatterException {
        for (ObjectEntity e : list) {
            if (e.getStartPosition() >= textLength) {
                throw new FormatterException(String.format("Entity start position (%d) is beyond text length (%d): '%s'", e.getStartPosition(), textLength, e));
            }
            if (e.getEndPosition() > textLength) {
                throw new FormatterException(String.format("Entity end position (%d) is beyond text length (%d): '%s'", e.getEndPosition(), textLength, e));
            }
        }
    }
}
