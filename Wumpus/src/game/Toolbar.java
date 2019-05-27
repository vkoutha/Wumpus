package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import game.GameData.ItemTypes;

public class Toolbar {

	
	private int flashlightCount, compassCount, explosiveCount, goldCount;
	private ArrayList<ItemTypes> items;
	public Toolbar() {
		items = new ArrayList<ItemTypes>();
	}
	
	public void addItem(ItemTypes item) {
		if(item == ItemTypes.FLASHLIGHT) {
			if(flashlightCount == 0) {
				items.add(ItemTypes.FLASHLIGHT);
			}
			flashlightCount++;
		}else if(item == ItemTypes.COMPASS) {
			if(compassCount == 0) {
				items.add(ItemTypes.COMPASS);
			}
			compassCount++;
		}else if (item == ItemTypes.EXPLOSIVE) {
			if(explosiveCount == 0) {
				System.out.println("ADDED EXPLOSIVE");
				items.add(ItemTypes.EXPLOSIVE);
			}
			explosiveCount++;
		}else if(item == ItemTypes.GOLD) {
			if(goldCount == 0) {
				items.add(ItemTypes.GOLD);
			}
			goldCount++;
		}
	}
	
	public void removeItem(ItemTypes item) {
		if(item == ItemTypes.FLASHLIGHT) {
			flashlightCount--;
			if(flashlightCount == 0) {
				items.remove(ItemTypes.FLASHLIGHT);
			}
			//Game.game.getPlayer().addFlashlight();
		}else if(item == ItemTypes.COMPASS) {
			compassCount--;
			if(compassCount == 0) {
				items.remove(ItemTypes.COMPASS);
			}
		}else if (item == ItemTypes.EXPLOSIVE) {
			explosiveCount--;
			if(explosiveCount == 0) {
				items.remove(ItemTypes.EXPLOSIVE);
			}
		}else if(item == ItemTypes.GOLD) {
			goldCount--;
			if(goldCount == 0) {
				items.remove(ItemTypes.GOLD);
			}
		}
	}
	
	public int getFlashlightRadius() {
		return flashlightCount;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(GameData.FRAME_WIDTH, 0, GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.FRAME_HEIGHT);
		g.setColor(Color.BLACK);
		//System.out.println(items.size());
		for(int i = 0; i < 4; i++) {
			g.drawLine(GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, GameData.FRAME_EXTENDED_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT);
		}
		for(int i = 0; i < items.size(); i++) {
			switch(items.get(i)) {
			case FLASHLIGHT:
				g.drawImage(GameData.flashlightSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
						GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + flashlightCount, GameData.FRAME_WIDTH + 5, (i * GameData.TOOLBAR_SLOT_HEIGHT) + 20);
				break;
			case COMPASS:
				g.drawImage(GameData.compassSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
						GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + compassCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case GOLD:
				g.drawImage(GameData.goldSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
						GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + goldCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case EXPLOSIVE:
				g.drawImage(GameData.explosiveSprite, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
						GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + explosiveCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case NONE:
				break;
			}
		}
	}
	
}
