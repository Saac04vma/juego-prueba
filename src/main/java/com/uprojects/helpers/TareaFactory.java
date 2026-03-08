package com.uprojects.helpers;

import com.uprojects.core.ArreglarCablesTarea;
import com.uprojects.core.GolpearBotonTarea;
import com.uprojects.core.Tarea;

public class TareaFactory {

    public static Tarea crearTarea(String type, int worldX, int worldY, int tareaWidth, int tareaHeight) {
        switch (type) {
            case "cables" -> {
                return new ArreglarCablesTarea(worldX, worldY, tareaWidth, tareaHeight);
            }
            case "swipe-card" -> {
                // return new SwipeCardTarea(x, y, w, h);
            }
            case "download-data" -> {
                // return new DownloadTarea(x, y, w, h);
            }
            case "motor" -> {
                return new GolpearBotonTarea(worldX, worldY, tareaWidth, tareaHeight);
            }
            default -> {
                return null;
            }
        }

        return null;
    }
}
