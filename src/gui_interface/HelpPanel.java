package gui_interface;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;
/**
 * HelpPanel  displays an image that provides 
 * instructions on how to use the LAN Simulator.
 * 
 * @author Nasra.
 */
public class HelpPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList <JPanel> helpCards = new ArrayList<>();

	/**
	 * Create the panel.
	 */
	public HelpPanel() {
		setBackground(new Color(255, 255, 224));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 0));
		add(panel, BorderLayout.SOUTH);
		
		JPanel cardPanel = new JPanel();
		add(cardPanel, BorderLayout.CENTER);
		cardPanel.setLayout(new CardLayout(0, 0));

		JPanel card1 = new JPanel();
		card1.setBackground(new Color(0, 0, 0));
		card1.setLayout(new BorderLayout(0,0));
		JLabel imageLabel1 = new JLabel();
		imageLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel1.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage1.png")));
		JTextArea textArea1 = new JTextArea();
		textArea1.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea1.setForeground(Color.BLACK);
		textArea1.setEditable(false);		
		textArea1.setText("LAN Simulator is a software built to allow a user create network models\r\nand simulate various network protocols. It reports back to the user by\r\ngenerating statistics that the user can use to compare the efficiency \r\nof the network.");
		card1.add(imageLabel1, BorderLayout.CENTER);
		card1.add(textArea1, BorderLayout.SOUTH);
		helpCards.add(card1);
		String card1String = "" + helpCards.indexOf(card1);
		cardPanel.add(card1, card1String);
		
		JPanel card2 = new JPanel();
		card2.setBackground(new Color(0, 0, 0));
		card2.setLayout(new BorderLayout(0,0));
		JLabel imageLabel2 = new JLabel();
		imageLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel2.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage2.png")));
		JTextArea textArea2 = new JTextArea();
		textArea2.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea2.setForeground(Color.BLACK);
		textArea2.setEditable(false);
		textArea2.setText("To start, a user selects a device group in the area circled red\r\nand proceeds to click on the icon representing the device of \r\nchoice. On clicking, a device will be added to the white area \r\nof the screen.");
		card2.add(imageLabel2, BorderLayout.CENTER);
		card2.add(textArea2, BorderLayout.SOUTH);
		helpCards.add(card2);
		String card2String = "" + helpCards.indexOf(card2);
		cardPanel.add(card2, card2String);
		
		JPanel card3 = new JPanel();
		card3.setBackground(new Color(0, 0, 0));
		card3.setLayout(new BorderLayout(0,0));
		JLabel imageLabel3 = new JLabel();
		imageLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel3.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage3.png")));
		JTextArea textArea3 = new JTextArea();
		textArea3.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea3.setForeground(Color.BLACK);
		textArea3.setEditable(false);
		textArea3.setText("All devices will be added  to the top left corner of the white\r\narea from which they can be dragged and connected to each \r\nother. When two devices are connected, the status label at the\r\nbottom left of the screen is updated.");
		card3.add(imageLabel3, BorderLayout.CENTER);
		card3.add(textArea3, BorderLayout.SOUTH);
		helpCards.add(card3);
		String card3String = "" + helpCards.indexOf(card3);
		cardPanel.add(card3, card3String);
		
		JPanel card4 = new JPanel();
		card4.setBackground(new Color(0, 0, 0));
		card4.setLayout(new BorderLayout(0,0));
		JLabel imageLabel4 = new JLabel();
		imageLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel4.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage4.png")));
		JTextArea textArea4 = new JTextArea();
		textArea4.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea4.setForeground(Color.BLACK);
		textArea4.setEditable(false);
		textArea4.setText("This is the statistics displaing art of the software. When a \r\nsimulation has been run, the Current values column is \r\nupdated. to save the values to the saved values colunmn, \r\nyou will click on the \"save values button\".");
		card4.add(imageLabel4, BorderLayout.CENTER);
		card4.add(textArea4, BorderLayout.SOUTH);
		helpCards.add(card4);
		String card4String = "" + helpCards.indexOf(card4);
		cardPanel.add(card4, card4String);
		
		JPanel card5 = new JPanel();
		card5.setBackground(new Color(0, 0, 0));
		card5.setLayout(new BorderLayout(0,0));
		JLabel imageLabel5 = new JLabel();
		imageLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel5.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage5.png")));
		JTextArea textArea5 = new JTextArea();
		textArea5.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea5.setForeground(Color.BLACK);
		textArea5.setEditable(false);
		textArea5.setText("The areas circled in red contain the different protocols that\r\ncan be simulated. The default protocols are enabled by\r\ndefault.");
		card5.add(imageLabel5, BorderLayout.CENTER);
		card5.add(textArea5, BorderLayout.SOUTH);
		helpCards.add(card5);
		String card5String = "" + helpCards.indexOf(card5);
		cardPanel.add(card5, card5String);
		
		JPanel card6 = new JPanel();
		card6.setBackground(new Color(0, 0, 0));
		card6.setLayout(new BorderLayout(0,0));
		JLabel imageLabel6 = new JLabel();
		imageLabel6.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel6.setIcon(new ImageIcon(HelpPanel.class.getResource("/Images/HelpImage6.png")));
		JTextArea textArea6 = new JTextArea();
		textArea6.setFont(new Font("Monospaced", Font.PLAIN, 15));
		textArea6.setForeground(Color.BLACK);
		textArea6.setEditable(false);
		textArea6.setText("The leftmost circle shows the simulation status label.\r\n\r\nThe center circle shows the simulation start and stop\r\nbuttons.\r\n\r\nThe rightmost one shows the edit buttons, new (to clear the \r\nmodel board), undo and redo.\r\n");
		card6.add(imageLabel6, BorderLayout.CENTER);
		card6.add(textArea6, BorderLayout.SOUTH);
		helpCards.add(card6);
		String card6String = "" + helpCards.indexOf(card6);
		cardPanel.add(card6, card6String);
		
		JButton btnPrevious = new JButton("Previous");
		JButton btnNext = new JButton("Next");
		
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(Component c : cardPanel.getComponents()){
					if(c.isShowing()){
						JPanel p = (JPanel) c;
						if(helpCards.indexOf(p) != 0){
							String s = "" + (helpCards.indexOf(p) - 1);
							CardLayout cl = (CardLayout) cardPanel.getLayout();
							cl.show(cardPanel, s);
							btnNext.setEnabled(true);
							if(helpCards.indexOf(p) == 1){
								btnPrevious.setEnabled(false);								
							}
							return;
						}
							
												
					}
				}
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(btnPrevious);
		
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Component c : cardPanel.getComponents()){
					if(c.isShowing()){
						JPanel p = (JPanel) c;
						if(helpCards.indexOf(p) < helpCards.size()){
							String s = "" + (helpCards.indexOf(p) + 1);
							CardLayout cl = (CardLayout) cardPanel.getLayout();
							cl.show(cardPanel, s);
							btnPrevious.setEnabled(true);
							if(helpCards.indexOf(p) == (helpCards.size() - 2)){
								btnNext.setEnabled(false);
							}
							return;
						}
					}
				}
			}
		});
		panel.add(btnNext);		
	}

}
