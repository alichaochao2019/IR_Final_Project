package Crawler;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class scmpCrawler {
	private static int globalCount = 0;
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
		queue.add("https://www.scmp.com/business");
		queue.add("https://www.scmp.com/tech");
		queue.add("https://www.scmp.com/economy");
		
		List<String> visited = new ArrayList<String>();
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
		
		
		
		// List<String> globalLinkPool = new ArrayList<String>();
		/*
        for(int i = 0; i < queryList.size(); i++) {
			List<String> tmp = getLinks(queryList.get(i));
			System.out.println(queryList.get(i));
			for(int j = 0; j < tmp.size(); j++) {
				if(!queryList.contains(tmp.get(j))) {
					queryList.add(tmp.get(j));
				}
				// // queryList.add(tmp.get(j));
			}
			System.out.println(queryList.size());
		}*/
	
        // System.out.println(queryList.size());
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
            System.out.println(url);
     		Elements body = doc.select("p.print-article__body");
     		//Elements body = doc.select("p");

     		Elements time = doc.select("p.last-update__published");
     		String tm = time.select("time").attr("datetime").toString();
     		if(url.contains("business") || url.contains("comment") || url.contains("sport")) {
     			body = doc.select("p");
     		}
     		if(body.text().length() > 200) {
     			System.out.println(tm);
         		System.out.println(body.text().toString());
         		String bodyText = "";
         		for(int k = 0 ; k < body.size(); k++) {
         			bodyText += body.get(k).text() + "\n"; 
         		}
         		String[] parts = url.split("/");
         		String title = parts[parts.length -1];
         		System.out.println(title);
         		globalCount++;

        		try {
                    FileWriter writer = new FileWriter("scmp/" +globalCount+".txt", true);
                    writer.write(title + "\n");
                    writer.write(url + "\n");
                    writer.write(tm + "\n");
                    writer.write(bodyText  + "\n");

                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
         		
     		}
     		
     		//System.out.println(doc.select("p.last-update__published").toString());
     		//System.out.println(time.select("time").attr("datetime").toString());
     		
        }
       
        
        
        
        
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
        }
        return links;
	}

}
