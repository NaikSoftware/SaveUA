package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ua.naiksoftware.waronline.game.unit.UnitCode;

public class EditCell extends Image {

    private boolean selected;
    private TextureRegion selBorder = ResKeeper.get(AtlasId.EDITOR_IMAGES)
            .findRegion("sel_tile");
    private Cell cell;
    private UnitCode unitCode;

    /**
     * Конструктор для создания тайлов и обьектов
     * @param cell
     * @param size 
     */
    public EditCell(Cell cell, int size) {
        super();
        this.cell = cell;
        TextureRegionDrawable d = new TextureRegionDrawable(cell.getTile().getTextureRegion());
        d.setMinWidth(size);
        d.setMinHeight(size);
        setDrawable(d);
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
     * @param region 
     * @param unitCode 
     */
    public EditCell(TextureRegion region, UnitCode unitCode) {
        super(region);
        this.unitCode = unitCode;
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
}
