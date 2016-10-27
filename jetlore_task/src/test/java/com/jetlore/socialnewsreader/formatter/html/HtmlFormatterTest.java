package com.jetlore.socialnewsreader.formatter.html;

import com.jetlore.socialnewsreader.formatter.model.FormatRequest;
import com.jetlore.socialnewsreader.formatter.model.FormatResult;
import com.jetlore.socialnewsreader.formatter.FormatterException;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntity;
import com.jetlore.socialnewsreader.formatter.model.ObjectEntityFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class HtmlFormatterTest {

    @Test
    public void testNormal() throws FormatterException {
        String text = "Obama visited Facebook headquarters: http://bit.ly/xyz @elversatile";
        List<ObjectEntity> list = Arrays.asList(
                ObjectEntityFactory.createEntity(14, 22),
                ObjectEntityFactory.createEntity(0, 5),
                ObjectEntityFactory.createTwitterUsername(56, 67),
                ObjectEntityFactory.createLink(37, 54)
        );
        FormatRequest req = new FormatRequest(text, list);
        FormatResult res = new HtmlFormatter().format(req);
        String shouldReceiveText = "<strong>Obama</strong> visited <strong>Facebook</strong> headquarters: <a href=\"http://bit.ly/xyz\">http://bit.ly/xyz</a> @<a href=\"http://twitter.com/elversatile\">elversatile</a>";
        assertEquals(shouldReceiveText, res.getFormattedText());
    }

    @Test
    public void testNoMarkedEntities() throws FormatterException {
        String text = "Obama visited Facebook headquarters: http://bit.ly/xyz @elversatile";
        FormatRequest req = new FormatRequest(text, Collections.emptyList());
        FormatResult res = new HtmlFormatter().format(req);
        assertEquals(text, res.getFormattedText());
    }

    @Test
    public void testSingleEntity() throws FormatterException {
        String text = "Obama visited Facebook headquarters: http://bit.ly/xyz @elversatile";
        List<ObjectEntity> list = Arrays.asList(
                ObjectEntityFactory.createEntity(14, 22)
        );
        FormatRequest req = new FormatRequest(text, list);
        FormatResult res = new HtmlFormatter().format(req);
        String shouldReceiveText = "Obama visited <strong>Facebook</strong> headquarters: http://bit.ly/xyz @elversatile";
        assertEquals(shouldReceiveText, res.getFormattedText());
    }

    @Test(expected = FormatterException.class)
    public void testOverlappingEntities() throws FormatterException {
        String text = "Obama visited Facebook headquarters: http://bit.ly/xyz @elversatile";
        List<ObjectEntity> list = Arrays.asList(
                ObjectEntityFactory.createEntity(0, 22),
                ObjectEntityFactory.createEntity(14, 22)
        );
        FormatRequest req = new FormatRequest(text, list);
        new HtmlFormatter().format(req);
    }

    @Test(expected = FormatterException.class)
    public void testEntityPositionsGoBeyondTextLength() throws FormatterException {
        String text = "Obama visited Facebook headquarters: http://bit.ly/xyz @elversatile";
        List<ObjectEntity> list = Arrays.asList(
                ObjectEntityFactory.createEntity(25, 100),
                ObjectEntityFactory.createEntity(14, 22)
        );
        FormatRequest req = new FormatRequest(text, list);
        new HtmlFormatter().format(req);
    }
}
