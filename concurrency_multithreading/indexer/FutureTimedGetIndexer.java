package concurrency_multithreading.indexer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Uses only 4 threads. Improvises FutureIndexer
 * 
 * Note: htmlPage is NOT declared volatile in Weblink due to the below 
 *         memory visibility guarantee
 * 
 * Memory Visibility: Actions in a thread prior to the submission of a 
 *         Runnable or Callable task to an ExecutorService happen-before any 
 *         actions taken by that task, which in turn happen-before the result 
 *         is retrieved via Future.get().
 *         
 * Benefit: 
 *    Executor framework is used. So, we get thread management benefit!
 *    Better coordination of control flow between threads. Cleaner than wait-notify
 *    Thread blocking is no longer an issue
 *    
 * Difference from FutureIndexer:
 *    Uses Future's timed get method and an extra catch clause is added for canceling the task
 *  
 * @author Dheeru Mundluru
 *
 */

public class FutureTimedGetIndexer {
	private Deque<Weblink> queue = new ArrayDeque<>();
	
	// Executors
	ExecutorService downloadExecutor = Executors.newFixedThreadPool(2);
	ExecutorService indexerExecutor = Executors.newFixedThreadPool(2);
	
	private static final long TIME_FRAME = 2000000000L; // 2 seconds
	
	private static class Weblink {
		private long id;
	    private String title;
		private String url;
	    private String host;
	    
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
	
	private static class Downloader<T extends Weblink> implements Callable<T> {
		private T weblink;
		public Downloader(T weblink) {
			this.weblink = weblink;
		}
		public T call() {
			try {
				String htmlPage = HttpConnect.download(weblink.getUrl());
				weblink.setHtmlPage(htmlPage);				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return weblink;
		}
	}
	
	private static class Indexer implements Runnable {
		private long endTime;
		private Future<Weblink> future;
		
		private Indexer(long endTime, Future<Weblink> future) {
			this.endTime = endTime;
			this.future = future;
		}
		
		private Weblink weblink;
		
		public void run() {		
			long timeLeft = endTime - System.nanoTime();
			try {
				this.weblink = future.get(timeLeft, TimeUnit.NANOSECONDS);
				String htmlPage = weblink.getHtmlPage();
				index(htmlPage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				future.cancel(true);
				System.out.println("\n\nTask is cancelled --> " + Thread.currentThread());
			}
		}
		
		private void index(String text) {
			if (text != null) {
				System.out.println("\nIndexed: " + weblink.getId() + "\n");
			}
		}
	}
	
	public void go() {
		long endTime = System.nanoTime() + TIME_FRAME;
		List<Future<Weblink>> futures = new ArrayList<>();
		
		while (queue.size() > 0) {
			Weblink weblink = queue.remove();
			futures.add(downloadExecutor.submit(new Downloader<Weblink>(weblink)));
		}
		
		for (Future<Weblink> future : futures) {
			indexerExecutor.execute(new Indexer(endTime, future));
		}		
			
		downloadExecutor.shutdown();
		indexerExecutor.shutdown();
	}
	public void add(Weblink link) {
		queue.add(link);
	}
	public Weblink createWeblink(long id, String title, String url, String host) {
		Weblink weblink = new Weblink();
    	weblink.setId(id);
    	weblink.setTitle(title);
    	weblink.setUrl(url);
    	weblink.setHost(host);
    	return weblink;
    }
	
	public static void main(String[] args) {
		FutureTimedGetIndexer futureTimedGetIndexer = new FutureTimedGetIndexer();
		futureTimedGetIndexer.add(futureTimedGetIndexer.createWeblink(2000, "Taming Tiger, Part 2", "http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html", "http://www.javaworld.com"));
		futureTimedGetIndexer.add(futureTimedGetIndexer.createWeblink(2001, "How do I import a pre-existing Java project into Eclipse and get up and running?", "http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running", "http://www.stackoverflow.com"));
		futureTimedGetIndexer.add(futureTimedGetIndexer.createWeblink(2002, "Interface vs Abstract Class", "http://mindprod.com/jgloss/interfacevsabstract.html", "http://mindprod.com"));
		futureTimedGetIndexer.add(futureTimedGetIndexer.createWeblink(2004, "Virtual Hosting and Tomcat", "http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html", "http://tomcat.apache.org"));
		futureTimedGetIndexer.go();
	}
}
