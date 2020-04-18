package Main;

public class Tuple {
	private String title;
	private String abs;
	private String source;
	private String url;
	private String date;
	public Tuple(String title, String abs, String source, String url, String date) {
		super();
		this.title = title;
		this.abs = abs;
		this.source = source;
		this.url = url;
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
