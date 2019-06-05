package game;


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.GameData.ItemTypes;

public class Tile {
	
	private int row;
	private int column;
	private boolean isDiscovered, isFlashlightAffected;
	private ItemTypes item;
	private BufferedImage itemSprite;
	private AlphaComposite maxOpacity;
	
	public Tile(int row, int column) {
		this.row = row;
		this.column = column;	
		item = ItemTypes.NONE;
		//isDiscovered = true;
		maxOpacity = AlphaComposite.SrcOver.derive(1f);
	} 
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public ItemTypes getItem() {
		return item;
	}
	
	public boolean isDiscovered() {
		return isDiscovered;
	}
	
	public void setDiscovered(boolean discovered) {
		isDiscovered = discovered;
	}
	
	public void setFlashlightAffected(boolean affected) {
		isFlashlightAffected = affected;
	}
	
	public void setItem(ItemTypes item) {
		this.item = item;
		switch(item) {
		case FLASHLIGHT:
			itemSprite = GameData.flashlightSprite;
			break;
		case COMPASS:
			itemSprite = GameData.compassSprite;
			break;
		case GOLD:
			itemSprite = GameData.goldSprite;
			break;
		case EXPLOSIVE:
			itemSprite = GameData.explosiveSprite;
			break;
		case SWORD:
			itemSprite = GameData.swordSprite;
			break;
		case NONE:
			itemSprite = null;
			break;
		}
	}
	
	public void removeItem() {
		item = ItemTypes.NONE;
		itemSprite = null;
	}
	
	public static void updateAffectedTiles() {
		for(Tile[] tileArr : Game.game.getTiles()) {
			for(Tile tile : tileArr) {
				if(tile.isFlashlightAffectedCalc()) {
					tile.setFlashlightAffected(true);
					tile.setDiscovered(true);
				}else{
					tile.setFlashlightAffected(false);
				}
			}
		}
	}
	
	public boolean isFlashlightAffected() {
		return isFlashlightAffected;
	}
	
	private boolean isFlashlightAffectedCalc() {
		for (int r = Player.getRow() - Toolbar.getFlashlightRadius(); r <= Player.getRow() + Toolbar.getFlashlightRadius(); r++) {
			for(int c = Player.getColumn() - Toolbar.getFlashlightRadius(); c <= Player.getColumn() + Toolbar.getFlashlightRadius(); c++) {
//				if (row == r && column == c) {
//					return true;
//				}
				//Uncomment to not include diagonals
				if((row == r && column == Player.getColumn()) || (column == c && row == Player.getRow())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		if(isDiscovered || isFlashlightAffected) {
			g2.drawImage(GameData.grassSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH+1, GameData.TILE_HEIGHT, null);
			if(item != ItemTypes.NONE) {
				g2.drawImage(itemSprite, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
			}
		}
		if(row == Wumpus.getRow() && column == Wumpus.getColumn()) {
			g2.setComposite(AlphaComposite.SrcOver.derive(Wumpus.getOpacity()));
			g2.drawImage(GameData.wumpusSprite, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
			g2.setComposite(maxOpacity);
		}
		if(!isDiscovered) {
			if(Toolbar.getFlashlightRadius() > 0 && isFlashlightAffected) {
				g2.setComposite(GameData.FLASHLIGHT_BLOCK_OPACITY);
			}
			g2.drawImage(GameData.rockSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		}
	}

}