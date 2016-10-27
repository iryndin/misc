package com.jetlore.socialnewsreader.formatter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 27/10/16
 */
public class ObjectEntityFactoryTest {

    @Test
    public void testCreateEntity() {
        assertEquals(new ObjectEntity(ObjectEntityType.ENTITY, 1,2), ObjectEntityFactory.createEntity(1,2));
    }

    @Test
    public void testCreateTwitterUsername() {
        assertEquals(new ObjectEntity(ObjectEntityType.USERNAME_TWITTER, 1,2), ObjectEntityFactory.createTwitterUsername(1,2));
    }

    @Test
    public void testCreateLink() {
        assertEquals(new ObjectEntity(ObjectEntityType.LINK, 1,2), ObjectEntityFactory.createLink(1,2));
    }

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
