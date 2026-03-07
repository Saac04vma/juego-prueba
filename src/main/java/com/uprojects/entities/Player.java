package com.uprojects.entities;

import com.uprojects.core.Tarea;
import com.uprojects.helpers.CollisionChecker;
import com.uprojects.helpers.KeyHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.List;
import java.util.Objects;


public class Player {

    private String ID, nombre;
    private boolean paused, killed;
    public int worldX, worldY;
    public final int cameraX, cameraY;
    public int spd;
    private final KeyHandler keyH;
    private final int tileSize;
    // Image with accessible buffer of image data (our character sprite)
    //protected BufferedImage up, up2, down, down2;
    protected Image[] left; // left, left2, left3, left4, left5, left6;
    protected Image[] right;  //right2, right3, right4, right5, right6;
    private Image lastSprite;
    private Image[] idleR; // idle2, idle3, idle4;
    private Image[] idleL; //idleL2, idleL3, idleL4;
    private Image[] electrocutado;
    public Rectangle areaSolida;
    private boolean colision;
    private List<Tarea> tareasAsignadas;

    protected String direction, facingTowards;
    public int spriteCounter = 0;
    public int spriteNumber = 1;

    // IDEA: El jugador recibe el KeyH y un SpawnPoint como argumentos solamente
    // IDEA 2: Crear una clase Camara que maneje todos esos calculos
    public Player(KeyHandler kh, int screenW, int screenH, int tileSize, String name) {

        this.keyH = kh;
        this.nombre = name;
        this.tileSize = tileSize;
        this.left = new Image[6];
        this.right = new Image[6];
        this.idleL = new Image[4];
        this.idleR = new Image[4];
        this.areaSolida = new Rectangle(12, 30, 32, 32);
        this.getPlayerImage();
        this.resetValues();

        this.lastSprite = idleR != null ? idleR[0] : null;

        this.cameraX = screenW / 2 - (tileSize / 2);
        this.cameraY = screenH / 2 - (tileSize / 2);

    }

    public void resetValues() {
        this.worldX = tileSize * 16;
        this.worldY = tileSize * 12;
        this.spd = 6;
        this.paused = false;
        this.killed = false;
        this.direction = "right";
        this.facingTowards = "right";
        this.colision = false;
    }

    public void asignarTareas(List<Tarea> tareas) {
        this.tareasAsignadas = tareas;
    }

    public void updatePosition(CollisionChecker collisionChecker) {

        if (paused || killed) {
            return;
        }

        if (keyH.isMoving()) {
            int dx = 0;
            int dy = 0;

            if (keyH.getUpPressed() && keyH.getRightPressed()) {
                this.direction = "up";
                this.facingTowards = "right";

                int normVector = this.spd / 2;
                dx += normVector;
                dy -= normVector;


                //this.worldX += normVector;
                //this.worldY -= normVector;
            } else if (keyH.getUpPressed() && keyH.getLeftPressed()) {
                this.direction = "up";
                this.facingTowards = "left";

                int normVector = this.spd / 2;
                dx -= normVector;
                dy -= normVector;

            } else if (keyH.getDownPressed() && keyH.getRightPressed()) {
                this.direction = "down";
                this.facingTowards = "right";

                int normVector = this.spd / 2;
                dx += normVector;
                dy += normVector;

            } else if (keyH.getDownPressed() && keyH.getLeftPressed()) {
                this.direction = "down";
                this.facingTowards = "left";

                int normVector = this.spd / 2;
                dx -= normVector;
                dy += normVector;

            } else if (keyH.getUpPressed()) {
                this.direction = "up";
                dy -= spd;
            } else if (keyH.getDownPressed()) {
                this.direction = "down";
                dy += spd;
            } else if (keyH.getLeftPressed()) {
                this.direction = "left";
                this.facingTowards = "left";
                dx -= spd;
            } else if (keyH.getRightPressed()) {
                this.direction = "right";
                this.facingTowards = "right";
                dx += spd;
            }

            // Chequeando si hubo una colision antes de mover al personaje
            colision = false;
            //collisionChecker.checkTile(this);
            if (!collisionChecker.estaChocando(worldX + dx, worldY, this)) {
                this.worldX += dx;
            }

            if (!collisionChecker.estaChocando(worldX, worldY + dy, this)) {
                this.worldY += dy;
            }

            actualizarSprite();
        } else {
            direction = facingTowards.contains("right") ? "idle-r" : "idle-l";

        }


    }

