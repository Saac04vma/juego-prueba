package com.uprojects.entities;

import com.uprojects.helpers.KeyHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;


public class Player {

    private String ID, nombre;
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

    protected String direction, facingTowards;
    public int spriteCounter = 0;
    public int spriteNumber = 1;

    // IDEA: El jugador recibe el KeyH y un SpawnPoint como argumentos solamente
    // IDEA 2: Crear una clase Camara que maneje todos esos calculos
    public Player(KeyHandler kh, int screenW, int screenH, int tileSize) {

        this.keyH = kh;
        this.tileSize = tileSize;
        this.left = new Image[6];
        this.right = new Image[6];
        this.idleL = new Image[4];
        this.idleR = new Image[4];
        this.getPlayerImage();
        this.resetValues();

        this.lastSprite = idleR != null ? idleR[0] : null;

        this.cameraX = screenW / 2 - (tileSize / 2);
        this.cameraY = screenH / 2 - (tileSize / 2);

    }

    public void resetValues() {
        this.worldX = tileSize * 12;
        this.worldY = tileSize * 12;
        this.spd = 4;
        this.direction = "right";
        this.facingTowards = "right";
    }

    public void updatePosition() {


        if (keyH.isMoving()) {
            if (keyH.getUpPressed() && keyH.getRightPressed()) {
                int normVector = this.spd / 2;
                this.direction = "right";
                this.facingTowards = "right";
                this.worldX += normVector;
                this.worldY -= normVector;
            } else if (keyH.getUpPressed() && keyH.getLeftPressed()) {
                int normVector = this.spd / 2;
                this.direction = "left";
                this.facingTowards = "left";
                this.worldX -= normVector;
                this.worldY -= normVector;
            } else if (keyH.getDownPressed() && keyH.getRightPressed()) {
                int normVector = this.spd / 2;
                this.direction = "right";
                this.facingTowards = "right";
                this.worldX += normVector;
                this.worldY += normVector;

            } else if (keyH.getDownPressed() && keyH.getLeftPressed()) {
                int normVector = this.spd / 2;
                this.direction = "left";
                this.facingTowards = "left";
                this.worldX -= normVector;
                this.worldY += normVector;

            } else if (keyH.getUpPressed()) {
                this.direction = "up";
                this.worldY -= this.spd;
            } else if (keyH.getDownPressed()) {
                this.direction = "down";
                this.worldY += this.spd;
            } else if (keyH.getLeftPressed()) {
                this.direction = "left";
                this.facingTowards = "left";
                this.worldX -= this.spd;
            } else if (keyH.getRightPressed()) {
                this.direction = "right";
                this.facingTowards = "right";
                this.worldX += spd;
            }

            spriteCounter++;

            if (spriteCounter > 7) {

                spriteNumber = (spriteNumber + 1) % 6;
                spriteCounter = 0;
            }
        } else {
            if (facingTowards.contains("right")) {
                direction = "idle-r";
            } else if (facingTowards.contains(("left"))) {
                direction = "idle-l";
            }
        }


    }

    public void draw(GraphicsContext gc) {

        //g2d.setColor(Color.white);
        //g2d.fillRect(this.x, this.y, this.gp.getTileSize(), this.gp.getTileSize());

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
                sprite = this.idleR[0];
            }
            case "idle-l" -> {
                sprite = this.idleL[0];
            }
            default -> {
                break;
            }
        }

        lastSprite = sprite;


        // Depending on the current direction we draw the respective sprite
        gc.drawImage(sprite, worldX - (tileSize / 2), worldY - (tileSize / 2));
    }

    public void getPlayerImage() {
        try {

            //String cwd = System.getProperty("user.dir");


            for (int i = 0; i < 6; i++) {

                if (this.left != null)
                    this.left[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/pink_walk/left/" + (i + 1) + "-left.png")));

            }


            for (int i = 0; i < 6; i++) {


                if (this.right != null)
                    this.right[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/pink_walk/right/" + (i + 1) + ".png")));

            }


            for (int i = 0; i < 4; i++) {

                if (this.idleL != null)
                    this.idleL[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/pink_idle/left/" + (i + 1) + "-left.png")));

            }

            for (int i = 0; i < 4; i++) {

                if (this.idleR != null)

                    this.idleR[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/units/pink_idle/right/" + (i + 1) + ".png")));


            }


        } catch (IllegalArgumentException e) {
            System.out.println("Bad reading of sprite");
            e.printStackTrace();
        }

    }
}
