package gui_interface;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.GridLayout;
import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.BevelBorder;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import simulation.SimulationEngine;
import simulation.Statistics;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
/**
 * A LAN Simulator 
 * This is a program to simulate local area networks 
 * It allows users to drag and connect different network devices,
 * select the datalink protocols to be simulated, and generate 
 * statistics on the simulations run.
 * 
 * @author Olawale
 * @co_author Nasra 
 */
public class LAN_Simulator  {
	//Swing Component fields.
	private JFrame frmLanSimulator;
	
	//the panel where modelling happens.
	private JPanel modelPanel  = new JPanel();
	
	//the devices tabbed pane.
	private JTabbedPane devicesPane = new JTabbedPane(JTabbedPane.TOP);
	
	//Labels to display statistic values.
	private JLabel lblReceivedVal = new JLabel("value");
	private JLabel lblFramesRetransmittedVal = new JLabel("value");
	private JLabel lblCollisionVal = new JLabel("value");		
	private JLabel lblTotalFramesValue = new JLabel("value");
	private JLabel lblFramesDroppedValue = new JLabel("value");
	private JLabel lblSimulationTimeValue = new JLabel("value");
	
	//panels that contain graph.
	private JPanel cards = new JPanel(new CardLayout());
	
	//panel to contain graph cards.
	private JPanel graphPanel = new JPanel();
	
	//label to display the simulation status.
	JLabel lblSimulationStatus = new JLabel("Simulation Status....");
		
	//The Simulation Engine Object.
	private SimulationEngine simulationEngine = new SimulationEngine();
	
	//An arrayList that will contain all labels placed on the modelPanel.
	private ArrayList<JLabel> labels = new ArrayList<>();
	
	//MouseListeners to handle user mouse inputs.
	private MouseListener listener = new ClickMouseAdapter(this, labels, modelPanel, frmLanSimulator);
	//An integer value to hold the index of the protocol to be run. 
	private int activeProtocolIndex;
	
	//long values used to calculate simulation run time.
	private long startTime = 0;
	private long endTime  = 0;
	
	//undo Button to reverse last action
	JButton btnUndo = new JButton("Undo");
	//redo button to repeat undone action.
	JButton btnRedo = new JButton("Redo");
	
	//integer value to help in comparing stat
	private int simCount = 0;
	
	//ArrayList to contain graphs.
	ArrayList<Integer> recieved = new ArrayList<>();
	ArrayList<Integer> collisions = new ArrayList<>();
	
	ArrayList<String> protocolsSimulated = new ArrayList<>();
	
	private UndoManager undoManager;

