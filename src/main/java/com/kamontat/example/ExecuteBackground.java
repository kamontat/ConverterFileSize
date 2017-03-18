package com.kamontat.example;

import com.kamontat.utilities.MultiThread;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 11:42 PM
 */
public class ExecuteBackground {
	public static void main(String[] args) {
		MultiThread multiThread = new MultiThread(Executors.newFixedThreadPool(1));
		// execution in bg
		Future f = multiThread.execute(() -> {
			// long long task
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				int a = 1;
			}
			System.out.println("done");
		});
		
		System.out.println("might appear first");
		multiThread.shutdown();
	}
}
