package Indexing;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * Handler used to parse, read and identify stop words.
 * 
 * @author chaoli
 *
 */
public class StopWordHandler {

	private HashSet<String> stopwords;
	private String stopWordFilePath;

	/**
	 * Set up stop words.
	 */
	public void setUpStopWords() {
		this.stopwords = this.processStopWords();
	}

	/**
	 * Set up stop words.
	 */
	public StopWordHandler(String path) {
		this.stopWordFilePath = path;
		this.setUpStopWords();
	}

	/**
	 * Check if a txt string is a stop word.
	 * 
	 * @param txt
	 * @return true if the text is a stop word.
	 */
	public boolean isStopWord(String txt) {
		if (txt == null || txt.isEmpty())
			return true;
		return stopwords.contains(txt);
	}

	/**
	 * Read in the stop words list from input file.
	 * 
	 * @return Stopwords list as a HashSet<String>
	 */
	private HashSet<String> processStopWords() {

		HashSet<String> retVal = new HashSet<>();
		ClassLoader classLoader = StopWordHandler.class.getClassLoader();
		File file = new File(stopWordFilePath);

		try {
			BufferedReader b = new BufferedReader(new FileReader(file));
			String readLine = "";
			while ((readLine = b.readLine()) != null) {
				String word = readLine.trim();
				word = word.toLowerCase();
				retVal.add(word);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(retVal.size());
		return retVal;
	}

}