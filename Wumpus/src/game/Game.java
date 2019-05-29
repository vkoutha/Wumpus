package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
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
	private Player player;
	private Wumpus wumpus;
	private Toolbar toolbar;
	private GameState gameState;
	private JLabel explosionAnimation, losingAnimation, winningAnimation;
	private JButton startGameButton, settingsMenuButton, rulesButton;
	private AudioInputStream audioInputStream;
	private Clip themePlayer;
	private boolean explosionInProgress;

	public Game() {
		frame = new JFrame(GameData.FRAME_NAME);
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		renderer = new Renderer();
		renderer.setPreferredSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		renderer.setLayout(new BoxLayout(renderer, BoxLayout.PAGE_AXIS));
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
		startMusic();
		timer.start();
	}

	private void initialize() {
		gameState = GameState.MENU;
		addButtons();
		tiles = new Tile[GameData.TILE_AMOUNT][GameData.TILE_AMOUNT];
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				tiles[r][c] = new Tile(r, c);
			}
		}
		tiles[0][0].setDiscovered(true);
		player = new Player();
		wumpus = new Wumpus();
		toolbar = new Toolbar();
		initItems();
		explosionAnimation = new JLabel(GameData.explosionAnimation);
		losingAnimation = new JLabel(GameData.losingAnimation);
		winningAnimation = new JLabel(GameData.winningAnimation);
		GameData.FRAME_WIDTH_DIFFERENCE = frame.getWidth() - GameData.FRAME_EXTENDED_WIDTH;
		GameData.FRAME_HEIGHT_DIFFERENCE = frame.getHeight() - GameData.FRAME_HEIGHT;
	}

	private void initItems() {
		toolbar.addItem(ItemTypes.EXPLOSIVE);
		toolbar.addItem(ItemTypes.EXPLOSIVE);
		toolbar.addItem(ItemTypes.EXPLOSIVE);
		tiles[5][4].setItem(ItemTypes.FLASHLIGHT);
		tiles[3][3].setItem(ItemTypes.GOLD);
		tiles[6][5].setItem(ItemTypes.SWORD);
	}
	
	private void addButtons() {
		startGameButton = new JButton(GameData.startGameUnselectedIcon);
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

		settingsMenuButton = new JButton(GameData.settingsUnselectedIcon);
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
		renderer.add(Box.createVerticalStrut(20));
		renderer.add(settingsMenuButton);
		
		rulesButton = new JButton(GameData.rulesUnselectedIcon);
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
		renderer.add(Box.createVerticalStrut(20));
		renderer.add(rulesButton);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch(gameState) {
		case MENU:
			break;
		case SETTINGS:
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				gameState = GameState.MENU;
				renderer.revalidate();
				initialize();
				break;
			}
			break;
		case RULES:
			break;
		case IN_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				player.move(GameData.MovementDirections.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				player.move(GameData.MovementDirections.RIGHT);
				break;
			case KeyEvent.VK_UP:
				player.move(GameData.MovementDirections.UP);
				break;
			case KeyEvent.VK_DOWN:
				player.move(GameData.MovementDirections.DOWN);
				break;
			case KeyEvent.VK_SPACE:
				if(!explosionInProgress) {
					player.shoot();
				}
				break;
			case KeyEvent.VK_A:
				if(tiles[player.getRow()][player.getColumn()].getItem() != ItemTypes.NONE) {
					toolbar.addItem(tiles[player.getRow()][player.getColumn()].getItem());
					Tile.updateAffectedTiles();
					tiles[player.getRow()][player.getColumn()].removeItem();
				}
				break;
			case KeyEvent.VK_M:
				themePlayer.stop();
				break;
			case KeyEvent.VK_T:
				frame.setResizable(false);
				losingAnimation.setBounds(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
				renderer.add(losingAnimation);
				new Thread(new Runnable() {
					@Override
					public void run() {
						long startTime = System.currentTimeMillis();
						while(System.currentTimeMillis() - startTime < 20000);
						renderer.remove(losingAnimation);
						frame.setResizable(true);
					}
				}).start();
				break;
			case KeyEvent.VK_ESCAPE:
				gameState = GameState.MENU;
				break;
			}
			break;
		case PAUSED:
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}
	
	public void render(Graphics g) {
		switch(gameState) {
		case MENU:
			g.drawImage(GameData.menuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
			g.drawImage(GameData.wumpusLogo, (GameData.FRAME_EXTENDED_WIDTH/2) - (GameData.wumpusLogo.getWidth()/2), 60, GameData.wumpusLogo.getWidth(), 
					GameData.wumpusLogo.getHeight(), null);
			break;
		case SETTINGS:
			g.drawImage(GameData.settingsMenuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
			break;
		case RULES:
			break;
		case IN_GAME:
			for (Tile[] tileArr : tiles)
				for (Tile tile : tileArr)
					tile.render(g);
			player.render(g);
			toolbar.render(g);
			g.setColor(Color.WHITE);
			break;
		case PAUSED:
			break;
		}
	}

	private void startMusic() { 
		try {
			themePlayer = AudioSystem.getClip();
			audioInputStream =  AudioSystem.getAudioInputStream(getClass().getResource("/sound/theme.wav"));
			themePlayer.open(audioInputStream);
			themePlayer.start();
			themePlayer.loop(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void update() {
		updateSize();
	}

	private void updateSize() {
		if (frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE != GameData.FRAME_WIDTH
				|| frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE != GameData.FRAME_HEIGHT) {
			renderer.setPreferredSize(new Dimension(frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE,
					frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE));
			GameData.FRAME_EXTENDED_WIDTH = (int) frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE;
			GameData.FRAME_WIDTH = GameData.FRAME_EXTENDED_WIDTH - 140;
			GameData.FRAME_HEIGHT = (int) frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE;
			GameData.TILE_WIDTH = GameData.FRAME_WIDTH / GameData.TILE_AMOUNT;
			GameData.TILE_HEIGHT = GameData.FRAME_HEIGHT / GameData.TILE_AMOUNT;
			GameData.TOOLBAR_SLOT_HEIGHT = GameData.FRAME_HEIGHT/GameData.TOOLBAR_SLOTS;
			GameData.rescaleAnimations();
			explosionAnimation = new JLabel(GameData.explosionAnimation);
			losingAnimation = new JLabel(GameData.losingAnimation);
			winningAnimation = new JLabel(GameData.winningAnimation);
		}
	}
	
	public void explodeTile(int row, int column) {
		explosionInProgress = true;
		explosionAnimation.setBounds(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		renderer.add(explosionAnimation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				while(System.currentTimeMillis() - startTime < GameData.EXPLOSION_ANIMATION_TIME_MS);
				renderer.remove(explosionAnimation);
				explosionInProgress = false;
			}
		}).start();
		toolbar.removeItem(ItemTypes.EXPLOSIVE);
		tiles[row][column].setDiscovered(true);
		tiles[row][column].setItem(ItemTypes.NONE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		renderer.repaint();
		update();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Wumpus getWumpus() {
		return wumpus;
	}
	
	public Toolbar getToolbar() {
		return toolbar;
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