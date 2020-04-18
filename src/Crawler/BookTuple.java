package Crawler;

public class BookTuple {
	private String book;
	private String year;
	private String size;
	private String url1;
	private String url2;
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	
	public BookTuple(String book, String year, String size, String url1, String url2) {
		super();
		this.book = book;
		this.year = year;
		this.size = size;
		this.url1 = url1;
		this.url2 = url2;
	}
	
	
}
