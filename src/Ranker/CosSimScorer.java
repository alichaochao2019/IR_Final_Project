package Ranker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Indexing.News;
import Indexing.StopWordHandler;
import opennlp.tools.stemmer.PorterStemmer;

public class CosSimScorer {
	private static String STOPWORDPath = "./data/stoplist.txt";
	private static StopWordHandler swHandler = new StopWordHandler(STOPWORDPath);
	private static PorterStemmer stemmer = new PorterStemmer();
	
	public static double getCosSimScoreByTitle(String query, int id, News news,
		HashMap<String, TreeMap<Integer, Integer>> tfIndex) {

		int N = tfIndex.size();
		
		// Process Query Vector to get weight for each term
		Map<String, Double> queryVectorTFRaw = queryVectorization(query);
		Map<String, Double> queryVectorTFWeight = getTFWeightVector(queryVectorTFRaw);
		HashMap<String, Double> queryVectorWeight = new HashMap<String, Double>();

		for (Map.Entry e : queryVectorTFWeight.entrySet()) {
			String k = (String) e.getKey();
			Double v = (Double) e.getValue();
			int DF = tfIndex.containsKey(k) ? tfIndex.get(k).size() : 0;
			double IDF = 0;
			if (DF != 0) {
				IDF = Math.log10((double) N / (double) DF);
			}
			queryVectorWeight.put(k, IDF * v);
		}

	
		// Process Document Vector to get normalized weight for each term
		Map<String, Integer> termMap = vectorizeNews(news.getTitle());
		DocVector docVector = new DocVector(termMap, id, news.getTitle());
		HashMap<String, Integer> docVectorTFRaw = (HashMap<String, Integer>) docVector.getMap();
		HashMap<String, Double> docVectorTFWeight = new HashMap<String, Double>();
		double normalizationSum = 0.0;

		for (Map.Entry e : docVectorTFRaw.entrySet()) {
			String key = (String) e.getKey();
			double tfraw = docVector.getDocFrequency((String) e.getKey());
			double tfweight = (1 + Math.log10(tfraw));
			// * Math.log10(N/globalInvertedIndex.getDocFrequency(key));
			docVectorTFWeight.put(key, tfweight);
			normalizationSum += Math.pow(tfweight, 2);
		}

		double normalizationFactor = Math.sqrt(normalizationSum);
		HashMap<String, Double> docVectorNormalized = new HashMap<String, Double>();
		for (Map.Entry e : docVectorTFWeight.entrySet()) {
			String key = (String) e.getKey();
			Double val = (Double) e.getValue();
			Double normalizedVal = val / normalizationFactor;
			docVectorNormalized.put(key, normalizedVal);
			// docVectorNormalized.put(key, val);
		}

		// Calculate Final Score
		double score = 0.0;
		for (Map.Entry e : queryVectorWeight.entrySet()) {
			String key = (String) e.getKey();
			double qVal = (double) e.getValue();
			double dVal = docVectorNormalized.containsKey(key) ? docVectorNormalized.get(e.getKey()) : 0.0;
			// System.out.println(key + " " + qVal + " " + dVal);
			score += qVal * dVal;
		}
		return score;
	}
	
