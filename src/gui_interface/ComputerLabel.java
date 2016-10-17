package gui_interface;

import java.awt.Point;

import javax.swing.JLabel;

import simulation.Computer;


/**
 * ComputerLabel is a subclass of JLabel that
 * contains a computer object.
 * 
 * @author Olawale
 */
public class ComputerLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//A computer obect.
	private Computer computer;
	//fields that store the location properties of the Label to facillitate undo.
	private Point oldP;
	private Point newP;
	
	public ComputerLabel(String text){
		super(text);
		activate();
		oldP = new Point();
		newP = new Point(0,0);
	}

	/**
	 * initialise the computer object.
	 */
	public void activate(){
		computer = new Computer(this.getText());
	}
	
	/**
	 * 
	 * @return Computer object.
	 */
	public Computer getComputer() {
		return computer;
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
