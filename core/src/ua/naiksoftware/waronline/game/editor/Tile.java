package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.game.TileCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Tile extends Image {

	private boolean selected;
	private TextureRegion selBorder = ResKeeper.get(AtlasId.EDITOR_IMAGES)
			.findRegion("sel_tile");
	private Cell cell;

	public Tile(Cell cell) {
		super(cell.getTile().getTextureRegion());
		this.cell = cell;
		setOrigin(getWidth() / 2, getHeight() / 2);
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
}
