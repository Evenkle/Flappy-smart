package ui;

import game.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        primaryStage.setTitle("Flap Flap rEvolution");
        Scene scene = new Scene(root, Game.Companion.getWIDTH() * 2, Game.Companion.getHEIGHT());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(value -> System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
