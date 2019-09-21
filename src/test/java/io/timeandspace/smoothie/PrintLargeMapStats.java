package io.timeandspace.smoothie;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;

import static org.junit.Assert.assertTrue;

final class PrintLargeMapStats {
    private static final SmoothieMap<Integer, Integer> map =
            SmoothieMap.<Integer, Integer>newBuilder().allocateIntermediateSegments(true).build();
    private static final Random random = new Random(0);

    public static void main(String[] args) {
        fillMap();

        System.out.println("Average segment order: " + map.lastComputedAverageSegmentOrder);
        System.out.printf("Map size per entry: %.2f%n",
                (double) map.sizeInBytes() / (double) map.size());
        SmoothieMapStats smoothieMapStats = new SmoothieMapStats();
        map.aggregateStats(smoothieMapStats);
        System.out.println(smoothieMapStats);
        System.out.println(smoothieMapStats.computeTotalOrdinarySegmentStats());

        System.out.println(prepareUnsuccessfulKeySearchStats());
    }

    private static void fillMap() {
        for (int i = 0; i < 20_000_000; i++) {
            int key = random.nextInt();
            @Nullable Integer res = map.put(key, key);
            assertTrue(res == null || res == key);
        }
    }

    private static KeySearchStats prepareUnsuccessfulKeySearchStats() {
        KeySearchStats keySearchStats = new KeySearchStats();
        for (int i = 0; i < 100_000; i++) {
            Integer key = random.nextInt();
            if (!map.containsKey(key)) {
                map.aggregateKeySearchStats(key, keySearchStats);
            }
        }
        return keySearchStats;
    }
}