	/**
	 * Launch the application.
	 * 
	 * Author Olawale.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LAN_Simulator window = new LAN_Simulator();
					window.frmLanSimulator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * Author Olawale.
	 */
	public LAN_Simulator() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 * 
	 * Author Olawale.
	 */
	private void initialize() {
		frmLanSimulator = new JFrame();
		
		frmLanSimulator.getContentPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				//enableConfigPanel(modelPanel);
			}
		});
		frmLanSimulator.setTitle("LAN Simulator");
		frmLanSimulator.getContentPane().setBackground(new Color(255, 215, 0));
		frmLanSimulator.setBounds(100, 100, 1024, 768);
		frmLanSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		createDevicePane();
		createStatPane();
		createConsolePanel();
		createModPanel();
	}

	/**
	 * Handles the creation of the MenuBar
	 * 
	 * Author Olawale.
	 */
	private void createMenu(){
		JMenuBar menuBar = new JMenuBar();
		frmLanSimulator.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelPanel.removeAll();
				modelPanel.revalidate();
				modelPanel.repaint();
			}
		});
		mnFile.add(mntmNew);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoManager.undo();
				updateButtons(undoManager);
			}
		});	
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				undoManager.redo();
				updateButtons(undoManager);
			}
		});
		mnEdit.add(mntmRedo);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDialog jd = new JDialog();
				jd.setTitle("Help");
				HelpPanel helpPanel = new HelpPanel();
				helpPanel.setVisible(true);
				jd.getContentPane().add(helpPanel);
				jd.pack();
				jd.setVisible(true);
				
			}
		});
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frmLanSimulator, "A LAN SIMULATOR\n"
						+ "Designed by\n"
						+ "Olawale O. Komolafe\n"
						+ "Nasra Al-Barwani \n"
						+ "Clement D. Bomedem \n"
						+ "Patrick N. Odai \n"
						+ "Copyright 2016", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAbout);
	}
	
	/**
	 * Handles the creation of the ModPanel
	 * 
	 *  the ModPanel is the part of the interface that 
	 *  allows users to drag and connect different devices
	 *  
	 *  Author Olawale.
	 */
	private void createModPanel(){
		
		modelPanel.setBackground(new Color(248, 248, 255));
		frmLanSimulator.getContentPane().add(modelPanel, BorderLayout.CENTER);
		modelPanel.setLayout(new DragLayout());
	}
	
	/**
	 * Handles the creation of the ConsolePanel
	 * 
	 *  the ConsolePanel is the part of the interface that 
	 *  contains the control options for what type of simulation 
	 *  is to be run.
	 *  
	 *  Author Olawale.
	 */
	private void createConsolePanel(){
		JPanel conPanel = new JPanel();
		conPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		conPanel.setBackground(new Color(255, 215, 0));
		frmLanSimulator.getContentPane().add(conPanel, BorderLayout.SOUTH);
		conPanel.setLayout(new BorderLayout(0, 0));
		
		//-----------------------------------------------------------------------------------//
		JPanel protocolPanel = new JPanel();
		conPanel.add(protocolPanel, BorderLayout.NORTH);
		protocolPanel.setLayout(new GridLayout(1,3,0,0));
		
		//the medium Access protocols//
		JPanel mediumAccess = new JPanel();
		mediumAccess.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, new Color(218, 165, 32), null, null, null),
				"Medium Access Protocols", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mediumAccess.setBackground(new Color(255, 215, 0));
		protocolPanel.add(mediumAccess);
		mediumAccess.setLayout(new GridLayout(4,2,2,2));
				
		JRadioButton rdbtnPureAloha = new JRadioButton("Pure Aloha");
		rdbtnPureAloha.setSelected(true);
		rdbtnPureAloha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeProtocolIndex = 0;
			}
		});
		rdbtnPureAloha.setBackground(new Color(255, 215, 0));
		mediumAccess.add(rdbtnPureAloha);
		
		JRadioButton rdbtnSlottedAloha = new JRadioButton("Slotted Aloha");
		rdbtnSlottedAloha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeProtocolIndex = 1;
			}
		});
		rdbtnSlottedAloha.setBackground(new Color(255, 215, 0));
		mediumAccess.add(rdbtnSlottedAloha);
		
		JRadioButton rdbtnCsma = new JRadioButton("CSMA");
		rdbtnCsma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeProtocolIndex = 2;
			}
		});
		rdbtnCsma.setBackground(new Color(255, 215, 0));
		mediumAccess.add(rdbtnCsma);
		
		JRadioButton rdbtnCsma_cd = new JRadioButton("CSMA/CD");
		rdbtnCsma_cd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeProtocolIndex = 3;
			}
		});
		rdbtnCsma_cd.setBackground(new Color(255, 215, 0));
		mediumAccess.add(rdbtnCsma_cd);
		
		ButtonGroup mediumAccessGroup = new ButtonGroup();
		mediumAccessGroup.add(rdbtnPureAloha);
		mediumAccessGroup.add(rdbtnSlottedAloha);
		mediumAccessGroup.add(rdbtnCsma);
		mediumAccessGroup.add(rdbtnCsma_cd);
		
		//The error detection protocols.
		JPanel errorDetection = new JPanel();
		errorDetection.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, new Color(218, 165, 32),
				null, null, null), "Error Detection", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		errorDetection.setBackground(new Color(255, 215, 0));
		protocolPanel.add(errorDetection);
		errorDetection.setLayout(new GridLayout(4,1,2,2));
		
		JRadioButton rdbtnMC = new JRadioButton("Modular Checksum");
		rdbtnMC.setEnabled(false);
		rdbtnMC.setBackground(new Color(255, 215, 0));
		errorDetection.add(rdbtnMC);
		
		JRadioButton rdbtnCRC = new JRadioButton("Cyclic Redundancy Check");
		rdbtnCRC.setEnabled(false);
		rdbtnCRC.setBackground(new Color(255, 215, 0));
		errorDetection.add(rdbtnCRC);
		
		ButtonGroup errorDetectionGroup = new ButtonGroup();
		errorDetectionGroup.add(rdbtnMC);
		errorDetectionGroup.add(rdbtnCRC);
		
		//The error handling protocols.//
		JPanel errorHandling = new JPanel();
		errorHandling.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, new Color(218, 165, 32), null, null, null),
				"Error Handling", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		errorHandling.setBackground(new Color(255, 215, 0));
		protocolPanel.add(errorHandling);
		errorHandling.setLayout(new GridLayout(4,1,2,2));
		
		JRadioButton rdbtnGA = new JRadioButton("Go-Back-N ARQ");
		rdbtnGA.setEnabled(false);
		rdbtnGA.setBackground(new Color(255, 215, 0));
		errorHandling.add(rdbtnGA);
		
		JRadioButton rdbtnSA = new JRadioButton("Selective ARQ");
		rdbtnSA.setEnabled(false);
		rdbtnSA.setBackground(new Color(255, 215, 0));
		errorHandling.add(rdbtnSA);
		
		ButtonGroup errorHandlingGroup = new ButtonGroup();
		errorHandlingGroup.add(rdbtnGA);
		errorHandlingGroup.add(rdbtnSA);
		
		//------------------------------------------------------------------------------------------------//
		JPanel executionPanel = new JPanel();
		executionPanel.setBackground(new Color(240, 230, 140));
		conPanel.add(executionPanel, BorderLayout.SOUTH);
		executionPanel.setLayout(new GridLayout(1, 3, 0, 0));
		
		JPanel simulationStatusPanel = new JPanel();
		simulationStatusPanel.setBackground(new Color(240, 230, 140));
		executionPanel.add(simulationStatusPanel);
		simulationStatusPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		//declared as a field
		simulationStatusPanel.add(lblSimulationStatus);
		
		JPanel simulationControlPanel = new JPanel();
		simulationControlPanel.setBackground(new Color(240, 230, 140));
		executionPanel.add(simulationControlPanel);
		
		//the start button
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(false);
				// get the number on components on the modelPanel
				int count = modelPanel.getComponentCount();
				
				if(count == 0){
					//Do nothing 
					return;
				}
				
				//increase the number of simulation.
				simCount++;			
				
				if(simCount == 1){
					//clear previous graph
					graphPanel.removeAll();
					JLabel lblInsufficientDataTo = new JLabel("Insufficient data to plot  graphs.");
					lblInsufficientDataTo.setHorizontalAlignment(SwingConstants.CENTER);
					graphPanel.add(lblInsufficientDataTo, BorderLayout.NORTH);
					graphPanel.revalidate();
					graphPanel.repaint();
				}
				
				//update the simulation status label to inform the user the simulation is starting.
				lblSimulationStatus.setText("Starting Simulation");
				
				//Delete the nodes currently in the simulationEngine;
				simulationEngine.empty();
				
				//Get all the nodes in the model and pass them to the simulation engine. cableLabels are 
				//not included since they only reference the nodes contained in the other devices.
				for(int i = 0; i < count; i++){
					Component c = modelPanel.getComponent(i);
					
					if(c instanceof ComputerLabel){
						ComputerLabel cl = (ComputerLabel)c;						
						simulationEngine.addNodes(cl.getComputer().getNodes());
					}					
					if(c instanceof HubLabel){
						HubLabel hl = (HubLabel)c;
						simulationEngine.addNodes(hl.getHub().getNodes());
					}							
					if(c instanceof SwitchLabel){
						SwitchLabel sl = (SwitchLabel)c;
						simulationEngine.addNodes(sl.getSwitch().getNodes());
					}
				}		
			
				simulationEngine.run(activeProtocolIndex);
				startTime = System.currentTimeMillis();
						
					//A thread to update the simulation status label of the current state of the simulation.
					new Thread(new Runnable(){
						public void run(){
							while(simulationEngine.threadStatus()){
								lblSimulationStatus.setText("Running Simulation");
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {								
									e.printStackTrace();
								}
								lblSimulationStatus.setText("Running Simulation.");
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {								
									e.printStackTrace();
								}
								lblSimulationStatus.setText("Running Simulation..");
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {								
									e.printStackTrace();
								}
								lblSimulationStatus.setText("Running Simulation...");
								
								if(!simulationEngine.threadStatus()){
									lblSimulationStatus.setText("Simulation Ended");
									endTime = System.currentTimeMillis();							
									try {
										Thread.sleep(300);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									showStatistics();
									showLogReader();
									btnStart.setEnabled(true);
								}
							}
						}												
					}).start();
				
			}
			
		});
		simulationControlPanel.add(btnStart);
		
		//the stop button
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(modelPanel.getComponentCount() == 0){
					//do nothing
					return;
				}
				simulationEngine.stop();
				endTime = System.currentTimeMillis();
				showStatistics();
				showLogReader();
				btnStart.setEnabled(true);
			}
		});
		simulationControlPanel.add(btnStop);
		
		//Edit Menu Panel
		JPanel editMenuPanel = new JPanel();
		editMenuPanel.setBackground(new Color(240, 230, 140));
		executionPanel.add(editMenuPanel);
		
		//new button to clear modelPanel
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelPanel.removeAll();
				modelPanel.revalidate();
				modelPanel.repaint();
				undoManager.discardAllEdits();
				updateButtons(undoManager);
			}
		});
		editMenuPanel.add(btnNew);
		
		//undo button
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				undoManager.undo();
				updateButtons(undoManager);
				}
				catch(CannotUndoException cue){
					JOptionPane.showMessageDialog(frmLanSimulator, "No further Undo action exist.");
				}
			}
		});
		editMenuPanel.add(btnUndo);
		
		//redo button
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				undoManager.redo();
				updateButtons(undoManager);
				}
				catch(CannotRedoException cre){
					JOptionPane.showMessageDialog(frmLanSimulator, "No further Undo action exist.");
				}
			}
		});

		btnRedo.setEnabled(false);
		editMenuPanel.add(btnRedo);
	}

	/**
	 * Handles the creation of the DevicePane
	 * 
	 *  the DevicePane is the part of the interface that 
	 *  contains labels representing network devices.
	 *  
	 *  Author Olawale.
	 */
	private void createDevicePane(){
		frmLanSimulator.getContentPane().add(devicesPane, BorderLayout.NORTH);
		
		JPanel switches = new JPanel();
		switches.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)));
		switches.setBackground(new Color(240, 230, 140));
		devicesPane.addTab("Switches", null, switches, null);
		switches.setLayout(new BoxLayout(switches, BoxLayout.X_AXIS));
		
		SwitchLabel lblSwitch = new SwitchLabel("Switch");
		lblSwitch.setHorizontalAlignment(SwingConstants.CENTER);
		switches.add(lblSwitch);
		lblSwitch.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/Switch.png")));
		lblSwitch.setVerticalTextPosition(JLabel.BOTTOM);
		lblSwitch.setHorizontalTextPosition(JLabel.CENTER);
		labels.add(lblSwitch);
		lblSwitch.addMouseListener(listener);
		
		JPanel hubs = new JPanel();
		hubs.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)));
		hubs.setBackground(new Color(240, 230, 140));
		devicesPane.addTab("Hubs", null,hubs , null);
		hubs.setLayout(new BoxLayout(hubs, BoxLayout.X_AXIS));
		
		HubLabel lblHub = new HubLabel("Hub");
		lblHub.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/hub.png")));
		lblHub.setHorizontalAlignment(SwingConstants.CENTER);
		lblHub.setHorizontalTextPosition(JLabel.CENTER);
		lblHub.setVerticalTextPosition(JLabel.BOTTOM);
		labels.add(lblHub);
		lblHub.addMouseListener(listener);
		hubs.add(lblHub);
		
		JPanel computers = new JPanel();
		computers.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)));
		computers.setBackground(new Color(240, 230, 140));
				
		devicesPane.addTab("Computers", null, computers, null);
		computers.setLayout(new BoxLayout(computers, BoxLayout.X_AXIS));
		
		ComputerLabel lblDesktop = new ComputerLabel("Desktop");
		lblDesktop.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/desktop.png")));
		lblDesktop.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesktop.setHorizontalTextPosition(JLabel.CENTER);
		lblDesktop.setVerticalTextPosition(JLabel.BOTTOM);
		labels.add(lblDesktop);
		lblDesktop.addMouseListener(listener);
		computers.add(lblDesktop);
		
		ComputerLabel lblLaptop = new ComputerLabel("Laptop");
		lblLaptop.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/laptop.png")));
		lblLaptop.setHorizontalAlignment(SwingConstants.CENTER);
		lblLaptop.setHorizontalTextPosition(JLabel.CENTER);
		lblLaptop.setVerticalTextPosition(JLabel.BOTTOM);
		labels.add(lblLaptop);
		lblLaptop.addMouseListener(listener);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		computers.add(horizontalStrut);
		computers.add(lblLaptop);
		
		JPanel cables = new JPanel();
		cables.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0), new Color(0, 0, 0)));
		cables.setBackground(new Color(240, 230, 140));
		devicesPane.addTab("Cables", null, cables, null);
		cables.setLayout(new BoxLayout(cables, BoxLayout.X_AXIS));
		
		CableLabel lblHCable = new CableLabel("Horizontal cable");
		lblHCable.setText("Horizontal Cable");
		lblHCable.setHorizontalTextPosition(SwingConstants.CENTER);
		lblHCable.setVerticalTextPosition(JLabel.BOTTOM);
		lblHCable.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable.png")));
		lblHCable.addMouseListener(listener);
		lblHCable.setPlane("horizontal");
		labels.add(lblHCable);
		cables.add(lblHCable);
		
		CableLabel lblVCable = new CableLabel("Vertical cable");
		lblVCable.setText("Vertical Cable");
		lblVCable.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblVCable.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable.png")));
		lblVCable.addMouseListener(listener);
		lblVCable.setPlane("vertical");
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		cables.add(rigidArea);
		labels.add(lblVCable);
		cables.add(lblVCable);
		
	}
	
	/**
	 * Handles the creation of the StatPane
	 * 
	 *  the StatPane is the part of the interface that 
	 *  displays the results of the simulation.
	 *  
	 *  Author: Nasra.
	 */
	private void createStatPane(){

		JTabbedPane statPane = new JTabbedPane(JTabbedPane.TOP);
		statPane.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frmLanSimulator.getContentPane().add(statPane, BorderLayout.EAST);
		
		JPanel summaryPanel = new JPanel();
		summaryPanel.setBackground(new Color(240, 230, 140));
		statPane.addTab("Summary", null, summaryPanel, null);
		summaryPanel.setLayout(new BorderLayout(0, 0));
				
		JPanel labelsPanel = new JPanel();
		labelsPanel.setBackground(new Color(250, 250, 210));
		summaryPanel.add(labelsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_labelsPanel = new GridBagLayout();
		gbl_labelsPanel.columnWidths = new int[]{130, 110, 100, 0};
		gbl_labelsPanel.rowHeights = new int[]{50, 35, 35, 35, 35, 35,35 ,0};
		gbl_labelsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_labelsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 ,Double.MIN_VALUE};
		labelsPanel.setLayout(gbl_labelsPanel);
		
		JLabel lblStatistics = new JLabel("STATISTICS");
		lblStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatistics.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblStatistics = new GridBagConstraints();
		gbc_lblStatistics.fill = GridBagConstraints.BOTH;
		gbc_lblStatistics.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatistics.gridx = 0;
		gbc_lblStatistics.gridy = 0;
		labelsPanel.add(lblStatistics, gbc_lblStatistics);
		
		JLabel lblCurrentValues = new JLabel("CURRENT VALUES");
		lblCurrentValues.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentValues.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblCurrentValues = new GridBagConstraints();
		gbc_lblCurrentValues.fill = GridBagConstraints.BOTH;
		gbc_lblCurrentValues.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentValues.gridx = 1;
		gbc_lblCurrentValues.gridy = 0;
		labelsPanel.add(lblCurrentValues, gbc_lblCurrentValues);
		
		JLabel lblSavedValues = new JLabel("SAVED VALUES");
		lblSavedValues.setHorizontalAlignment(SwingConstants.CENTER);
		lblSavedValues.setFont(new Font("Tahoma", Font.BOLD, 15));
		GridBagConstraints gbc_lblSavedValues = new GridBagConstraints();
		gbc_lblSavedValues.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValues.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavedValues.gridx = 2;
		gbc_lblSavedValues.gridy = 0;
		labelsPanel.add(lblSavedValues, gbc_lblSavedValues);
		
		JLabel lblTotalFramesSent = new JLabel("Total Frames Sent:");
		GridBagConstraints gbc_lblTotalFramesSent = new GridBagConstraints();
		gbc_lblTotalFramesSent.fill = GridBagConstraints.BOTH;
		gbc_lblTotalFramesSent.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalFramesSent.gridx = 0;
		gbc_lblTotalFramesSent.gridy = 1;
		labelsPanel.add(lblTotalFramesSent, gbc_lblTotalFramesSent);
		
		//declared as a field
		GridBagConstraints gbc_lblTotalFramesValue = new GridBagConstraints();
		gbc_lblTotalFramesValue.fill = GridBagConstraints.BOTH;
		gbc_lblTotalFramesValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalFramesValue.gridx = 1;
		gbc_lblTotalFramesValue.gridy = 1;
		labelsPanel.add(lblTotalFramesValue, gbc_lblTotalFramesValue);
		
		JLabel lblSavedValue = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue = new GridBagConstraints();
		gbc_lblSavedValue.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavedValue.gridx = 2;
		gbc_lblSavedValue.gridy = 1;
		labelsPanel.add(lblSavedValue, gbc_lblSavedValue);
		
		JLabel lblCollision = new JLabel("Total Frame Collision:");
		GridBagConstraints gbc_lblCollision = new GridBagConstraints();
		gbc_lblCollision.fill = GridBagConstraints.BOTH;
		gbc_lblCollision.insets = new Insets(0, 0, 5, 5);
		gbc_lblCollision.gridx = 0;
		gbc_lblCollision.gridy = 2;
		labelsPanel.add(lblCollision, gbc_lblCollision);
		
		//declared as a field
		GridBagConstraints gbc_lblCollisionVal = new GridBagConstraints();
		gbc_lblCollisionVal.fill = GridBagConstraints.BOTH;
		gbc_lblCollisionVal.insets = new Insets(0, 0, 5, 5);
		gbc_lblCollisionVal.gridx = 1;
		gbc_lblCollisionVal.gridy = 2;
		labelsPanel.add(lblCollisionVal, gbc_lblCollisionVal);
		
		JLabel lblSavedValue_1 = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue_1 = new GridBagConstraints();
		gbc_lblSavedValue_1.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavedValue_1.gridx = 2;
		gbc_lblSavedValue_1.gridy = 2;
		labelsPanel.add(lblSavedValue_1, gbc_lblSavedValue_1);
		
		JLabel lblReceived = new JLabel("Total Frames Recieved:");
		GridBagConstraints gbc_lblReceived = new GridBagConstraints();
		gbc_lblReceived.fill = GridBagConstraints.BOTH;
		gbc_lblReceived.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceived.gridx = 0;
		gbc_lblReceived.gridy = 3;
		labelsPanel.add(lblReceived, gbc_lblReceived);
		
		//declared as a field.
		GridBagConstraints gbc_lblReceivedVal = new GridBagConstraints();
		gbc_lblReceivedVal.fill = GridBagConstraints.BOTH;
		gbc_lblReceivedVal.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceivedVal.gridx = 1;
		gbc_lblReceivedVal.gridy = 3;
		labelsPanel.add(lblReceivedVal, gbc_lblReceivedVal);
		
		JLabel lblSavedValue_2 = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue_2 = new GridBagConstraints();
		gbc_lblSavedValue_2.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavedValue_2.gridx = 2;
		gbc_lblSavedValue_2.gridy = 3;
		labelsPanel.add(lblSavedValue_2, gbc_lblSavedValue_2);
		
		JLabel lblPacketRetransmitted = new JLabel("Frames Retransmitted:");
		GridBagConstraints gbc_lblPacketRetransmitted = new GridBagConstraints();
		gbc_lblPacketRetransmitted.fill = GridBagConstraints.BOTH;
		gbc_lblPacketRetransmitted.insets = new Insets(0, 0, 5, 5);
		gbc_lblPacketRetransmitted.gridx = 0;
		gbc_lblPacketRetransmitted.gridy = 4;
		labelsPanel.add(lblPacketRetransmitted, gbc_lblPacketRetransmitted);
		
		//declared as a field.
		GridBagConstraints gbc_lblFramesRetransmittedVal = new GridBagConstraints();
		gbc_lblFramesRetransmittedVal.fill = GridBagConstraints.BOTH;
		gbc_lblFramesRetransmittedVal.insets = new Insets(0, 0, 5, 5);
		gbc_lblFramesRetransmittedVal.gridx = 1;
		gbc_lblFramesRetransmittedVal.gridy = 4;
		labelsPanel.add(lblFramesRetransmittedVal, gbc_lblFramesRetransmittedVal);
		
		JLabel lblSavedValue_3 = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue_3 = new GridBagConstraints();
		gbc_lblSavedValue_3.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblSavedValue_3.gridx = 2;
		gbc_lblSavedValue_3.gridy = 4;
		labelsPanel.add(lblSavedValue_3, gbc_lblSavedValue_3);
		
		JLabel lblFramesDropped = new JLabel("Frames Dropped:");
		GridBagConstraints gbc_lblFramesDropped = new GridBagConstraints();
		gbc_lblFramesDropped.fill = GridBagConstraints.BOTH;
		gbc_lblFramesDropped.insets = new Insets(0, 0, 0, 5);
		gbc_lblFramesDropped.gridx = 0;
		gbc_lblFramesDropped.gridy = 5;
		labelsPanel.add(lblFramesDropped, gbc_lblFramesDropped);
		
		//declared as field
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.fill = GridBagConstraints.BOTH;
		gbc_lblValue.insets = new Insets(0, 0, 0, 5);
		gbc_lblValue.gridx = 1;
		gbc_lblValue.gridy = 5;
		labelsPanel.add(lblFramesDroppedValue, gbc_lblValue);
		
		JLabel lblSavedValue_4 = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue_4 = new GridBagConstraints();
		gbc_lblSavedValue_4.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue_4.gridx = 2;
		gbc_lblSavedValue_4.gridy = 5;
		labelsPanel.add(lblSavedValue_4, gbc_lblSavedValue_4);
		
		JLabel lblSimulationTime = new JLabel("Simulation Time:");
		GridBagConstraints gbc_lblSimulationTime = new GridBagConstraints();
		gbc_lblSimulationTime.fill = GridBagConstraints.BOTH;
		gbc_lblSimulationTime.gridx = 0;
		gbc_lblSimulationTime.gridy = 6;
		labelsPanel.add(lblSimulationTime, gbc_lblSimulationTime);
		
		//declared as a field.
		GridBagConstraints gbc_lblSimulationTimeValue = new GridBagConstraints();
		gbc_lblSimulationTimeValue.fill = GridBagConstraints.BOTH;
		gbc_lblSimulationTimeValue.gridx = 1;
		gbc_lblSimulationTimeValue.gridy = 6;
		labelsPanel.add(lblSimulationTimeValue, gbc_lblSimulationTimeValue);
		
		JLabel lblSavedValue_5 = new JLabel("saved value");
		GridBagConstraints gbc_lblSavedValue_5 = new GridBagConstraints();
		gbc_lblSavedValue_5.fill = GridBagConstraints.BOTH;
		gbc_lblSavedValue_5.gridx = 2;
		gbc_lblSavedValue_5.gridy = 6;
		labelsPanel.add(lblSavedValue_5, gbc_lblSavedValue_5);
		
		JPanel saveStatPanel = new JPanel();
		saveStatPanel.setBackground(new Color(250, 250, 210));
		FlowLayout fl_saveStatPanel = (FlowLayout) saveStatPanel.getLayout();
		fl_saveStatPanel.setAlignment(FlowLayout.RIGHT);
		summaryPanel.add(saveStatPanel, BorderLayout.SOUTH);
		
		JButton btnSaveValues = new JButton("Save Values.");
		btnSaveValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(modelPanel.getComponentCount() == 0){
					//do nothing
					return;
				}
				
				lblSavedValue.setText(lblTotalFramesValue.getText());
				lblSavedValue_1.setText(lblCollisionVal.getText());
				lblSavedValue_3.setText(lblFramesRetransmittedVal.getText());
				lblSavedValue_2.setText(lblReceivedVal.getText());
				lblSavedValue_4.setText(lblFramesDroppedValue.getText());
				lblSavedValue_5.setText(lblSimulationTimeValue.getText());
			}
		});
		saveStatPanel.add(btnSaveValues);
		
		//declared as a field.
		graphPanel.setBackground(new Color(255, 255, 224));
		statPane.addTab("Graphs", graphPanel);
		graphPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblInsufficientDataTo = new JLabel("Insufficient data to plot  graphs.");
		lblInsufficientDataTo.setHorizontalAlignment(SwingConstants.CENTER);
		graphPanel.add(lblInsufficientDataTo, BorderLayout.NORTH);
	}
	
	/**
	 * Displays the results of the simulation to
	 * the summary panel on the StatPane.
	 * 
	 * Author Olawale.
	 */
	private void showStatistics() {
		lblTotalFramesValue.setText("" + simulationEngine.generateSummaryStats().getSent());
		lblCollisionVal.setText("" + simulationEngine.generateSummaryStats().getCollisions());
		lblFramesRetransmittedVal.setText("" + simulationEngine.generateSummaryStats().getRetransmitted());
		lblReceivedVal.setText("" + simulationEngine.generateSummaryStats().getRecieved());
		lblFramesDroppedValue.setText("" + simulationEngine.generateSummaryStats().getDropped());
		lblSimulationTimeValue.setText("" + (endTime - startTime)/1000 + "Seconds");
		
		updateStatistics(simulationEngine.generateSummaryStats());
		
	}
	
	/**
	 * Displays the results of the simulation as a graph
	 * to the graph panel of the StatPane.
	 * 
	 * Author Olawale.
	 */
	private void showGraphs(){
		if(recieved != null && collisions != null){		
			String Collision = "Collision";
			String Received = "Received";
						
			String comboBoxItems [] = {Received, Collision};
						
			JComboBox<String> cb= new JComboBox<String>(comboBoxItems);
			cb.setEditable(false);
			cb.addItemListener(new ItemListenerEvent());
			graphPanel.removeAll();
			graphPanel.add(cb, BorderLayout.NORTH);
			
			//Create the "cards".
			JPanel card1 = new JPanel();
			
			new BarChart("Recieved", card1, "Total Frame Attempts","Total Frames Received", protocolsSimulated, recieved);
			
			//Create the "cards".
			JPanel card2 = new JPanel();
			new BarChart("Collision", card2, "Total Frame Attempts","No of Collisions",protocolsSimulated, collisions);
					
			//Create the panel that contains the "cards".
			cards.add(card1, Received);
			cards.add(card2, Collision);
			
			//
	        graphPanel.add(cards, BorderLayout.CENTER);
	        graphPanel.revalidate();
	        graphPanel.repaint();
		}
	}
	
	/**
	 * updates the text of the lblSimultionStatus label.
	 * @param text
	 * 
	 * Author Olawale
	 */
	public void setlabelText(String text){
		lblSimulationStatus.setText(text);
	}
	
	/**
	 *Update the status of the undo and redo button
	 *
	 * Author Olawale.	
	 */
	public void updateButtons(UndoManager undoManager){
		btnUndo.setEnabled(undoManager.canUndo());
		btnRedo.setEnabled(undoManager.canRedo());
	}
	
	/**
	 * Takes in a statistic object and assigns it to a 
	 * String used to identify the statistic object
	 *  when plottng the graph
	 * @param s
	 * 
	 * Author Olawale.
	 */
	private void updateStatistics(Statistics s){
		if(simCount == 1){			
			//clear the arrayList so that only two results are compared.
			if(protocolsSimulated.size() == 2){
				protocolsSimulated.clear();
			}
			
			//add the name of the last Simulated protocol to the arrayList.  
			if (activeProtocolIndex == 0){
				protocolsSimulated.add("Aloha");
			}
			if (activeProtocolIndex == 1){
				protocolsSimulated.add(" Slotted Aloha");
			}
			if (activeProtocolIndex == 2){
				protocolsSimulated.add("CSMA");
			}
			if (activeProtocolIndex == 3){
				protocolsSimulated.add("CSMA/CD");
			}
			
			recieved.add(simulationEngine.generateSummaryStats().getRecieved());
			collisions.add(simulationEngine.generateSummaryStats().getCollisions());
		}
		if(simCount == 2){			
			//reset the simCount to limit comparison to two results only.
			simCount = 0;
			
			//add the name of the last Simulated protocol to the arrayList.
			if (activeProtocolIndex == 0){
				protocolsSimulated.add("Aloha");
			}
			if (activeProtocolIndex == 1){
				protocolsSimulated.add(" Slotted Aloha");
			}
			if (activeProtocolIndex == 2){
				protocolsSimulated.add("CSMA");
			}
			if (activeProtocolIndex == 3){
				protocolsSimulated.add("CSMA/CD");
			}
			
			recieved.add(simulationEngine.generateSummaryStats().getRecieved());
			collisions.add(simulationEngine.generateSummaryStats().getCollisions());
			
			//the required arrayList to plot the graph have been initialised 
			//so the graph can be created.
			showGraphs();
			
			recieved.clear();
			collisions.clear();
		}
	}
	
	private void showLogReader(){
		JDialog jd = new JDialog();
		jd.setTitle("Log Reader");
		LogReader reader = new LogReader();
		reader.updateTextArea(simulationEngine.collateLog().trim());
		reader.setVisible(true);
		jd.getContentPane().add(reader);
		jd.pack();
		jd.setVisible(true);
	}
	

	/**
	 * an itemListener class for selecting items to show graphs.
	 * 
	 * @author Olawale
	 *
	 */
	class ItemListenerEvent implements ItemListener{

		public void itemStateChanged(ItemEvent evt) {
	        CardLayout cl = (CardLayout)(cards.getLayout());
	        cl.show(cards, (String)evt.getItem());
		}		
	}

}