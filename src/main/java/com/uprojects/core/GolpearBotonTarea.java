package com.uprojects.core;

import com.uprojects.ui.GolpearBotonPane;
import com.uprojects.ui.TareaPane;

public class GolpearBotonTarea extends Tarea {

    private int clicsActuales = 0;
    private final int clicsRequeridos = 10;

    public GolpearBotonTarea(int x, int y, int w, int h) {
        super(x, y, w, h, "Reiniciar Motor");
    }

    @Override
    public TareaPane crearUI() {
        return new GolpearBotonPane();
    }

    @Override
    public void actualizarTarea(double valor) {
        clicsActuales++;
        if (clicsActuales >= clicsRequeridos) {
            this.completada = true;
        }

        if (uiPane != null) uiPane.actualizarUI();
    }

    public int getClicsActuales() {
        return clicsActuales;
    }

    public int getClicsRequeridos() {
        return clicsRequeridos;
    }
}
