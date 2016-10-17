package simulation;

 

/**
 * A computer is a subclass of device,
 * it has a single node and performs no
 * action on recieving a frame.
 *  
 * @author Olawale
 */
public class Computer extends Device {

	Node node; 
	
	public Computer(String ID){
		super(ID);
		activate();
	}
	
	/**
	 * Assign an null value to the node.
	 */
	public void setToNull() {
		node = null;
	}
	
	/**
	 * Initialize the node.
	 */
	public void activate(){
		node = new Node(this);
	}

	@Override
	public void recieveFrame(Frame frame) {
		// do nothing.		
	}

	/**
	 * Returns a non empty node that doesnt have an link.
	 * @return Node
	 */
	public Node getNode() {
		if(!node.hasLink()){
			return node;
		}
		return null;
	}
	/**
	 * Returns the a non-empty node that has a link.
	 * @return Node
	 */
	public Node getNodes() {
		if(node == null || !node.hasLink()){
			return null;
		}
		return node;
	}
	
	/**
	 * Returns a boolean value that tells if the node 
	 * has been initialised
	 * @return boolean
	 */ 
	public boolean hasNode(){
		if(node == null){
			return false;
		}
		return true;
	}
}
