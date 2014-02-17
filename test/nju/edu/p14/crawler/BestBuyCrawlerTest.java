package nju.edu.p14.crawler;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BestBuyCrawlerTest {

	private BestBuyCrawler crawler;
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		crawler = (BestBuyCrawler)context.getBean("BestBuyCrawler");
	}

	@Test
	public void testCrawl() {
		crawler.crawl(BestBuyCrawler.bestBuyUrl_by_selling);
		crawler.crawl(BestBuyCrawler.bestBuyUrl_by_rating);
		crawler.crawl(BestBuyCrawler.bestBuyUrl_by_price);
		crawler.crawl(BestBuyCrawler.bestBuyUrl_by_arrival);
	}
	
	public static void main(String args[]) {
		BestBuyCrawlerTest crawler = new BestBuyCrawlerTest();
		try {
			crawler.setUp();
			crawler.testCrawl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
