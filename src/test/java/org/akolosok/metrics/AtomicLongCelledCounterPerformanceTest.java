package org.akolosok.metrics;

import org.akolosok.metrics.metrics.AtomicLongCelledCounter;

/**
 * @author Andrei Kolosok
 */
public class AtomicLongCelledCounterPerformanceTest extends AbstractPerformanceTest {

	private static AtomicLongCelledCounter metric = new AtomicLongCelledCounter();

	@Override
	protected void mark() {
		metric.mark();
	}
}
