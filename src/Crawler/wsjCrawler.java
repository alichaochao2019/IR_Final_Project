package Crawler;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class wsjCrawler {
	
	
	public static void main (String [] args) throws InterruptedException {
		
		//String query = "https://api.scraperapi.com/?api_key=bb7b525ff953d97fb704d40204b8e4ed&url=https://www.wsj.com/news/archive/";

		String query = "https://www.wsj.com/news/archive/";
		List<String> queryList =  new ArrayList<String>();
		
		// query for 2020
		for(int i = 1; i < 4; i++) {
			for(int j = 1; j < 31; j++) {
				String part = j < 10? ("0"+String.valueOf(j)):String.valueOf(j);
				//System.out.println("20200"+i+part);
				// queryList.add(query+"20200"+i+part);
			}
		}
		
		// query for 2019
		for(int i = 7; i < 13; i++) {
			for(int j = 1; j < 31; j++) {
				String month = i <10?("0"+String.valueOf(i)):String.valueOf(i);
				String part = j < 10? ("0"+String.valueOf(j)):String.valueOf(j);
				//System.out.println("2019"+month+part);
				//queryList.add(query+"2019"+month+part);
			}
		}
		
		// query for 2018
		for(int i = 9; i < 13; i++) {
			for(int j = 1; j < 31; j++) {
				String month = i <10?("0"+String.valueOf(i)):String.valueOf(i);
				String part = j < 10? ("0"+String.valueOf(j)):String.valueOf(j);
				//System.out.println("2018"+month+part);
				queryList.add(query+"2016"+month+part);
			}
		}
		
		
		System.out.println(queryList.size());
		Map<String, List<String>> outputMap = new HashMap<String, List<String>>();
		
		
		List<String> reRan = new ArrayList<String>();
		int totalCount = 0;
		for(int i = 0; i < queryList.size(); i++) {
			List<String> dailyNews = linksDaily(queryList.get(i));
			//fullList.addAll(dailyNews);
			//System.out.println("size: " + dailyNews.size());
			int count = 0;
			while(dailyNews.size() < 1) {
				dailyNews = linksDaily(queryList.get(i));
				//System.out.println(queryList.get(i)   +" " + dailyNews.size()+" results");
				count++;
				if(count >= 20) {
					break;
				}
			}
			
			TimeUnit.SECONDS.sleep(2);
			if(dailyNews.size() <= 1) {
				reRan.add(queryList.get(i));
				//System.out.println(queryList.get(i)   +" " + dailyNews.size()+" results");
			} else {
				outputMap.put(queryList.get(i), dailyNews);
				totalCount += dailyNews.size();
			}
		}
		
		
		// System.out.println(reRan.size());
		
		/*
		while(reRan.size() > 2) {
			List<String> badRan = new ArrayList<String>();
			for(int i = 0; i< reRan.size();i++) {
 				List<String> dailyNews = linksDaily(reRan.get(i));
				if(dailyNews.size() == 0) {
					badRan.add(reRan.get(i));
					System.out.println(reRan.get(i));
				} else {
					outputMap.put(reRan.get(i), dailyNews);
					totalCount += dailyNews.size();
				}
			}
			if(reRan.size() == badRan.size()) {
				break;
			}
			reRan = badRan;
		}
		*/
		
		
		System.out.println(outputMap.size() + "  " + totalCount);
	    outputMap("2016-3", outputMap);
	}
	
	
	public static void outputMap(String name, Map<String, List<String>> map) {
		
		
		try {
            FileWriter writer = new FileWriter(name + ".txt", true);
            writer.write("Day Counts: " + map.size() +"\n");
            for(Map.Entry e : map.entrySet()) {
                writer.write(e.getKey() +"\n");
    			List<String> linkList = (List<String>) e.getValue();
    			for(int i = 0; i < linkList.size(); i++) {
                    writer.write(linkList.get(i) +"\n");
    				//System.out.println(linkList.get(i));
    			}
    		}
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
	}
	
	public static List<String> linksDaily(String query){
		Document doc = null;
		
        try {
			doc = Jsoup.connect(query).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //System.out.println(doc.toString());
        Elements title = doc.select("title");
        System.out.println(title.toString());
        //System.out.println(doc.toString());
        Elements body = doc.select("h3").select("a");
        // Elements body2 = doc.select("h4");
        //System.out.println(body.toString());
        Elements script = doc.select("script");
        //System.out.println(script);
        Elements summary = doc.select("p");
        //System.out.println(summary.toString());
        
        Elements div = doc.select("p.WSJTheme--summary--12br5Svc ");
        //System.out.println(div.toString());
        
        Elements h3 = doc.select("div.WSJTheme--headline--3qd-ycaT ");
        
        
        Elements article = doc.select("article");
        //System.out.println(article.get(0).toString());
        System.out.println("article size: " + article.size());
        List<String> retval = new ArrayList<String>();

        for(int i = 0; i < article.size(); i++) {
        	//System.out.println(article.get(i).select("p.WSJTheme--summary--12br5Svc "));
        	Elements h = article.get(i).select("h3");
        	String TITLE = article.get(i).select("h3").text();
        	String URL = h.select("a").attr("href").toString();
        	String SUMMARY = article.get(i).select("p.WSJTheme--summary--12br5Svc ").text().toString();
        	//System.out.println(TITLE + "\n" + URL + "\n" + SUMMARY + "\n");
        	String entry = "Title: " + TITLE + "\n";
        	entry += "Url: " + URL + "\n";
        	entry += "Abstract: " + SUMMARY;
        	retval.add(entry);
        }
        
        
        return retval;
        /*
        List<String> summaryList = new ArrayList<String>();
        for(int i = 0; i < summary.size(); i++) {
        	// System.out.println(summary.get(i).attr("class").trim().equals("WSJTheme--summary--12br5Svc"));
        	if(summary.get(i).attr("class").trim().equals("WSJTheme--summary--12br5Svc")) {
            	summaryList.add(summary.get(i).text());
            	// System.out.println(summary.get(i).text());
        	}
        }
        
        // System.out.println("Summary size: " + summaryList.size());
        List<String> links = new ArrayList<String>();
        for(int i = 0; i < body.size(); i++) {
        	//System.out.println(body.get(i).text());
        	//System.out.println(body.get(i).attr("href").toString());
        	if(body.get(i).hasAttr("href")) {
        		String tit = body.get(i).text();
        		String url = body.get(i).attr("href").toString();
        		if(!links.contains(url)) {
        			links.add(tit + "\n" + url);
        			//System.out.println(url);
        			// System.out.println(summary.get(i).text());
        			//System.out.println(body.get(i).text() + " " + body.get(i).attr("href").toString());
        		} 
        	}
        }
        
        
        
        // System.out.println("Summary size: " + summaryList.size() +" " +"Link size: " + links.size());
        // System.out.println(summaryList.size() == links.size());
        //System.out.println("Link size: " + links.size());
        if(summaryList.size() == links.size()) {
        	List<String> retval = new ArrayList<String>();
        	for(int i = 0; i < links.size(); i++) {
        		retval.add(links.get(i) + "\n" + summaryList.get(i));
        	}
        	return retval;
        } else {
        	List<String> retval = new ArrayList<String>();
        	retval.add("one");
            System.out.println(h3.size() + " " + div.size());
        	return retval;
        }
        
        
        //System.out.println(links.get(123));
        //System.out.println(summaryList.get(1));
        // return links;
        // System.out.println(links.size());
         * 
         */
	}
}
