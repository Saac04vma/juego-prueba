package com.uprojects.screens;

import com.uprojects.core.ArreglarCablesTarea;
import com.uprojects.core.Tarea;
import com.uprojects.helpers.CollisionChecker;
import com.uprojects.helpers.KeyHandler;
import com.uprojects.entities.Player;
import com.uprojects.stages.MapHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamePane extends Pane {

    // Config Pantalla
    private final int originalTileSize = 32; // Eje: 16x16 tiles
    private final int scale = 1;


    // World settings
    // Tamano de cuadros
    public final int tileSize = originalTileSize * scale; // 32 * 2 = 64x64 tiles


    private KeyHandler keyH;
    private List<Tarea> taresPorHacer;
    private Tarea tareaActual;

    // Canvas JavaFx
    public final Canvas canvas;
    private final GraphicsContext gc;


    // Contador de FPS
    private long lastNanoTime = 0;
    private String fpsDisplay = "FPS: 0";
    private int frameCount = 0;
    private long fpsTimer = 0;

    // Player creation
    private HashMap<String, Player> players;
    private Player localPlayer;

    // Map handler (more like map manager but u get it)
    private MapHandler mapHandler;

    // Colision Checker
    private CollisionChecker collisionChecker;

    public GamePane(Scene scene) {

        this.canvas = new Canvas();

        Group group = new Group(canvas);

        // Permitimos que el canvas reciba inputs del teclado
        this.canvas.setFocusTraversable(true);
        this.canvas.setOnMouseClicked(e -> this.canvas.requestFocus());

        this.gc = this.canvas.getGraphicsContext2D();

        // Anclamos el canvas al tamaño del Pane que a su vez se ajusta a todo el tamaño disponible en el Stage (Frame principal)
        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());
        this.gc.setImageSmoothing(false); // Para evitar vista pixelada por zoom


        this.sceneProperty().addListener((obs, oldS, nuevaScene) -> {
            if (nuevaScene != null) {
                this.keyH = new KeyHandler(scene);
                System.out.println("KeyHandler attached to active scene: " + nuevaScene);
            }
        });


        // Inicializando lista de jugadores
        this.players = new HashMap<>();
        this.tareaActual = null;
        this.taresPorHacer = new ArrayList<>();
        //this.taresPorHacer.add(new ArreglarCablesTarea());


        this.getChildren().add(group);

        // Nos aseguramos de que sea visible el pane antes de iniciar el juego, de lo contrario el ancho y alto calculado seria de 0,0
        this.layoutBoundsProperty().addListener((obs, oldV, newV) -> {
            if (this.localPlayer == null && newV.getWidth() > 0) {
                startGameThread();
            }
        });

    }

    public void startGameThread() {

        Scene currScene = this.getScene();

        if (currScene != null) {
            this.keyH.anclarScene(currScene);
        } else {
            System.out.println("[ADVERTENCIA]: GamePane no tiene scene al comenzar");
        }

        this.localPlayer = new Player(keyH, (int) canvas.getWidth(), (int) canvas.getHeight(), tileSize, "Alejandro");
        this.players.put("id1", localPlayer);
        this.mapHandler = new MapHandler(players.get("id1"));
        this.collisionChecker = new CollisionChecker(mapHandler);
        this.taresPorHacer = mapHandler.calcularPosicionTareas();


        new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (lastNanoTime == 0) {
                    lastNanoTime = now;
                    fpsTimer = now;
                    return;
                }

                // Calculate FPS
                frameCount++;
                // If 1 second has passed (1,000,000,000 nanoseconds)
                if (now - fpsTimer >= 1_000_000_000L) {
                    fpsDisplay = "FPS: " + frameCount;
                    frameCount = 0;
                    fpsTimer = now;
                }

                lastNanoTime = now;

                update();
                renderPane();
            }
        }.start();
    }


    public void update() {

        // Si la ventana no esta enfocada o esta realizando una tarea, se paraliza al jugador
        if (!canvas.isFocused() || tareaActual != null) {
            keyH.resetPressedKeys();
            //canvas.requestFocus();
        }


        localPlayer.updatePosition(collisionChecker);

        for (Tarea tarea : this.taresPorHacer) {
            tarea.update(localPlayer);
        }

        if (keyH.accionarTarea()) {
            for (Tarea tarea : this.taresPorHacer) {
                if (!tarea.fueCompletada() && tarea.getJugadorCerca()) {
                    tareaActual = tarea;
                    tarea.comenzarTarea();
                    break;
                }
            }
        }

        /*
        if (keyH.accionarTarea()) {

        }

         */

    }

    public void renderPane() {
        // Limpiamos pantalla
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.save();


        // Tamaño del canvas (para ajustar el campo visible)
        //int anchoCampo = (int) canvas.getWidth();
        //int altoCampo = (int) canvas.getHeight();

        // Creando zoom para que el personaje no vea todo el mapa
        int zoom = 3;


        gc.translate((canvas.getWidth() / 2), (canvas.getHeight() / 2));
        gc.scale(zoom, zoom);
        gc.translate(-localPlayer.worldX, -localPlayer.worldY);


        mapHandler.draw(this.gc, zoom);
        localPlayer.draw(this.gc);

        for (Tarea tarea : this.taresPorHacer) {
            tarea.drawInteractionBox(gc);
        }

        // We pass the graphics object to be able to set graphics per player
        //mapHandler.draw(this.gc, zoom);
        //localPlayer.draw(this.gc);
        // Release any system resources use by this graphics context
        gc.restore();


        // Aqui dibujamos componentes de UI (menus, botones, etc)

        // Draw UI Elements in Screen Space (Fixed position)
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillText(fpsDisplay, 300, 120); // Draws at top-left

    }

    public int getTileSize() {
        return this.tileSize;
    }
}
