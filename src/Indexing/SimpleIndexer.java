package Indexing;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.PorterStemmer;


public class SimpleIndexer {

	private static HashMap<String, TreeMap<Integer, Integer>> tfIndex; // term : docID-frequency
	
	private static HashMap<String, TreeMap<Integer, Integer>> wsjIndex; // term : docID-frequency
	private static HashMap<String, TreeMap<Integer, Integer>> ntIndex; // term : docID-frequency
	private static HashMap<String, TreeMap<Integer, Integer>> bbcIndex; // term : docID-frequency
	private static HashMap<String, TreeMap<Integer, Integer>> scmpIndex; // term : docID-frequency

	private static HashMap<Integer, News> newsMap;
	private static StopWordHandler swHandler = new StopWordHandler("./data/stoplist.txt");
	private static PorterStemmer stemmer = new PorterStemmer();
	private static HashMap<String, Integer> dfIndex;;
	
	
	
	public static List<String> getTopTerm(int k){
		//Map<String, Integer> ndex = SimpleIndexer.getSortedDFIndex();
		int index = 0;
		List<String> retval = new ArrayList<String>();
		
		for(Map.Entry e : dfIndex.entrySet()) {
			retval.add((String) e.getKey());
			if(index++ == k) {break;}
		}
		return retval;
	}
	
	
	public static HashMap<String, TreeMap<Integer, Integer>> getTFIndex(){
		return tfIndex;
	}
	public static HashMap<String, TreeMap<Integer, Integer>> getWSJTFIndex(){
		return wsjIndex;
	}public static HashMap<String, TreeMap<Integer, Integer>> getBBCTFIndex(){
		return bbcIndex;
	}public static HashMap<String, TreeMap<Integer, Integer>> getSCMPTFIndex(){
		return scmpIndex;
	}public static HashMap<String, TreeMap<Integer, Integer>> getNTTFIndex(){
		return ntIndex;
	}
	
	public static HashMap<Integer, News> getNewsMap(){
		return newsMap;
	}
	
	public static HashMap<String, Integer> getDFIndex(){
		return dfIndex;
	}
	
	public static Set<String> getSortedDFIndex(){
        Map<String, Integer> sortedMap = sortByComparator(dfIndex, false);
        return sortedMap.keySet();
	}
	
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	
	public static void initialize() {
		tfIndex = new  HashMap<String, TreeMap<Integer, Integer>>();
		newsMap = new HashMap<Integer, News>();
		dfIndex = new HashMap<String, Integer>();
		wsjIndex = new  HashMap<String, TreeMap<Integer, Integer>>(); // term : docID-frequency
		ntIndex = new  HashMap<String, TreeMap<Integer, Integer>>(); // term : docID-frequency
		bbcIndex = new  HashMap<String, TreeMap<Integer, Integer>>(); // term : docID-frequency
		scmpIndex = new  HashMap<String, TreeMap<Integer, Integer>>();
	}
	
