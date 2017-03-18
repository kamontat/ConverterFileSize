package com.kamontat.example;

import com.kamontat.utilities.MultiThread;

import java.util.concurrent.*;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 11:53 PM
 */
public class DoInterval {
	public static void main(String[] args) {
		// create schedule pool
		MultiThread multiThread = new MultiThread(Executors.newScheduledThreadPool(1));
		// print `Hello world!` every 1 second for 10 second
		MultiThread.ScheduleFutureImp future = multiThread.schedule(() -> System.out.println("Hello world!"), 1, 1, 10, TimeUnit.SECONDS, true);
		
		// after this method will call automatically when schedule was done
		if (future.waitAndDone()) {
			System.out.println("done");
		} else {
			System.err.println("Some exception");
		}
		// must shut down
		multiThread.shutdown();
		
		/*
		The result will be:
		
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		Hello world!
		done
		 */
		
		
		// error example
		MultiThread thread = new MultiThread(Executors.newCachedThreadPool());
		MultiThread.ScheduleFutureImp newFuture = thread.schedule(() -> System.out.println("Hello world!"), 1, 1, 10, TimeUnit.SECONDS, true);
		
		/*
		The result will be:
		
		Exception in thread "main" com.kamontat.utilities.MultiThread$ScheduleException: The service must be ScheduledExecutorService
			at com.kamontat.utilities.MultiThread.schedule(MultiThread.java:78)
			at com.kamontat.example.DoInterval.main(DoInterval.java:46)
			. . .
		 */
	}
}
