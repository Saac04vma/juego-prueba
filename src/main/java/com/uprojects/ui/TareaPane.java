package com.uprojects.ui;

import com.uprojects.core.Tarea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TareaPane extends Pane {

    protected Tarea tarea;
    protected Runnable tareaCompletada;
    protected Runnable cerrarTarea;

    @FXML
    protected Label lableTitulo;
    @FXML
    protected Button btnCerrar;

    public TareaPane() {
        super();
        this.setVisible(false);
        this.setMouseTransparent(false);
    }


}
