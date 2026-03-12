package com.uprojects.server;


import com.uprojects.core.Tarea;

import java.util.ArrayList;

public class ServerPlayer {

    public int id;
    public String nombre;
    public String color;
    public double x, y;
    public int tareasCompletadas = 0;
    public int tareasTotales = 4; // Maximo de tareas del jugador
    public boolean killed, oculto;
    public String accion, facingTowards;
    public int spriteCounter = 0;
    public int spriteNumber = 1;
    public boolean impostor;
    public long tiempoUltimaKill;
    public ArrayList<String> tareasFinalizadas;

    public ServerPlayer(int id, String nombre, String color) {
        this.id = id;
        this.nombre = nombre;
        this.killed = false;
        this.color = color;
        this.accion = "up";
        this.facingTowards = "left";
        this.impostor = false;
        this.tiempoUltimaKill = 0;
        this.oculto = false;
        this.tareasFinalizadas = new ArrayList<>(4); // Manejamos por ahora solo 4 posibles tareas
    }

    public void setTargets(double worldX, double worldY) {
        this.x = worldX;
        this.y = worldY;
    }

    public void setImpostor(boolean esImpostor) {
        this.impostor = esImpostor;
    }

    public void setOculto(boolean oculto) {
        this.oculto = oculto;
    }
}
