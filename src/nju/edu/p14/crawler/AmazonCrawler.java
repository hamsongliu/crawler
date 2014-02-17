package nju.edu.p14.crawler;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nju.edu.common.BaseCrawler;
import nju.edu.p14.dao.ProductDAO;
import nju.edu.p14.model.Product;

public class AmazonCrawler extends BaseCrawler {
	
	private static Logger logger = Logger.getLogger(AmazonCrawler.class);
	private ProductDAO productDAO;
	
	final static String url_rank_by_selling = "http://www.amazon.com/s/ref=sr_pg_3?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A502394%2Cn%3A281052%2Cn%3A3017941&page=${page}&ie=UTF8&qid=1390012909";
	final static String url_rank_by_price = "http://www.amazon.com/gp/search/ref=sr_pg_2?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A502394%2Cn%3A281052%2Cn%3A3017941&page=${page}&sort=price&ie=UTF8&qid=1390018343";
	final static String url_rank_by_rating = "http://www.amazon.com/gp/search/ref=sr_pg_2?rh=n%3A172282%2Cn%3A%21493964%2Cn%3A502394%2Cn%3A281052%2Cn%3A3017941&page=${page}&sort=reviewrank_authority&ie=UTF8&qid=1390018406";

	@Override
	public void crawl(String url) {
		crawlList(url);
	}
	
	@SuppressWarnings("unchecked")
	public void crawlDetail() {
		List<Product> theProducts = productDAO.getHibernateTemplate().find("from Product where rankBySelling is not null and model is null");
		for(Product product : theProducts) {
			logger.warn("Crawling product: " + product.getId());
			try {
				String html = super.getHtmlByUrl(product.getUrl(), null, 3);
				Document document = Jsoup.parse(html);
				Elements properties = document.select("div#detail-bullets>table div.content>ul>li");
				for(int i = 0; i< properties.size(); i++) {
					String ctext = properties.get(i).text().trim();
					if(ctext.startsWith("Item model number: ")) {
						String model = ctext.replace("Item model number: ", "");
						product.setModel(model);
						productDAO.save(product);
						break;
					}
				}
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	private void crawlList(String url) {
		int incrementRank = 0;
		for(int i=1; i< 100; i++) {
			String purl = url.replace("${page}", String.valueOf(i));
			logger.warn("Crawling page: "+ purl);
			String html = super.getHtmlByUrl(purl, null, 3);
			if(null == html) {
				logger.error("Fetching url: " + purl +" failed");
				break;
			}
			Document document = Jsoup.parse(html);
			List<Element> productEls = document.select("div.grid>div.prod");
			
			for(Element el : productEls) {
				incrementRank++;
				try {
					Product product = extractSingleProduct(el);
					Product theProduct = productDAO.findById(product.getId());
					product = theProduct == null ? product : theProduct;
					if (url_rank_by_selling.equals(url)) {
						product.setRankBySelling(incrementRank);
					} else if(url_rank_by_price.equals(url)) {
						product.setRankByPrice(incrementRank);
					} else if(url_rank_by_rating.equals(url)) {
						product.setRankByRating(incrementRank);
					} 
					productDAO.save(product);
				} catch(Throwable e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private Product extractSingleProduct(Element el) {
		Product product = new Product();
		product.setStore("Amazon");
		product.setCategory("DSLR");
		product.setId(el.attr("name"));
		product.setTitle(el.select("h3.newaps>a>span.lrg").get(0).text());
		product.setBrand(product.getTitle().split(" ")[0]);
		try {
			// price of some product will not be visible
			String str_price = el.select("span.price").get(0).text().replace("$", "").replace(",", "");
			product.setPrice(Float.parseFloat(str_price));
		} catch(Throwable e) {
			logger.error("error parsing price: " + product.getId() );
		}
		try {
			String str_rating = el.select("li.rvw>span.asinReviewsSummary>a").get(0).attr("alt").split(" ")[0];
			product.setRating(Float.parseFloat(str_rating));
		} catch(Throwable e) {
			logger.error("error parsing rating: " + product.getId());
		}
		try {
			String str_review = el.select("span.rvwCnt>a").get(0).text().replace(",", "");
			product.setReviewCount(Integer.parseInt(str_review));
		} catch(Throwable e) {
			logger.error("error parsing reviews: " + product.getId());
		}
		product.setUrl(el.select("h3.newaps>a").get(0).attr("href"));
		return product;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}
}
