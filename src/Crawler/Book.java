package Crawler;
import java.util.List;

public class Book {
	private List<String> authorTitleISBN;
	private String md5;
	private List<String> sixInfo;
	private List<String> downloadLinks;
	private String id;
	
	
	public Book(List<String> authorTitleISBN, String md5, List<String> sixInfo, List<String> downloadLinks, String id) {
		super();
		this.authorTitleISBN = authorTitleISBN;
		this.md5 = md5;
		this.sixInfo = sixInfo;
		this.downloadLinks = downloadLinks;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getAuthorTitleISBN() {
		return authorTitleISBN;
	}
	public void setAuthorTitleISBN(List<String> authorTitleISBN) {
		this.authorTitleISBN = authorTitleISBN;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public List<String> getSixInfo() {
		return sixInfo;
	}
	public void setSixInfo(List<String> sixInfo) {
		this.sixInfo = sixInfo;
	}
	public List<String> getDownloadLinks() {
		return downloadLinks;
	}
	public void setDownloadLinks(List<String> downloadLinks) {
		this.downloadLinks = downloadLinks;
	}
	
	
}
