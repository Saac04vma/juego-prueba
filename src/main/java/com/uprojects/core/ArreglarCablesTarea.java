package com.uprojects.core;

public class ArreglarCablesTarea extends Tarea {
    private int cablesConectados = 0;
    private final int totalCables = 3;


    public ArreglarCablesTarea(int worldX, int worldY, int width, int height, String nombre) {
        super(worldX, worldY, width, height, nombre);


    }

    @Override
    public String getNombre() {
        return "conectar-cables";
    }

    @Override
    public void comenzarTarea() {
        this.completada = false;
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
    }

    @Override
    public void reset() {
        this.completada = false;
        this.cablesConectados = 0;
    }

    public int getCablesConectados() {
        return this.cablesConectados;
    }
}
