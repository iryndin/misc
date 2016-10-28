package com.jetlore.socialnewsreader.formatter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class ObjectEntityFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testStartPositionNegative() {
        ObjectEntityFactory.createLink(-1,2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartPositionGTEndPositionNegative() {
        ObjectEntityFactory.createLink(10,2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartPositionEqualsEndPositionNegative() {
        ObjectEntityFactory.createLink(10,10);
    }
}
