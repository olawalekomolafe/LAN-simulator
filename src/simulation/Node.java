package simulation;

import java.util.ArrayList;

/**
 * Node class represents a network port and its interface, it
 * can recieve and send frames, montor the network link and 
 * interact with its parent device.
 *   
 * @author Olawale
 *
 */
public class Node {
	//The network link its connected to
	private NetworkLink link;
	//the parent device
	private Device device;
	//An ArrayList of protocols 
    private ArrayList<Protocol> protocols;
    //An ArrayList of Frames
    private ArrayList<Frame> frames;
    //The Active protocol for a transmission.
    private Protocol activeProtocol;
    //Boolean vaue to check if the node has a link.
    private boolean hasLink;
    //integer values that keep track of events
    private int transmissions;
    private int framesSent;
    private int framesReceived;
    private int frameCollisions;
    private int framesRetransmitted;
    private int framesDropped;
    private int resend;
    //The thread running this node
    private Thread thread;
    //The wait time befor retransmission 
    private long wait;
    //The Nodes name.
    private String nodeID;
    
    public Node(Device device){
    	nodeID = device.getID();
    	frames = new ArrayList<>();
    	protocols = new ArrayList<>();
    	this.device = device;
    	link = null;
        hasLink = false;
        addProtocols();
        reset();         
    }
        
    /**
     * Add a link to the Node
     * @param link
     */
    public void addLink(NetworkLink link){
        if(!hasLink){
	    	this.link = link ;
	        hasLink = true;
        } 
    }
    
    /**
     * Send a number of frames to the link
     * @param transmissions
     */
    public void sendFrame(int transmissions){
    	//Initialize the link log object.
    	link.initializeLog();
    	this.transmissions = transmissions;
    	
    	for(int i= 1; i <= this.transmissions; i++){
    
 			if(wait != 0){
 				try {
					Thread.sleep(wait);
					wait = 0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			    	Frame frame = new Frame(this);
			    	addFramesSent();
			        activeProtocol.run(frame,this,link);		        
		
		    }else if(wait == 0){
		    	Frame frame = new Frame(this);
		    	addFramesSent();
		        activeProtocol.run(frame,this,link);
		    }
    	}
    	
    	//resend();
    	return;
    }
    
    /**
     * Send a single frame across the link
     * @param frame
     */
    public void sendFrame(Frame frame) {
    	if(wait != 0){
				try {
				Thread.sleep(wait);
				wait = 0;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    	frame = new Frame(this);
		    	addFramesSent();
		        activeProtocol.run(frame,this,link);		        
	
	    }else if(wait == 0){
	    	frame = new Frame(this);
	    	addFramesSent();
	        activeProtocol.run(frame,this,link);
	    }
		
	}
    
    /**
     * Resends frames that have been corrupted or lost.
     */
    public void resend(){
    	for(int i= 1; i < resend; i++){    	
    			if(wait == 0 && frames.size() != 0){
    				Frame f = frames.get(frames.size()-1);
    				if(f.getTransmissionAttempts() < 5){
    					activeProtocol.run(f,this,link);
    					frames.remove(f);
    					f.increaseTransmissionAttempts();
    				}    				
    				else{
    					frames.remove(f);
    					addFramesDropped();
    				}
		    	}
    			else{
    				try {
    					Thread.sleep(wait * 50);
    					wait = 0;
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    	}
    	return;
    }
    
    /**
     * Recieves a frame and passes it to the parent
     * device
     * @param frame
     */
    public void receiveFrame(Frame frame){
    	if(!frame.checkCollided()){
	    	frame.setLastNode(this);
	    	device.recieveFrame(frame);
	    	addFramesReceived();
	    	updateLog("Node " + nodeID + " has recieved frame at: " + (System.currentTimeMillis() - link.getStartTime()));
    	}
    	else{
    		frame = null; 
    	}
    }

    /**
     * Increment the node stats
     */
	public synchronized void addFramesSent(){    
        framesSent++;
    }
    
	/**
     * Increment the node stats
     */
    public synchronized void addFramesReceived(){    
        framesReceived++;
    }
    
    /**
     * Increment the node stats
     */
    public synchronized void addFrameCollisions(){
        frameCollisions++;
    }
    
    /**
     * Increment the node stats
     */
    public synchronized void addFramesDropped(){
        framesDropped++;
    }
    
    /**
     * Increment the node stats
     */
    public synchronized void addFrameRetransmitted(Frame frame){
    	framesRetransmitted++;
    	resend++;
    	frames.add(frame);
    }  
       
    /**
     * Add the protocols to the protocol list.
     */
    public void addProtocols(){
    	for(int i =0; i < 4; i++){
    		protocols.add(null);
    	}
        protocols.set(0, new PureAloha());
        protocols.set(1, new SlottedAloha());
        protocols.set(2, new CSMA());
        protocols.set(3, new CSMA_CD());
    }
    
    /**
     * sets the active protocol to a protocol in the
     * protocol list
     * @param index
     */
    public void setActiveProtocol(int index){
        activeProtocol = protocols.get(index);
    }
    
    /**
     * Reset the nod statistics and wait value
     * and reset the link.
     * 
     */
    public void reset(){
        framesSent = 0;
        framesReceived = 0;
        frameCollisions = 0;
        framesRetransmitted = 0;
        framesDropped = 0;
        transmissions = 0;
        wait = 0;
        if(link != null)
        link.reset();
    }

    /**
     * Return a statistics object containing the statistics
     * of the node. 
     * @return Statistics 
     */
	public Statistics getStats() {
		Statistics statistics = new Statistics(framesSent, frameCollisions, framesReceived, framesRetransmitted, framesDropped);
		return statistics;
	}
	
	/**
     * Set the node wait
     */
	public synchronized void setWait(long wait){
		this.wait = wait;			
	}

	/**
	 * Checks if the node has a link.
	 * @return Boolean
	 */
	public boolean hasLink() {
		if(hasLink){
			return true;
		}
		return false;
	}

	/**
	 * Removes the link
	 */
	public void removeLink() {
		link = null;
		hasLink = false;
	}

	/**
	 * Passes a thread object to the nodes thread. 
	 * @param Thread t
	 */
	public void setThread(Thread t) {
		thread = t;		
	}

	/**
	 * Interrupts the nodes thread.
	 */
	public void interruptThread() {
		thread.interrupt();		
	}

	/**
	 * Returns the nodes thread.
	 * @return Thread
	 */
	public Thread getThread() {
		return thread;		
	}

	/**
	 * Returns the nodes name
	 * @return String
	 */
	public String getID() {
		return nodeID.toUpperCase();
	}

	/**
	 * Returns the active Protocol.
	 * @return
	 */
	public Protocol getActiveProtocol() {
		return activeProtocol;
	}

	/**
	 * Returns the parent device.
	 * @return Device
	 */
	public Device getDevice() {
		return device;
	}
	
	/**
	 * Update the links log.
	 * @param string
	 */
	private void updateLog(String string) {
		link.updateLog(string);
	}

	/**
	 * Return the network link of the node.
	 * @return NetworkLink
	 */
	public NetworkLink getLink() {
		return link;
	}
}
