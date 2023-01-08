package org.poker;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class CacheTest {
    private static final Logger LOG = LoggerFactory.getLogger(CacheTest.class);
    private int mapSize = 2598960;
    private int[] handValues = new int [mapSize];

    @Test
    public void testCache() throws IOException {
        HandRankCache cache = new HandRankCache();
        cache.initializeFromText();
        Map<Long, Integer> map = cache.getCardslong2rank();
        //Let's see if an array of ints is more efficient
        //keep in mind not every value is present. e.g. there is no key of 0 or 1.
        //the indexes are currently 000...01111, 000...101111, etc. so how to turn those into indexes into memory array?

        for (long i = 0; i < mapSize; i++) {
            handValues[(int)i] = map.get(i);
        }
        LOG.info("handValues first {}, last {} ",handValues[0], handValues[mapSize]);


//        cache.serializeLongIntHashmapToFile();
//        cache.deserializeLongIntHashmapFromFile();
    }
}
