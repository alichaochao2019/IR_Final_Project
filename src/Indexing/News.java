package Indexing;

public class News {
	private String title;
	private String date;
	private String url;
	private String text;
	
	
	@Override
	public String toString() {
		return "News [title=" + title + ", date=" + date + ", url=" + url + ", text=" + "]";
	}
	

	public News(String title, String url, String text, String date) {
		super();
		this.title = title;
		this.url = url;
		this.text = text;
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
