package com.jetlore.socialnewsreader.formatter;

import com.jetlore.socialnewsreader.formatter.model.FormatRequest;
import com.jetlore.socialnewsreader.formatter.model.FormatResult;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntity;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntityFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class AbstractFormatterTest {

    @Test
    public void testSortObjectEntitiesByStartPos() {
        List<ObjectEntity> list = Arrays.asList(
                ObjectEntityFactory.createEntity(14, 22),
                ObjectEntityFactory.createEntity(0, 5),
                ObjectEntityFactory.createTwitterUsername(56, 67),
                ObjectEntityFactory.createLink(37, 54)
        );

        List<ObjectEntity> shouldReceive = Arrays.asList(
                ObjectEntityFactory.createEntity(0, 5),
                ObjectEntityFactory.createEntity(14, 22),
                ObjectEntityFactory.createLink(37, 54),
                ObjectEntityFactory.createTwitterUsername(56, 67)
        );

        AbstractFormatter af = new AbstractFormatter() {

            @Override
            public FormatResult format(FormatRequest request) throws FormatterException {
                return null;
            }
        };

        assertEquals(shouldReceive, af.sortObjectEntitiesByStartPos(list));
    }

    @Test(expected = FormatterException.class)
    public void testCheckOverlappingEntitiesInSortedList() throws FormatterException {

        List<ObjectEntity> sortedList = Arrays.asList(
                ObjectEntityFactory.createEntity(0, 5),
                ObjectEntityFactory.createEntity(14, 22),
                ObjectEntityFactory.createLink(37, 56),
                ObjectEntityFactory.createTwitterUsername(56, 67)
        );

        AbstractFormatter af = new AbstractFormatter() {

            @Override
            public FormatResult format(FormatRequest request) throws FormatterException {
                return null;
            }
        };

        af.checkOverlappingEntitiesInSortedList(sortedList);
    }

    @Test(expected = FormatterException.class)
    public void testCheckEntityPositionsAreWithinText() throws FormatterException {

        List<ObjectEntity> sortedList = Arrays.asList(
                ObjectEntityFactory.createEntity(0, 5),
                ObjectEntityFactory.createEntity(14, 22),
                ObjectEntityFactory.createLink(37, 56),
                ObjectEntityFactory.createTwitterUsername(56, 67)
        );

        AbstractFormatter af = new AbstractFormatter() {

            @Override
            public FormatResult format(FormatRequest request) throws FormatterException {
                return null;
            }
        };

        af.checkEntityPositionsAreWithinText(25, sortedList);
    }
}
