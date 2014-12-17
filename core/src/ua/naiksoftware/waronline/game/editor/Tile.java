package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Tile extends Image {

	private boolean selected;
	
	public Tile(TextureRegion region) {
		super(region);
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
