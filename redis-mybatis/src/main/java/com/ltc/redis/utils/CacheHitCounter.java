package com.ltc.redis.utils;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * CacheHitCounter
 * <p>
 * Created by Bob Jiang on 18-5-17.
 */
public class CacheHitCounter {

    private static final AtomicLongFieldUpdater<CacheHitCounter> hit_count_updater = AtomicLongFieldUpdater.newUpdater(CacheHitCounter.class, "hitCount");
    private static final CopyOnWriteArrayList<CacheHitCounter> hitCounters = new CopyOnWriteArrayList<>();

    private Object key;
    private volatile long hitCount;

    public CacheHitCounter(Object key, long hitCount) {
        this.key = key;
        this.hitCount = hitCount;
    }

    public static long getHitCount(Object key) {
        return getHitCounter(key).hitCount;
    }

    public static long incrementAndGet(Object key) {
        return hit_count_updater.incrementAndGet(getHitCounter(key));
    }

    public static void reset(Object key) {
        hit_count_updater.set(getHitCounter(key), 0L);
    }

    private static CacheHitCounter getHitCounter(Object key) {
        CacheHitCounter hitCounter;
        if (hitCounters.stream().filter(i -> i.key.equals(key)).count() > 0L) {
            hitCounter = hitCounters.stream().filter(i -> i.key.equals(key)).findFirst().get();
        } else {
            hitCounter = new CacheHitCounter(key, 0L);
            hitCounters.add(hitCounter);
        }
        return hitCounter;
    }
}