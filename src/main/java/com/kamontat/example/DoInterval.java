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
		MultiThread multiThread = new MultiThread(Executors.newScheduledThreadPool(1));
		// print `Hello world!` every 1 second for 10 second
		MultiThread.ScheduleFutureImp future = multiThread.schedule(() -> System.out.println("Hello world!"), 1, 1, 10, TimeUnit.SECONDS, true);
		
		if (future.waitAndDone()) {
			System.out.println("done");
		} else {
			System.err.println("Some exception");
		}
		
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
	}
}
