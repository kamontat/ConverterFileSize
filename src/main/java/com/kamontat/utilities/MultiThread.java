package com.kamontat.utilities;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 10:08 PM
 */
public class MultiThread {
	private ExecutorService service;
	
	/**
	 * create multi-thread service. <br>
	 * the parameter, can pass by {@link Executors} method
	 *
	 * @param service
	 * 		the running service
	 */
	public MultiThread(ExecutorService service) {
		this.service = service;
	}
	
	/**
	 * The service must be {@link java.util.concurrent.ScheduledExecutorService} class
	 *
	 * @param runnable
	 * 		method that want to execute
	 * @param initial
	 * 		the time interval at first run <code>runnable</code>
	 * @param delay
	 * 		the time interval
	 * @param until
	 * 		the time end
	 * @param unit
	 * 		unit if the time
	 * @return {@link ScheduledFuture} - this class will let you know what going on
	 */
	public ScheduledFuture schedule(Runnable runnable, int initial, int delay, int until, TimeUnit unit) {
		if (service.getClass().equals(ScheduledExecutorService.class))
			throw new ClassCastException("The service must be ScheduledExecutorService");
		
		ScheduledExecutorService scheduled = (ScheduledExecutorService) service;
		ScheduledFuture future = scheduled.scheduleAtFixedRate(runnable, initial, delay, unit);
		scheduled.schedule(() -> future.cancel(true), until, unit);
		return future;
	}
	
	public Future<?> execute(Runnable runnable) throws RejectedExecutionException {
		return service.submit(runnable);
	}
	
	public <T> Future<T> execute(Callable<T> callable) throws RejectedExecutionException {
		return service.submit(callable);
	}
	
	@SafeVarargs
	public final <T> List<Future<T>> executeAndWaitAll(Callable<T>... callable) throws RejectedExecutionException {
		try {
			return service.invokeAll(Arrays.asList(callable));
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	@SafeVarargs
	public final <T> T executeAndWaitAny(Callable<T>... callable) throws RejectedExecutionException {
		try {
			return service.invokeAny(Arrays.asList(callable));
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}
	
	public List<Future> executeAndWaitAll(Runnable... runnable) throws RejectedExecutionException {
		Callable[] cs = new Callable[runnable.length];
		int i = 0;
		for (Runnable run : runnable) {
			cs[i++] = () -> {
				run.run();
				return null;
			};
		}
		return executeAndWaitAll(cs);
	}
	
	public void executeAndWaitAny(Runnable... runnable) throws RejectedExecutionException {
		Callable<Object>[] cs = new Callable[runnable.length];
		int i = 0;
		for (Runnable run : runnable) {
			cs[i++] = () -> {
				run.run();
				return null;
			};
		}
		executeAndWaitAny(cs);
	}
	
	/**
	 *
	 */
	public void shutdown() {
		if (!service.isShutdown()) service.shutdown();
	}
	
	public boolean isShutDown() {
		return service.isShutdown();
	}
	
	public boolean isTerminate() {
		return service.isTerminated();
	}
	
	public List<Runnable> forceShutdown() {
		if (!service.isShutdown()) return service.shutdownNow();
		return null;
	}
	
	/**
	 * Blocks until all tasks have completed. <br>
	 * There are 3 ways that program can pass this method
	 * <ol>
	 * <li>Shutdown - user already call {@link #shutdown()} or {@link #forceShutdown()} ~ result will be {@link Boolean#TRUE}</li>
	 * <li>Timeout - user the runnable/callable run/call exceed timeout interval ~ result will be {@link Boolean#FALSE}</li>
	 * <li>Exception - when have exception while running the program ~ result will be {@link InterruptedException}</li>
	 * </ol>
	 *
	 * @param time
	 * 		the maximum time for waiting
	 * @param unit
	 * 		the unit for waiting time
	 * @return true if successful terminate the thread, false if timeout
	 * @throws InterruptedException
	 * 		when error occurred while waiting
	 * @see ExecutorService#awaitTermination(long, TimeUnit)
	 */
	public boolean wait(long time, TimeUnit unit) throws InterruptedException {
		return service.awaitTermination(time, unit);
	}
}
