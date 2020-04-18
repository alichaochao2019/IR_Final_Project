package Crawler;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class bbcCrawler {
	private static int globalCount = 0;
	static List<String> visited = new ArrayList<String>();

	public static void main(String [] args) {
		
		String query = "https://www.scmp.com/topics/coronavirus-china"; 
		List<String> queryList = new ArrayList<String>();
		//queryList.add("https://www.scmp.com/news/hong-kong");
		//queryList.add("https://www.scmp.com/news/china");
		//queryList.add("https://www.scmp.com/news/world");
		//queryList.add("https://www.scmp.com/topics/coronavirus-outbreak");

        LinkedList<String> queue = new LinkedList<String>(); 
        //queue.add("https://www.scmp.com/news/hong-kong/politics/article/3078524/hong-kongs-opposition-targets-legislative-council-seats-it");
		//queue.add("https://www.scmp.com/news/china");
		//queue.add("https://www.scmp.com/news/world");
		//queue.add("https://www.scmp.com/sport");
        queue.add("https://www.bbc.com/future/article/20190924-the-benefits-of-rebounding-after-a-break-up");
        queue.add("https://www.bbc.com/news/world-us-canada-52186185?intlink_from_url=&link_location=live-reporting-story");
		queue.add("https://www.bbc.com/news/coronavirus");
		queue.add("https://www.bbc.com/news/world");
		queue.add("https://www.bbc.com/news/world/us_and_canada");
		
		// List<String> visited = new ArrayList<String>();
		int i = 0;
		while(!queue.isEmpty()) {
			String url = queue.poll();
			if(visited.contains(url)) {
				continue;
			} 
			visited.add(url);
			
			List<String> tmp = getLinks(url);
			for(int j = 0; j < tmp.size(); j++) {
				//if(!visited.contains(tmp.get(j))) {
					queue.push(tmp.get(j));
					queryList.add(tmp.get(j));
					//System.out.println(tmp.get(j));
				//}
			}
			
			System.out.println(queue.size() + " " + queryList.size());
			if(i == 1) {
				//break;
			}
			i++;
		}
		
	
	}
	
	public static List<String> getLinks(String query){
		Document doc = null;
        try {
			doc = Jsoup.connect(query).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(doc == null) {
        	return new ArrayList<String>();
        } else {
        	String url = query;
            //System.out.println(url);
     		//Elements body = doc.select("p.print-article__body");
        	Elements p = doc.select("p");
     		Elements body = doc.select("p:not([class])");
     		String text = "";
     		for(int i = 0; i < body.size(); i ++) {
     			// System.out.println(body.get(i).text());
     			text += body.get(i).text() + "\n";
     		}
     		String abs = p.select("p.story-body__introduction").text().toString();
     		String title = doc.select("title").text();
     		Elements tm = doc.select("li.mini-info-list__item")
     				.select("div[data-datetime]");
     		String date = tm.size()>0?tm.get(0).text():"";
     		//System.out.println(title + " " + date);
     		//System.out.println(url);
     		if(url.contains("/article")) {
     			if(doc.select("span.author-unit__date").size() > 0) {
         			date = doc.select("span.author-unit__date").get(0).text();
     			}
     			// date = doc.select("span.author-unit__date").get(0).text();
     			// System.out.println(date);
     		}
 			// System.out.println(doc.select("span.author-unit__date").text());

     		if(text.length() > 1000) {
         		System.out.println(globalCount++);
     			try {
                    FileWriter writer = new FileWriter("bbc/" + globalCount+".txt", true);
                    writer.write(title + "\n");
                    writer.write(url + "\n");
                    writer.write(date + "\n");
                    writer.write(abs + " \n");
                    writer.write(text  + "\n");

                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
     		}
     		
        }
       
        
        Elements set = doc.select("a");
        List<String> retval = new ArrayList<String>();
        for(int i = 0; i < set.size(); i++) {
        	String url = set.get(i).attr("href");
        	int www = url.lastIndexOf("www");
        	if (url.equals("/") || url.equals("//") || url.contains("facebook") || www > 10){
        		continue;
        	} else if(url.contains("https://www.bbc.") ||
        			url.contains("http://www.bbc."))
        	{
        		// System.out.println(url);
        		String[] parts = url.split("/");
        		if(parts.length > 4 && !retval.contains("facebook")&&!retval.contains(url) && (url.contains("news") || url.contains("article"))) {
            		if(!visited.contains(url)) {
            			retval.add(url);

            		}
        			// retval.add(url);
        		}
        	} else if (url.contains("news") && !retval.contains("facebook") ) {
        		String newUrl = "https://www.bbc.com/" + url;
        		if(!retval.contains(newUrl) && !visited.contains(newUrl)) {
        			retval.add(newUrl);
        		}
        	}
        }
        
        /*
        Elements set = doc.select("a");
        
        List<String> relativePath = new ArrayList<String>();
        List<String> absolutePath = new ArrayList<String>();

        for(int i = 0; i < set.size(); i++) {
        	String url = set.get(i).attr("href");
        	if (url.equals("/") || url.equals("//")){
        		continue;
        	} else if (url.contains("https://www.scmp.com") || 
        			url.contains("http://www.scmp.com"))
        		{
        		if(!absolutePath.contains(url) && !url.contains("facebook")) {
            		absolutePath.add(url);
        		}
        	} else {
        		if(!relativePath.contains(url) && url.contains("article") && !url.contains("facebook")) {
            		relativePath.add(url);
        		}
        	}
        }
        
        // System.out.println("Total web links: " + (relativePath.size() + absolutePath.size()));
        
        List<String> links = new ArrayList<String>();
        
        for(int i = 0; i < relativePath.size(); i++) {
        	//System.out.println("https://www.scmp.com" +relativePath.get(i));
        	links.add("https://www.scmp.com" +relativePath.get(i));
        }
        for(int i = 0; i < absolutePath.size(); i++) {
        	links.add(absolutePath.get(i));
        }*/
    	return retval;
	}
}
