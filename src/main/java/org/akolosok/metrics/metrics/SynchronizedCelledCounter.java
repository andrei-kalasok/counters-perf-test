package org.akolosok.metrics.metrics;

import com.codahale.metrics.Clock;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Andrei Kolosok
 */
public class SynchronizedCelledCounter{

	private final List<Long> cellList;
	private volatile int current = 0;

	private final AtomicLong lastTick;
	private final Clock clock;
	private final long discreteness;

	private volatile long rate = 0;

	public SynchronizedCelledCounter() {
		this(10, 10, TimeUnit.SECONDS, Clock.defaultClock());
	}

	public SynchronizedCelledCounter(int cellCount, long discreteness, TimeUnit timeUnit, Clock clock) {
		this.cellList = prepareList(cellCount);
		this.discreteness = timeUnit.toNanos(discreteness);
		this.lastTick = new AtomicLong(clock.getTick());
		this.clock = clock;
	}

	private List<Long> prepareList(int cellCount) {
		CopyOnWriteArrayList<Long> rateList = new CopyOnWriteArrayList<>();
		for (int i = 0; i < cellCount; ++i) {
			rateList.add(0L);
		}

		return rateList;
	}

	public synchronized void mark() {
		tickIfNecessary();
		rate++;
	}

	public synchronized long[] getLast(int count) {
		tickIfNecessary();
		Object[] cells = cellList.toArray();
		int current = this.current;
		long[] items = new long[count];
		for (int i = 0; i < count; ++i) {
			current--;
			if (current < 0) {
				current = cells.length - 1;
			}
			items[i] = (long) cells[current];
		}

		return items;
	}

	private synchronized void tickIfNecessary() {
		final long oldTick = lastTick.get();
		final long newTick = clock.getTick();
		final long age = newTick - oldTick;
		if (age >= discreteness) {
			final long newIntervalStartTick = newTick - age % discreteness;
			if (lastTick.compareAndSet(oldTick, newIntervalStartTick)) {
				final long requiredTicks = age / discreteness;
				for (long i = 0; i < requiredTicks; i++) {
					tick();
				}
			}
		}
	}

	private synchronized void tick() {
		if (current == cellList.size()) {
			current = 0;
		}

		cellList.set(current++, rate);
		rate = 0;
	}



}
