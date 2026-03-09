package com.uprojects.ui;

import com.uprojects.server.Red;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class LobbyPane extends VBox {
    private ListView<String> listaNombres = new ListView<>();
    private Button btnEmpezar = new Button("Iniciar Partida");
    private Label lblStatus = new Label("Esperando jugadores (min. 5)...");

    public LobbyPane(com.esotericsoftware.kryonet.Client client, Scene scene) {
        this.getChildren().addAll(new Label("LOBBY"), listaNombres, lblStatus, btnEmpezar);
        btnEmpezar.setVisible(false); // Hidden by default

        btnEmpezar.setOnAction(e -> client.sendTCP(new Red.PaquetePedirInicio()));
    }


    public void actualizar(Red.PaqueteLobbyInfo info, boolean soyHost) {
        /*
        Platform.runLater(() -> {
            listaNombres.getItems().setAll(info.nombres);
            if (soyHost) {
                btnEmpezar.setVisible(true);
                btnEmpezar.setDisable(!info.puedeEmpezar);
            }
            lblStatus.setText(info.puedeEmpezar ? "¡Listo para empezar!" : "Faltan " + (5 - info.nombres.size()) + " jugadores...");
        });

         */
    }
}
