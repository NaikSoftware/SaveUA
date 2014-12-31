package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.game.TileCode;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class MapCell extends Cell {

    private final TileCode code;

    public MapCell(TileCode code) {
        this.code = code;
    }

    public TileCode getCode() {
        return code;
    }
}
