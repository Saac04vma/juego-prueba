package com.uprojects.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeScreen extends ControladorPantalla {

    public HomeScreen() {
    }

    @FXML
    public void switchScenes(ActionEvent e) {
        System.out.println(e.getTarget());
    }

    @FXML
    public void startGame(ActionEvent e) {


        stageManager.setRoot(new GamePane(stageManager.scene), "Now Playing");
    }


}
