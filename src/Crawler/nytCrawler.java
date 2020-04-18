package Crawler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class nytCrawler {

	public static void main(String [] args) {
		String query = "http://www.nytimes.com";
		
		Document doc = null;
		
        try {
			doc = Jsoup.connect(query).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Elements set = doc.select("a[href]");
        
        List<String> url = new ArrayList<String>();        
        
        for(int i = 0; i < set.size(); i++) {
        	String link = set.get(i).attr("href").toString();
        	// System.out.println(set.get(i).attr("href").toString());
        	if(link.contains(".html")) {
        		link = link.substring(0, link.indexOf(".html")+5);
        		if(!link.contains("nytimes")) {
        			link = "https://www.nytimes.com" + link;
        		}
        		url.add(link);
        	} else if (link.contains("http")) {
        		if(link.contains("www.nytimes.com")) {
        			url.add(link);
        		}
        	}
        }
        
        for(int i = 0; i < url.size(); i++) {
        	//System.out.println(url.get(i));
        }
        
		
        String tmp = "https://www.nytimes.com/2020/04/02/us/politics/special-education-coronavirus.html";
        for(int i = 0; i < url.size(); i++) {
        	
        
        	try {
        		doc = Jsoup.connect(url.get(i)).get();
        		Elements body = doc.select("div.StoryBodyCompanionColumn");
        		String text = "";
        		Elements p = body.select("p");        
        		for(int j = 0; j < p.size(); j++) {
        			//System.out.println(p.get(j).text());
        			text += p.get(j).text();
        		}
        		
        		if(text.length() > 100) {
        			System.out.println(url.get(i));
        			Elements title = doc.select("head");
        			System.out.println(title.select("title").text());
        		}
	        
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        
        
        
        
		
	}
}
