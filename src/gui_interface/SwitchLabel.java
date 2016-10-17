package gui_interface;

import java.awt.Point;

import javax.swing.JLabel;

import simulation.Switch;
/**
 * SwitchLabel is a subclass of JLabel that contains
 * a Switch Object.
 * @author Olawale
 *
 */
public class SwitchLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Switch object
	private Switch aSwitch;
	//fields that store the location properties of the Label to facillitate undo.
	private Point oldP;
	private Point newP;
	
	public SwitchLabel(String text){
		super(text);
		activate();
		oldP = new Point();
		newP = new Point(0,0);
	}
	
	/**
	 * 
	 * @return Switch.
	 */
	public Switch getSwitch() {
		
		return aSwitch;
	}
	
	/**
	 * Initialise switch object.
	 */
	public void activate(){
		aSwitch = new Switch(this.getText());
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
	 * @return Point.
	 */
	public Point getPreviousLocation() {
		return oldP;
	}
		
}
