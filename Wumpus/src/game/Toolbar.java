package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.GameData.ItemTypes;

public class Toolbar {

	
	private static int flashlightCount, compassCount, explosiveCount, goldCount, swordCount;
	private static ArrayList<ItemTypes> items = new ArrayList<ItemTypes>();
	
	public static void addItem(ItemTypes item) {
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
				items.add(ItemTypes.EXPLOSIVE);
			}
			explosiveCount++;
		}else if(item == ItemTypes.GOLD) {
			if(goldCount == 0) {
				items.add(ItemTypes.GOLD);
			}
			goldCount++;
		}else if (item == ItemTypes.SWORD) {
			Player.setSuperSayain(true);
			items.add(ItemTypes.SWORD);
			swordCount++;
		}
	}
	
	public static boolean weaponAvailable() {
		if(swordCount > 0) {
			return true;
		}
		return false;
	}
	
	public static void removeItem(ItemTypes item) {
		if(item == ItemTypes.FLASHLIGHT) {
			flashlightCount--;
			if(flashlightCount == 0) {
				items.remove(ItemTypes.FLASHLIGHT);
			}
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
	
	public static int getExplosiveCount() {
		return explosiveCount;
	}
	
	public static int getFlashlightRadius() {
		return flashlightCount;
	}
	
	public static void render(Graphics g) {
		g.setColor(Color.BLACK);
		for(int i = 0; i < GameData.TOOLBAR_SLOTS; i++) {
			g.drawImage(GameData.toolbarBox, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT, 
					GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
		}
		for(int i = 0; i < items.size(); i++) {
			switch(items.get(i)) {
			case FLASHLIGHT:
				g.drawImage(GameData.flashlightSprite, GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION), 
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)+(GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION)), 
						GameData.FRAME_EXTRA_WIDTH - (2*GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT - (2*GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION), null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + flashlightCount, GameData.FRAME_WIDTH + 5, (i * GameData.TOOLBAR_SLOT_HEIGHT) + 20);
				break;
			case COMPASS:
		        drawCompass(g, i);
				g.drawString("Uses: " + compassCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case GOLD:
				g.drawImage(GameData.goldSprite, GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION), 
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)+(GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION)), 
						GameData.FRAME_EXTRA_WIDTH - (2*GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT - (2*GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION), null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + goldCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case EXPLOSIVE:
				g.drawImage(GameData.explosiveSprite, GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION), 
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)+(GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION)), 
						GameData.FRAME_EXTRA_WIDTH - (2*GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT - (2*GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION), null);
				//g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + explosiveCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case SWORD:
				g.drawImage(GameData.swordSprite, GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION), 
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)+(GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION)), 
						GameData.FRAME_EXTRA_WIDTH - (2*GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT - (2*GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION), null);
				g.drawString("x1", GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case NONE:
				break;
			}
		}
	}
	
	private static void drawCompass(Graphics g, int slot) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(GameData.blankCompassSprite, GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION), 
		(slot * (GameData.TOOLBAR_SLOT_HEIGHT)+(GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION)), 
		GameData.FRAME_EXTRA_WIDTH - (2*GameData.FRAME_EXTRA_WIDTH/GameData.TOOLBAR_ITEM_PROPORTION),
		GameData.TOOLBAR_SLOT_HEIGHT - (2*GameData.TOOLBAR_SLOT_HEIGHT/GameData.TOOLBAR_ITEM_PROPORTION), null);
		//g.setFont(new Font("Arial", 15, Font.BOLD));
		
		int xDiff = Player.getRow() - Wumpus.getRow();
		int yDiff = Player.getColumn() - Wumpus.getColumn(); 
		double yVect = GameData.COMPASS_VECTOR_SCALE * (yDiff / Math.hypot(yDiff, xDiff));
		double xVect = GameData.COMPASS_VECTOR_SCALE * (xDiff / Math.hypot(xDiff, yDiff));
		if(xDiff == 0 && yDiff == 0) {
			g2.setColor(Color.GREEN);
		}else {
			g2.setColor(Color.RED);
		}
		g2.setStroke(new BasicStroke(6));
		g2.drawLine(GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/2), -4 + (slot * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT/2), 
				GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/2) - (int) (yVect), 
				(slot * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT/2) - (int) (xVect));
		
	}
	
}
