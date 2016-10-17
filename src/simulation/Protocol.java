package simulation;

 
/**
 * Abstract class to represent network Protocols.
 * @author Olawale
 *
 */
public abstract class Protocol
{
    /**
     * Constructor for objects of class Protocol
     */
    public Protocol(){
    }

    /**
     * Recieves a frame from a node and passes it to the link.
     * @param frame
     * @param node
     * @param link
     */
    public void run(Frame frame, Node node, NetworkLink link){
    }
    
    /**
     * Events that happen when a collision occurs
     * @param frame
     * @param transmittingFrame
     * @param node
     * @param link
     */
    @SuppressWarnings("unused")
	private void collisionEvent(Frame frame, Frame transmittingFrame,Node node,NetworkLink link){
    }
}
