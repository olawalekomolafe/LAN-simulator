package gui_interface;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A MouseAdapter class to handle adding a label
 * representing a network device to the modelPanel
 * when a user clicks on a label in the devicePane. 
 * 
 * Author Olawale.
 */
class ClickMouseAdapter extends MouseAdapter {
	ArrayList<JLabel> labels;
	JFrame frmLanSimulator;
	JPanel modelPanel;
	private ComponentEventAdapter ca;
	private ComponentMover cm;
	
	
	public ClickMouseAdapter(LAN_Simulator lanSim,ArrayList<JLabel> labels, JPanel panel, JFrame frame){
		this.labels = labels;
		modelPanel = panel;
		frmLanSimulator = frame;		
		cm = new ComponentMover(lanSim);
		ca = new ComponentEventAdapter(lanSim, panel);
	}
	
    public void mouseClicked(MouseEvent e){
    	JLabel label = ((JLabel)e.getSource());
    	//searches for the label in the label arrayList 
        for(int i = 0; i < labels.size(); i++)
        	if(labels.get(i) == label){
        		if(labels.get(i) instanceof SwitchLabel){
            		String labelText = JOptionPane.showInputDialog(frmLanSimulator, "Please enter the device's name",
            				"Device Name", JOptionPane.QUESTION_MESSAGE);
            		SwitchLabel newLabel = new SwitchLabel(labelText);
            		newLabel.setHorizontalTextPosition(JLabel.CENTER);
            		newLabel.setVerticalTextPosition(JLabel.BOTTOM);
            		newLabel.setIcon(label.getIcon());
            		newLabel.addComponentListener(ca);
            		modelPanel.add(newLabel);
            		newLabel.setVisible(false);
            		newLabel.setVisible(true);
            		newLabel.setLocation(0,0);
            		newLabel.setSize(new Dimension(90,90));
            		cm.registerComponent(newLabel);
            		modelPanel.revalidate();
            		modelPanel.repaint();
            		return;
        		}
        		else if(labels.get(i) instanceof CableLabel){
            		String labelText = JOptionPane.showInputDialog(frmLanSimulator, "Please enter the device's name",
            				"Device Name", JOptionPane.QUESTION_MESSAGE);
            		CableLabel newLabel = new CableLabel(labelText);
            		newLabel.setHorizontalTextPosition(labels.get(i).getHorizontalTextPosition());
            		newLabel.setVerticalTextPosition(labels.get(i).getVerticalTextPosition());
            		newLabel.setIcon(label.getIcon());
            		newLabel.addComponentListener(ca);
            		modelPanel.add(newLabel);
            		newLabel.setVisible(false);
            		newLabel.setVisible(true);
            		newLabel.setLocation(10,10);
            		newLabel.setSize(new Dimension(90,90));
            		newLabel.setPlane(((CableLabel)labels.get(i)).getPlane());
            		cm.registerComponent(newLabel);
            		modelPanel.revalidate();
            		modelPanel.repaint();
            		return;
        		}
        		else if(labels.get(i) instanceof ComputerLabel){
            		String labelText = JOptionPane.showInputDialog(frmLanSimulator, "Please enter the device's name",
            				"Device Name", JOptionPane.QUESTION_MESSAGE);
            		ComputerLabel newLabel = new ComputerLabel(labelText);
            		newLabel.setHorizontalTextPosition(JLabel.CENTER);
            		newLabel.setVerticalTextPosition(JLabel.BOTTOM);
            		newLabel.setIcon(label.getIcon());
            		newLabel.addComponentListener(ca);
            		modelPanel.add(newLabel);
            		newLabel.setVisible(false);
            		newLabel.setVisible(true);
            		newLabel.setLocation(0,0);
            		newLabel.setSize(new Dimension(90,90));
            		cm.registerComponent(newLabel);
            		modelPanel.revalidate();
            		modelPanel.repaint();
            		return;
        		}
        		else if(labels.get(i) instanceof HubLabel){
            		String labelText = JOptionPane.showInputDialog(frmLanSimulator, "Please enter the device's name",
            				"Device Name", JOptionPane.QUESTION_MESSAGE);
            		HubLabel newLabel = new HubLabel(labelText);
            		newLabel.setHorizontalTextPosition(JLabel.CENTER);
            		newLabel.setVerticalTextPosition(JLabel.BOTTOM);
            		newLabel.setIcon(label.getIcon());
            		newLabel.addComponentListener(ca);
            		modelPanel.add(newLabel);
            		newLabel.setVisible(false);
            		newLabel.setVisible(true);
            		newLabel.setLocation(0,0);
            		newLabel.setSize(new Dimension(90,90));
            		cm.registerComponent(newLabel);
            		modelPanel.revalidate();
            		modelPanel.repaint();
            		return;
        		}
        	}                   	
    }

}	