    public void draw(GraphicsContext gc) {


        Image sprite = lastSprite;

        switch (direction) {
            case "up", "down" -> {
                if (facingTowards.contains("right")) {
                    sprite = this.right[spriteNumber];
                } else {

                    sprite = this.left[spriteNumber];
                }
            }

            case "left" -> {
                sprite = this.left[spriteNumber];
            }
            case "right" -> {
                sprite = this.right[spriteNumber];
            }
            case "idle-r" -> {
                sprite = this.idleR[spriteNumber];
            }
            case "idle-l" -> {
                sprite = this.idleL[spriteNumber];
            }
            case "paused" -> {
                if (facingTowards.contains("right")) {
                    sprite = this.idleR[spriteNumber];
                } else {

                    sprite = this.idleL[spriteNumber];
                }
            }
            case "killed" -> {
                sprite = this.electrocutado[spriteNumber];
            }
            default -> {
                break;
            }
        }

        lastSprite = sprite;


        // Depending on the current direction we draw the respective sprite


        //gc.setFill(Color.RED);
        //gc.fillText("Presione E", worldX, worldY - 12, 32);
        gc.drawImage(sprite, worldX - (double) (tileSize / 2), worldY - (double) (tileSize / 2));
    }

    private void actualizarSprite() {
        spriteCounter++;

        if (spriteCounter > 8) {

            spriteNumber = (spriteNumber + 1) % 2;
            spriteCounter = 0;
        }
    }

    public void getPlayerImage() {
        try {

            //String cwd = System.getProperty("user.dir");


            for (int i = 0; i < 2; i++) {

                if (this.left != null)
                    this.left[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/Amarillo/Izquierda/Walk/" + (i + 1) + ".png")));

            }


            for (int i = 0; i < 2; i++) {

                if (this.right != null)
                    this.right[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/Amarillo/Derecha/Walk/" + (i + 1) + ".png")));

            }


            for (int i = 0; i < 2; i++) {

                if (this.idleL != null)
                    this.idleL[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/Amarillo/Izquierda/Idle/" + (i + 1) + ".png")));

            }


            for (int i = 0; i < 2; i++) {

                if (this.idleR != null)
                    this.idleR[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/Amarillo/Derecha/Idle/" + (i + 1) + ".png")));

            }


        } catch (IllegalArgumentException e) {
            System.out.println("Bad reading of sprite");
            e.printStackTrace();
        }

    }

    private void movePlayer() {
        //if (!colision) {

        if (keyH.getUpPressed() && keyH.getRightPressed()) {

            int normVector = this.spd / 2;
            this.worldX += normVector;
            this.worldY -= normVector;

        } else if (keyH.getUpPressed() && keyH.getLeftPressed()) {

            int normVector = this.spd / 2;
            this.worldX -= normVector;
            this.worldY -= normVector;

        } else if (keyH.getDownPressed() && keyH.getRightPressed()) {

            int normVector = this.spd / 2;
            this.worldX += normVector;
            this.worldY += normVector;


        } else if (keyH.getDownPressed() && keyH.getLeftPressed()) {


            int normVector = this.spd / 2;
            this.worldX -= normVector;
            this.worldY += normVector;


        } else if (keyH.getUpPressed()) {
            this.worldY -= this.spd;
        } else if (keyH.getDownPressed()) {
            this.worldY += this.spd;
        } else if (keyH.getLeftPressed()) {
            this.worldX -= this.spd;
        } else if (keyH.getRightPressed()) {
            this.worldX += spd;
        }

        //}
    }

    public String getDirection() {
        return this.direction;
    }

    public int getSpd() {
        return this.spd;
    }

    public void setColision(boolean colisiono) {
        this.colision = colisiono;
    }

    public boolean getColision() {
        return this.colision;
    }
}
