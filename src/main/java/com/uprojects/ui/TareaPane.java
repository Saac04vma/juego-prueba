package com.uprojects.ui;

import com.uprojects.core.Tarea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public abstract class TareaPane extends Pane {

    protected Tarea tarea;
    protected Runnable onTareaCompletada;
    protected Runnable onCerrarTarea;

    @FXML
    protected Label labelTitulo;
    @FXML
    protected Button btnCerrar;

    public TareaPane() {
        super();
        this.setVisible(false);
        this.setMouseTransparent(false);
    }

    public void setTarea(Tarea nuevaTarea) {
        if (nuevaTarea == null) {
            System.out.println("[ADVERTENCIA] Tarea nueva no valida");
            return;
        }

        this.tarea = nuevaTarea;

        if (labelTitulo != null) {
            labelTitulo.setText(tarea.getNombre());
        }
    }

    public void setOnTareaCompletada(Runnable cb) {
        this.onTareaCompletada = cb;
    }

    public void setOnCerrarTarea(Runnable cb) {
        this.onCerrarTarea = cb;
    }

    public void mostrarTarea() {
        this.setVisible(true);
        this.setDisable(false);

        if (tarea != null) {
            tarea.comenzarTarea();
        }

        uiAbrirTarea();
    }

    public void ocultarTarea() {
        if (!this.isVisible()) {
            return;
        }

        if (tarea != null && !tarea.fueCompletada()) {
            tarea.cerrarTarea();
        }

        this.setVisible(false);
        this.setDisable(true);

        uiCerrarTarea();

        if (onCerrarTarea != null) {
            onCerrarTarea.run();
        }
    }

    protected abstract void uiAbrirTarea();

    protected abstract void uiCerrarTarea();

    public abstract void actualizarUI();
}
