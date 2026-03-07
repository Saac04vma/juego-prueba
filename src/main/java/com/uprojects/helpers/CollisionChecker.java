package com.uprojects.helpers;

import com.uprojects.core.Tarea;
import com.uprojects.entities.Player;
import com.uprojects.stages.MapHandler;

import java.util.List;

public class CollisionChecker {

    private MapHandler mapH;

    public CollisionChecker(MapHandler mh) {
        this.mapH = mh;
    }

    public void checkTile(Player jugador) {

        int playerLeftX = jugador.worldX + jugador.areaSolida.x;
        int playerRightX = jugador.worldX + jugador.areaSolida.x + jugador.areaSolida.width;
        int playerTopY = jugador.worldY + jugador.areaSolida.y;
        int playerBottomY = jugador.worldY + jugador.areaSolida.y + jugador.areaSolida.height;

        int playerLeftColumna = playerLeftX / mapH.getTileSize();
        int playerRightColumna = playerRightX / mapH.getTileSize();
        int playerTopFila = playerTopY / mapH.getTileSize();
        int playerBottomFila = playerBottomY / mapH.getTileSize();


        int tileNum1, tileNum2;
        int tilesize = mapH.getTileSize();

        switch (jugador.getDirection()) {
            case "up" -> {
                playerTopFila = (playerTopY - jugador.getSpd()) / tilesize;

                if (mapH.tileSolido(playerLeftColumna, playerTopFila) || mapH.tileSolido(playerRightColumna, playerTopFila)) {
                    jugador.setColision(true);
                }
            }
            case "down" -> {
                playerBottomFila = (playerBottomY + jugador.getSpd()) / tilesize;

                if (mapH.tileSolido(playerLeftColumna, playerBottomFila) || mapH.tileSolido(playerRightColumna, playerBottomFila)) {
                    jugador.setColision(true);
                }
            }
            case "left" -> {
                playerLeftColumna = (playerLeftX - jugador.getSpd()) / tilesize;

                if (mapH.tileSolido(playerLeftColumna, playerTopFila) || mapH.tileSolido(playerRightColumna, playerBottomFila)) {
                    jugador.setColision(true);
                }
            }
            case "right" -> {
                playerRightColumna = (playerRightX + jugador.getSpd()) / tilesize;

                if (mapH.tileSolido(playerRightColumna, playerTopFila) || mapH.tileSolido(playerRightColumna, playerBottomFila)) {
                    jugador.setColision(true);
                }
            }
        }
    }

    // Determinamos si en una posible proxima posicion podemos salirnos del choque con un objeto (el jugador se clipea y piensa que no puede moverse)
    public boolean estaChocando(int nextX, int nextY, Player jugador) {


        int tilesize = mapH.getTileSize();

        // Coordenadas en una posible proxima posicion
        int leftX = nextX - (tilesize / 2) + jugador.areaSolida.x;
        int rightX = nextX - (tilesize / 2) + jugador.areaSolida.x + jugador.areaSolida.width;
        int topY = nextY - (tilesize / 2) + jugador.areaSolida.y;
        int bottomY = nextY - (tilesize / 2) + jugador.areaSolida.y + jugador.areaSolida.height;


        int leftColumna = leftX / tilesize;
        int rightColumna = rightX / tilesize;
        int topFila = topY / tilesize;
        int bottomFila = bottomY / tilesize;

        return mapH.tileSolido(leftColumna, topFila) || mapH.tileSolido(leftColumna, bottomFila) || mapH.tileSolido(rightColumna, topFila) || mapH.tileSolido(rightColumna, bottomFila);
    }

    public boolean puedeJugarTarea(Player jugador, List<Tarea> tareas) {


        int tilesize = mapH.getTileSize();

        int leftColumna = jugador.areaSolida.x / tilesize;
        int rightColumna = (jugador.areaSolida.x + jugador.areaSolida.width) / tilesize;
        int topFila = jugador.areaSolida.y / tilesize;
        int bottomFila = (jugador.areaSolida.y + jugador.areaSolida.height) / tilesize;

        //String cercaDe = mapH.objetoInteractuable(leftColumna, topFila) || mapH.objetoInteractuable(leftColumna, bottomFila) || mapH.objetoInteractuable(rightColumna, topFila) || mapH.objetoInteractuable(rightColumna, bottomFila);

        //if (mapH.objetoInteractuable())

        return false;
    }
}
