package simulation;

 

import java.util.ArrayList;

/**
 * Switch class represents a switch device 
 * it recieves a frame from one node and 
 * retransmitts it out through another node. 
 * @author Olawale
 *
 */
public class Switch extends Device {
	//list of nodes belonging to the switch
	ArrayList<Node> nodes = new ArrayList<>();	
	
	public Switch(String ID){
		super(ID);
		activate();
	}
	
	/**
	 * Set al the switches node to null
	 */
	@SuppressWarnings("unused")
	public void setToNull() {
		for(Node node: nodes){
			node = null;
		}
	}
	
	/**
	 * Initialize the nodes belonging to the switch
	 */
	public void activate(){
		for(int i = 0; i < 4; i++){
			Node node = new Node(this);
			nodes.add(node);
		}
	}

	/**
	 * Recieves a frame and sends it out through 
	 * another node that has a link attached.
	 */
	@Override
	public void recieveFrame(Frame frame) {
		for(int i = 0; i < nodes.size(); i++){
			Node node = nodes.get(i);
			if(node != frame.getLastNode() && node.hasLink()){
				new Thread(new Runnable() {
					public void run() {
						try{
							node.sendFrame(1);;
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}).start();
			}
			
		}				
	}
	
	/**
	 * Returns a node that doesnt have a link
	 * @return Node
	 */
	public Node getNode(){
		for(Node node: nodes){
			if(!node.hasLink()){
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Returns all the nodes belonging to the switch
	 * @return ArrayList<Node>
	 */
	public ArrayList<Node> getNodes(){
		if(nodes.size() > 0){
			return nodes;
		}
		return null;
	}

	/**
	 * Checks if a node belonging to the switch 
	 * has not yet been initialized.
	 * @return boolean
	 */
	public boolean connected() {		
		for(Node node: nodes){
			if (node == null){
				return false;
			}			
		}
		return true;
	}
}
