package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.GameData.GameState;
import game.GameData.ItemTypes;
import game.GameData.TrackingOptions;

public class Toolbar {

	private static int flashlightCount, compassCount, explosiveCount, goldCount, swordCount;
	private static ArrayList<ItemTypes> items = new ArrayList<ItemTypes>();
	private static TrackingOptions itemToTrack;

	public static void addItem(ItemTypes item) {
		if (item == ItemTypes.FLASHLIGHT) {
			if (flashlightCount == 0) {
				items.add(ItemTypes.FLASHLIGHT);
			}
			flashlightCount++;
		} else if (item == ItemTypes.COMPASS) {
			if (compassCount == 0) {
				items.add(ItemTypes.COMPASS);
			}
			compassCount++;
		} else if (item == ItemTypes.EXPLOSIVE) {
			if (explosiveCount == 0) {
				items.add(ItemTypes.EXPLOSIVE);
			}
			explosiveCount++;
		} else if (item == ItemTypes.GOLD) {
			if (goldCount == 0) {
				items.add(ItemTypes.GOLD);
			}
			goldCount++;
		} else if (item == ItemTypes.SWORD) {
			Player.setSuperSayain(true);
			items.add(ItemTypes.SWORD);
			swordCount++;
		}
	}

	public static boolean compassAvailable() {
		if (compassCount > 0) {
			return true;
		}
		return false;
	}

	public static boolean weaponAvailable() {
		if (swordCount > 0) {
			return true;
		}
		return false;
	}

	public static void removeItem(ItemTypes item) {
		if (item == ItemTypes.FLASHLIGHT) {
			flashlightCount--;
			if (flashlightCount == 0) {
				items.remove(ItemTypes.FLASHLIGHT);
			}
		} else if (item == ItemTypes.COMPASS) {
			compassCount--;
			if (compassCount == 0) {
				items.remove(ItemTypes.COMPASS);
			}
		} else if (item == ItemTypes.EXPLOSIVE) {
			explosiveCount--;
			if (explosiveCount == 0) {
				items.remove(ItemTypes.EXPLOSIVE);
			}
		} else if (item == ItemTypes.GOLD) {
			goldCount--;
			if (goldCount == 0) {
				items.remove(ItemTypes.GOLD);
			}
		}
	}

	public static void setTrackingItem(TrackingOptions item) {
		itemToTrack = item;
	}

	public static void removeAll() {
		items.clear();
		itemToTrack = null;
		flashlightCount = 0;
		compassCount = 0;
		explosiveCount = 0;
		swordCount = 0;
		goldCount = 0;
	}

