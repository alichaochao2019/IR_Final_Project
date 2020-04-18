package Crawler;
import java.io.IOException;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class SimpleBookCrawler {
	
	private final int MAX_PAGE_SIZE = 30;
	private static final int RESPONSE_TIMEOUT = 10;
	
	public static void main (String [] args) {
		
		//http://gen.lib.rus.ec/json.php?ids=1265&fields=Title,Author,MD5
		//https://b-ok.cc/md5/0B62E39F3BF4FB9BEACEE0B12E5D23BD
		//http://en.bookfi.net/md5/0B62E39F3BF4FB9BEACEE0B12E5D23BD
	
		List<BookTuple> books = crawlBookLists("Information Retrieval");
		
	}
	
	public static List<BookTuple> crawlBookLists(String q){
		String query = "http://gen.lib.rus.ec/search.php?req=" + q;
		List<Book> books = crawlBooks(query);
		List<BookTuple> bookTuples = new ArrayList<BookTuple>();
		
		for(int i = 0; i < books.size(); i++) {
			List<String> bookInfo = books.get(i).getAuthorTitleISBN();
			String bookIntro = "";
			for(int j = 0; j < bookInfo.size(); j++) {
				bookIntro += bookInfo.get(j) + " \n";
			}
			String year = books.get(i).getSixInfo().get(2);
			String size = books.get(i).getSixInfo().get(3);
			String url1, url2;
			if(books.get(i).getMd5().length() < 10) {
				url1 = books.get(i).getDownloadLinks().get(3);
				url2 = books.get(i).getDownloadLinks().get(4);

			} else {
				url1 = "https://b-ok.cc/md5/"+ books.get(i).getMd5();
				url2 = "http://en.bookfi.net/"+ books.get(i).getMd5();	
			}
			
			bookTuples.add(new BookTuple(bookIntro, year, size, url1, url2));
		}
		return bookTuples;
	}
	
	public static void crawlBookByID(String id) {
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(RESPONSE_TIMEOUT * 1000).build();
		// add request parameter, form parameters
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("gen.lib.rus.ec").setPath("/json.php/")
				.setParameter("ids", id).setParameter("fields", "Title,Author,MD5");
		builder.setParameter("column", "title");
		HttpPost newPost = null;
		try {
			URI uri = builder.build();
			newPost = new HttpPost(uri);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String json = "";
		HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

		try {
			HttpResponse response = client.execute(newPost);
			json = EntityUtils.toString(response.getEntity());

		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONArray js = (JSONArray) JSONObject.parse(json);
			System.out.println(js.toString());

		} catch(JSONException e) {
			return ;
		}
		
	}
	
	public static List<Book> crawlBooks(String query){
		Document doc = null;
		
        try {
			doc = Jsoup.connect(query).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Elements media = doc.select("table");
        //System.out.println(media.get(2).toString());
        
        Element trResults = media.get(2);
        //System.out.println(trResults.toString());

        
		Elements tr_list = trResults.select("tr");
		// System.out.println(tr_list.size());
		List<Book> retval = new ArrayList<Book>();
		int limit = tr_list.size();
		if(tr_list.size() > 30) {
			limit = 30;
		}
		for(int i = 1; i < tr_list.size(); i++) {
			Element tr = tr_list.get(i);
			Elements td = tr.select("td");
			int pos = 0;
			
			
			String md5 = "";
			List<String> downloadLinks = new ArrayList<String>();
			List<String> authorAndTitle = new ArrayList<String>();
			List<String> sixInfo = new ArrayList<String>();
			String id = "";
			for(int j = 0; j < td.size(); j++) {
				// System.out.println(td.get(j).toString());
				if(td.get(j).childrenSize() == 0) {
					String str = td.get(j).getElementsByTag("td").text().toString();
					if(pos++ == 0) {
						id = str;
						// System.out.println(id);
					}
					sixInfo.add(str);
					
					// System.out.println(pos++ + " " + str);
					// sixInfo.add(pos, str);
				}
				
				if(td.get(j).childrenSize() >= 1) {
					if(td.get(j).getElementsByTag("a").hasAttr("href") &&
							td.get(j).getElementsByTag("a").text().toString().contains("[")) {
						// download links
						String url = (td.get(j).getElementsByTag("a").attr("href").toString());
						// System.out.println(url);
						if(!downloadLinks.contains(url)) {
							downloadLinks.add(url);
						}
					} else {
						String val  = td.get(j).getElementsByTag("a").text().toString();
						if(td.get(j).getElementsByTag("a").size() > 1) {
							//val = td.get(j).getElementsByTag("a").get(1).text().toString();
						}
						//System.out.println(td.get(j).getElementsByTag("a").text().toString());
						if(td.get(j).getElementsByTag("a").hasAttr("href")) {
							String str = td.get(j).getElementsByTag("a").attr("href").toString();
							String part = str.split("=")[1];
							if(!part.contains("&")) {
								md5 = part;
							} 
						}
						// System.out.println(val);
						authorAndTitle.add(val);
					}
				} 
			}
			// System.out.println(authorAndTitle.toString());
			//if(authorAndTitle.size()>0) {
				Book book = new Book(authorAndTitle,md5,sixInfo, downloadLinks, id);
				// System.out.println(id);
				retval.add(book);
			//}
		}
		return retval;
	}

}
//