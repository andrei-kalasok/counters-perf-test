package org.akolosok.metrics;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrei Kolosok
 */
public abstract class AbstractPerformanceTest extends AbstractBenchmark {

	private static final int THREADS_COUNT = 7;
	private final Runnable markMethod = new Runnable() {
		@Override
		public void run() {
			mark();
		}
	};

	private ScheduledExecutorService scheduledExecutorService;

	protected AbstractPerformanceTest() {
		scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
		runThreads(THREADS_COUNT);
		System.gc();
	}

	@After
	public void tearDown() throws InterruptedException {
		scheduledExecutorService.shutdown();
		while (!scheduledExecutorService.isTerminated()) {
			Thread.sleep(100);
		}
	}

	@Test
	@BenchmarkOptions(warmupRounds = 500000, benchmarkRounds = 2000000)
	public void performance() throws InterruptedException {
		mark();
	}

	protected abstract void mark();

	private void runThreads(int count) {
		for (int i = 0; i < count; ++i) {
			scheduledExecutorService.scheduleAtFixedRate(markMethod, 100, 100, TimeUnit.NANOSECONDS);
		}
	}
}

