package Main;


import java.io.File;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewsProUI.fxml"));
			AnchorPane root = loader.load();
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			File img = new File("./data/icon.jpg");
			System.out.println(img.exists());
			
			Image imga = new Image(img.toURI().toString());
			primaryStage.getIcons().add(imga);
			
			primaryStage.setTitle("News+ Pro");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
