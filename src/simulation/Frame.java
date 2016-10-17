package simulation;

 
/**
 * A Frame object that is transmitted across the 
 * simulated network.
 * 
 * @author Olawale.
 *
 */
public class Frame {
	//the frame legnth
	private long length;
	//the last node the frame was at.
	private Node lastNode;
	//the number of times the frame has been retransmitted.
	private int transmissionAttempts;
	//the frame collision status
	private boolean collided;
	
	public Frame(Node node){	
		length = 0;
		lastNode = node;
		transmissionAttempts = 0;
		collided = false;
	}
	
	/**
	 * set the length of the frame to l.
	 * @param l
	 */
	public void setLength(Long l){
		length = l;
	}
	
	/**
	 * Retrieve the length of the frame
	 * @return long
	 */
	public long getLength() {
		return length;
	}
	
	/**
	 * return transmission attempts.
	 * @return int
	 */
	public int getTransmissionAttempts() {
		return transmissionAttempts;
	}
	
	/**
	 * increase the value of transmission attempts
	 */
	public void increaseTransmissionAttempts(){
		transmissionAttempts++;
	}
	
	/**
	 * Assigns a node to the lastNode
	 * @param node
	 */
	public void setLastNode(Node node){
		lastNode = node;
	}
	
	/**
	 * Retrieve the last node.
	 * @return Node
	 */
	public Node getLastNode(){
		return lastNode;
	}
	
	/**
	 * sets the collision status of the frame
	 * @param b
	 */
	public synchronized void setCollided(boolean b){
		collided = b;
	}
	/**
	 * Retrives the collision status of the frame.
	 * @return boolean
	 */
	public synchronized boolean checkCollided(){
		return collided;
	}
}
