package com.uprojects.core;

import com.uprojects.ui.ArreglarCablesPane;
import com.uprojects.ui.TareaPane;

public class ArreglarCablesTarea extends Tarea {
    private int cablesConectados = 0;
    private final int totalCables = 4;


    public ArreglarCablesTarea(int worldX, int worldY, int width, int height) {
        super(worldX, worldY, width, height, "Conectar Cables");


    }

    @Override
    public TareaPane crearUI() {
        ArreglarCablesPane ui = new ArreglarCablesPane();
        ui.setTarea(this); // Enlazamos la logica a la UI
        return ui;
    }

    @Override
    public String getNombre() {
        return "conectar-cables";
    }

    @Override
    public void comenzarTarea() {
        super.comenzarTarea();
        this.cablesConectados = 0;
    }

    @Override
    public void cerrarTarea() {
    }

    @Override
    public boolean fueCompletada() {
        return this.completada;
    }

    @Override
    public void actualizarTarea(double progreso) {
        this.cablesConectados++;

        if (cablesConectados >= totalCables)
            this.completada = true;

        if (uiPane != null) {
            uiPane.actualizarUI();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.cablesConectados = 0;
    }

    public int getCablesConectados() {
        return this.cablesConectados;
    }
}
