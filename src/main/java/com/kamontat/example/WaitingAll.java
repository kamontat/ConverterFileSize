package com.kamontat.example;

import com.kamontat.utilities.ExecuteWorker;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 11:28 PM
 */
public class WaitingAll {
	public static void main(String[] args) {
		ExecuteWorker worker = new ExecuteWorker(Executors.newCachedThreadPool());
		// example 1 task to execute
		List<Future<Long>> list = worker.executeAndWaitAll(() -> {
			long a = 0;
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				a += i;
			}
			return a;
		});
		// beware return value can be null
		// learn more in javadoc
		if (list == null) throw new NullPointerException("null list");
		
		// must shutdown
		worker.shutdown();
	}
}
