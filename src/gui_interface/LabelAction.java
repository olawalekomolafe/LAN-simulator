package gui_interface;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * LabelAction handles what happens when Devices are dragged
 * and dropped in the modelPane.  
 * @author Olawale
 *
 */
class LabelAction{
	
	LAN_Simulator lanSim;
	
	public LabelAction(LAN_Simulator lanSim){
		this.lanSim = lanSim;
	}
	
	/**
	 * interact takes in a mouse event, determines if the source component
	 * is an instance of one of the subclasses of JLabel and checks its
	 * current location to see if its bounds intersects with the bounds 
	 * of any other component.
	 * 
	 * if it intersects, the components intersect with each other in manners listed 
	 * below
	 * Switch, hubs and computer interact with cable by giving it their node.
	 * cable interacts with switch, hub or computer by collecting their node. 
	 * 
	 * if it doesn't, it calls separate(e)
	 * 
	 * @param e
	 */
	public void interact(MouseEvent e){
			JPanel jPanel = null;
			boolean intersects = false;
			
			if(e.getComponent().getParent() instanceof JPanel){
				 jPanel = (JPanel) e.getComponent().getParent();
			}
			
			if(e.getComponent() instanceof CableLabel && jPanel != null){
				Rectangle eBounds = e.getComponent().getBounds();
				
				int count = jPanel.getComponentCount();
				for(int i = 0; i < count;i++){
					if(jPanel.getComponent(i).getBounds().intersects(eBounds) && 
							jPanel.getComponent(i) != e.getComponent()){
						
						CableLabel cable = (CableLabel) e.getComponent();
						
						if(jPanel.getComponent(i) instanceof ComputerLabel){
							ComputerLabel computer = (ComputerLabel) jPanel.getComponent(i);
							
							if(!computer.getComputer().hasNode())
							computer.activate();												
							
							if(computer.getComputer().hasNode() && computer.getComputer().getNode() != null){
								boolean b = cable.getLink().addNode(computer.getComputer().getNode());
								if(b){
									lanSim.setlabelText("Cable is connected to Computer ");
									intersects = true;
								}
								else{
									JOptionPane.showMessageDialog(jPanel, "This cable already has two devices");
								}	
							}else {
									JOptionPane.showMessageDialog(jPanel, "Computer has link, drag it to remove link");
								}								
						}
						
						if(jPanel.getComponent(i) instanceof SwitchLabel){
							SwitchLabel aSwitch = (SwitchLabel) jPanel.getComponent(i);
							
							if(!aSwitch.getSwitch().connected())
								aSwitch.activate();
							
							if(aSwitch.getSwitch().connected() && aSwitch.getSwitch().getNode() != null){
								boolean b = cable.getLink().addNode(aSwitch.getSwitch().getNode());
								if(b){
									lanSim.setlabelText("Cable is connected to Switch.");
									intersects = true;
								}
								else{
									JOptionPane.showMessageDialog(jPanel, "This cable already has two devices");
								}
							}else {
								JOptionPane.showMessageDialog(jPanel, "Maximum number of devices have been added to this Switch");
							}
						}
						
						if(jPanel.getComponent(i) instanceof HubLabel){
							HubLabel hub = (HubLabel) jPanel.getComponent(i);
							
							if(!hub.getHub().connected())
							hub.activate();
							
							if(hub.getHub().connected() && hub.getHub().getNode() != null){
								boolean b = cable.getLink().addNode(hub.getHub().getNode());
								if(b){
									lanSim.setlabelText("Cable is connected to Hub.");
									intersects = true;
								}
								else{
									JOptionPane.showMessageDialog(jPanel, "This cable already has two devices");
								}
							}else {
								JOptionPane.showMessageDialog(jPanel, "Maximum number of devices have been added to this Hub");
							}
						}
						return;
					}
				}
			}
			
			if(e.getComponent() instanceof HubLabel && jPanel != null){				
				Rectangle eBounds = e.getComponent().getBounds();
				
				int count = jPanel.getComponentCount();
				for(int i = 0; i < count;i++){
					if(jPanel.getComponent(i).getBounds().intersects(eBounds) && 
							jPanel.getComponent(i) != e.getComponent() && 
							jPanel.getComponent(i) instanceof CableLabel){
						
						CableLabel cable = (CableLabel) jPanel.getComponent(i);
						HubLabel hub = (HubLabel) e.getComponent();
						
						hub.activate();
						
						if(hub.getHub().getNode() != null){
							boolean b = cable.getLink().addNode(hub.getHub().getNode());
							if(b){
								lanSim.setlabelText("Hub is connected to cable ");
								intersects = true;
							}
							else{								
								JOptionPane.showMessageDialog(jPanel, "This cable already has two devices, drag it to reset");
							}
						}else {
							JOptionPane.showMessageDialog(jPanel, "Well done you managed to achieve the impossible");
						}								
						return;
					}
				}
			}
			
			if(e.getComponent() instanceof SwitchLabel && jPanel != null){				
				Rectangle eBounds = e.getComponent().getBounds();
				
				int count = jPanel.getComponentCount();
				for(int i = 0; i < count;i++){
					if(jPanel.getComponent(i).getBounds().intersects(eBounds) && 
							jPanel.getComponent(i) != e.getComponent() && 
							jPanel.getComponent(i) instanceof CableLabel){
						
						CableLabel cable = (CableLabel) jPanel.getComponent(i);
						SwitchLabel aSwitch = (SwitchLabel) e.getComponent();
						
						aSwitch.activate();
						
						if(aSwitch.getSwitch().getNode() != null){
							boolean b = cable.getLink().addNode(aSwitch.getSwitch().getNode());
							if(b){
								lanSim.setlabelText("Switch is connected to cable.");
								intersects = true;
							}
							else{								
								JOptionPane.showMessageDialog(jPanel, "This cable already has two devices, drag it to reset");
							}
						}else {
							JOptionPane.showMessageDialog(jPanel, "Well done you managed to achieve the impossible");
						}						
						return;
					}
				}
			}
			
			if(e.getComponent() instanceof ComputerLabel && jPanel != null){
				Rectangle eBounds = e.getComponent().getBounds();
				
				int count = jPanel.getComponentCount();
				for(int i = 0; i < count;i++){
					if(jPanel.getComponent(i).getBounds().intersects(eBounds) && 
							jPanel.getComponent(i) != e.getComponent() && 
							jPanel.getComponent(i) instanceof CableLabel){
						
						CableLabel cable = (CableLabel) jPanel.getComponent(i);
						ComputerLabel computer = (ComputerLabel) e.getComponent();
						
						computer.activate();
						
						if(computer.getComputer().getNode() != null){
							boolean b = cable.getLink().addNode(computer.getComputer().getNode());
							if(b){
								lanSim.setlabelText("Computer is connected to cable.");
								intersects = true;
							}
							else{
								JOptionPane.showMessageDialog(jPanel, "This cable already has two devices, drag it to reset");
							}
						}else {
							JOptionPane.showMessageDialog(jPanel, "Well done you managed to achieve the impossible");
						}						
						return;
					}
				}
			}
			
			if (!intersects){
				separate(e);
			}
	}
	
	/**
	 * interact takes in a mouse event, determines if the source component
	 * is an instance of one of Switch, hubs, computer or cable and calls
	 * their respective device object setToNull method. 
	 * @param e
	 */
	public void separate(MouseEvent e){
		JPanel jPanel = null;
		
		if(e.getComponent().getParent() instanceof JPanel){
			 jPanel = (JPanel) e.getComponent().getParent();
		}
		
		if(e.getComponent() instanceof CableLabel && jPanel != null){
			CableLabel cbLabel = (CableLabel) e.getComponent();
			cbLabel.getLink().setToNull();
		}		
		if(e.getComponent() instanceof HubLabel && jPanel != null){
			HubLabel hlabel =(HubLabel) e.getComponent();
			hlabel.getHub().setToNull();
		}		
		if(e.getComponent() instanceof SwitchLabel && jPanel != null){
			SwitchLabel slabel =(SwitchLabel) e.getComponent();
			slabel.getSwitch().setToNull();
		}		
		if(e.getComponent() instanceof ComputerLabel && jPanel != null){
			ComputerLabel cLabel =(ComputerLabel) e.getComponent();
			cLabel.getComputer().setToNull();
		}		
		
}
}