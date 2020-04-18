package Main;

import java.awt.Desktop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.application.HostServicesDelegate;

import Crawler.BookTuple;
import Crawler.SimpleBookCrawler;
import Indexing.News;
import Searching.SimpleSearcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import Crawler.SimpleBookCrawler;

public class NewsProUIController implements Initializable{
	
	@FXML
	private AnchorPane homePage;
	
	@FXML
	private AnchorPane integratedSearchPage;
	
	@FXML
	private AnchorPane multiMediaSearchPage;
	
	@FXML
	private AnchorPane bookSearchPage;
	
	@FXML
	private Label indexResultLabel;
	
	@FXML 
	private JFXTextField searchBar;
	@FXML 
	private JFXTextField integratedSearchBar;
	
	@FXML 
	private JFXTextField multiMediaSearchBar;
	@FXML
	private Label integreatedResultLabel;
	
	@FXML 
	private JFXTextField bookSearchBar;
	
	
	public void searchBook() {
		String query = bookSearchBar.getText();
		if(!query.equals("")) {
			List<BookTuple> results = SimpleBookCrawler.crawlBookLists(query);
			updateBookTable(results);
		}
	}
	
	@FXML
	private TableView<BookTuple> bookTable;
	@FXML
	private TableColumn bookCol;
	@FXML
	private TableColumn yearCol;
	@FXML
	private TableColumn sizeCol;
	@FXML
	private TableColumn url1Col;
	@FXML
	private TableColumn url2Col;
	public void updateBookTable(List<BookTuple> books) {
		ObservableList<BookTuple> data = FXCollections.observableArrayList();
		if (books != null) {
			for(int i =0; i < books.size(); i++) {
				data.add(books.get(i));
			}
		}
		
		bookCol.setCellValueFactory(new PropertyValueFactory<>("book"));
		yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
		url1Col.setCellValueFactory(new PropertyValueFactory<>("url1"));
		url2Col.setCellValueFactory(new PropertyValueFactory<>("url2"));

		
		Callback<TableColumn<BookTuple, String>, TableCell<BookTuple, String>> openFactory = //
				new Callback<TableColumn<BookTuple, String>, TableCell<BookTuple, String>>() {
					@Override
					public TableCell call(final TableColumn<BookTuple, String> param) {
						final TableCell<BookTuple, String> cell = new TableCell<BookTuple, String>() {
							final Button btn = new Button("Link");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										BookTuple announcement = getTableView().getItems().get(getIndex());
										/*
										 * Alert alert = new Alert(AlertType.INFORMATION); alert.setTitle("下载地址");
										 * alert.setHeaderText(""); alert.setContentText(announcement.getAdjunctUrl());
										 * alert.show();
										 */
										try {
										    Desktop.getDesktop().browse(new URL(announcement.getUrl1()).toURI());
										} catch (IOException e) {
										    e.printStackTrace();
										} catch (URISyntaxException e) {
										    e.printStackTrace();
										}
										
										//openWebpage(announcement.getUrl());
										TextArea textArea = new TextArea(announcement.getUrl1());
										textArea.setEditable(false);
										textArea.setWrapText(true);
										textArea.setMaxHeight(30);
										GridPane gridPane = new GridPane();
										gridPane.setMaxWidth(Double.MAX_VALUE);
										gridPane.add(textArea, 0, 0);
										Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
										alert.setTitle("Link");
										alert.setHeaderText("");
										alert.getDialogPane().setContent(gridPane);
										alert.showAndWait();
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
				
				Callback<TableColumn<BookTuple, String>, TableCell<BookTuple, String>> openFactory2 = //
						new Callback<TableColumn<BookTuple, String>, TableCell<BookTuple, String>>() {
							@Override
							public TableCell call(final TableColumn<BookTuple, String> param) {
								final TableCell<BookTuple, String> cell = new TableCell<BookTuple, String>() {
									final Button btn = new Button("Link");

									@Override
									public void updateItem(String item, boolean empty) {
										super.updateItem(item, empty);
										if (empty) {
											setGraphic(null);
											setText(null);
										} else {
											btn.setOnAction(event -> {
												BookTuple announcement = getTableView().getItems().get(getIndex());
												/*
												 * Alert alert = new Alert(AlertType.INFORMATION); alert.setTitle("下载地址");
												 * alert.setHeaderText(""); alert.setContentText(announcement.getAdjunctUrl());
												 * alert.show();
												 */
												try {
												    Desktop.getDesktop().browse(new URL(announcement.getUrl2()).toURI());
												} catch (IOException e) {
												    e.printStackTrace();
												} catch (URISyntaxException e) {
												    e.printStackTrace();
												}
												
												//openWebpage(announcement.getUrl());
												TextArea textArea = new TextArea(announcement.getUrl2());
												textArea.setEditable(false);
												textArea.setWrapText(true);
												textArea.setMaxHeight(30);
												GridPane gridPane = new GridPane();
												gridPane.setMaxWidth(Double.MAX_VALUE);
												gridPane.add(textArea, 0, 0);
												Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
												alert.setTitle("Link");
												alert.setHeaderText("");
												alert.getDialogPane().setContent(gridPane);
												alert.showAndWait();
											});
											setGraphic(btn);
											setText(null);
										}
									}
								};
								return cell;
							}
						};
		
		url1Col.setCellFactory(openFactory);
		url2Col.setCellFactory(openFactory2);

		bookTable.setItems(data);
		
	}
	
	
	public void displayBookSearchPage() {
		homePage.setVisible(false);
		integratedSearchPage.setVisible(false);
		multiMediaSearchPage.setVisible(false);
		bookSearchPage.setVisible(true);
		bookSearchBar.setText(searchBar.getText());
		searchBook();
	}
	
	public void displayMultimediaSearchPage() {
		homePage.setVisible(false);
		integratedSearchPage.setVisible(false);
		multiMediaSearchPage.setVisible(true);
		bookSearchPage.setVisible(false);
		multiMediaSearchBar.setText(searchBar.getText());
		if(SimpleSearcher.initizlied()) {
			searchMultimediaTable();
		}
	}
	
	public void displayIntegratedSearchPage() {
		homePage.setVisible(false);
		integratedSearchPage.setVisible(true);
		multiMediaSearchPage.setVisible(false);
		bookSearchPage.setVisible(false);
		integratedSearchBar.setText(searchBar.getText());
		if(SimpleSearcher.initizlied()) {
			updateIntegreatedTable();
		}
	}
	
	public void displayHomePage() {
		homePage.setVisible(true);
		integratedSearchPage.setVisible(false);
		multiMediaSearchPage.setVisible(false);
		bookSearchPage.setVisible(false);
	}
	
	public void initializeIndexing() {
		try {
			if(SimpleSearcher.initizlied()) {
				
			} else {
				String retval = SimpleSearcher.initialize();
				indexResultLabel.setText(retval);
				String[] possibleWords = SimpleSearcher.getTopWords();
				TextFields.bindAutoCompletion(searchBar, possibleWords);
				TextFields.bindAutoCompletion(integratedSearchBar, possibleWords);
				TextFields.bindAutoCompletion(multiMediaSearchBar, possibleWords);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
		
	}
	
	
	@FXML
	private TableView<Tuple> scmpTable;
	@FXML
	private TableColumn scmpCol;
	@FXML
	private TableColumn scmpDateCol;
	@FXML
	private Label scmpLabel;
	@FXML
	private TableView<Tuple> bbcTable;
	@FXML
	private TableColumn bbcCol;
	@FXML
	private TableColumn bbcDateCol;
	@FXML
	private Label bbcLabel;
	@FXML
	private TableView<Tuple> nytTable;
	@FXML
	private TableColumn nytCol;
	@FXML
	private TableColumn nytDateCol;
	@FXML
	private Label nytLabel;
	@FXML
	private TableView<Tuple> wsjTable;
	@FXML
	private TableColumn wsjCol;
	@FXML
	private TableColumn wsjDateCol;
	@FXML
	private Label wsjLabel;
	
	@FXML
	private TableView<Tuple> twitterTable;
	@FXML
	private TableColumn twitterCol;
	@FXML
	private TableColumn twitterDateCol;
	@FXML
	private Label twitterLabel;

	public void searchMultimediaTable() {
		String query = multiMediaSearchBar.getText();
		if(!query.equals("")) {
			System.out.println(query);
			Map<String, List<News>> news = SimpleSearcher.getNewsByMedia(query);
			
			// access individual List<News>
			List<News> wsjNews = news.get("wsj");
			List<News> nytNews = news.get("nyt");
			List<News> bbcNews = news.get("bbc");
			List<News> scmpNews = news.get("scmp");
			List<News> twitterNews = news.get("twitter");

			updateMediaTable(wsjTable, wsjCol,wsjDateCol, wsjNews);
			updateMediaTable(bbcTable, bbcCol,bbcDateCol, bbcNews);
			updateMediaTable(scmpTable, scmpCol, scmpDateCol,scmpNews);
			updateMediaTable(nytTable, nytCol, nytDateCol,nytNews);
			updateMediaTable(twitterTable, twitterCol, null,twitterNews);

			wsjLabel.setText("About " +wsjNews.size() + " results");
			bbcLabel.setText("About " +bbcNews.size() + " results");
			scmpLabel.setText("About " +scmpNews.size() + " results");
			nytLabel.setText("About " +nytNews.size() + " results");
			twitterLabel.setText("About " +twitterNews.size() + " results");

		}
	}
	
	public void updateMediaTable(TableView<Tuple> atable, TableColumn acol,TableColumn dateCol, List<News>  news) {
		ObservableList<Tuple> data = FXCollections.observableArrayList();
		if (news != null) {
			for(int i = 0; i < news.size(); i++) {
				News n = news.get(i);
				String text = n.getText();
				String abs = text.length() > 100?n.getText().substring(0, 100):text;
				data.add(new Tuple(n.getTitle() + "\n" + abs, "","", n.getUrl(),n.getDate()));
			}
		}
		acol.setCellValueFactory(new PropertyValueFactory<>("title"));
		if(dateCol!= null) {
			dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		}
		atable.setItems(data);
	}

	
	@FXML
	private TableView<Tuple> table;
	@FXML
	private TableColumn newsCol;
	@FXML
	private TableColumn sourceCol;
	@FXML
	private TableColumn dateCol;
	@FXML
	private TableColumn readCol;
	
	public void updateIntegreatedTable() {
		String query = integratedSearchBar.getText();
		if(!query.equals("")) {
			System.out.println(integratedSearchBar.getText());
			List<News> news = SimpleSearcher.searchRankByTitle(query, 10000);
			List<Tuple> tuple = new ArrayList<Tuple>();
			for(int i = 0; i < news.size(); i++) {
				News ns = news.get(i);
				String text = ns.getText();
				String abs = text.length() > 100?ns.getText().substring(0, 100):text;
				String source = "NYT";
				String url =ns.getUrl();
				if(url.contains("scmp")) source = "SCMP";
				if(url.contains("bbc")) source = "BBC";
				if(url.contains("wsj")) source = "WSJ";
				if(url.contains("nytimes")) source = "NYT";
				if(url.contains("TWITTERID"))continue;
				Tuple tp = new Tuple(ns.getTitle() + "\n" + abs, abs, source, url, ns.getDate());
				tuple.add(tp);
			}
			System.out.println(news.size());
			updateIntegreatedNewsTable(tuple);
			Map<String, Integer> toDisplay = SimpleSearcher.getTopAssociatedWords(news);
			updateIntegreatedCountTable(toDisplay);
			integreatedResultLabel.setText("About " +tuple.size() + " results");
		}
	}
	
	@FXML
	private TableView<WordPair> wordTable;
	@FXML
	private TableColumn wordCol;
	@FXML
	private TableColumn countCol;
	public void updateIntegreatedCountTable(Map<String, Integer> toDisplay) {
		ObservableList<WordPair> data = FXCollections.observableArrayList();
		if (toDisplay != null) {
			for(Map.Entry e : toDisplay.entrySet()) {
				data.add(new WordPair((String)e.getKey(), (int)e.getValue()));
			}
		}
		wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
		countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
		wordTable.setItems(data);
		
	}

	public void updateIntegreatedNewsTable(List<Tuple> tuple) {
		ObservableList<Tuple> data = FXCollections.observableArrayList();
		if (tuple != null) {
			for (int i = 0; i < tuple.size(); i++) {
				data.add(tuple.get(i));
			}
		}
		
		newsCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		readCol.setCellValueFactory(new PropertyValueFactory<>("url"));
		
		Callback<TableColumn<Tuple, String>, TableCell<Tuple, String>> openFactory = //
				new Callback<TableColumn<Tuple, String>, TableCell<Tuple, String>>() {
					@Override
					public TableCell call(final TableColumn<Tuple, String> param) {
						final TableCell<Tuple, String> cell = new TableCell<Tuple, String>() {
							final Button btn = new Button("Read");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										Tuple announcement = getTableView().getItems().get(getIndex());
										/*
										 * Alert alert = new Alert(AlertType.INFORMATION); alert.setTitle("下载地址");
										 * alert.setHeaderText(""); alert.setContentText(announcement.getAdjunctUrl());
										 * alert.show();
										 */
										try {
										    Desktop.getDesktop().browse(new URL(announcement.getUrl()).toURI());
										} catch (IOException e) {
										    e.printStackTrace();
										} catch (URISyntaxException e) {
										    e.printStackTrace();
										}
										//openWebpage(announcement.getUrl());
										TextArea textArea = new TextArea(announcement.getUrl());
										textArea.setEditable(false);
										textArea.setWrapText(true);
										textArea.setMaxHeight(30);
										GridPane gridPane = new GridPane();
										gridPane.setMaxWidth(Double.MAX_VALUE);
										gridPane.add(textArea, 0, 0);
										Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
										alert.setTitle("Link");
										alert.setHeaderText("");
										alert.getDialogPane().setContent(gridPane);
										alert.showAndWait();
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		
		readCol.setCellFactory(openFactory);
		table.setItems(data);
		
	}
	
	
	
	
	
}
