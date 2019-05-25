package game;

import java.awt.Color;
import java.awt.Graphics;

import game.GameData.ItemTypes;

public class Toolbar {

	private ItemTypes[] items;
	private int explosiveCount, goldCount;
	public Toolbar() {
		items = new ItemTypes[GameData.TOOLBAR_SLOTS];
	}
	
	public void addItem(ItemTypes item) {
		int index = 0;
		while(index < GameData.TOOLBAR_SLOTS-1 && items[index] != null) {
			index++;
		}
		items[index] = item;
		if(item == ItemTypes.EXPLOSIVE) {
			explosiveCount++;
		}else if (item == ItemTypes.GOLD){
			goldCount++;
		}
		if(item == ItemTypes.FLASHLIGHT) {
			Game.game.getPlayer().addFlashlight();
		}
	}
	
	public void removeItem(ItemTypes item) {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(GameData.FRAME_WIDTH, 0, GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.FRAME_HEIGHT);
		g.setColor(Color.BLACK);
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				switch(items[i]) {
				case FLASHLIGHT:
					g.drawImage(GameData.flashlightSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
							GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
					break;
				case COMPASS:
					g.drawImage(GameData.compassSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
							GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
					break;
				case GOLD:
					g.drawImage(GameData.goldSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
							GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
					break;
				case EXPLOSIVE:
					g.drawImage(GameData.explosiveSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
							GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
					break;
				case NONE:
					break;
				}
			}
			g.drawLine(GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, GameData.FRAME_EXTENDED_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT);
		}
	}
	
}
