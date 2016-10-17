package simulation;

 

/**
 * Abstract class that represent a network Device.
 * @author Olawale
 *
 */
public abstract class Device{
	//An Identification String
	private final String ID;
	
	public Device(String ID){
		this.ID = ID;
	}
	
	/**
	 * Recives a frame object
	 * @param frame
	 */
	public abstract void recieveFrame(Frame frame);

	/**
	 * Returns the devices ID.
	 * @return String
	 */
	public String getID() {
		return ID;
	}
}
