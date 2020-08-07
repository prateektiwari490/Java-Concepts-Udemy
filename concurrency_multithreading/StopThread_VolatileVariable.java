package concurrency_multithreading;

import java.util.concurrent.TimeUnit;

public class StopThread_VolatileVariable {
	private static volatile boolean stop; // this volatile method is more preferrable

	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			public void run() {
				while (!stop) {
					System.out.println("In while ...");
				}
				System.out.println("Stop....");
			}
		}).start();

		TimeUnit.SECONDS.sleep(1);
		stop = true;
	}
}

// ------------------------------- Another method by using Synchronized
// method------------------------------

// public class StopThread {
// private static boolean stop;
// private static synchronized void requestStop() {
// stop = true;
// }
// private static synchronized boolean stop() {
// return stop;
// }
//
// public static void main(String[] args) throws InterruptedException {
// new Thread(new Runnable() {
// public void run() {
// while (!stop()) {
// System.out.println("In while ...");
// }
// System.out.println("Stop....");
// }
// }).start();
//
// TimeUnit.SECONDS.sleep(1);
// requestStop();
// }
// }
