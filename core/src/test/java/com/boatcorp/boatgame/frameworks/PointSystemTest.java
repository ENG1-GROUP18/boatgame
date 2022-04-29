package com.boatcorp.boatgame.frameworks;

import junit.framework.TestCase;

public class PointSystemTest extends TestCase {


    public void testSetGetPoints() {
        PointSystem.setPoints(10);
        assertEquals("Is points equal to the amount set",10,PointSystem.getPoints());
    }

    public void testIncrementPoint() {
        PointSystem.incrementPoint(5);
        assertEquals("Is points equal to the amount incremented",15,PointSystem.getPoints());

    }
}