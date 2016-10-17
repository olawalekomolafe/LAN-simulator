package simulation;

import java.util.Random;

/**
 * Write a description of class SlottedAloha here.
 * 
 * @author Patric Afotey. 
 * @version (a version number or a date)
 */
public class SlottedAloha extends Protocol{
	private long startTime;
	
	public SlottedAloha(){
		startTime = 0;
	}

	 /**
    * Method run takes a a frame, its containing node and the
    * networkLink said node is connected to and depending on
    * the transmission status of the node attempts to send the 
    * frame through the link.  
    */
    public void run(Frame frame,Node node ,NetworkLink link){  
    	//To get the simulation start time
    	if(startTime == 0)
        	startTime = link.getStartTime();
    	//set the networkLink maximum frame queue size.
        link.setMaxSize(2);
        //start the slots
        link.startSlot();        
        //In slotted aloha,all frames have the same length.	
        frame.setLength((long) 1);
        link.updateLog("Node " + node.getID() + " generated a frame of length " + frame.getLength());
        //control value for the while loop
        boolean start = false;
        
        //listen until next slot
        while(!start){         	
        	if(link.getSlot() == 0){
        		//if slot is empty, add a frame to the link queue.
            	if(!link.transmitting()){
            		link.updateLog("It is the beginning of a new slot- transmit");
            		link.setTransmitting(true);
            		link.updateLog("Node " + node.getID() + " passed a Frame to the link at: " + getTimeDifference(link));
                    link.addToFrameQueue(frame);                  
                    //contention period where collisions can happen.            
                    link.updateLog("Node " + node.getID() + " is transmitting a Frame within the propagation delay of " +
                    			link.getContentionPeriod() + "which forms the vulnerable period");
                    //check if the link frame queue has more than one frame, if it doesn't send frame successfully.
                    if(link.checkFrameQueue()&& !frame.checkCollided()){
                    	link.emptyQueue();
                    	link.updateLog("Node " + node.getID() + " succefully sent a frame across the cable " + getTimeDifference(link));
                    	link.receiveFrame(node, frame);
                    	start = true;
                    	link.setTransmitting(false);
                    }
                    //if the links frame queue contains more than one frame, a collision event has occurred.
                    else{
                    	link.emptyQueue();
                    	link.updateLog("Node " + node.getID() + "'s Frame collided at: " + getTimeDifference(link));
                    	collisionEvent(frame, link.getTransmittingFrame(), node, link);
                    	start = true;
                    	link.setTransmitting(false);
                    }
            	}            	
        	}else {
        		try {            	 
        			//checks for slot starts at every half of T.
    				Thread.sleep(link.getContentionPeriod()/2);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
        }
    }
    
    /**
     * When a collision happens on the network link
     * both nodes amend their statistic values.
     * @param frame
     * @param transmittingFrame
     * @param node
     * @param link
     */
    private void collisionEvent(Frame frame, Frame transmittingFrame, Node node, NetworkLink link){        
        link.changeLabelIconCollision();
    	for(Node n : link.getNodes()){
        	if(n == node){
                node.addFrameCollisions();
                //node.addFrameRetransmitted(frame);
        	}
        	else{        	
        		transmittingFrame.setCollided(true);
        		//n.addFrameRetransmitted(transmittingFrame);
        	}
        	
        	int contention = Math.toIntExact( link.getContentionPeriod());
        	long randomDelay = (long) (new Random().nextInt(contention*50)+ contention);
        	node.setWait(randomDelay);
        }
    }
    
    private long getTimeDifference(NetworkLink link){
    	return (System.currentTimeMillis() - startTime);
    }
}
