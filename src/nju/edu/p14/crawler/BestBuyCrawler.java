package nju.edu.p14.crawler;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import nju.edu.common.BaseCrawler;
import nju.edu.p14.dao.ProductDAO;
import nju.edu.p14.model.Product;

public class BestBuyCrawler extends BaseCrawler {
	
	private static Logger logger = Logger.getLogger(BestBuyCrawler.class);
	private ProductDAO productDAO;

	final static String bestBuyUrl_by_selling = "http://www.bestbuy.com/site/olstemplatemapper.jsp?id=pcat17071&type=page&st=pcmcat186400050004_categoryid%24abcat0401005&sc=Global&nrp=50&sp=-bestsellingsort+skuidsaas&qp=&usc=All+Categories&fs=saas&browsedCategory=pcmcat186400050004&seeAll=&gf=y&cp=";
	final static String bestBuyUrl_by_rating = "http://www.bestbuy.com/site/olstemplatemapper.jsp?id=pcat17071&type=page&st=pcmcat186400050004_categoryid%24abcat0401005&sc=Global&nrp=50&sp=customerrating+numberofreviewssaas&qp=&usc=All+Categories&fs=saas&browsedCategory=pcmcat186400050004&seeAll=&gf=y&cp=";
	final static String bestBuyUrl_by_price = "http://www.bestbuy.com/site/olstemplatemapper.jsp?id=pcat17071&type=page&st=pcmcat186400050004_categoryid%24abcat0401005&sc=Global&nrp=50&sp=%2Bcurrentprice+skuidsaas&qp=&usc=All+Categories&fs=saas&browsedCategory=pcmcat186400050004&seeAll=&gf=y&cp=";
	final static String bestBuyUrl_by_arrival = "http://www.bestbuy.com/site/olstemplatemapper.jsp?id=pcat17071&type=page&st=pcmcat186400050004_categoryid%24abcat0401005&sc=Global&nrp=50&sp=-displaydate+skuidsaas&qp=&usc=All+Categories&fs=saas&browsedCategory=pcmcat186400050004&seeAll=&gf=y&cp=";
	
	@Override
	public void crawl(String url) {
		int incrementRank = 0;
		for(int i=0; i< 10; i++) {
			String page = url+String.valueOf(i+1);
			String html = super.getHtmlByUrl(page, null, 3);
			if(null == html) {
				logger.error("Fetching url: " + page +" failed");
				break;
			}
			Document document = Jsoup.parse(html);
			List<Element> productEls = document.select("div.hproduct");
			
			for(Element el : productEls) {
				incrementRank++;
				try {
					Product product = extractSingleProduct(el);
					Product theProduct = productDAO.findById(product.getId());
					product = theProduct == null ? product : theProduct;
					if (bestBuyUrl_by_selling.equals(url)) {
						product.setRankBySelling(incrementRank);
					} else if(bestBuyUrl_by_price.equals(url)) {
						product.setRankByPrice(incrementRank);
					} else if(bestBuyUrl_by_rating.equals(url)) {
						product.setRankByRating(incrementRank);
					} else if(bestBuyUrl_by_arrival.equals(url)) {
						product.setRankByArrival(incrementRank);
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
		product.setCategory("DSLR");
		product.setStore("BestBuy");
		product.setId(el.attr("id"));
		product.setTitle(el.select("div.info-main>h3>a").get(0).text());
		product.setBrand(product.getTitle().split(" - ")[0]);
		product.setModel(el.select("div.attributes>h5>strong").get(0).text());
		product.setSku(el.select("div.attributes>h5>strong").get(1).text());
		try {
			// price of some product will not be visible
			String str_price = el.select("h4.price>span").get(0).text().replace("$", "").replace(",", "");
			product.setPrice(Float.parseFloat(str_price));
		} catch(Throwable e) {
			logger.error("error parsing price: " + product.getId() );
		}
		try {
			String str_rating = el.select("div.rating>span>strong>span").get(0).text().replace(" ", "");
			product.setRating(Float.parseFloat(str_rating));
		} catch(Throwable e) {
			logger.error("error parsing rating: " + product.getId());
		}
		try {
			String str_review = el.select("div.rating>span>a>span").get(0).text().replace(",", "");
			product.setReviewCount(Integer.parseInt(str_review));
		} catch(Throwable e) {
			logger.error("error parsing reviews: " + product.getId());
		}
		product.setUrl("http://www.bestbuy.com" + el.select("div.info-main>h3>a").get(0).attr("href"));
		return product;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}
