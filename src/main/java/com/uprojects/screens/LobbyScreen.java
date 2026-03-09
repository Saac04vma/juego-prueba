package com.uprojects.screens;

import com.uprojects.ui.LobbyPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LobbyScreen extends ControladorPantalla {

    public LobbyScreen() {
    }

    @FXML
    public void switchScenes(ActionEvent e) {
        System.out.println(e.getTarget());
    }

    @FXML
    public void startGame(ActionEvent e) {

        //stageManager.setRoot(new LobbyPane(stageManager.scene));
        //stageManager.setRoot(new GamePane(stageManager.scene), "Now Playing");
    }
}
