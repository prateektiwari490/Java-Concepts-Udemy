package concurrency_multithreading.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExplicitLocksWithInterruption {
	private Deque<Weblink> queue = new ArrayDeque<>();

	private List<Thread> downloaderThreadList = new ArrayList<>();
	private List<Thread> indexerThreadList = new ArrayList<>();

	private static class Weblink {
		private long id;
		private String title;
		private String url;
		private String host;

		private volatile boolean stop;
		public boolean isStop() {
			return stop;
		}
		public void setStop(boolean stop) {
			this.stop = stop;
		}

		private String htmlPage;

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getHtmlPage() {
			return htmlPage;
		}
		public void setHtmlPage(String htmlPage) {
			this.htmlPage = htmlPage;
		}
	}

	private static class Downloader implements Runnable {
		private Weblink weblink;
		private Lock lock;
		private Condition pageCondition;
		public Downloader(Weblink weblink, Lock lock, Condition pageCondition) {
			this.weblink = weblink;
			this.lock = lock;
			this.pageCondition = pageCondition;
		}
		public void run() {
			lock.lock();
			try {
				// synchronized (weblink) {
				InputStream in = HttpConnect.getInputStream(weblink.getUrl());

				// Background thread for stopping download process
				Thread bgThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							while (!weblink.isStop()) {
								TimeUnit.SECONDS.sleep(1);
							}
							System.out.println("Time out. Closing stream for "
									+ weblink.getId());
							in.close();
						} catch (InterruptedException e) {
							System.out.println("bgThread interrupted -- "
									+ weblink.getId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				bgThread.start();

				String htmlPage;
				try {
					htmlPage = HttpConnect.download(in);
					System.out.println(
							weblink.getId() + " download complete ...");
					weblink.setHtmlPage(htmlPage);

					bgThread.interrupt();

					// weblink.notifyAll();
					pageCondition.signal();
				} catch (IOException e) {
					System.out.println(weblink.getId()
							+ " could not be downloaded. Terminating as stream closed!!!");;
				}
				// }
				// lock is released

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	private static class Indexer implements Runnable {
		private Weblink weblink;
		private Lock lock;
		private Condition pageCondition;
		private Indexer(Weblink weblink, Lock lock, Condition pageCondition) {
			this.weblink = weblink;
			this.lock = lock;
			this.pageCondition = pageCondition;
		}
		public void run() {

			try {

				// synchronized (weblink) {
				lock.lockInterruptibly();
				String htmlPage = weblink.getHtmlPage();

				while (htmlPage == null) {
					try {
						System.out.println(weblink.getId()
								+ " not yet downloaded. Waiting ...");
						// weblink.wait();
						pageCondition.await();
						System.out.println(weblink.getId() + " awakened!");
						htmlPage = weblink.getHtmlPage();
					} catch (InterruptedException e) {
						throw e;
					} // WAITING
				}
				index(htmlPage);
				// }
			} catch (InterruptedException e) {
				System.out
						.println(weblink.getId() + " (indexer) interrupted!!");

				// Clean-up: Stopping downloader thread indirectly
				weblink.setStop(true);

				Thread.currentThread().interrupt();
			} finally {
				if (!Thread.currentThread().isInterrupted())
					lock.unlock();
			}
		}
		private void index(String text) {
			if (text != null) {
				System.out.println("\nIndexed: " + weblink.getId() + "\n");
			}
		}
	}

	public void go() {
		while (queue.size() > 0) {
			Weblink weblink = queue.remove();

			Lock lock = new ReentrantLock();
			Condition pageCondition = lock.newCondition();

			Thread downloaderThread = new Thread(
					new Downloader(weblink, lock, pageCondition));
			Thread indexerThread = new Thread(
					new Indexer(weblink, lock, pageCondition));

			downloaderThread
					.setName("Downloader Thread with ID: " + weblink.getId());
			indexerThread.setName("Indexer Thread with ID: " + weblink.getId());

			downloaderThreadList.add(downloaderThread);
			indexerThreadList.add(indexerThread);

			downloaderThread.start();
			indexerThread.start();
		}

		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Interrupting downloader threads that got BLOCKED
		System.out.println("\nTime Up!!\n");
		for (int i = 0; i < downloaderThreadList.size(); i++) {
			Thread downloaderThread = downloaderThreadList.get(i);
			if (downloaderThread.isAlive()) {
				System.out.println(downloaderThread.getName()
						+ " is still active. Stopping it ...");
				indexerThreadList.get(i).interrupt();
			}
		}
	}
	public void add(Weblink link) {
		queue.add(link);
	}
	public Weblink createWeblink(long id, String title, String url,
			String host) {
		Weblink weblink = new Weblink();
		weblink.setId(id);
		weblink.setTitle(title);
		weblink.setUrl(url);
		weblink.setHost(host);
		return weblink;
	}

	public static void main(String[] args) {
		ExplicitLocksWithInterruption explicitLocksWithInterruption = new ExplicitLocksWithInterruption();
		explicitLocksWithInterruption.add(explicitLocksWithInterruption
				.createWeblink(2000, "Taming Tiger, Part 2",
						"https://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
						"http://www.javaworld.com"));

		explicitLocksWithInterruption.add(explicitLocksWithInterruption
				.createWeblink(2001, "Maruti reports 47% plunge in March sales",
						"https://www.rediff.com/business/report/coronavirus-maruti-reports-47-plunge-in-march-sales/20200401.htm",
						"http://www.rediff.com"));

		explicitLocksWithInterruption.add(explicitLocksWithInterruption
				.createWeblink(2002, "Java Generic Methods",
						"http://tutorials.jenkov.com/java-generics/methods.html",
						"http://tutorials.jenkov.com"));

		explicitLocksWithInterruption.add(explicitLocksWithInterruption
				.createWeblink(2003, "Virtual Hosting and Tomcat",
						"http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html",
						"http://tomcat.apache.org"));
		explicitLocksWithInterruption.go();
	}
}