	public static double getCosSimScoreByTitleText(String query, int id, News news,
			HashMap<String, TreeMap<Integer, Integer>> tfIndex) {

			int N = tfIndex.size();
			
			// Process Query Vector to get weight for each term
			Map<String, Double> queryVectorTFRaw = queryVectorization(query);
			Map<String, Double> queryVectorTFWeight = getTFWeightVector(queryVectorTFRaw);
			HashMap<String, Double> queryVectorWeight = new HashMap<String, Double>();

			for (Map.Entry e : queryVectorTFWeight.entrySet()) {
				String k = (String) e.getKey();
				Double v = (Double) e.getValue();
				int DF = tfIndex.containsKey(k) ? tfIndex.get(k).size() : 0;
				double IDF = 0;
				if (DF != 0) {
					IDF = Math.log10((double) N / (double) DF);
				}
				queryVectorWeight.put(k, IDF * v);
			}

		
			// Process Document Vector to get normalized weight for each term
			Map<String, Integer> termMap = vectorizeNews(news.getTitle() + "\n" + news.getText());
			DocVector docVector = new DocVector(termMap, id, news.getTitle());
			HashMap<String, Integer> docVectorTFRaw = (HashMap<String, Integer>) docVector.getMap();
			HashMap<String, Double> docVectorTFWeight = new HashMap<String, Double>();
			double normalizationSum = 0.0;

			for (Map.Entry e : docVectorTFRaw.entrySet()) {
				String key = (String) e.getKey();
				double tfraw = docVector.getDocFrequency((String) e.getKey());
				double tfweight = (1 + Math.log10(tfraw));
				// * Math.log10(N/globalInvertedIndex.getDocFrequency(key));
				docVectorTFWeight.put(key, tfweight);
				normalizationSum += Math.pow(tfweight, 2);
			}

			double normalizationFactor = Math.sqrt(normalizationSum);
			HashMap<String, Double> docVectorNormalized = new HashMap<String, Double>();
			for (Map.Entry e : docVectorTFWeight.entrySet()) {
				String key = (String) e.getKey();
				Double val = (Double) e.getValue();
				Double normalizedVal = val / normalizationFactor;
				docVectorNormalized.put(key, normalizedVal);
				// docVectorNormalized.put(key, val);
			}

			// Calculate Final Score
			double score = 0.0;
			for (Map.Entry e : queryVectorWeight.entrySet()) {
				String key = (String) e.getKey();
				double qVal = (double) e.getValue();
				double dVal = docVectorNormalized.containsKey(key) ? docVectorNormalized.get(e.getKey()) : 0.0;
				// System.out.println(key + " " + qVal + " " + dVal);
				score += qVal * dVal;
			}
			return score;
		}
	
	public static Map<String, Integer> vectorizeNews(String text){

		// Tokenize query: Chop on whitespace, remove punctuation, keep ' and -
		String[] words = text.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase().split("\\s+");

		// Drop common terms, aka stop words
		List<String> terms = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			if (!swHandler.isStopWord(words[i])) {
					terms.add((words[i]));
			}
			
		}

		// Step 3: Stemming and Lemmatization
		Map<String, Integer>  queryVector = new HashMap<String, Integer>();
		for (int i = 0; i < terms.size(); i++) {
			String stemWord = stemmer.stem(terms.get(i));
			if (queryVector.containsKey(stemWord)) {
				queryVector.put(stemWord, queryVector.get(stemWord) + 1);
			} else {
				queryVector.put(stemWord, 1);
			}
		}

		return queryVector;
	}
	
	public static Map<String, Double> queryVectorization(String query) {

		// Tokenize query: Chop on whitespace, remove punctuation, keep ' and -
		String[] words = query.trim().replaceAll("[\\p{P}&&[^\u0027]&&[^\u002D]]", "").toLowerCase().split("\\s+");

		// Drop common terms, aka stop words
		List<String> terms = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			if (!swHandler.isStopWord(words[i])) {
					terms.add((words[i]));
			}
			
		}

		// Step 3: Stemming and Lemmatization
		Map<String, Double> queryVector = new HashMap<String, Double>();
		for (int i = 0; i < terms.size(); i++) {
			String stemWord = stemmer.stem(terms.get(i));
			if (queryVector.containsKey(stemWord)) {
				queryVector.put(stemWord, queryVector.get(stemWord) + 1.0);
			} else {
				queryVector.put(stemWord, 1.0);
			}
		}

		return queryVector;
	}
	
	public static Map<String, Double> getTFWeightVector(Map<String, Double> queryVector) {
		Map<String, Double> retval = new HashMap<String, Double>();

		for (Map.Entry e : queryVector.entrySet()) {
			String term = (String) e.getKey();
			Double tfWeight = 1 + Math.log10((double) e.getValue());
			retval.put(term, tfWeight);
		}
		return retval;

	}
}
