package com.uprojects.stages;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

// Funciona solo como un portador de datos
public record TileData(Image tilesetImg,
                       Rectangle2D dimensiones) {

}
