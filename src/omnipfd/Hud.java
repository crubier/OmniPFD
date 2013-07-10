package omnipfd;

import javax.swing.JFrame;

public class Hud {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame mainFrame = new JFrame("Test");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.getContentPane().add(new QuaternionPFDPanel());
		mainFrame.pack();
		mainFrame.setVisible(true);
		

	}

}
