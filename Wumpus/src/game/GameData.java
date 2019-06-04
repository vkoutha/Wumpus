package game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameData {
	
	public static final String FRAME_NAME = "Wumpus";
	public static int FRAME_EXTENDED_WIDTH = 940;
	public static int FRAME_WIDTH = 800;
	public static int FRAME_EXTRA_WIDTH = FRAME_EXTENDED_WIDTH - FRAME_WIDTH;
	public static int FRAME_HEIGHT = 800;
	public static int FRAME_WIDTH_DIFFERENCE = 0;
	public static int FRAME_HEIGHT_DIFFERENCE = 0;
	public static final int UPDATE_SPEED_MS = 30;
	public static final int EXPLOSION_ANIMATION_TIME_MS = 1500;
	
	public static final int VERTICAL_DISTANCE_BETWEEN_BUTTONS = 20;
	
	public static final int TILE_AMOUNT = 8;
	public static int TILE_WIDTH = (FRAME_WIDTH/TILE_AMOUNT);
	public static int TILE_HEIGHT = (FRAME_HEIGHT/TILE_AMOUNT);
	
	public static int PLAYER_VELOCITY = 5;
	public static final double FRAME_SIZE_TO_VELOCITY = (FRAME_WIDTH * FRAME_HEIGHT) / PLAYER_VELOCITY;
	
	public static final int TOOLBAR_SLOTS = 4;
	public static int TOOLBAR_SLOT_HEIGHT = FRAME_HEIGHT/TOOLBAR_SLOTS;
	public static final int TOOLBAR_ITEM_PROPORTION = 5;
	
	public static final float FLASHLIGHT_BLOCK_OPACITY = 0.7f;
	public static final float WUMPUS_FADE_SPEED = .04f;

	public static AudioInputStream themeSong, battleSong;
	
	public static BufferedImage wumpusLogo, menuBackground, settingsMenuBackground;
	public static BufferedImage rockSprite, grassSprite, litGrassSprite;
	public static BufferedImage characterSpriteSheet, superSayainSpriteSheet, wumpusSprite;
	public static BufferedImage[] characterBackwardsSprite, characterForwardsSprite, characterLeftSprite, characterRightSprite;
	public static BufferedImage[] superSayainBackwardsSprite, superSayainForwardsSprite, superSayainLeftSprite, superSayainRightSprite;
	public static BufferedImage toolbarBox;
	public static BufferedImage flashlightSprite, compassSprite, goldSprite, explosiveSprite, swordSprite;
	public static Icon startGameUnselectedIcon, startGameSelectedIcon, settingsUnselectedIcon, settingsSelectedIcon, rulesUnselectedIcon, 
			rulesSelectedIcon;
	private static Image explosionAnimationImage, losingAnimationImage, winningAnimationImage;
	public static ImageIcon explosionAnimation, losingAnimation, winningAnimation;
	
	static {
		initMusic();
		initMenuSprites();
		initCharacterSprites();
		initTileSprites();
		initItemSprites();
		initAnimations();
	}
	
	private static void initMusic() {
		try {
			themeSong =  AudioSystem.getAudioInputStream(GameData.class.getResource("/sound/theme.wav"));
			battleSong = AudioSystem.getAudioInputStream(GameData.class.getResource("/sound/battleMusic.wav"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	 
	private static void initMenuSprites() {
		try {
			wumpusLogo = ImageIO.read(GameData.class.getResource("/img/wumpusLogo.png"));
			menuBackground = ImageIO.read(GameData.class.getResource("/img/backgrounds/menuBackground.jpg"));
			menuBackground = menuBackground.getSubimage(0, 150, menuBackground.getWidth(), menuBackground.getHeight()-150);
			settingsMenuBackground = ImageIO.read(GameData.class.getResource("/img/backgrounds/settingsMenuBackground.jpg"));
			//Silkscreen, 81px, https://www.befunky.com/create/photo-editor/
			BufferedImage startGameUnselectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/startGameUnclicked.png"));
			startGameUnselectedIcon = new ImageIcon(startGameUnselectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage startGameSelectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/startGameClicked.png"));
			startGameSelectedIcon = new ImageIcon(startGameSelectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage settingsUnselectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/settingsUnclicked.png"));
			settingsUnselectedIcon = new ImageIcon(settingsUnselectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage settingsSelectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/settingsClicked.png"));
			settingsSelectedIcon = new ImageIcon(settingsSelectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage rulesUnselectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/rulesUnclicked.png"));
			rulesUnselectedIcon = new ImageIcon(rulesUnselectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
			BufferedImage rulesSelectedImage = ImageIO.read(GameData.class.getResource("/img/buttons/rulesClicked.png"));
			rulesSelectedIcon = new ImageIcon(rulesSelectedImage.getScaledInstance(FRAME_EXTENDED_WIDTH/3, 65, Image.SCALE_DEFAULT));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initCharacterSprites() {
		try {
			
			characterSpriteSheet = ImageIO.read(GameData.class.getResource("/img/characters/character.png"));
			superSayainSpriteSheet = ImageIO.read(GameData.class.getResource("/img/characters/characterSuperSayain.png"));
			
			characterBackwardsSprite = new BufferedImage[4];
			characterBackwardsSprite[0] = characterSpriteSheet.getSubimage(448, 0, 480, 560);
			characterBackwardsSprite[1] = characterSpriteSheet.getSubimage(944, 32, 432, 528);
			characterBackwardsSprite[2] = characterSpriteSheet.getSubimage(1376, 0, 448, 560);
			characterBackwardsSprite[3] = characterSpriteSheet.getSubimage(0, 32, 432, 528);
			
			superSayainBackwardsSprite = new BufferedImage[4];
			superSayainBackwardsSprite[0] = superSayainSpriteSheet.getSubimage(448, 0, 480, 560);
			superSayainBackwardsSprite[1] = superSayainSpriteSheet.getSubimage(944, 32, 432, 528);
			superSayainBackwardsSprite[2] = superSayainSpriteSheet.getSubimage(1376, 0, 448, 560);
			superSayainBackwardsSprite[3] = superSayainSpriteSheet.getSubimage(0, 32, 432, 528);
			
			characterForwardsSprite = new BufferedImage[4];
			characterForwardsSprite[0] = characterSpriteSheet.getSubimage(448, 1856, 480, 544);
			characterForwardsSprite[1] = characterSpriteSheet.getSubimage(944, 1856, 432, 544);
			characterForwardsSprite[2] = characterSpriteSheet.getSubimage(1376, 1856, 448, 544);
			characterForwardsSprite[3] = characterSpriteSheet.getSubimage(0, 1856, 432, 544);
			
			superSayainForwardsSprite = new BufferedImage[4];
			superSayainForwardsSprite[0] = superSayainSpriteSheet.getSubimage(448, 1856, 480, 544);
			superSayainForwardsSprite[1] = superSayainSpriteSheet.getSubimage(944, 1856, 432, 544);
			superSayainForwardsSprite[2] = superSayainSpriteSheet.getSubimage(1376, 1856, 448, 544);
			superSayainForwardsSprite[3] = superSayainSpriteSheet.getSubimage(0, 1856, 432, 544);
			
			characterRightSprite = new BufferedImage[4];
			characterRightSprite[0] = characterSpriteSheet.getSubimage(448, 576, 464, 608);
			characterRightSprite[1] = characterSpriteSheet.getSubimage(944, 576, 384, 608);
			characterRightSprite[2] = characterSpriteSheet.getSubimage(1360, 576, 420, 608);
			characterRightSprite[3] = characterSpriteSheet.getSubimage(16, 576, 400, 608);
			
			superSayainRightSprite = new BufferedImage[4];
			superSayainRightSprite[0] = superSayainSpriteSheet.getSubimage(448, 576, 464, 608);
			superSayainRightSprite[1] = superSayainSpriteSheet.getSubimage(944, 576, 384, 608);
			superSayainRightSprite[2] = superSayainSpriteSheet.getSubimage(1360, 576, 420, 608);
			superSayainRightSprite[3] = superSayainSpriteSheet.getSubimage(16, 576, 400, 608);
			
			characterLeftSprite = new BufferedImage[4];
			characterLeftSprite[0] = characterSpriteSheet.getSubimage(0, 1200, 416, 608);
			characterLeftSprite[1] = characterSpriteSheet.getSubimage(448, 1200, 464, 608);
			characterLeftSprite[2] = characterSpriteSheet.getSubimage(944, 1200, 384, 608);
			characterLeftSprite[3] = characterSpriteSheet.getSubimage(1360, 1200, 400, 608);
			
			superSayainLeftSprite = new BufferedImage[4];
			superSayainLeftSprite[0] = superSayainSpriteSheet.getSubimage(0, 1200, 416, 608);
			superSayainLeftSprite[1] = superSayainSpriteSheet.getSubimage(448, 1200, 464, 608);
			superSayainLeftSprite[2] = superSayainSpriteSheet.getSubimage(944, 1200, 384, 608);
			superSayainLeftSprite[3] = superSayainSpriteSheet.getSubimage(1360, 1200, 400, 608);
			
			wumpusSprite = ImageIO.read(GameData.class.getResource("/img/characters/wumpusSprite.png"));

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
			litGrassSprite = grassSprite.getSubimage(50, 50, grassSprite.getWidth()-70,grassSprite.getHeight()-80);
			float scaleFactor = (float) (1.0 + (.75 / 10.0)); //(1 + (brightnessScaleFactor /10))
			RescaleOp op = new RescaleOp(scaleFactor, 0, null);
			litGrassSprite = op.filter(litGrassSprite, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initAnimations() {
		explosionAnimation = new ImageIcon(GameData.class.getResource("/img/animations/explosionAnimation.gif"));
		explosionAnimationImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH), TILE_HEIGHT, Image.SCALE_DEFAULT);
		explosionAnimation = new ImageIcon(explosionAnimationImage);
		losingAnimation = new ImageIcon(GameData.class.getResource("/img/animations/losingAnimation.gif"));
		losingAnimationImage = losingAnimation.getImage().getScaledInstance(FRAME_EXTENDED_WIDTH, FRAME_HEIGHT, Image.SCALE_DEFAULT);
		losingAnimation = new ImageIcon(losingAnimationImage);
		winningAnimation = new ImageIcon(GameData.class.getResource("/img/animations/winningAnimation.gif"));
		winningAnimationImage = winningAnimation.getImage().getScaledInstance(FRAME_EXTENDED_WIDTH, FRAME_HEIGHT, Image.SCALE_DEFAULT);
		winningAnimation = new ImageIcon(winningAnimationImage);
	}
	
	private static void initItemSprites() {
		try {
			toolbarBox = ImageIO.read(GameData.class.getResource("/img/items/toolbarBox.png"));
			flashlightSprite = ImageIO.read(GameData.class.getResource("/img/items/flashlightSprite.png")); 
			compassSprite = ImageIO.read(GameData.class.getResource("/img/items/compassSprite.png"));
			//goldSprite = ImageIO.read(GameData.class.getResource("/img/items/goldSprite.png"));
			explosiveSprite = ImageIO.read(GameData.class.getResource("/img/items/explosiveSprite.png"));
			swordSprite = ImageIO.read(GameData.class.getResource("/img/items/swordSprite.png"));
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
		explosionAnimation = new ImageIcon(GameData.class.getResource("/img/animations/explosionAnimation.gif"));
		explosionAnimationImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH), TILE_HEIGHT, Image.SCALE_DEFAULT);
		explosionAnimation = new ImageIcon(explosionAnimationImage);
		
		losingAnimation = new ImageIcon(GameData.class.getResource("/img/animations/losingAnimation.gif"));
		losingAnimationImage = losingAnimation.getImage().getScaledInstance(FRAME_EXTENDED_WIDTH, FRAME_HEIGHT, Image.SCALE_DEFAULT);
		losingAnimation = new ImageIcon(losingAnimationImage);
		winningAnimation = new ImageIcon(GameData.class.getResource("/img/animations/winningAnimation.gif"));
		winningAnimationImage = winningAnimation.getImage().getScaledInstance(FRAME_EXTENDED_WIDTH, FRAME_HEIGHT, Image.SCALE_DEFAULT);
		winningAnimation = new ImageIcon(winningAnimationImage);
		
	}
	
	public static boolean within(double x, double y) {
		return Math.abs(x - y) < PLAYER_VELOCITY;
	}
	
	public enum GameState{
		MENU,
		SETTINGS,
		RULES,
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
		SWORD,
		NONE
	}

}