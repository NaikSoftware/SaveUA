package ua.naiksoftware.waronline.map;


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
