package com.kamontat.example;

import com.kamontat.utilities.ExecuteWorker;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author kamontat
 * @version 1.0
 * @since Wed 15/Mar/2017 - 11:42 PM
 */
public class ExecuteBackground {
	public static void main(String[] args) throws InterruptedException {
		int choose = 1;
		if (args.length > 1) {
			try {
				choose = Integer.parseInt(args[0]);
				if (choose != 1 && choose != 0)
					throw new NumberFormatException("The parameter should be integer 1 or 2 ONLY");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		if (choose == 1) {
			/* ---------- using executor ---------- */
			ExecuteWorker worker = new ExecuteWorker(Executors.newFixedThreadPool(1));
			// execution in bg
			Future f = worker.execute(() -> {
				// long long task
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					int a = 1;
				}
				System.out.println(Thread.currentThread() + ": done!");
			});
			
			System.out.println("might appear first");
			worker.shutdown();
			/* ---------- END ---------- */
		} else {
			/* ---------- using swing worker ---------- */
			ExecuteWorker.doBackground(() -> {
				int i = 0;
				while (i++ < 100) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread() + ": " + i);
				}
			});
			
			int i = 0;
			while (i++ < 40) {
				Thread.sleep(2000);
				System.out.println(Thread.currentThread() + ": " + i);
			}
			/* ---------- END ---------- */
		}
	}
}
