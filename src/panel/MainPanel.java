package panel;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import client.Client;
import util.CenterPanel;


public class MainPanel extends JPanel {
	
	//public static MainPanel instance;
	public Client curClient;

	public JToolBar tb = new JToolBar();
	public JButton spendBtn = new JButton();
	public JButton recordBtn = new JButton();
	public JButton categoryBtn = new JButton();
	public JButton budgetBtn = new JButton();
	public JButton ListBtn = new JButton();

	public CenterPanel workingPanel;
	//public WorkingPanel workingPanel;
	private BudgetPanel budgetPanel;
	private ListPanel listPanel;
	private CategoryPanel categoryPanel;
	private RecordPanel recordPanel;
	private SpendPanel spendPanel;
	
	
	
//	public MainPanel() {
//		 //this.instance = new MainPanel();
//		spendBtn = new JButton("Spend summary");
//		recordBtn = new JButton("Add item");
//		categoryBtn = new JButton("Category");
//		budgetBtn = new JButton("Budget");
//		ListBtn = new JButton("Record");
//		
//		
//		
//		budgetPanel = new BudgetPanel();
//		listPanel = new ListPanel();
//		categoryPanel = new CategoryPanel();
//		recordPanel = new RecordPanel();
//		spendPanel = new SpendPanel();
//		
//
//
//		addListener();
//		tb.add(spendBtn);
//		tb.add(recordBtn);
//		tb.add(categoryBtn);
//		tb.add(budgetBtn);
//		tb.add(ListBtn);
//		tb.setFloatable(false);
//		
//		workingPanel = new CenterPanel(0.8);
//		//workingPanel = spendPanel;
//		setLayout(new BorderLayout());
//		add(tb, BorderLayout.NORTH);
//		add(workingPanel, BorderLayout.CENTER);
//		workingPanel.show(spendPanel);
//		
//	}

	public MainPanel(Client curClient) {
		this.curClient = curClient;
		 //this.instance = new MainPanel();
		spendBtn = new JButton("Spend summary");
		recordBtn = new JButton("Add item");
		categoryBtn = new JButton("Category");
		budgetBtn = new JButton("Budget");
		ListBtn = new JButton("Record");
		
		
		
		budgetPanel = new BudgetPanel(curClient);
		listPanel = new ListPanel(curClient);
		categoryPanel = new CategoryPanel(curClient);
		recordPanel = new RecordPanel(curClient);
		spendPanel = new SpendPanel(curClient);
		


		addListener();
		tb.add(spendBtn);
		tb.add(recordBtn);
		tb.add(categoryBtn);
		tb.add(budgetBtn);
		tb.add(ListBtn);
		tb.setFloatable(false);
		
		workingPanel = new CenterPanel(0.8);
//		workingPanel = spendPanel;
		setLayout(new BorderLayout());
		add(tb, BorderLayout.NORTH);
		add(workingPanel, BorderLayout.CENTER);
		workingPanel.show(spendPanel);
	}
	private void addListener() {
		spendBtn.addActionListener((e) -> {
			workingPanel.show(spendPanel);
		});
		recordBtn.addActionListener((e) -> {
			workingPanel.show(recordPanel);
		});
		categoryBtn.addActionListener((e) -> {
			workingPanel.show(categoryPanel);
		});
		budgetBtn.addActionListener((e) -> {
			workingPanel.show(budgetPanel);
		});
		ListBtn.addActionListener((e) ->{
			workingPanel.show(listPanel);
		});
	}

//	private void addListener() {
//		spendBtn.addActionListener((e) -> {
//			workingPanel.show(spendPanel);
//		});
//		recordBtn.addActionListener((e) -> {
//			workingPanel.show(recordPanel);
//		});
//		categoryBtn.addActionListener((e) -> {
//			workingPanel.show(categoryPanel);
//		});
//		budgetBtn.addActionListener((e) -> {
//			workingPanel.show(budgetPanel);
//		});
//		ListBtn.addActionListener((e) ->{
//			workingPanel.show(listPanel);
//		});
//	}

}