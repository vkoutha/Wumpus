package game;


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.GameData.ItemTypes;

public class Tile {
	
	private int row;
	private int column;
	private float opacity;
	private boolean isDiscovered, isFlashlightAffected;
	private ItemTypes item;
	private BufferedImage itemSprite;
	private AlphaComposite maxOpacity;
	
	public Tile(int row, int column) {
		this.row = row;
		this.column = column;	
		item = ItemTypes.NONE;
		//isDiscovered = true;
		opacity = 1f;
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
		if(discovered) {
			opacity = 0;
		}
	}
	
	public void setFlashlightAffected(boolean affected) {
		isFlashlightAffected = affected;
		if(affected && !isDiscovered) {
			//opacity = GameData.FLASHLIGHT_BLOCK_OPACITY.getAlpha();
		}else {
			//opacity = 1f;
		}
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
	
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public static void updateTiles() {
		for(Tile[] tileArr : Game.game.getTiles()) {
			for(Tile tile : tileArr) {
				if(tile.getRow() != Player.getRow() || tile.getColumn() != Player.getColumn()) {
					tile.setDiscovered(false);
				}
				if(tile.isFlashlightAffectedCalc()) {
					tile.setFlashlightAffected(true);
				}else{
					tile.setFlashlightAffected(false);
				}
			}
		}
	}
	
	public static void updateTilesOpacity() {
		for(Tile[] tileArr : Game.game.getTiles()) {
			for(Tile tile : tileArr) {
				if(tile.isFlashlightAffected() && tile.getOpacity() <= GameData.FLASHLIGHT_BLOCK_OPACITY.getAlpha() - GameData.TILE_DIMMING_SPEED) {
					tile.setOpacity(tile.getOpacity() + GameData.TILE_DIMMING_SPEED);
				}else if(tile.isFlashlightAffected() && tile.getOpacity() >= GameData.FLASHLIGHT_BLOCK_OPACITY.getAlpha() + GameData.TILE_BRIGHTENING_SPEED){
					tile.setOpacity(tile.getOpacity() - GameData.TILE_BRIGHTENING_SPEED);
				}else if(!tile.isFlashlightAffected() && tile.getOpacity() <= 1f - GameData.TILE_DIMMING_SPEED) {
					tile.setOpacity(tile.getOpacity() + GameData.TILE_DIMMING_SPEED);
				}
			}
		}
	}
	
	public float getOpacity() {
		return opacity;
	}
	
	public boolean isFlashlightAffected() {
		return isFlashlightAffected;
	}
	
	private boolean isFlashlightAffectedCalc() {
		if(row == Player.getRow() && column == Player.getColumn()) {
			return true;
		}
		if(Toolbar.getFlashlightRadius() == 0) {
			return false;
		}
		for (int r = Player.getRow() - Toolbar.getFlashlightRadius(); r <= Player.getRow() + Toolbar.getFlashlightRadius(); r++) {
			for(int c = Player.getColumn() - Toolbar.getFlashlightRadius(); c <= Player.getColumn() + Toolbar.getFlashlightRadius(); c++) {
				if (row == r && column == c) {
					return true;
				}
				//Uncomment to not include diagonals
//				if((row == r && column == Player.getColumn()) || (column == c && row == Player.getRow())) {
//					return true;
//				}
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		if(isDiscovered || isFlashlightAffected || opacity <= .99f) {
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
		g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
		if(!isDiscovered)
			g2.drawImage(GameData.rockSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}