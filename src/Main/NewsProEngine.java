package Main;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import Indexing.News;
import Searching.SimpleSearcher;;

public class NewsProEngine {

	public static void main(String [] args) throws FileNotFoundException {
		SimpleSearcher.initialize();
		String query = "trump";
		
		// feature 1: integrated search
		List<News> results = SimpleSearcher.searchRankByTitle(query, 1000);
		
		// feature 2: search by different medias
		Map<String, List<News>> newsByMedia = SimpleSearcher.getNewsByMedia(query);
		for(Map.Entry e: newsByMedia.entrySet()) {
			System.out.println(e.getKey() + " " + ((List<News>) e.getValue()).size());
		}
		
		// feature 3: search top associated words
		Map<String, Integer> associatedWords = SimpleSearcher.getTopAssociatedWords(results);
		for(Map.Entry e: associatedWords.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}
}
