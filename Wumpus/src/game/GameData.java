package game;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameData {
	
	public static final String FRAME_NAME = "Wumpus";
	public static int FRAME_EXTENDED_WIDTH = 940;
	public static int FRAME_WIDTH = 800;
	public static int FRAME_HEIGHT = 800;
	public static int FRAME_WIDTH_DIFFERENCE = 0;
	public static int FRAME_HEIGHT_DIFFERENCE = 0;
	public static final int UPDATE_SPEED_MS = 20;
	public static final int EXPLOSION_ANIMATION_TIME_MS = 1500;
	
	public static final int TILE_AMOUNT = 10;
	public static int TILE_WIDTH = (FRAME_WIDTH/TILE_AMOUNT);
	public static int TILE_HEIGHT = (FRAME_HEIGHT/TILE_AMOUNT);
	
	public static final int TOOLBAR_SLOTS = 4;
	public static int TOOLBAR_SLOT_HEIGHT = FRAME_HEIGHT/TOOLBAR_SLOTS;
	
	public static final float FLASHLIGHT_BLOCK_OPACITY = .85f;

	public static BufferedImage wumpusLogo = null, menuBackground = null, settingsMenuBackground;
	public static BufferedImage rockSprite = null, grassSprite = null;
	public static BufferedImage characterSprite = null, characterFowardsStillSprite = null, characterForwardsRunningSprite = null,
			characterBackwardsStillSprite = null, characterBackwardsRunningSprite = null, characterLeftStillSprite = null, characterLeftRunningSprite = null,
			characterRightStillSprite = null, characterRightRunningSprite = null;
	public static BufferedImage flashlightSprite, compassSprite, goldSprite, explosiveSprite;
	public static Icon startGameUnselectedIcon = null, startGameSelectedIcon = null;
	private static Image explosionAnimationImage = null;
	public static ImageIcon explosionAnimation = null;
	
	static {
		initMenuSprites();
		initCharacterSprites();
		initTileSprites();
		initItemSprites();
	}
	
	private static void initMenuSprites() {
		try {
			wumpusLogo = ImageIO.read(GameData.class.getResource("/img/wumpusLogo.png"));
			menuBackground = ImageIO.read(GameData.class.getResource("/img/backgrounds/menuBackground.jpg"));
			menuBackground = menuBackground.getSubimage(0, 150, menuBackground.getWidth(), menuBackground.getHeight()-150);
			settingsMenuBackground = ImageIO.read(GameData.class.getResource("/img/backgrounds/settingsMenuBackground.jpg"));
			//Silkscreen, 81px, https://www.befunky.com/create/photo-editor/
			BufferedImage startGameUnclickedImage = ImageIO.read(GameData.class.getResource("/img/buttons/startGameUnclicked.png"));
			startGameUnselectedIcon = new ImageIcon(startGameUnclickedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage startGameClickedImage = ImageIO.read(GameData.class.getResource("/img/buttons/startGameClicked.png"));
			startGameSelectedIcon = new ImageIcon(startGameClickedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initCharacterSprites() {
		try {
			characterSprite = ImageIO.read(GameData.class.getResource("/img/characters/character.png"));
			characterBackwardsRunningSprite = characterSprite.getSubimage(0, 0, 460, 590);
			characterBackwardsStillSprite = characterSprite.getSubimage(470, 0, 460, 560);
			characterForwardsRunningSprite = characterSprite.getSubimage(0, 1810, 460, 590); 
			characterFowardsStillSprite = characterSprite.getSubimage(470, 1810, 460, 590);
			characterRightRunningSprite = characterSprite.getSubimage(0, 600, 460, 590);
			characterRightStillSprite = characterSprite.getSubimage(470, 600, 470, 590);
			characterLeftRunningSprite = characterSprite.getSubimage(470, 1200, 460, 580);
			characterLeftStillSprite = characterSprite.getSubimage(0, 1200, 460, 580);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initTileSprites() {
		try {
			rockSprite = ImageIO.read(GameData.class.getResource("/img/tiles/rockTile.png"));
			rockSprite = rockSprite.getSubimage(10, 10, rockSprite.getWidth()-20, rockSprite.getHeight()-20);
			grassSprite = ImageIO.read(GameData.class.getResource("/img/tiles/grassTile.jpg"));
			grassSprite = grassSprite.getSubimage(50, 50, grassSprite.getWidth()-70, grassSprite.getHeight()-80);
			explosionAnimation = new ImageIcon(GameData.class.getResource("/img/animations/explosionGIF.gif"));
			explosionAnimationImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH), TILE_HEIGHT, Image.SCALE_DEFAULT);
			explosionAnimation = new ImageIcon(explosionAnimationImage);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initItemSprites() {
		try {
			flashlightSprite = ImageIO.read(GameData.class.getResource("/img/items/flashlightSprite.png")); 
			compassSprite = ImageIO.read(GameData.class.getResource("/img/items/compassSprite.gif"));
			goldSprite = ImageIO.read(GameData.class.getResource("/img/items/goldSprite.png"));
			explosiveSprite = ImageIO.read(GameData.class.getResource("/img/items/explosiveSprite.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeBackground(JButton button) {
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
	}
	
	public static void rescaleAnimations() {
		explosionAnimationImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH), TILE_HEIGHT, Image.SCALE_DEFAULT);
		explosionAnimation = new ImageIcon(explosionAnimationImage);
	}
	
	public enum GameState{
		MENU,
		SETTINGS,
		IN_GAME,
		PAUSED
	}
	
	public enum MovementDirections{
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public enum ItemTypes{
		FLASHLIGHT,
		COMPASS,
		GOLD,
		EXPLOSIVE,
		NONE
	}

}