package com.kamontat.example;

import com.kamontat.utilities.MultiThread;

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
		MultiThread multiThread = new MultiThread(Executors.newScheduledThreadPool(1));
		
		List<Future<Long>> list = multiThread.executeAndWaitAll(() -> {
			long a = 0;
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				a += i;
			}
			return a;
		});
		
		if (list == null) throw new NullPointerException("null list");
		
		System.out.println(list.get(0).isDone());
		
		// must shutdown
		multiThread.shutdown();
	}
}
