package frame;

import javax.swing.*;

import client.Client;
import panel.*;

public class MainFrame extends JFrame {
	private Client curClient;
//	//public static MainFrame instance = new MainFrame();
//	public MainFrame() {
//		this.setSize(500, 450);
//		this.setTitle("Budget book");
//		MainPanel mainPanel = new MainPanel();
//		this.setContentPane(mainPanel);
//		this.setLocationRelativeTo(null);
//		this.setResizable(false);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
	
	public MainFrame(Client curClient) {
		this.curClient = curClient;
		this.setSize(500, 450);
		this.setTitle("Budget book");
		MainPanel mainPanel = new MainPanel(curClient);
		this.setContentPane(mainPanel);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	

	public void MainFrameStart() {
		// TODO Auto-generated method stub	
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Client curClient = new Client();
		MainFrame thisFrame = new MainFrame(curClient);
//		MainFrame thisFrame = new MainFrame();
		thisFrame.setVisible(true);
	}
}