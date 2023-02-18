package panel;

import javax.swing.JPanel;

import client.Client;

public abstract class WorkingPanel extends JPanel {
	private Client curClient;
	protected String userName;
	private String passWord;
	
	public WorkingPanel() {
	}
	public WorkingPanel(Client curClient) {
		String[] token = curClient.getToken();
		userName = token[0];
		passWord = token[1];
	}
	public abstract void updateData();
	

	public abstract void addListener();
}