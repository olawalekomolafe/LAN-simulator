package simulation;

import java.util.ArrayList;

/**
 * Hub class represents a hub device known as a multiport 
 * repeater.it retransmitss any frame it recieves through all the 
 * nodes it has excluding the one that recieved the frame.
 * @author Olawale
 *
 */
public class Hub extends Device {

	//The list of nodes belonging to the hub.
	ArrayList<Node> nodes = new ArrayList<>();	
	
	public Hub(String ID){
		super(ID);
		activate();
	}
	
	/**
	 * set all nodes to null
	 */
	@SuppressWarnings("unused")
	public void setToNull() {
		for(Node node: nodes){
			node = null;
		}
	}
	
	/**
	 * initialize the nodes in the hub 
	 */
	public void activate(){
		for(int i = 0; i < 4; i++){
			Node node = new Node(this);
			nodes.add(node);
		}
	}

	/**
	 * 
	 * Recieves a frame and retransmits it through
	 * all its nodes excluding the node it recieved it from.
	 */
	@Override
	public void recieveFrame(Frame frame) {
		for(Node node: nodes){
			if(node == frame.getLastNode()){
				//do nothing
			}
			else{
				new Thread(new Runnable() {
					public void run() {
						try{
							node.sendFrame(frame);;
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
	 * Returns a node that has no link
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
	 * Returns all the nodes in the hub
	 * @return ArrayList<Node>
	 */
	public ArrayList<Node> getNodes(){
		if(nodes.size() > 0){
			return nodes;
		}
		return null;
	}
	
	/**
	 * Checks if the hub has uninitialised nodes.
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
