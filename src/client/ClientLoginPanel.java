package client;

import java.awt.BorderLayout;
import util.Check;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

import frame.MainFrame;

public class ClientLoginPanel extends JFrame {
	private Client curClient;
	private JTextField userNameIn = new JTextField(20);
	private JTextField passWordIN = new JTextField(20);

	// Button for sending a student to the server
	private JButton clientRegister = new JButton("Register");
	private JButton clientLogin = new JButton(" login");
	// Host name or ip
	String host = "localhost";

	public ClientLoginPanel(Client curClient) {
		super("BillBook");
		this.curClient = curClient;
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(6, 2));
		infoPanel.add(new JLabel("Username"));
		infoPanel.add(userNameIn);
		infoPanel.add(new JLabel("Password"));
		infoPanel.add(passWordIN);

		mainPanel.add(infoPanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.add(clientRegister);
		controlPanel.add(clientLogin);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		this.add(mainPanel);
		clientRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String userName = userNameIn.getText().trim();
					String passWord = passWordIN.getText().trim();
					String registerResult = curClient.clientRegister(userName, passWord);
					System.out.println("line 56: " + registerResult);
					
					if(!Check.checkEmpty(passWordIN, "password"))
						return;
					
					if ("Success".equals(registerResult)) {
						JOptionPane.showMessageDialog(null, "Register successfully! Now log in!");
						new MainFrame(curClient).MainFrameStart();
						setVisible(false);
					} else {
						JOptionPane.showMessageDialog(null, "Username already exist.");
					}
				} catch (StringIndexOutOfBoundsException e1) {
					JOptionPane.showMessageDialog(null, "Username can not be empty.");
				}

			}
		});
		clientLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String userName = userNameIn.getText().trim();
					String passWord = passWordIN.getText().trim();
					String loginResult = curClient.clientLogin(userName, passWord);
					System.out.print("login.." + loginResult);
					
					if(!Check.checkEmpty(passWordIN, "password"))
						return;

					if ("Success".equals(loginResult)) {
						JOptionPane.showMessageDialog(null, "Login successfully");
						new MainFrame(curClient).MainFrameStart();
						setVisible(false);

					} else {
						JOptionPane.showMessageDialog(null, "The username or password is wrong");
					}
				} catch (StringIndexOutOfBoundsException e1) {
					JOptionPane.showMessageDialog(null, "Username can not be empty.");
				}
			}
		});

		setSize(450, 200);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client newClient = new Client();
		ClientLoginPanel sc = new ClientLoginPanel(newClient);
		sc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sc.setVisible(true);
	}

}
