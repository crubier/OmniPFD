package hud;

import javax.swing.JFrame;

public class Hud {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame mainFrame = new JFrame("Test");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.getContentPane().add(new HeavyHudPanel());
		mainFrame.pack();
		mainFrame.setVisible(true);
		

	}

}
