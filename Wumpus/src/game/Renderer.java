package game;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel{
	 
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Game.game.render(g);
	}

}