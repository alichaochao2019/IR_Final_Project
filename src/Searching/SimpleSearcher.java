package Searching;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import Indexing.News;
import Indexing.SimpleIndexer;
import Indexing.StopWordHandler;
import opennlp.tools.stemmer.PorterStemmer;
import Ranker.CosSimScorer;

public class SimpleSearcher {
	private static StopWordHandler swHandler = new StopWordHandler("./data/stoplist.txt");
	private static PorterStemmer stemmer = new PorterStemmer();
	private static HashMap<String, TreeMap<Integer, Integer>> tfIndex;
	private static HashMap<String, TreeMap<Integer, Integer>> bbcIndex;
	private static HashMap<String, TreeMap<Integer, Integer>> ntIndex;
	private static HashMap<String, TreeMap<Integer, Integer>> scmpIndex;
	private static HashMap<String, TreeMap<Integer, Integer>> wsjIndex;

	private static HashMap<Integer, News> newsMap;
	
	public static void main(String [] args) throws FileNotFoundException {
		
		initialize();
		String query = "trump";
		
		// feature 1: integrated search
		List<News> results = searchRankByTitle(query, 1000);
		
		// feature 2: search by different medias
		Map<String, List<News>> newsByMedia = getNewsByMedia(query);
		for(Map.Entry e: newsByMedia.entrySet()) {
			System.out.println(e.getKey() + " " + ((List<News>) e.getValue()).size());
		}
		
		// feature 3: search top associated words
		Map<String, Integer> associatedWords = getTopAssociatedWords(results);
		for(Map.Entry e: associatedWords.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}
	
	public static Map<String, Integer> getTopAssociatedWords (List<News> newsList){
		Map<String, Integer> retval = new HashMap<String, Integer>();
		for(int i = 0; i < newsList.size(); i++) {
			News news = newsList.get(i);
			List<String> tokens = tokenizeQueryWithoutStem(news.getTitle() + " \n" + news.getText());
			// System.out.println(tokens.size());
			for(int j = 0; j < tokens.size(); j++) {
				if(retval.containsKey(tokens.get(j))) {
					retval.put(tokens.get(j), retval.get(tokens.get(j))+1);
				} else {
					retval.put(tokens.get(j), 1);
				}
			}
		}
		
		return sortByComparator(retval, false);
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
	
	public static Map<String, List<News>> getNewsByMedia(String query){
		Map<String, List<News>> retval = 
				new HashMap<String,List<News>>();
		List<News> wsj = new ArrayList<News>();
		List<News> bbc = new ArrayList<News>();
		List<News> nyt = new ArrayList<News>();
		List<News> scmp = new ArrayList<News>();
		List<News> twitter= new ArrayList<News>();

		List<News> results = searchRankByTitle(query, 50000);
		for(int i = 0; i < results.size(); i++) {
			
			if(results.get(i).getUrl().contains("bbc")) {
				bbc.add(results.get(i));
			} else if (results.get(i).getUrl().contains("wsj")) {
				wsj.add(results.get(i));
			} else if (results.get(i).getUrl().contains("scmp")) {
				scmp.add(results.get(i));
			} else if (results.get(i).getUrl().contains("nyt")) {
				nyt.add(results.get(i));
			} else if(results.get(i).getUrl().contains("TWITTERID")) {
				twitter.add(results.get(i));
			}
		}
		
		retval.put("scmp", scmp);
		retval.put("bbc",bbc);
		retval.put("wsj", wsj);
		retval.put("nyt", nyt);
		retval.put("twitter", twitter);

		
		return retval;
	}
	
	// scmp, bbc, wsj, n -> news
	
	// scmp, bbc, wsj, n -> dfndex
	
	public static String[] getTopWords() {
	    
	    Set<String> topWords = SimpleIndexer.getSortedDFIndex();
	    String[] myArray = new String[topWords.size()];

	    int length = topWords.size() > 500? 500: topWords.size();
	    String[] newArray = Arrays.copyOf(myArray, length);
	    topWords.toArray(myArray);
	    
		return myArray;
	}
	public static boolean initizlied() {
		return tfIndex != null;
	}
	public static String initialize() throws FileNotFoundException {
		SimpleIndexer.initialize();
		SimpleIndexer.indexWSJ();
		SimpleIndexer.indexNYT();
		SimpleIndexer.indexSCMP();
		SimpleIndexer.indexBBC();
		SimpleIndexer.indexTwitter();
		
		tfIndex = SimpleIndexer.getTFIndex();
		scmpIndex  = SimpleIndexer.getSCMPTFIndex();
		wsjIndex  = SimpleIndexer.getWSJTFIndex();
		bbcIndex  = SimpleIndexer.getBBCTFIndex();
		ntIndex  = SimpleIndexer.getNTTFIndex();
		newsMap = SimpleIndexer.getNewsMap();
		
		System.out.println(tfIndex.size());
		System.out.println(newsMap.size());
		
		String retval = "Index size: " + tfIndex.size() + " ";
		retval += "News size: " + newsMap.size();
		return retval;
	}
	
	
	
	public static List<News> searchRankByTitle(String query, int k) {
		List<News> results = getTopKNews(query, k, true);
		for(int i = 0; i < results.size(); i++) {
			// System.out.println(results.get(i).toString());
		}
		return results;
	}
	
	public static List<News> searchRankByTitleText(String query, int k) {
		List<News> results = getTopKNews(query, k, false);
		for(int i = 0; i < results.size(); i++) {
			// System.out.println(results.get(i).toString());
		}
		return results;
	}
	
	
	
	public static List<News> getTopKNews(String query, int k, boolean titleOnly){
		List<Map.Entry<Integer, Double>> results = getSortedResults(query, titleOnly);
		// System.out.println(results.size());
		List<News> retval = new ArrayList<News>();
		if(k > results.size()) k = results.size();
		for(int i = 0; i < k; i++) {
			// System.out.println(results.get(i).getKey() + " " + results.get(i).getValue());
			// System.out.println(newsMap.get(results.get(i).getKey()).toString());
			retval.add(newsMap.get(results.get(i).getKey()));
		}
		return retval;
	}
	
	public static List<Map.Entry<Integer, Double>> getSortedResults (String query, boolean titleOnly){
		
		Set<Integer> newsSet = findRelevantIDS(query); // retrieve relevant doc
		Map<Integer, Double> newsScoreMap = new HashMap<Integer, Double>();
		
		for(int i : newsSet) {
			News news = newsMap.get(i);
			double score = 0.0;
			if(titleOnly) {
				score = CosSimScorer.getCosSimScoreByTitle(query, i, news, tfIndex);
			} else {
				score = CosSimScorer.getCosSimScoreByTitleText(query, i, news, tfIndex);
			}
			newsScoreMap.put(i, score);
		}
		
		// Create a list from elements of HashMap
		List<Map.Entry<Integer, Double>> list 
			= new LinkedList<Map.Entry<Integer, Double>>(newsScoreMap.entrySet());
		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
		public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
		}
		});
		
