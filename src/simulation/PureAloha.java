package simulation;

 

import java.util.Random;
/**
 * A class to simulate the basic operation of pure aloha
 * media access protocol.
 * 
 * @author Olawale
 *
 */
public class PureAloha extends Protocol
{   
	private long startTime;
	
	public PureAloha(){
		startTime = 0;
    }
   
   /**
    * Method run takes a a frame, its containing node and the
    * networkLink said node is connected to and depending on
    * the transmission status of the node attempts to send the 
    * frame through the link.  
    */
    public void run(Frame frame,Node node ,NetworkLink link){
    	
    	if(startTime == 0)
    	startTime = link.getStartTime();
    	
        //set the networkLink maximum frame queue size.
        link.setMaxSize(2);
        //In pure aloha frames can have variable length.	
        frame.setLength((long)(new Random().nextInt(3)+1));
        link.updateLog("Node " + node.getID() + " generated a frame of length" + frame.getLength());
        link.addToFrameQueue(frame);  
        link.updateLog("Node " + node.getID() + "'s frame was passed to the link at: " + getTimeDifference());
        //contention period where collisions can happen.
        try {
        	//contention period for pure aloha is equal to double the propagation time for a single frame.
        	//i.e. frame vulnerability = 2T
        	//where T = propagation Time for a single frame across a link. 
			Thread.sleep(link.getContentionPeriod() * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        //check if the link frame queue has more than one frame if it doesn't, send frame successfully.
        if(link.checkFrameQueue() && !frame.checkCollided()){
        	link.emptyQueue();        	
        	link.updateLog("Node " + node.getID() + "'s frame was succefully sent across the cable: " + getTimeDifference());
        	link.simpleReceiveFrame(node, frame);
        	
        }
        //if the links frame queue contains more than one frame, a collision event has occurred.
        else{
        	link.emptyQueue();
        	link.updateLog("Node " + node.getID() + "'s frame collided during transmission: " + getTimeDifference());
        	collisionEvent(frame, link.getTransmittingFrame(), node, link);        	
        }
    }   
    /**
     * Events that occur when a collision happens on the network link
     * both nodes amend their statistic values.
     * @param frame
     * @param transmittingFrame
     * @param node
     * @param link
     */
    private void collisionEvent(Frame frame, Frame transmittingFrame, Node node, NetworkLink link){   	
    	//animate the link to show a collision has happened.
    	link.changeLabelIconCollision();
    	//update the statistics of the nodes
    	for(Node n : link.getNodes()){
        	if(n == node){
                node.addFrameCollisions();
                //node.addFrameRetransmitted(frame);                
        	}
        	else{        		        		
        		transmittingFrame.setCollided(true);
        		//n.addFrameRetransmitted(transmittingFrame);
        	}
        	//
        	int contention = Math.toIntExact( link.getContentionPeriod());
        	long randomDelay = (long) (new Random().nextInt(contention*50)+ contention);
        	//set the time for wait befor the node can send its next frame.
        	node.setWait(randomDelay);
        }
    }
    
    /**
     * Time difference from start time
     * @return
     */
    private long getTimeDifference(){
    	return (System.currentTimeMillis() - startTime);
    }
}