package simulation;

 

import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui_interface.CableLabel;
import gui_interface.LAN_Simulator;

/**
 * A network link object represents a network cabel 
 * It recieves and transmit frames.
 * In this implementation, it can only be connected to
 * a maximum of two nodes to simulate a network cable 
 * connecting two network devices like two computers,
 * a switch and a computer, a hub and a computer etc.
 * @author Olawale
 *
 */
public class NetworkLink {
   
	//The List of nodes connected to the link
	private ArrayList<Node> nodes;
	//An array list to contain frames passed to the link
	private ArrayList<Frame> frameQueue;
	//Frame in transmission
	private Frame transmittingFrame;
	//The time it takes for a frame to be sent across the link 
	private long propagationDelay;
	//Maximum size of the nodes list.
	private int maxSize;
	//An integer to reprsent a slot number
	private int slot;
	// Transmission status of the link
	private boolean transmitting;
	//The CableLabel the link belongs to
	private CableLabel label;
	//A Thread used to manage the slots
	private Thread t;
	//Boolean control field 
	private boolean started;
	//A String object to log events.
	private String log;
	//A long value to help show timing in the log
	private long startTime;
	
	
	public NetworkLink(CableLabel cl){
	  nodes = new ArrayList<>();
	  frameQueue = new ArrayList<>();
	  transmittingFrame = new Frame(null);
	  propagationDelay = 10;
	  label = cl;
	  t = new Thread();
	  started = false;
	  transmitting = false;
	  log = null;
	}
	
	/**
	 *Adds a node to the Link 
	 * @param node
	 * @return
	 */
	public boolean addNode(Node node){
	  if(nodes.size() < 2){
	      nodes.add(node);
	      node.addLink(this);
	      return true;
	  }
	  return false;
	}
	
	/**
	 * Transmit a frame to a node
	 * @param node
	 * @param frame
	 */
	public void sendFrame(Node node, Frame frame){
	  node.receiveFrame(frame);
	}
	