		return list;
	}
	
	public static Set<Integer> findRelevantIDS(String query) {
		//query = "trump coronavirus";
		List<String> queryVector = tokenizeQuery(query);

		Set<Integer> newsSet = new HashSet<Integer>();

		for(int i = 0; i < queryVector.size(); i++) {
			String term = queryVector.get(i);
			if(tfIndex.containsKey(term)) {
				Set<Integer> newsList = tfIndex.get(term).keySet();
				// System.out.println(term + " " + newsList.size());
				if(newsSet.size() == 0) {
					newsSet = newsList;
				} else {
					newsSet = mergeSet(newsSet, newsList);
				}
			}
		}
		
		// System.out.println(newsSet.size());
		
		Iterator<Integer> itr = newsSet.iterator();
		int index = 0;
		while(itr.hasNext()) {
			News news = newsMap.get(itr.next());
			//System.out.println(news.toString());
			if (index++ == 10) {
				break;
			}
		}
		return newsSet;
	}
	
	public static Set<Integer> mergeSet(Set<Integer> a, Set<Integer> b) {
		Set<Integer> intersection = new HashSet<Integer>(a); // use the copy constructor
		intersection.retainAll(b);
		// System.out.println(intersection.size());
		return intersection;
	}
	
	
	private static List<String> tokenizeQueryWithoutStem(String query){
		List<String> retval = new ArrayList<String> ();
		String[] tokens =query.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
				.split("\\s+");
		for (int i = 0; i < tokens.length;i++) {
			if (swHandler.isStopWord(tokens[i])) {
				tokens[i] = "";
				continue;
			}
			if (tokens[i].indexOf('-') < 0) {
				// String pureTerm = tokens[i];
				String term = tokens[i];

				if(!retval.contains(term)) {
					retval.add(term);
				}
			}else {
				String[] subtokens = tokens[i].split("-");
				
				String term = String.join("-", subtokens);
				if(!retval.contains(term)) {
					retval.add(term);
				}
			}
			
		}	
		
		return retval;
	}
	
	
	private static List<String> tokenizeQuery(String query){
		List<String> retval = new ArrayList<String> ();
		String[] tokens =query.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase()
				.split("\\s+");
		for (int i = 0; i < tokens.length;i++) {
			if (swHandler.isStopWord(tokens[i])) {
				tokens[i] = "";
				continue;
			}
			if (tokens[i].indexOf('-') < 0) {
				// String pureTerm = tokens[i];
				String term = stemmer.stem(tokens[i]);

				if(!retval.contains(term)) {
					retval.add(term);
				}
			}else {
				String[] subtokens = tokens[i].split("-");
				for (int j = 0; j < subtokens.length; j++) {
					subtokens[j] = stemmer.stem(subtokens[j]);
				}
				String term = String.join("-", subtokens);
				if(!retval.contains(term)) {
					retval.add(term);
				}
			}
			
		}	
		
		return retval;
	}
}
