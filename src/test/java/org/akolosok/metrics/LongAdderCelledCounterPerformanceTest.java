package org.akolosok.metrics;

import org.akolosok.metrics.metrics.LongAdderCelledCounter;

/**
 * @author Andrei Kolosok
 */
public class LongAdderCelledCounterPerformanceTest extends AbstractPerformanceTest {

	private static LongAdderCelledCounter metric = new LongAdderCelledCounter();

	@Override
	protected void mark() {
		metric.mark();
	}
}
