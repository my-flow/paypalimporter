// PayPal Importer for Moneydance - http://my-flow.github.io/paypalimporter/
// Copyright (C) 2013-2014 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.util;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class TrackerTest {

    private Tracker tracker;

    @Before
    public void setUp() {
        this.tracker = new Tracker(0, "extension name", "full version",
                "tracking code");
    }

    @Test
    public void testTrack() {
        for (Tracker.EventName eventName : Tracker.EventName.values()) {
            this.tracker.track(eventName);
        }
    }

}