	public static void indexTwitter() throws FileNotFoundException {
		List<News> twList = new ArrayList<News>();
		try {
			twList = parseAllTwitterFile("twitter");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("twitter collection size: " + twList.size());

		for(int i = 0; i < twList.size(); i++) {
			News news = twList.get(i);
			newsMap.put(i + 300000, news);
			addNewsToIndex(news, i + 300000);
			// addNewsToWSJIndex(news, i);
		}
	}
	public static void indexWSJ() throws FileNotFoundException{
		List<News> wsjList = parseAllWSJFile("wsj");
		System.out.println("Wsj collection size: " + wsjList.size());
	
		for(int i = 0; i < wsjList.size(); i++) {
			News news = wsjList.get(i);
			newsMap.put(i, news);
			addNewsToIndex(news, i);
			// addNewsToWSJIndex(news, i);
		}
	}
	
	public static void indexNYT() throws FileNotFoundException{
		List<News> nytList = parseAllNYTFile("nyt");
		System.out.println("Nyt collection size: " + nytList.size());
		for(int i = 0; i < nytList.size(); i++) {
			News news = nytList.get(i);
			newsMap.put(i + 200000, news);
			addNewsToIndex(news, i + 200000);
			// addNewsToNTIndex(news, i + 200000);

		}
	}
	
	public static void indexBBC() throws FileNotFoundException{
		List<News> bbcList = parseAllBBCFile("bbc");
		System.out.println("Bbc collection size: " + bbcList.size());
		for(int i = 0; i < bbcList.size(); i++) {
			News news = bbcList.get(i);
			newsMap.put(i + 220000, news);
			addNewsToIndex(news, i + 220000);
			// addNewsToBBCIndex(news, i + 220000);

		}
	}
	
	public static void indexSCMP() throws FileNotFoundException{
		List<News> scmpList = parseAllSCMPFile("scmp");
		System.out.println("Scmp collection size: " + scmpList .size());
		for(int i = 0; i < scmpList.size(); i++) {
			News news = scmpList.get(i);
			newsMap.put(i + 240000, news);
			addNewsToIndex(news, i + 240000);
			// addNewsToSCMPIndex(news, i + 240000);
		}
	}
	
	
	
	public static void main(String [] args) throws IOException {
		
		//List<News> twitterList = parseAllTwitterFile("twitter");
		//System.out.println("Twiteets size : " + twitterList.size());
		// index all crawled news
		
	}
	
	public static void addNewsToIndex(News news, int id) {
		String title = news.getTitle();
		String body = news.getText();
		String text = title + "\n" + body;
		boolean wsj = news.getUrl().contains("wsj");
		boolean bbc= news.getUrl().contains("bbc");
		boolean nyt = news.getUrl().contains("nyt");
		boolean scmp = news.getUrl().contains("scmp");

		String[] lines = text.split(System.getProperty("line.separator"));
		for (String tmpLine : lines) {
			
			// Step 1: Tokenization
			// Chop on whitespace, throw away punctuation characters
			// Keep special characters, apostrophe ' and hyphen -
			String[] tokens = tmpLine.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
					.split("\\s+");

			for (int i = 0; i < tokens.length;i++) {
				if (swHandler.isStopWord(tokens[i])) {
					tokens[i] = "";
					continue;
				}
				if (tokens[i].indexOf('-') < 0) {
					String pureTerm = tokens[i];
					String term = stemmer.stem(tokens[i]);
					
					if(wsj) {
						if(dfIndex.containsKey(pureTerm)) {
							dfIndex.put(pureTerm, dfIndex.get(pureTerm) + 1);
						} else {
							dfIndex.put(pureTerm, 1);
						}
					}
					
					
					if (tfIndex.containsKey(term)) {
						if(tfIndex.get(term).containsKey(id)) {
							tfIndex.get(term).put(id, tfIndex.get(term).get(id)+1);
						} else {
							tfIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						tfIndex.put(term, newTree);
					}
				}else {
					String[] subtokens = tokens[i].split("-");
					String pureTerm = String.join(" ", subtokens);
					for (int j = 0; j < subtokens.length; j++) {
						subtokens[j] = stemmer.stem(subtokens[j]);
					}

					String term = String.join("-", subtokens);
					
					if(wsj) {
						if(dfIndex.containsKey(pureTerm)) {
							dfIndex.put(pureTerm, dfIndex.get(pureTerm) + 1);
						} else {
							dfIndex.put(pureTerm, 1);
						}
					}
					
					
					if (tfIndex.containsKey(term)) {
						if(tfIndex.get(term).containsKey(id)) {
							tfIndex.get(term).put(id, tfIndex.get(term).get(id)+1);
						} else {
							tfIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						tfIndex.put(term, newTree);
					}
				}
			}
		
		}
		
	}
	
	public static void addNewsToWSJIndex(News news, int id) {
		String title = news.getTitle();
		String body = news.getText();
		String text = title + "\n" + body;
		
		String[] lines = text.split(System.getProperty("line.separator"));
		for (String tmpLine : lines) {
			
			// Step 1: Tokenization
			// Chop on whitespace, throw away punctuation characters
			// Keep special characters, apostrophe ' and hyphen -
			String[] tokens = tmpLine.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
					.split("\\s+");

			for (int i = 0; i < tokens.length;i++) {
				if (swHandler.isStopWord(tokens[i])) {
					tokens[i] = "";
					continue;
				}
				if (tokens[i].indexOf('-') < 0) {
					String pureTerm = tokens[i];
					String term = stemmer.stem(tokens[i]);
					
					if (wsjIndex.containsKey(term)) {
						if(wsjIndex.get(term).containsKey(id)) {
							wsjIndex.get(term).put(id, wsjIndex.get(term).get(id)+1);
						} else {
							wsjIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						wsjIndex.put(term, newTree);
					}
				}else {
					String[] subtokens = tokens[i].split("-");
					String pureTerm = String.join(" ", subtokens);
					for (int j = 0; j < subtokens.length; j++) {
						subtokens[j] = stemmer.stem(subtokens[j]);
					}

					String term = String.join("-", subtokens);
					if (wsjIndex.containsKey(term)) {
						if(wsjIndex.get(term).containsKey(id)) {
							wsjIndex.get(term).put(id, wsjIndex.get(term).get(id)+1);
						} else {
							wsjIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						wsjIndex.put(term, newTree);
					}
				}
			}
		
		}
	}
	public static void addNewsToNTIndex(News news, int id) {
		String title = news.getTitle();
		String body = news.getText();
		String text = title + "\n" + body;
		
		String[] lines = text.split(System.getProperty("line.separator"));
		for (String tmpLine : lines) {
			
			// Step 1: Tokenization
			// Chop on whitespace, throw away punctuation characters
			// Keep special characters, apostrophe ' and hyphen -
			String[] tokens = tmpLine.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
					.split("\\s+");

			for (int i = 0; i < tokens.length;i++) {
				if (swHandler.isStopWord(tokens[i])) {
					tokens[i] = "";
					continue;
				}
				if (tokens[i].indexOf('-') < 0) {
					String pureTerm = tokens[i];
					String term = stemmer.stem(tokens[i]);
					
					if (ntIndex.containsKey(term)) {
						if(ntIndex.get(term).containsKey(id)) {
							ntIndex.get(term).put(id, ntIndex.get(term).get(id)+1);
						} else {
							ntIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						ntIndex.put(term, newTree);
					}
				}else {
					String[] subtokens = tokens[i].split("-");
					String pureTerm = String.join(" ", subtokens);
					for (int j = 0; j < subtokens.length; j++) {
						subtokens[j] = stemmer.stem(subtokens[j]);
					}

					String term = String.join("-", subtokens);
					if (ntIndex.containsKey(term)) {
						if(ntIndex.get(term).containsKey(id)) {
							ntIndex.get(term).put(id, ntIndex.get(term).get(id)+1);
						} else {
							ntIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						ntIndex.put(term, newTree);
					}
				}
			}
		
		}
	}
	
	
	public static void addNewsToBBCIndex(News news, int id) {
		String title = news.getTitle();
		String body = news.getText();
		String text = title + "\n" + body;
		
		String[] lines = text.split(System.getProperty("line.separator"));
		for (String tmpLine : lines) {
			
			// Step 1: Tokenization
			// Chop on whitespace, throw away punctuation characters
			// Keep special characters, apostrophe ' and hyphen -
			String[] tokens = tmpLine.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
					.split("\\s+");

			for (int i = 0; i < tokens.length;i++) {
				if (swHandler.isStopWord(tokens[i])) {
					tokens[i] = "";
					continue;
				}
				if (tokens[i].indexOf('-') < 0) {
					String pureTerm = tokens[i];
					String term = stemmer.stem(tokens[i]);
					
					if (bbcIndex.containsKey(term)) {
						if(bbcIndex.get(term).containsKey(id)) {
							bbcIndex.get(term).put(id, bbcIndex.get(term).get(id)+1);
						} else {
							bbcIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						bbcIndex.put(term, newTree);
					}
				}else {
					String[] subtokens = tokens[i].split("-");
					String pureTerm = String.join(" ", subtokens);
					for (int j = 0; j < subtokens.length; j++) {
						subtokens[j] = stemmer.stem(subtokens[j]);
					}

					String term = String.join("-", subtokens);
					if (bbcIndex.containsKey(term)) {
						if(bbcIndex.get(term).containsKey(id)) {
							bbcIndex.get(term).put(id, bbcIndex.get(term).get(id)+1);
						} else {
							bbcIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						bbcIndex.put(term, newTree);
					}
				}
			}
		
		}
	}
	
	
	public static void addNewsToSCMPIndex(News news, int id) {
		String title = news.getTitle();
		String body = news.getText();
		String text = title + "\n" + body;
		
		String[] lines = text.split(System.getProperty("line.separator"));
		for (String tmpLine : lines) {
			
			// Step 1: Tokenization
			// Chop on whitespace, throw away punctuation characters
			// Keep special characters, apostrophe ' and hyphen -
			String[] tokens = tmpLine.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
					.split("\\s+");

			for (int i = 0; i < tokens.length;i++) {
				if (swHandler.isStopWord(tokens[i])) {
					tokens[i] = "";
					continue;
				}
				if (tokens[i].indexOf('-') < 0) {
					String pureTerm = tokens[i];
					String term = stemmer.stem(tokens[i]);
					
					if (scmpIndex.containsKey(term)) {
						if(scmpIndex.get(term).containsKey(id)) {
							scmpIndex.get(term).put(id, scmpIndex.get(term).get(id)+1);
						} else {
							scmpIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						scmpIndex.put(term, newTree);
					}
				}else {
					String[] subtokens = tokens[i].split("-");
					String pureTerm = String.join(" ", subtokens);
					for (int j = 0; j < subtokens.length; j++) {
						subtokens[j] = stemmer.stem(subtokens[j]);
					}

					String term = String.join("-", subtokens);
					if (scmpIndex.containsKey(term)) {
						if(scmpIndex.get(term).containsKey(id)) {
							scmpIndex.get(term).put(id, scmpIndex.get(term).get(id)+1);
						} else {
							scmpIndex.get(term).put(id, 1);
						}
					} else {
						TreeMap<Integer, Integer> newTree = new TreeMap<Integer, Integer>();
						newTree.put(id,1);
						scmpIndex.put(term, newTree);
					}
				}
			}
		
		}
	}
	
	
	public static List<News> parseAllTwitterFile(String twPath) throws IOException{
		File f = new File("twitter");
		File[] fileLists = f.listFiles();
		List<News> retval = new ArrayList<News>();

		for(int i = 0; i < fileLists.length; i++) {
			if(fileLists[i].getName().contains("DS_Store")){
				continue;
			}
			// System.out.println(fileLists[i].getName());
			BufferedReader csvReader = new BufferedReader(new FileReader(fileLists[i]));
			String row = csvReader.readLine();
			while ((row = csvReader.readLine()) != null) {
			    String[] data = row.split(",");
			    // System.out.println(Arrays.toString(data));
			    String accountName = data[1];
			    String timeStamp = data[3];
			    String text = data[4];
			    String [] parts = timeStamp.split("-");
			    if(parts.length < 2) {
			    	continue;
			    }
			    String[] secondParts = parts[1].split(" ");
			    // System.out.println(Arrays.toString(secondParts));
			    if(secondParts.length < 2)continue;
			    String add = secondParts[1].length() > 1? "" :"0";
			    String newTimeStamp = timeStamp.contains("Apr")?secondParts[3] + "04" +add+ secondParts[1]
			    		: secondParts[3] + "03" + add+secondParts[1];
			    if(!text.contains("Null")) {
			    	retval.add(new News(accountName, "TWITTERID", text, newTimeStamp));
			    }
			}
			csvReader.close();
		}
		return retval;
	}
	public static List<News> parseAllBBCFile(String scmpPath) throws FileNotFoundException{
		File f = new File("bbc");
		File[] fileLists = f.listFiles();
		// System.out.println("BBC collection size: " + fileLists.length);
		List<News> retval = new ArrayList<News>();
		for(int i = 0; i < fileLists.length; i++) {
			if(fileLists[i].getName().contains("DS_Store")){
				continue;
			}
			File file = fileLists[i];

		    Scanner sc = new Scanner(file); 

			String title = sc.nextLine();
			title = title.replace("-", "");
			String url = sc.nextLine();
			String date = sc.nextLine();
			String abs = sc.nextLine();
			String body = "";
			while(sc.hasNextLine()) {
		    	body += sc.nextLine();
		    }
			String fullBody = abs + body;
			String newTime = "";
			if(date.contains("www") || date.contains("http")) {
				newTime = "20190909";
			} else {
				String[] parts = date.split(" ");
				String month = "";
				if(parts.length < 3) {
					newTime = "20190909";
				} else {
					if(date.contains("January")) month = "01";
					if(date.contains("February")) month = "02";
					if(date.contains("Mar")) month = "03";
					if(date.contains("April")) month = "04";
					if(date.contains("May")) month = "05";
					if(date.contains("June")) month = "06";
					if(date.contains("Jul")) month = "07";
					if(date.contains("August")) month = "08";
					if(date.contains("September")) month = "09";
					if(date.contains("October")) month = "10";
					if(date.contains("Nov")) month = "11";
					if(date.contains("Dec")) month = "12";
					String da = parts[0].length() >1? parts[0] : "0" + parts[0].replaceAll("[^0-9]", "");
					da = da.replaceAll("[^0-9]", "");
					newTime = parts[2] + month + da;
				}
				
			}
    		retval.add(new News(title.replace("BBC News", ""), url, fullBody, newTime));

		}
		
		return retval;
	}
	

	public static List<News> parseAllSCMPFile(String scmpPath) throws FileNotFoundException{
		File f = new File("scmp");
		File[] fileLists = f.listFiles();
		//System.out.println("SCMP collection size: " + fileLists.length);
		List<News> retval = new ArrayList<News>();
		for(int i = 0; i < fileLists.length; i++) {
			if(fileLists[i].getName().contains("DS_Store")){
				continue;
			}
			//System.out.println(fileLists[i].getName());
			File file = fileLists[i];
		    Scanner sc = new Scanner(file); 
		    String tit = sc.nextLine();
		    String url = sc.nextLine();
		    String time = sc.nextLine();
		    String body = "";
		    // System.out.println(time);
		    String date = time.trim().equals("")?"20190909" : time.substring(0, 10).replace("-", "");
		    // System.out.println(date);

		    while(sc.hasNextLine()) {
		    	body += sc.nextLine();
		    }
    		retval.add(new News(tit.replace("-", " "), url, body, date));

		}
		return retval;
	}
	
	public static List<News> parseAllNYTFile(String nytPath) throws FileNotFoundException{
		File f = new File("nyt");
		File[] fileLists = f.listFiles();
		// System.out.println("Nyt collection size: " + fileLists.length);
		List<News> retval = new ArrayList<News>();
		for(int i = 0; i < fileLists.length; i++) {
			File file = fileLists[i];
			if(fileLists[i].getName().contains("DS_Store")){
				continue;
			}
		    Scanner sc = new Scanner(file); 
		    String url = sc.nextLine();
		    String tit = sc.nextLine();
		    String meta = sc.nextLine();
		    String num = sc.nextLine();
		    String body = "";
		    while(sc.hasNextLine()) {
		    	body += sc.nextLine();
		    }
		    String[] time = url.split("/");
		    if(url.contains("es")) {
		    	url = url.replace("es", "");
		    }
		    String date = url.contains("201") || url.contains("202") && time.length > 6 ? time[3] + time[4] + time[5] : "20190909";
		    date = date.replaceAll("[^0-9]", "");
    		//if(tit.contains("New York Times")) {
    		tit = tit.replace("- The New York Times", "");
    		
		    retval.add(new News(tit, url, body, date));

		    //System.out.println(date);
			//System.out.println(url + " \n" + tit + "\n");
			////System.out.println(file);
			////System.out.println(newsList.size());
		}
		return retval;
	}
	
	
	public static List<News> parseAllWSJFile(String wsjPath) throws FileNotFoundException{
		File f = new File("wsj");
		File[] fileLists = f.listFiles();
		List<News> fullList = new ArrayList<News>();
		for(int i = 0; i < fileLists.length; i++) {
			File file = fileLists[i];
			//System.out.println(file);
			List<News> newsList = parseSingleWSJFile(file);
			fullList.addAll(newsList);
			//System.out.println(newsList.size());
		}
		return fullList;
	}
	
	public static List<News> parseSingleWSJFile(File file) throws FileNotFoundException {
		File xmlFile = new File(file.getAbsolutePath());
		String xmlPath = xmlFile.getName();
		List<News> retval = new ArrayList<News>();
		if(xmlPath.contains(".txt")) {
			//System.out.println(xmlPath);
		    Scanner sc = new Scanner(file); 
		    // read day counts
		    // System.out.println(sc.nextLine()); 
		    String date = "";
		    // read line by line
		    while(sc.hasNextLine()) {
		    	String line = sc.nextLine();
		    	if(line.contains("Day Counts")) {
		    		continue;
		    	}
		    	if(line.startsWith("https://www.wsj.com/news/archive/")) {
		    		date = line.replace("https://www.wsj.com/news/archive/", "");
		    		//System.out.println(date);
		    	} else {
		    		if(line.trim().equals("")) {
		    			break;
		    		}
		    		if(sc.hasNextLine()) {
		    			String title = line.replace("Title: ", "");
			    		String url = sc.nextLine().replace("Url: ", "");
			    		String abs = sc.nextLine().replace("Abstract: ", "");
			    		retval.add(new News(title, url, abs, date));
		    		}
		    		
		    	}
		    }
		    
		}
		
		return retval;
	}
}
