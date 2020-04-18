package Ranker;

import java.util.Map;

/**
 * Data Object to store a single document terms.
 * 
 * @author chaoli
 *
 */
public class DocVector {

	private Map<String, Integer> termMap;
	private int docId;
	private String title;

	public String getTitle(){
		return this.title;
	}
	public Map<String, Integer> getMap() {
		return this.termMap;
	}

	public String toString(){
		return "Document Id: " + docId  + "<br>" + "Document Title: " + title + "<br>";
	}
	public int getDocId() {
		return this.docId;
	}

	public void printTermMap() {
		for (Map.Entry e : termMap.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}

	public DocVector(Map<String, Integer> mp, int id, String title) {
		this.termMap = mp;
		this.docId = id;
		this.title = title;
	}

	public boolean containsTerm(String term) {
		return termMap.containsKey(term);
	}

	public int getDocFrequency(String term) {
		if (termMap.containsKey(term)) {
			return termMap.get(term);
		}
		return -1;
	}

}
