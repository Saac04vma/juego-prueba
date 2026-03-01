package com.uprojects.stages;


import com.uprojects.entities.Player;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.mapeditor.core.*;
import org.mapeditor.io.TMXMapReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

public class MapHandler {

    private Map map;
    private Player player;
    private HashMap<Tile, Image> tileImages;
    private HashMap<Tile, TileData> tilesData;

    public MapHandler(Player player1) {
        this.player = player1;
        this.loadMapFile();
        this.tileImages = new HashMap<>();
        this.tilesData = new HashMap<>();
        this.buffToFxImage();
        //this.loadMapData(this.map);
    }


    public void loadMapFile() {

        String cwd = System.getProperty("user.dir");

        try {


            TMXMapReader mapReader = new TMXMapReader();


            this.map = mapReader.readMap("src/main/resources/maps/test3.tmx");

            if (this.map == null) {
                System.out.println("MAP NOT LOADED");
            } else {
                System.out.println("Map Loaded Successfully!");
                System.out.println("Dimensions: " + map.getWidth() + "x" + map.getHeight());
                System.out.println("Layers found: " + map.getLayerCount());
                System.out.println("Tilesets found: " + map.getTileSets().size());
            }

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void buffToFxImage() {

        try {


            int tileW = map.getTileWidth();
            int tileH = map.getTileHeight();

            for (TileSet ts : this.map.getTileSets()) {


                //InputStream imgSource = ts.getImageData().getSource().getClass().getResourceAsStream("/maps/");
                InputStream imgSource = getClass().getResourceAsStream("/maps/tilesets/tileset.png");


                if (imgSource == null) {
                    System.out.println("No existe el tileset -> /tilesets/tileset.png");
                    continue;
                }


                BufferedImage imgSet = ImageIO.read(imgSource);
                //if (fullSheet == null) continue;


                // Calculate how many columns are in the tileset image
                int columns = imgSet.getWidth() / tileW;


                for (Tile tile : ts) {
                    if (tile == null) continue;

                    int id = tile.getId();

                    /*
                    BufferedImage awtImg = (BufferedImage) tile.getImage();
                    Image fxImg = SwingFXUtils.toFXImage(awtImg, null);
                    this.tileImages.put(id, fxImg);
*/

                    int tileXPos = (id % columns) * tileW;
                    int tileYPos = (id / columns) * tileH;

                    BufferedImage singleTileImg = imgSet.getSubimage(tileXPos, tileYPos, tileW, tileH);
                    Image fxImg = SwingFXUtils.toFXImage(singleTileImg, null);
                    tileImages.put(tile, fxImg);


                    //                  if (tileXPos + tileW <= fullSheet.getWidth() && tileYPos + tileH <= fullSheet.getHeight()) {
                    // GraphicsContext solo acepta la clase JavaFx Image, libtiled devuelve BufferedImage. Debemos convertirlo antes de poder pintar el mapa
                    //BufferedImage subImg = fullSheet.getSubimage(tileXPos, tileYPos, tileW, tileH);
                    //Image fxImg = SwingFXUtils.toFXImage(tile.getImage(), null);
                    //tileImages.put(tile, fxImg);
                    //                }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMapData(Map map) {

        for (TileSet ts : map.getTileSets()) {
            BufferedImage tileImg = ts.getFirstTile().getImage();
            Image fxImg = SwingFXUtils.toFXImage(tileImg, null);

            int tileW = ts.getTileWidth();
            int tileH = ts.getTileHeight();
            int columns = ts.getColumns();
            //int margins = ts.getTileMargin();
            //int spacing = ts.getTileSpacing();


            for (Tile tile : ts) {
                int id = tile.getId();

                // Calculamos la posicion x,y de la cuadricula
                double posX = (id % columns) * (tileW);
                double posY = (id / columns) * (tileH);


                Rectangle2D limites = new Rectangle2D(posX, posY, tileW, tileH);
                tilesData.put(tile, new TileData(fxImg, limites));
            }
        }
    }

    public Image getFxImage(Tile tile) {

        if (tile == null) {
            return null;
        }

        /*
        int id = tile.getId();

        if (!tileImages.containsKey(id)) {
            BufferedImage awtImg = (BufferedImage) tile.getImage();
            Image fxImg = SwingFXUtils.toFXImage(awtImg, null);
            this.tileImages.put(tile, fxImg);
        }
*/
        return this.tileImages.get(tile);
    }

    public void draw(GraphicsContext gc, int zoom) {

        try {

            // Dimensiones de los tiles individuales (64x64 o 32x32 por ejemplo)
            int tileWidth = map.getTileWidth();
            int tileHeight = map.getTileHeight();

            // Pixeles visibles despues de aplicar zoom
            //int xVisible = (player.cameraX * 2) / zoom;
            //int yVisible = (player.cameraY * 2) / zoom;

            int xVisible = (int) (gc.getCanvas().getWidth() / zoom) / 2;
            int yVisible = (int) (gc.getCanvas().getHeight() / zoom) / 2;

            // Calculamos Tiles visibles para el jugador (campo de vision) para no renderizar mas de la cuenta
            int colInicial = Math.max(0, (player.worldX - xVisible) / tileWidth);
            int colFinal = Math.min(map.getWidth(), (player.worldX + xVisible) / tileWidth) + 2;
            int filaInicial = Math.max(0, (player.worldY - yVisible) / tileHeight);
            int filaFinal = Math.min(map.getHeight(), (player.worldY + yVisible) / tileHeight) + 2;


            for (MapLayer layer : this.map.getLayers()) {

                if (layer instanceof TileLayer) {


                    TileLayer tLayer = (TileLayer) layer;

                    // Dimensiones de una capa del mapa
                    int anchoCapa = layer.getBounds().width;
                    int altoCapa = layer.getBounds().height;


                    //int offsetX = (player.cameraX / zoom) - player.worldX;
                    //int offsetY = (player.cameraY / zoom) - player.worldY;

                    for (int y = filaInicial; y < filaFinal; y++) {
                        for (int x = colInicial; x < colFinal; x++) {
                            Tile singleTile = tLayer.getTileAt(x, y);
                            //Image img = getFxImage(singleTile);


                            if (singleTile == null) continue;


                            Image img = this.getFxImage(singleTile);


                            // Posicion absoluta del mundo (pixeles)
                            int posX = x * tileWidth;
                            int posY = y * tileHeight;

                            // Posicion relativa a la camara del jugador
                            //int screenX = posX - this.player.worldX + this.player.cameraX;
                            //int screenY = posY - this.player.worldY + this.player.cameraY;

                            // Posicion considerando zoom
                            //int screenX = posX + offsetX;
                            //int screenY = posY + offsetY;
                            // POR HACER: IMPLEMENTAR LOGICA DE COLISIONES Y PROPIEDADES PERSONALIZADAS AQUI
                            boolean isVisible = true;


                            if (isVisible) {
                                //gc.drawImage(this.getFxImage(singleTile), screenX, screenY);

                                gc.drawImage(this.getFxImage(singleTile), posX, posY);
                            }

                        }
                    }

                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
