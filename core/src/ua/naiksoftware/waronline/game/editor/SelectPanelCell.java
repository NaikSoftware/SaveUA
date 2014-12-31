package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ua.naiksoftware.waronline.game.MapObjCode;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

public class SelectPanelCell extends Image {

    private boolean selected;
    private TextureRegion selBorder = ResKeeper.get(AtlasId.EDITOR_IMAGES)
            .findRegion("sel_tile");
    private Cell cell;
    private UnitCode unitCode;
    private MapObjCode mapObjCode;

    /**
     * Конструктор для создания тайлов на панель в редакторе
     *
     * @param cell
     * @param size
     */
    public SelectPanelCell(Cell cell) {
        super();
        this.cell = cell;
        TextureRegionDrawable d = new TextureRegionDrawable(cell.getTile().getTextureRegion());
        setDrawable(d);
		float size = d.getMinHeight();
        setOrigin(size / 2, size / 2);

        switch (cell.getRotation()) {
            case Cell.ROTATE_90:
                setRotation(90);
                break;
            case Cell.ROTATE_180:
                setRotation(180);
                break;
            case Cell.ROTATE_270:
                setRotation(270);
                break;
            default:
                break;
        }
    }

    /**
     * Конструктор для создания юнитов на панель в редакторе
     *
     * @param region
     * @param unitCode
     */
    public SelectPanelCell(TextureRegion region, UnitCode unitCode) {
        super(region);
        this.unitCode = unitCode;
		
    }

    /**
     * Конструктор для создания других обьектов карты на панель в редакторе
     *
     * @param region
     * @param mapObjCode
	 * @param size - preffered cell size
     */
    public SelectPanelCell(TextureRegion region, MapObjCode mapObjCode, int size) {
        super(region);
        this.mapObjCode = mapObjCode;
		Drawable d = getDrawable();
		d.setMinWidth(size);
		d.setMinHeight(size);
		setSize(size, size);
        setOrigin(size / 2, size / 2);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (selected) {
            batch.draw(selBorder, getX(), getY());
        }
    }

    public Cell getInsertCell() {
        return cell;
    }

    public UnitCode getUnitCode() {
        return unitCode;
    }

    public MapObjCode getMapObjCode() {
        return mapObjCode;
    }
}
