package com.kamontat.utilities;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Execute some very very slow code in some way, that make java code much more Asynchronous programming <br>
 * Meaning: if you want to run some code in background or want to wait until the code been finished. <br>
 * In this class I use {@link ExecutorService} to manage the task, I create 3 type of execution <br>
 * <ol>
 * <li>{@link #schedule(Runnable, int, int, int, TimeUnit, boolean) Schedule} - to schedule execute action with delay until some ime</li>
 * <li>{@link #execute(Callable)} - To do some task in the background</li>
 * <li>{@link #executeAndWaitAll(Runnable...)} or {@link #executeAndWaitAny(Callable[])} - Exxecute some very task and want program to wait until the task was end</li>
 * </ol>
 * <b>Beware</b>: To use the multiThread class, after you don't use the class anymore please call {@link #shutdown()} or {@link #forceShutdown()} to stop and terminal the thread
 * <p>
 * <b>Example: </b> <br>
 * <pre>{@code
 *     // create new thread
 *     MultiThread thread = new MultiThread(Executors.newScheduledThreadPool(1));
 *     // print "Hello world!" in every 1 second for 1 minutes
 *     thread.schedule(() -> System.out.println("Hello world!"), 0, 1, 60, TimeUnit.SECONDS);
 *     // close the thread
 *     multiThread.shutdown();
 * }</pre>
 *
 * @author kamontat
 * @version 1.0
 * @see ExecutorService
 * @see Executors
 * @see Future
 * @since Wed 15/Mar/2017 - 10:08 PM
 */
public class MultiThread {
	private ExecutorService service;
	
	/**
	 * create multi-thread service. <br>
	 * the parameter, can pass by {@link Executors} factory
	 *
	 * @param service
	 * 		the running service
	 */
	public MultiThread(ExecutorService service) {
		this.service = service;
	}
	
	/**
	 * The service must be {@link java.util.concurrent.ScheduledExecutorService} class <br>
	 * the scheduling the task in time interval at the future,
	 * <p>
	 * And because of ScheduledExecutorService don't have method that can rerun the task and end at some time so I need to implement by myself by using {@link ScheduledFuture#cancel(boolean)} method to <b>cancel</b> it when task should be end. <br>
	 * <b>Example</b>:
	 * <pre>{@code
	 *
	 * }</pre>
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
	 * @param fix
	 * 		{@code true} when the {@code delay} time meaning delay time will fixed (not whether execute time is what but program will add more delay time on it); otherwise is {@code false} <br>
	 * 		<ol>
	 * 		<li>if {@code true} you can learn more at {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}</li>
	 * 		<li>if {@code false} you can learn more at {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}</li>
	 * 		</ol>
	 * @return {@link ScheduledFuture} - this class will let you know what going on, since this method will close from cancellation only so you cannot use the method {@code get()} normally
	 */
	public ScheduleFutureImp schedule(Runnable runnable, int initial, int delay, int until, TimeUnit unit, boolean fix) {
		if (service.getClass().equals(ScheduledExecutorService.class))
			throw new ClassCastException("The service must be ScheduledExecutorService");
		
		ScheduledExecutorService scheduled = (ScheduledExecutorService) service;
		ScheduledFuture future = fix ? scheduled.scheduleWithFixedDelay(runnable, initial, delay, unit): scheduled.scheduleAtFixedRate(runnable, initial, delay, unit);
		scheduled.schedule(() -> future.cancel(true), until, unit);
		return new ScheduleFutureImp(future, service);
	}
	
	/**
	 * To execute method {@link Runnable#run()} in the background
	 *
	 * @param runnable
	 * 		run task
	 * @return the object that represent future result, if you call {@link Future#get()} when task execute successfully, it's wil return {@code null}
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	public Future<?> execute(Runnable runnable) throws RejectedExecutionException {
		return service.submit(runnable);
	}
	
	/**
	 * To execute method {@link Runnable#run()} in the background
	 *
	 * @param callable
	 * 		the run task
	 * @param <T>
	 * 		the return object value
	 * @return the object that represent future result, if you call {@link Future#get()} when task execute successfully, it's wil return {@code object} {@code T}
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	public <T> Future<T> execute(Callable<T> callable) throws RejectedExecutionException {
		return service.submit(callable);
	}
	
	/**
	 * execute the tasks and wait until <b>all</b> task was successfully
	 *
	 * @param callable
	 * 		the run task
	 * @param <T>
	 * 		the return type
	 * @return list of all task that represent the task that has been executed, or <br>
	 * {@code Null} if there have {@link InterruptedException} cause
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	@SafeVarargs
	public final <T> List<Future<T>> executeAndWaitAll(Callable<T>... callable) throws RejectedExecutionException {
		try {
			return service.invokeAll(Arrays.asList(callable));
		} catch (InterruptedException e) {
			return null;
		}
	}
	
	/**
	 * execute the tasks and wait until <b>some of</b> task was successfully (require only one task and method will done)
	 *
	 * @param callable
	 * 		the run task
	 * @param <T>
	 * 		the return type
	 * @return The resulting of the execute task that finish at first, or <br>
	 * {@code Null} if there have {@link InterruptedException} cause
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	@SafeVarargs
	public final <T> T executeAndWaitAny(Callable<T>... callable) throws RejectedExecutionException {
		try {
			return service.invokeAny(Arrays.asList(callable));
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}
	
	
	/**
	 * convert to {@link Callable} and run {@link #executeAndWaitAll(Callable[])}
	 *
	 * @param runnable
	 * 		the run task
	 * @return list of all task that represent the task that has been executed ({@link Future#get()} will return null if successful), or <br>
	 * {@code Null} if there have {@link InterruptedException} cause
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	public List<Future<?>> executeAndWaitAll(Runnable... runnable) throws RejectedExecutionException {
		Callable[] cs = Arrays.stream(runnable).map((Function<Runnable, Callable>) runnable1 -> toCall(runnable1, null)).toArray(Callable[]::new);
		return executeAndWaitAll(cs);
	}
	
	/**
	 * convert to {@link Callable} and run {@link #executeAndWaitAny(Callable[])}
	 *
	 * @param runnable
	 * 		the run task
	 * @throws RejectedExecutionException
	 * 		if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 * 		if runnable is null
	 * @see Future
	 */
	public void executeAndWaitAny(Runnable... runnable) throws RejectedExecutionException {
		Callable[] cs = Arrays.stream(runnable).map((Function<Runnable, Callable>) runnable1 -> toCall(runnable1, null)).toArray(Callable[]::new);
		executeAndWaitAny(cs);
	}
	
	/**
	 * if create this class, you must shutdown it at last
	 */
	public void shutdown() {
		if (!service.isShutdown()) service.shutdown();
	}
	
	/**
	 * to check is this class already shutdown
	 *
	 * @return {@code true}, if already shutdown; otherwise, return {@code false}
	 * @see ExecutorService#shutdown()
	 */
	public boolean isShutDown() {
		return service.isShutdown();
	}
	
	/**
	 * <b>Inherit docs from {@link ExecutorService#isTerminated()}</b> <br>
	 * Returns {@code true} if all tasks have completed following shut down.
	 * Note that {@code isTerminated} is never {@code true} unless
	 * either {@code shutdown} or {@code shutdownNow} was called first.
	 *
	 * @return {@code true}, if already terminal; otherwise, return {@code false}
	 * @see ExecutorService#isTerminated()
	 */
	public boolean isTerminate() {
		return service.isTerminated();
	}
	
	/**
	 * force shutdown and return the task that not execute successfully.
	 *
	 * @return list of all task that not finish yet!, or null if all task is done or already shutdown the service
	 * @see ExecutorService#shutdownNow()
	 */
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
	
	/**
	 * convert callable to runnable with <b>ignore</b> all exception and return value.
	 *
	 * @param callable
	 * 		call that want to conversion
	 * @return {@link Runnable}
	 */
	public static Runnable toRun(Callable<?> callable) {
		return () -> {
			try {
				callable.call();
			} catch (Exception ignore) {
			}
		};
	}
	
	/**
	 * convert runnable to callable with return value is parameter {@code returnValue}
	 *
	 * @param runnable
	 * 		run that want to conversion
	 * @param returnValue
	 * 		the return value
	 * @param <T>
	 * 		the class of {@link Callable} generic type
	 * @return {@link Callable}
	 */
	public static <T> Callable<T> toCall(Runnable runnable, T returnValue) {
		return () -> {
			runnable.run();
			return returnValue;
		};
	}
	
	/**
	 * This class implemented because I need the easy way to manage the {@link ScheduledFuture} <br>
	 * This class have new method call {@link #waitAndDone()} so that program will wait until program done
	 */
	public class ScheduleFutureImp implements ScheduledFuture {
		private ScheduledFuture future;
		private ExecutorService service;
		
		private ScheduleFutureImp(ScheduledFuture future, ExecutorService service) {
			this.future = future;
			this.service = service;
		}
		
		/**
		 * wait until execution was done, and return the result in term of true/false
		 *
		 * @return true iff the execute out because of cancellation (like method {@link #schedule(Runnable, int, int, int, TimeUnit, boolean)})
		 */
		public boolean waitAndDone() {
			Object o = get();
			done();
			return o != null;
		}
		
		private void done() {
			service.shutdown();
			if (!service.isShutdown()) service.shutdownNow();
		}
		
		@Override
		public long getDelay(TimeUnit unit) {
			return future.getDelay(unit);
		}
		
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return future.cancel(mayInterruptIfRunning);
		}
		
		@Override
		public boolean isCancelled() {
			return future.isCancelled();
		}
		
		@Override
		public boolean isDone() {
			return future.isDone();
		}
		
		/**
		 * this get will return 2 thing
		 * <ol>
		 * <li>{@link CancellationException} - if the task got cancel ({@link MultiThread#schedule(Runnable, int, int, int, TimeUnit, boolean)})</li>
		 * <li>{@code Null} - if have another exception or {@link ScheduledFuture#get()} return object successfully</li>
		 * </ol>
		 *
		 * @return null or {@link CancellationException}
		 */
		@Override
		public Object get() {
			try {
				future.get();
			} catch (CancellationException e) {
				return e;
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		@Override
		public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return future.get(timeout, unit);
		}
		
		public int compareTo(Delayed o) {
			return future.compareTo(o);
		}
		
		public int compareTo(Object o) {
			return future.compareTo(o);
		}
	}
}
