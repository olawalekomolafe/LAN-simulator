package simulation;

 


/**
 * Write a description of class CSMA here.
 * 
 * @author Clement D. Bomedem . 
 * @version (a version number or a date)
 */
import java.util.*; 

public class CSMA extends Protocol{
   
    private long backOff = 2;
    private long startTime;   
    
    /**
     * simulating non persistent CSMA
     */
    public CSMA()
    {
        startTime = 0;
    }

    /**
     * Method run takes a a frame, its containing node and the
     * networkLink said node is connected to and depending on
     * the transmission status of the node attempts to send the 
     * frame through the link.  
     */
    public void run(Frame frame, Node node, NetworkLink link){
    	//To get the simulation start time
    	if(startTime == 0)
        	startTime = link.getStartTime();
    	
    	//Set a length for the frame to be transmitted.
    	frame.setLength((long)(new Random().nextInt(3) + 1));  
    	link.updateLog("Node [" +node.getID()+ "] is ready with a framee of length " +frame.getLength());
    	//set the networkLink maximum frame queue size.
    	link.setMaxSize(2);
    	
        boolean b;
        //Listen on the link(medium) to know if it's idle.
    	link.updateLog("Node [" +node.getID()+ "] is listening on the link....");
        if(!link.transmitting()){
        	//When link is idle, transmit frame onto the link.
        	link.updateLog("The link is idle. Node can go ahead and transmit.");
        	//Transmit frame            
            link.addToFrameQueue(frame);
            link.updateLog("Node [" +node.getID()+ "] has transmitted a frame at " +getTimeDifference());
            link.updateLog("Node [" +node.getID()+ "] has control of the link for the period " + frame.getLength()*link.getContentionPeriod());
            //contention period where collisions can happen.
            try {
				Thread.sleep(link.getContentionPeriod());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
            if(link.checkFrameQueue() && !frame.checkCollided()){ 
            	link.emptyQueue();
            	link.updateLog("Node [" +node.getID()+ "] succesfully transmitted frame without collison");
            	link.receiveFrame(node, frame);
            	backOff = 2;
            	return;
            	
            }
            else{
            	link.updateLog("A collision occured, so Node [" +node+ "] back off range increased to " + backOff);
            	collisionEvent(frame, link.getTransmittingFrame(), node, link);
            	link.emptyQueue();
            	//double the backOff range
            	if(backOff <= 32)
            	backOff = backOff * 2;
            	return;
            }
        }
        
      //If otherwise the link is busy, keep listening until idle and transmit.
        b = link.transmitting();
                    
        while(b){
        	link.updateLog("Link is busy, but Node [" +node+ "] will wait a random time and check again.");
            try{
            	//If busy wait a random amount of time before beginning to listen again
             Thread.sleep(backOff * (long) (new Random().nextInt(50) + 1)); 
            }
            catch(InterruptedException e){
            	e.printStackTrace();
            }
            
            
            if(!link.transmitting()){
            	link.updateLog("Link is idle again, so node can transmit");
                b = false;
                link.addToFrameQueue(frame);
                
                //contention period where collisions can happen.
                try {
					Thread.sleep(link.getContentionPeriod());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                if(link.checkFrameQueue() && !frame.checkCollided()){
                	link.emptyQueue();
                	link.updateLog("Node [" +node.getID()+ "] succesfully transmitted frame without collison");
                	link.receiveFrame(node, frame);  
                	backOff = 2;
                }
                else{
                	link.updateLog("A collision occured, so Node [" +node+ "] back off range increased to " + backOff);
                	collisionEvent(frame, link.getTransmittingFrame(), node, link);
                	link.emptyQueue();
                	//double the backOff range
                	if(backOff <= 32)
                	backOff = backOff * 2;
                	
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
        	long randomDelay = (long) (new Random().nextInt(Math.toIntExact(backOff))) * contention;
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

    
    


