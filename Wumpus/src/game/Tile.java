package game;


import java.awt.Color;
import java.awt.Graphics;

public class Tile {
	
	private int row;
	private int column;
	private boolean isDiscovered;
	
	public Tile(int row, int column) {
		this.row = row;
		this.column = column;	
	} 
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setDiscovered(boolean discovered) {
		isDiscovered = discovered;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		if(!isDiscovered) {
			g.drawImage(GameData.rockSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		}else{
			g.drawImage(GameData.grassSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH+3, GameData.TILE_HEIGHT+5, null);
		}
	}

}