	/**
	 * Recieve a frame 
	 * this method involves a wait function for CSMA/CD.
	 * @param node
	 * @param frame
	 */
	public synchronized void receiveFrame(Node node, Frame frame) {
		transmitting = true;
	    addToFrameQueue(frame);   
	    transmittingFrame = frame;
	    
	    int index;
	    Node destinationNode = null;
	
	    index = nodes.indexOf(node);
	
	    if(index == 0){
	        try {
				Thread.sleep(frame.getLength()* propagationDelay);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
	        destinationNode = nodes.get(1);
	        sendFrame(destinationNode, frame);
	        changeLabelIconRecieved(1);
	        frameQueue.clear();
	        
	    }
	    if(index == 1){
	    	try {
				Thread.sleep(frame.getLength()* propagationDelay);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
	        destinationNode = nodes.get(0);
	        sendFrame(destinationNode, frame);
	        changeLabelIconRecieved(2);
	        frameQueue.clear();
	    }
	    transmitting = false;
	}
	
	/**
	 * Recieve a frame and find the destination node	
	 * @param node
	 * @param frame
	 */
	public synchronized void simpleReceiveFrame(Node node, Frame frame) {	    
	    int index;
	    Node destinationNode = null;
	
	    index = nodes.indexOf(node);
	
	    if(index == 0){
	        destinationNode = nodes.get(1);
	        sendFrame(destinationNode, frame);
	        changeLabelIconRecieved(1);
	        frameQueue.clear();
	        
	    }
	    if(index == 1){
	        destinationNode = nodes.get(0);
	        sendFrame(destinationNode, frame);
	        changeLabelIconRecieved(2);
	    }
	}
	
	/**
	 * Checks if the link is transmitting.
	 * @return
	 */
	public synchronized boolean transmitting(){
	   return transmitting;
	} 
	
	/**
	 * sets the transmission status.
	 * @param b
	 */
	public synchronized void setTransmitting(boolean b) {
		transmitting = b;
	}
	
	/**
	 * Adds Frame to frame queue.
	 * @param frame
	 * @return
	 */
	public synchronized boolean addToFrameQueue(Frame frame){
	    if(frameQueue.size() >= maxSize){
	       return false;
	    }
	    
	    frameQueue.add(frame);
	    return true;
	}
	
	/**
	 * Clear the Frame queue.
	 */
	public synchronized void emptyQueue(){
	    frameQueue.clear();
	}	

	/**
	 * Empty the Node List.
	 */
	public void setToNull() {
		for(int i = 0; i < nodes.size(); i++){
			nodes.get(i).removeLink();
			nodes.remove(i);	
		}
	}
	
	/**
	 * Checks if the Link can add another node.
	 * @return
	 */
	public boolean checkNodes(){
		if(nodes.size() < maxSize){
			return true;
		}		
		return false;
	}

	/**
	 * Gets the List of Nodes
	 * @return
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Gets the Transmitting Frame.
	 * @return
	 */
	public synchronized Frame getTransmittingFrame(){
		return transmittingFrame;
	}
	
	/**
	 * Sets the node list maximum size
	 * @param newSize
	 */
	public void setMaxSize(int newSize){
		maxSize = newSize;
	}
	
	/**
	 * Checks the current size of the frame queue. 
	 * @return
	 */
	public synchronized boolean checkFrameQueue(){
		if(frameQueue.size() > 1 && frameQueue.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * Returns the Propagation delay
	 * @return
	 */
	public long getContentionPeriod() {
		return propagationDelay;
	}
	
	/**
	 * Resets the Thread t and the log.
	 */
	public void reset(){
		t =  new Thread();
		log = "";
	}
	
	/**
	 * Starts the slot creation
	 */
	public void startSlot(){
		if(!started){
			started = true;
			if(!t.isAlive()){
			//method can only be called once.			
					t = new Thread(new Runnable(){
						public void run(){
							for(int i = 0; i <= 10000; i++){
								slot = 0;
								
								try {								
									Thread.sleep(propagationDelay);															
								} catch (InterruptedException e) {								
									e.printStackTrace();
								}
								
								slot = 1;
								
								if(i == 9999)
								t.interrupt();
								}
						}
					});
					t.start();
			}else{
				//do nothing
			}
		}
	}
	
	/**
	 * get the current value of the slot.
	 * @return
	 */
	public int getSlot(){
		return slot;
	}
	
	/**
	 * provide a sort of animation to the attached label 
	 */
	public void changeLabelIconCollision(){
		if(label.getPlane().equals("vertical")){	
					label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable_collision.png")));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable.png")));
		}
		
		if(label.getPlane().equals("horizontal")){	
					label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable_collision.png")));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable.png")));
		}
	}
	
	/**
	 * provide a sort of animation to the attached label 
	 * @param i
	 */
	public void changeLabelIconRecieved(int i){
		if(label.getPlane().equals("vertical")){
			if(i == 1){
				
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable_recieved1.png")));
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable.png")));		
			}
		
			if(i == 2){				
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable_recieved2.png")));
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/VCable.png")));
					
			}			
		}
		
		if(label.getPlane().equals("horizontal")){
			if(i == 1){
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable_recieved1.png")));
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable.png")));
					}
			
		
			if(i == 2){
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable_recieved2.png")));
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						label.setIcon(new ImageIcon(LAN_Simulator.class.getResource("/Images/HCable.png")));										
			}			
		}
	}	
	
	/**
	 * Returns the links name.
	 * @return
	 */
	public String getLinkName(){
		return label.getText().toUpperCase();
	}
	
	/**
	 * Starts the log
	 */
	public void initializeLog(){
			startTime = System.currentTimeMillis();							
			log = "Cable: " + getLinkName();
			for(Node n : nodes){
				log = log + "\r\nNode " + n.getID()
				  		  + "\r\nActive Protocol: " + n.getActiveProtocol().getClass()
				  		  + "\r\nParent Device: " + n.getDevice().getClass()					  		 
				  		  + "\r\nPropagation Delay " + propagationDelay
				  		  + "\r\n";
			}
	}
	
	/**
	 * Update the log
	 * @param logEvent
	 */
	public void updateLog(String logEvent){
		log = log + "\n" + logEvent;
	}
	
	/**
	 * Returns the log
	 * @return String
	 */
	public String getLog(){
		return log;
	}

	/**
	 * Returns the links log was initialized.
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}
}
