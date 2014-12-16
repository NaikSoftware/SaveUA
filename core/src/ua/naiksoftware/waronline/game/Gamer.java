package ua.naiksoftware.waronline.game;

public class Gamer {

	public static int count;
	
	private int id;
    
	public Gamer() {
		id = ++count;
	}
	
	public int getId() {
		return id;
	}
}
