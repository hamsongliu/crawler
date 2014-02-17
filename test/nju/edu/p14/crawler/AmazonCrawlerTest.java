package nju.edu.p14.crawler;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AmazonCrawlerTest {

	AmazonCrawler crawler;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		crawler = (AmazonCrawler)context.getBean("AmazonCrawler");
	}

	@Test
	public void testCrawl() {
		crawler.crawl(AmazonCrawler.url_rank_by_selling);
		crawler.crawl(AmazonCrawler.url_rank_by_rating);
		crawler.crawl(AmazonCrawler.url_rank_by_price);
	}
	
	public static void main(String args[]) {
		AmazonCrawlerTest test = new AmazonCrawlerTest();
		try {
			test.setUp();
			test.crawler.crawlDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
