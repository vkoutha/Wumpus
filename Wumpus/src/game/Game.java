package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.GameData.GameState;
import game.GameData.ItemTypes;

public class Game implements ActionListener, KeyListener {

	public static Game game;
	private JFrame frame;
	private Timer timer;
	private Renderer renderer;
	private Tile[][] tiles;
	private GameState gameState;
	private JLabel explosionAnimation, ultimateExplosionAnimation, losingAnimation, winningAnimation;
	private Clip themePlayer;
	private boolean explosionInProgress, battleInProgress;

	public Game() {
		frame = new JFrame(GameData.FRAME_NAME);
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		renderer = new Renderer();
		renderer.setPreferredSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		renderer.setBackground(Color.LIGHT_GRAY);
		frame.setMinimumSize(new Dimension(800, 800));
		frame.add(renderer);
		frame.addKeyListener(this);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		//startMusic(GameData.themeSong, true);
		gameState = GameState.MENU;
		addMenuWidgets();
		timer.start();
	}

	private void initialize() {
		tiles = new Tile[GameData.TILE_AMOUNT][GameData.TILE_AMOUNT];
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				tiles[r][c] = new Tile(r, c);
			}
		}
		tiles[0][0].setDiscovered(true);
		initItems();
		explosionAnimation = new JLabel(GameData.explosionAnimation);
		ultimateExplosionAnimation = new JLabel(GameData.ultimateExplosionAnimation);
		losingAnimation = new JLabel(GameData.losingAnimation);
		winningAnimation = new JLabel(GameData.winningAnimation);
		GameData.FRAME_WIDTH_DIFFERENCE = frame.getWidth() - GameData.FRAME_EXTENDED_WIDTH;
		GameData.FRAME_HEIGHT_DIFFERENCE = frame.getHeight() - GameData.FRAME_HEIGHT;
	}

	private void initItems() {
		Toolbar.addItem(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.FLASHLIGHT);
		assignNewRandomTile(ItemTypes.COMPASS);
		assignNewRandomTile(ItemTypes.SWORD);
	}
	
	private void assignNewRandomTile(ItemTypes item) {
		int randRow, randCol;
		do {
			randRow = (int) (Math.random() * GameData.TILE_AMOUNT);
			randCol = (int) (Math.random() * GameData.TILE_AMOUNT);
		}while(randRow + randCol == 0 || tiles[randRow][randCol].getItem() != ItemTypes.NONE);
	 	tiles[randRow][randCol].setItem(item);		
	}
	
	private void addMenuWidgets() {
		renderer.revalidate();
		renderer.setLayout(new BoxLayout(renderer, BoxLayout.PAGE_AXIS));
		JButton startGameButton = new JButton(GameData.startGameUnselectedIcon);
		startGameButton.setRolloverIcon(GameData.startGameSelectedIcon);
		startGameButton.setFocusable(false);
		GameData.removeBackground(startGameButton);
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gameState = GameState.IN_GAME;
				renderer.removeAll();
			}
		});
		startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.wumpusLogo.getHeight()*2));
		renderer.add(startGameButton);

		JButton settingsMenuButton = new JButton(GameData.settingsUnselectedIcon);
		settingsMenuButton.setRolloverIcon(GameData.settingsSelectedIcon);
		settingsMenuButton.setFocusable(false);
		GameData.removeBackground(settingsMenuButton);
		settingsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gameState = GameState.SETTINGS;
				renderer.removeAll();
			}
		});
		settingsMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.VERTICAL_DISTANCE_BETWEEN_BUTTONS));
		renderer.add(settingsMenuButton);
		
		JButton rulesButton = new JButton(GameData.rulesUnselectedIcon);
		rulesButton.setRolloverIcon(GameData.rulesSelectedIcon);
		rulesButton.setFocusable(false);
		GameData.removeBackground(rulesButton);
		settingsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gameState = GameState.SETTINGS;
				renderer.removeAll();
			}
		});
		rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.VERTICAL_DISTANCE_BETWEEN_BUTTONS));
		renderer.add(rulesButton);
	}
	
	private void addPausedWidgets(){
		addMenuWidgets();
	}
	
	private void addSettingsWidgets(){
		renderer.revalidate();
		GroupLayout layout = new GroupLayout(renderer);
		//JSlider for player speed
		//JSlider for amount of tiles
		//JSlider for amount of flashlights
		renderer.setLayout(layout);
	}
	
	public void render(Graphics g) {
		switch(gameState) {
		case MENU:
			renderMenu(g);
			break;
		case SETTINGS:
			renderSettings(g);
			break;
		case RULES:
			renderRules(g);
			break;
		case PAUSED:
			renderPaused(g);
		case IN_GAME:
			renderInGame(g);
			break;
		}
	}
	
	private void renderMenu(Graphics g){
		g.drawImage(GameData.menuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
		g.drawImage(GameData.wumpusLogo, (GameData.FRAME_EXTENDED_WIDTH/2) - (GameData.wumpusLogo.getWidth()/2), 60, GameData.wumpusLogo.getWidth(), 
				GameData.wumpusLogo.getHeight(), null);
	}

	
	private void renderSettings(Graphics g){
		g.drawImage(GameData.settingsMenuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
	}
	
	private void renderRules(Graphics g){
		
	}
	
	private void renderPaused(Graphics g){
		
	}
	
	private void renderInGame(Graphics g){
		for (Tile[] tileArr : tiles)
			for (Tile tile : tileArr)
				tile.render(g);
		Player.render(g);
		Toolbar.render(g);
		if(gameState == GameState.PAUSED) {
			g.setColor(Color.DARK_GRAY);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setComposite(AlphaComposite.SrcOver.derive(.8f));
			g2.fillRect(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
		}
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(gameState) {
		case MENU:
			break;
		case SETTINGS:
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				gameState = GameState.MENU;
				renderer.removeAll();
				addMenuWidgets();
				break;
			}
			break;
		case RULES:
			break;
		case IN_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				Player.move(GameData.MovementDirections.LEFT);
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				Player.move(GameData.MovementDirections.RIGHT);
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				Player.move(GameData.MovementDirections.UP);
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				Player.move(GameData.MovementDirections.DOWN);
				break;
			case KeyEvent.VK_SPACE:
				if(!explosionInProgress && Toolbar.getExplosiveCount() > 0) {
					Player.shoot();    
				}
				break;
			case KeyEvent.VK_X:
				if(!explosionInProgress) {
					Player.attack();
				}
			case KeyEvent.VK_Z:
				if(tiles[Player.getRow()][Player.getColumn()].getItem() != ItemTypes.NONE) {
					Toolbar.addItem(tiles[Player.getRow()][Player.getColumn()].getItem());
					Tile.updateTiles();
					tiles[Player.getRow()][Player.getColumn()].removeItem();
				}
				break;
			case KeyEvent.VK_M:
				themePlayer.stop();
				break;
			case KeyEvent.VK_T:
				break;
			case KeyEvent.VK_ESCAPE:
				System.out.println(gameState);
				gameState = GameState.PAUSED;
				addMenuWidgets();
				break;
			}
			break;
		case PAUSED:
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				renderer.removeAll();
				gameState = GameState.IN_GAME;
				break;
			}
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	private void startMusic(AudioInputStream stream, boolean loop) { 
		try {
			if(themePlayer != null && themePlayer.getMicrosecondPosition() > 0) {
				themePlayer.stop();
			}
			themePlayer = AudioSystem.getClip();
			themePlayer.open(stream);
			themePlayer.start();
			if(loop) {
				themePlayer.loop(-1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void update() {
		updateSize();
		Tile.updateTilesOpacity();
		Player.updatePos();
		Wumpus.updateFadeMove();
	}

	private void updateSize() {
		if (frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE != GameData.FRAME_EXTENDED_WIDTH
				|| frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE != GameData.FRAME_HEIGHT) {
			renderer.setPreferredSize(new Dimension(frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE,
					frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE));
			GameData.FRAME_EXTENDED_WIDTH = (int) frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE;
			GameData.FRAME_WIDTH = GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_EXTRA_WIDTH;
			GameData.FRAME_HEIGHT = (int) frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE;
			GameData.TILE_WIDTH = GameData.FRAME_WIDTH / GameData.TILE_AMOUNT;
			GameData.TILE_HEIGHT = GameData.FRAME_HEIGHT / GameData.TILE_AMOUNT;
			GameData.TOOLBAR_SLOT_HEIGHT = GameData.FRAME_HEIGHT/GameData.TOOLBAR_SLOTS;
			GameData.PLAYER_VELOCITY = (int) ((GameData.FRAME_WIDTH * GameData.FRAME_HEIGHT) / GameData.FRAME_SIZE_TO_VELOCITY);
			GameData.rescaleAnimations();
			explosionAnimation = new JLabel(GameData.explosionAnimation);
			ultimateExplosionAnimation = new JLabel(GameData.ultimateExplosionAnimation);
			losingAnimation = new JLabel(GameData.losingAnimation);
			winningAnimation = new JLabel(GameData.winningAnimation);
			Player.setXPos(Player.getColumn() * GameData.TILE_WIDTH);
			Player.setYPos(Player.getRow() * GameData.TILE_HEIGHT);
		}
	}
	
	public void explodeTile(final int row, final int column, final ImageIcon animationToUse) {
		//frame.setResizable(false);
		explosionInProgress = true;
		final JLabel animation;
		if(animationToUse == GameData.explosionAnimation) {
			animation = explosionAnimation;
		}else {
			animation = ultimateExplosionAnimation;
		}
		animation.setBounds(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		renderer.add(animation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				if(animation == explosionAnimation) {
					while(System.currentTimeMillis() - startTime < GameData.EXPLOSION_ANIMATION_TIME_MS) {
						tiles[row][column].setOpacity(0f); 
					}
				}else {
					while(System.currentTimeMillis() - startTime < GameData.ULTIMATE_EXPLOSION_ANIMATION_TIME_MS) {
						tiles[row][column].setOpacity(0f); 
					}
				}
				renderer.remove(animation);
				explosionInProgress = false;
				frame.setResizable(true);
			} 
		}).start();
		tiles[row][column].setDiscovered(true);
		tiles[row][column].setItem(ItemTypes.NONE);
	}
	
	public void setWinner(boolean playerWon) {
		battleInProgress = true;
		if(playerWon) {
			frame.setResizable(false);
			winningAnimation.setBounds(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
			renderer.add(winningAnimation);
			new Thread(new Runnable() {
				@Override
				public void run() {
					long startTime = System.currentTimeMillis();
					while(System.currentTimeMillis() - startTime < 15000) {
					}
					renderer.remove(winningAnimation);
					frame.setResizable(true);
					initialize();
				}
			}).start();
		}else {
			frame.setResizable(false);
			losingAnimation.setBounds(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
			renderer.add(losingAnimation);
			new Thread(new Runnable() {
				@Override
				public void run() {
					long startTime = System.currentTimeMillis();
					while(System.currentTimeMillis() - startTime < 15000) {
					}
					renderer.remove(losingAnimation);
					frame.setResizable(true);
					initialize();
				}
			}).start();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		renderer.repaint();
		update();
	}

	public JFrame getFrame() {
		return frame;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				game = new Game();
			}
		});
	}

}