package com.uprojects.screens;

import javafx.stage.Stage;

public class GameScreen extends ControladorPantalla {

    private final GamePane game;

    public GameScreen(String t, Stage primaryStage) {

        //super(t, primaryStage);
        this.game = new GamePane(primaryStage.getScene());

        //this.gRoot.getChildren().add(game.canvas);


    }

    public void startGame() {
        this.game.startGameThread();
    }


}
