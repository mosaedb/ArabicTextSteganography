package com.mosaedb.arabictextsteganography;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class MainScreen {

	private JFrame frame;
	private JTextArea coverTextArea;
	private JTextArea stegoTextAreaHideTab;
	private JTextArea stegoTextAreaRetrieveTab;
	private JTextField secretTextFieldHideTab;
	private JTextField secretTextFieldRetrieveTab;
	JScrollPane scrollPaneStegoText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws UnsupportedFlavorException 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Arabic Text Steganography");
		frame.setBounds(100, 100, 410, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 2, 0, 0));

		JPanel retrievePanel = new JPanel();
		retrievePanel.setBackground(Color.LIGHT_GRAY);
		retrievePanel.setLayout(null);

		JPanel hidePanel = new JPanel();
		hidePanel.setBackground(Color.DARK_GRAY);
		hidePanel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		tabbedPane.add("Hide Secret Message", hidePanel);
		tabbedPane.add("Retrieve Secret Message", retrievePanel);

		Font font = new Font("Tahoma", Font.PLAIN, 12);
		
		coverTextArea = new JTextArea();		
		coverTextArea.setBounds(10, 36, 377, 170);
		coverTextArea.setFont(font);
		coverTextArea.setToolTipText("Write a Cover Text In Arabic");
		coverTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		coverTextArea.setLineWrap(true);
		hidePanel.add(coverTextArea);
		JScrollPane scrollPaneCoverText = new JScrollPane(coverTextArea);
		scrollPaneCoverText.setBounds(10, 36, 377, 170);
		scrollPaneCoverText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		hidePanel.add(scrollPaneCoverText);		
		coverTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();				
				if (Character.isLowerCase(c) || Character.isUpperCase(c)){
					e.consume();
					JOptionPane.showMessageDialog(frame, "Write Arabic text, please!", "Information Message", JOptionPane.INFORMATION_MESSAGE);
				} else {					
					e.getKeyChar();
				}
			}
		});

		stegoTextAreaHideTab = new JTextArea();
		stegoTextAreaHideTab.setBounds(10, 365, 377, 170);
		stegoTextAreaHideTab.setFont(font);
		stegoTextAreaHideTab.setToolTipText("Copy Your Stego Text");
		stegoTextAreaHideTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		stegoTextAreaHideTab.setLineWrap(true);
		hidePanel.add(stegoTextAreaHideTab);
		scrollPaneStegoText = new JScrollPane(stegoTextAreaHideTab);
		scrollPaneStegoText.setBounds(10, 365, 377, 170);
		scrollPaneStegoText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		hidePanel.add(scrollPaneStegoText);
		
		stegoTextAreaRetrieveTab = new JTextArea();
		stegoTextAreaRetrieveTab.setBounds(10, 36, 377, 170);
		stegoTextAreaRetrieveTab.setFont(font);
		stegoTextAreaRetrieveTab.setToolTipText("Past The Stego Text");
		stegoTextAreaRetrieveTab.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		stegoTextAreaRetrieveTab.setLineWrap(true);
		retrievePanel.add(stegoTextAreaRetrieveTab);
		scrollPaneStegoText = new JScrollPane(stegoTextAreaRetrieveTab);
		scrollPaneStegoText.setBounds(10, 36, 377, 170);
		scrollPaneStegoText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		retrievePanel.add(scrollPaneStegoText);

		JLabel lblCoverText = new JLabel("Cover Text");
		lblCoverText.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCoverText.setForeground(Color.WHITE);
		lblCoverText.setBounds(169, 11, 62, 14);
		hidePanel.add(lblCoverText);

		JLabel lblSecretTextHideTab = new JLabel("Secret Message");
		lblSecretTextHideTab.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSecretTextHideTab.setForeground(Color.WHITE);
		lblSecretTextHideTab.setBounds(155, 242, 86, 14);
		hidePanel.add(lblSecretTextHideTab);
		
		JLabel lblSecretTextRetrieveTab = new JLabel("Secret Message");
		lblSecretTextRetrieveTab.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSecretTextRetrieveTab.setForeground(Color.BLACK);
		lblSecretTextRetrieveTab.setBounds(152, 340, 86, 14);
		retrievePanel.add(lblSecretTextRetrieveTab);
		
		JLabel lblStegoTextHideTab = new JLabel("Stego Text");
		lblStegoTextHideTab.setBounds(169, 340, 63, 14);
		hidePanel.add(lblStegoTextHideTab);
		lblStegoTextHideTab.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStegoTextHideTab.setForeground(Color.WHITE);
		
		JLabel lblStegoTextRetrieveTab = new JLabel("Stego Text");
		lblStegoTextRetrieveTab.setBounds(169, 11, 63, 14);
		retrievePanel.add(lblStegoTextRetrieveTab);
		lblStegoTextRetrieveTab.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStegoTextRetrieveTab.setForeground(Color.BLACK);		


		secretTextFieldHideTab = new JTextField();
		secretTextFieldHideTab.setFont(new Font("Tahoma", Font.PLAIN, 14));
		secretTextFieldHideTab.setBounds(10, 265, 377, 20);
		secretTextFieldHideTab.setToolTipText("Write Your Secret Message");
		secretTextFieldHideTab.setColumns(10);
		secretTextFieldHideTab.setHorizontalAlignment(JTextField.CENTER);
		hidePanel.add(secretTextFieldHideTab);
		
		secretTextFieldRetrieveTab = new JTextField();
		secretTextFieldRetrieveTab.setFont(new Font("Tahoma", Font.PLAIN, 14));
		secretTextFieldRetrieveTab.setBounds(10, 365, 377, 20);
		secretTextFieldRetrieveTab.setToolTipText("Your Secret Message");
		secretTextFieldRetrieveTab.setColumns(10);
		secretTextFieldRetrieveTab.setHorizontalAlignment(JTextField.CENTER);
		retrievePanel.add(secretTextFieldRetrieveTab);

		JButton btnGetStegoText = new JButton("Get Stego Text");
		btnGetStegoText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getStegoTextAreaHideTab().setText(getStegoText());
			}
		});
		btnGetStegoText.setBounds(139, 301, 120, 23);
		hidePanel.add(btnGetStegoText);
				
		JButton btnGetSecretText = new JButton("Get Secret Message");
		btnGetSecretText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getSecretTextFieldRetrieveTab().setText(getSecretMessage());
			}
		});
		btnGetSecretText.setBounds(120, 265, 150, 23);
		retrievePanel.add(btnGetSecretText);
				
	}

	public String getStegoText() {
		String stegoText = null;
		try {
			stegoText = Steganography.hideSecretMessage(coverTextArea.getText(), secretTextFieldHideTab.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "I need more cover text o_O", "Information Message", JOptionPane.INFORMATION_MESSAGE);
		}
		return stegoText;
	}

	public String getSecretMessage() {
		return Steganography.retrieveSecretMessage(stegoTextAreaRetrieveTab.getText());
	}

	public JTextArea getStegoTextAreaHideTab() {
		return stegoTextAreaHideTab;
	}

	public JTextArea getCoverTextArea() {
		return coverTextArea;
	}

	public JTextField getSecretTextFieldRetrieveTab() {
		return secretTextFieldRetrieveTab;
	}
	public JTextArea getStegoTextAreaRetrieveTab() {
		return stegoTextAreaRetrieveTab;
	}
}
