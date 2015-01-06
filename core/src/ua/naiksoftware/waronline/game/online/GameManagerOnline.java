package ua.naiksoftware.waronline.game.online;

import ua.naiksoftware.waronline.game.GameManager;
import ua.naiksoftware.waronline.game.GameScreen;

public class GameManagerOnline implements GameManager {

	/** For showing remote gamer actions */
	private final GameScreen gameScreen;
	
	/** Game manager for online game */
	public GameManagerOnline(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
	
	@Override
	public void prepareAndStartGame() {
		// TODO: Implement this method
	}
	
}
