package com.kamontat.example;

import com.kamontat.utilities.MultiThread;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 11:53 PM
 */
public class DoInterval {
	public static void main(String[] args) {
		MultiThread multiThread = new MultiThread(Executors.newScheduledThreadPool(1));
		// print `Hello world!` every 1 second for 1 minute
		multiThread.schedule(() -> System.out.println("Hello world!"), 0, 1, 60, TimeUnit.SECONDS);
		multiThread.schedule(() -> System.out.println("New world"), 0, 2, 60, TimeUnit.SECONDS);
		
		multiThread.shutdown();
	}
}
