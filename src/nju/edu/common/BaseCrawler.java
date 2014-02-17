package nju.edu.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public abstract class BaseCrawler {
	private static final Logger log = Logger.getLogger(BaseCrawler.class);

	/**
	 * get html document by given url
	 * @param url
	 * @return
	 */
	public String getHtmlByUrl(String url, HttpHost proxy, int tryTimes){
		HttpClient httpClient;
		String html = null;
		while(html == null && tryTimes-->0){
			httpClient = new DefaultHttpClient();
			if(proxy !=null ){
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	        }
			InputStream instream = null;  
			HttpGet httpget = null;
	        try {  
	        	httpget = new HttpGet(url);
		        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
		        HttpResponse responce = httpClient.execute(httpget);
	            int resStatus = responce.getStatusLine().getStatusCode();
	            if (resStatus==HttpStatus.SC_OK) {
	                HttpEntity entity = responce.getEntity();  
	                if (entity != null) {  
	                	instream = entity.getContent();
	                    html = EntityUtils.toString(entity);
	                    instream.close();
	                }  
	            } else {
	            	httpget.abort();
	            	html = null;
	            }
	        } catch (Exception e) {  
	        	if(httpget != null){
	        		httpget.abort();
	        	}
            	html = null;
	        	e.printStackTrace();
	        	log.warn("Retrive: "+url+" Failed!");  
	        } finally {  
	    		httpClient.getConnectionManager().shutdown();  
	    		if(instream != null){
	    			try {
						instream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	    		}
	        }
		}
		if(null != html)
			html = html.trim();
        return html;  
	}
	
	/**
	 * crawl data based on different tasks
	 */
	public abstract void crawl(String url);

}