	public static int getItemSlot(ItemTypes item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == item) {
				return i;
			}
		}
		return -1;
	}

	public static int getExplosiveCount() {
		return explosiveCount;
	}

	public static int getFlashlightRadius() {
		return flashlightCount;
	}

	public static int[] getTrackingItemCoordinate() {
		switch (itemToTrack) {
		case WUMPUS:
			return new int[] { Wumpus.getRow(), Wumpus.getColumn() };
		case FLASHLIGHT:
			for (int r = 0; r < GameData.BOARD_SIZE; r++) {
				for (int c = 0; c < GameData.BOARD_SIZE; c++) {
					if (Game.game.getTiles()[r][c].getItem() == ItemTypes.FLASHLIGHT) {
						return new int[] { r, c };
					}
				}
			}
			break;
		case SWORD:
			for (int r = 0; r < GameData.BOARD_SIZE; r++) {
				for (int c = 0; c < GameData.BOARD_SIZE; c++) {
					if (Game.game.getTiles()[r][c].getItem() == ItemTypes.SWORD) {
						return new int[] { r, c };
					}
				}
			}
			break;
		case EXPLOSIVE:
			for (int r = 0; r < GameData.BOARD_SIZE; r++) {
				for (int c = 0; c < GameData.BOARD_SIZE; c++) {
					if (Game.game.getTiles()[r][c].getItem() == ItemTypes.EXPLOSIVE) {
						return new int[] { r, c };
					}
				}
			}
			break;
		}
		return new int[] { -1, -1 };
	}

	public static void render(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < GameData.TOOLBAR_SLOTS; i++) {
			g.drawImage(GameData.toolbarBox, GameData.FRAME_WIDTH, i * GameData.TOOLBAR_SLOT_HEIGHT,
					GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_WIDTH, GameData.TOOLBAR_SLOT_HEIGHT, null);
		}
		for (int i = 0; i < items.size(); i++) {
			switch (items.get(i)) {
			case FLASHLIGHT:
				g.drawImage(GameData.flashlightSprite,
						GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)
								+ (GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION)),
						GameData.FRAME_EXTRA_WIDTH
								- (2 * GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT
								- (2 * GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION),
						null);
				// g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + flashlightCount, GameData.FRAME_WIDTH + 5, (i * GameData.TOOLBAR_SLOT_HEIGHT) + 20);
				break;
			case COMPASS:
				if (Game.game.inCompassMenu() == false) {
					drawCompass(g, i);
				}
				g.drawString("Uses: " + compassCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case GOLD:
				g.drawImage(GameData.goldSprite,
						GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)
								+ (GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION)),
						GameData.FRAME_EXTRA_WIDTH
								- (2 * GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT
								- (2 * GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION),
						null);
				// g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + goldCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case EXPLOSIVE:
				g.drawImage(GameData.explosiveSprite,
						GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)
								+ (GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION)),
						GameData.FRAME_EXTRA_WIDTH
								- (2 * GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT
								- (2 * GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION),
						null);
				// g.setFont(new Font("Arial", 15, Font.BOLD));
				g.drawString("x" + explosiveCount, GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case SWORD:
				g.drawImage(GameData.swordSprite,
						GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						(i * (GameData.TOOLBAR_SLOT_HEIGHT)
								+ (GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION)),
						GameData.FRAME_EXTRA_WIDTH
								- (2 * GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
						GameData.TOOLBAR_SLOT_HEIGHT
								- (2 * GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION),
						null);
				g.drawString("x1", GameData.FRAME_WIDTH + 5, i * GameData.TOOLBAR_SLOT_HEIGHT + 20);
				break;
			case NONE:
				break;
			}
		}
	}

	private static void drawCompass(Graphics g, int slot) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(GameData.blankCompassSprite,
				GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
				(slot * (GameData.TOOLBAR_SLOT_HEIGHT)
						+ (GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION)),
				GameData.FRAME_EXTRA_WIDTH - (2 * GameData.FRAME_EXTRA_WIDTH / GameData.TOOLBAR_ITEM_PROPORTION),
				GameData.TOOLBAR_SLOT_HEIGHT - (2 * GameData.TOOLBAR_SLOT_HEIGHT / GameData.TOOLBAR_ITEM_PROPORTION),
				null);
		int xDiff = -1, yDiff = -1;
		if (itemToTrack != null && getTrackingItemCoordinate()[0] != -1) {
			xDiff = getTrackingItemCoordinate()[1] - Player.getColumn();
			yDiff = getTrackingItemCoordinate()[0] - Player.getRow();
		}
		double xVect = GameData.COMPASS_VECTOR_SCALE * (xDiff / Math.hypot(yDiff, xDiff));
		double yVect = GameData.COMPASS_VECTOR_SCALE * (yDiff / Math.hypot(xDiff, yDiff));
		if (xDiff == 0 && yDiff == 0) {
			g2.setColor(Color.GREEN);
		} else if (itemToTrack == null || (itemToTrack != null && getTrackingItemCoordinate()[0] == -1)) {
			xVect = 0;
			yVect = 0;
			g2.setColor(Color.ORANGE);
		} else {
			g2.setColor(Color.RED);
		}
		g2.setStroke(new BasicStroke(6));
		g2.drawLine(GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / 2),
				(slot * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT / 2)
						+ GameData.COMPASS_NEEDLE_VERTICAL_SHIFT,
				GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH / 2) + (int) (xVect),
				(slot * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT / 2)
						+ GameData.COMPASS_NEEDLE_VERTICAL_SHIFT + (int) (yVect));

	}

}
