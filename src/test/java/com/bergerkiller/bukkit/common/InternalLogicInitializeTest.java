package com.bergerkiller.bukkit.common;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bergerkiller.bukkit.common.internal.logic.EntityAddRemoveHandler;
import com.bergerkiller.bukkit.common.internal.logic.EntityTypingHandler;
import com.bergerkiller.bukkit.common.internal.logic.LightingHandler;
import com.bergerkiller.bukkit.common.internal.logic.LightingHandler_Broken;
import com.bergerkiller.bukkit.common.internal.logic.RegionHandler;

/**
 * Test the initialization of internally used logic helpers
 */
public class InternalLogicInitializeTest {

    @Test
    public void testEntityAddRemoveHandler() {
        assertNotNull(EntityAddRemoveHandler.INSTANCE);
    }

    @Test
    public void testEntityTypingHandler() {
        assertNotNull(EntityTypingHandler.INSTANCE);
    }

    @Test
    public void testLightingHandler() {
        assertFalse(LightingHandler.INSTANCE instanceof LightingHandler_Broken);
    }

    @Test
    public void testRegionHandler() {
        assertNotNull(RegionHandler.INSTANCE);
    }
}
