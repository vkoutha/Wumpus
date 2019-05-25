package game;


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.GameData.ItemTypes;

public class Tile {
	
	private int row;
	private int column;
	private boolean isDiscovered;
	private ItemTypes item;
	private BufferedImage itemSprite;
	
	public Tile(int row, int column) {
		this.row = row;
		this.column = column;	
		item = ItemTypes.NONE;
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
	
	public void setDiscovered(boolean discovered) {
		isDiscovered = discovered;
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
		case NONE:
			itemSprite = null;
			break;
		}
	}
	
	public void removeItem() {
		itemSprite = null;
	}
	
	private boolean isNextToPlayer() {
		Player player = Game.game.getPlayer();
		for (int r = player.getRow() - player.getFlashlightRadius(); r <= player.getRow() + player.getFlashlightRadius(); r++) {
			for(int c = player.getColumn() - player.getFlashlightRadius(); c <= player.getColumn() + player.getFlashlightRadius(); c++) {
				if (row == r && column == c) {
					return true;
				}
				//Uncomment to not include diagonals
//				if((row == r && column == player.getColumn()) || (column == c && row == player.getRow())) {
//					return true;
//				}
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(GameData.grassSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH+1, GameData.TILE_HEIGHT, null);
		if(item != ItemTypes.NONE) {
			g2.drawImage(itemSprite, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		}
		if(!isDiscovered) {
			if(Game.game.getPlayer().getFlashlightRadius() > 0 && isNextToPlayer()) {
				g2.setComposite(AlphaComposite.SrcOver.derive(GameData.FLASHLIGHT_BLOCK_OPACITY));
			}
			g2.drawImage(GameData.rockSprite, (column*GameData.TILE_WIDTH), (row*GameData.TILE_HEIGHT), GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		}
	}

}