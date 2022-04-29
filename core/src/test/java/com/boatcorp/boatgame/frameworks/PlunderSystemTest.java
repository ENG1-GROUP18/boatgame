package com.boatcorp.boatgame.frameworks;

import junit.framework.TestCase;
import org.junit.Before;
import com.boatcorp.boatgame.frameworks.PlunderSystem;

public class PlunderSystemTest extends TestCase {



    public void testSetGetPlunder() {
        PlunderSystem.setPlunder(10);
        assertEquals("Get plunder equal to the amount set",10,PlunderSystem.getPlunder());
    }

    public void testIncrementPlunder() {
        PlunderSystem.setPlunder(10);
        PlunderSystem.incrementPlunder(5);
        assertEquals("Get plunder equal to the amount incremented",15,PlunderSystem.getPlunder());
    }

    public void testDecrementPlunder() {
        PlunderSystem.setPlunder(10);
        PlunderSystem.decrementPlunder(5);
        assertEquals("Get plunder equal to the amount decremented",5,PlunderSystem.getPlunder());
    }

}