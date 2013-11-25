package org.akolosok.metrics;

import org.akolosok.metrics.metrics.SynchronizedCelledCounter;

/**
 * @author Andrei Kolosok
 */
public class SynchronizedCelledCounterTest extends AbstractPerformanceTest {

	private static SynchronizedCelledCounter metric = new SynchronizedCelledCounter();

	@Override
	protected void mark() {
		metric.mark();
	}
}
