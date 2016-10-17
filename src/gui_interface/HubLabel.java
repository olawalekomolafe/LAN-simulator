package gui_interface;

import java.awt.Point;

import javax.swing.JLabel;

import simulation.Hub;
/**
 * HubLabel is a subclass of JLabel that contains 
 * a hub Object.
 * 
 * @author Olawale
 */
public class HubLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Hub Object
	private Hub hub;
	//fields that store the location properties of the Label to facillitate undo.
	private Point oldP;
	private Point newP;
	
	public HubLabel(String text){
		super(text);
		activate();
		oldP = new Point();
		newP = new Point(0,0);
	}

	/**
	 * 
	 * @return Hub object.
	 */
	public Hub getHub() {
		
		return hub;
	}
	/**
	 * Initialise the hub object.
	 */
	public void activate(){
		hub = new Hub(this.getText());
	}
	
	/**
	  * updates the labels current location
	  * @param p
	  */
	public void setNewLocation(Point p){
		oldP = newP;
		newP = p;
	}
	
	/**
	 * gets the labels  previous location
	 * @return Point
	 */
	public Point getPreviousLocation() {
		return oldP;
	}
}
