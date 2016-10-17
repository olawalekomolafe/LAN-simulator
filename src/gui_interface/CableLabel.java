package gui_interface;

import java.awt.Point;

import javax.swing.JLabel;


import simulation.NetworkLink;
/**
 * CableLabel is a subclass of JLabel that
 * contains a networkLink.
 * 
 * @author Olawale
 */
public class CableLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//A networkLink object.
	private NetworkLink link;
	//fields that store the location properties of the Label to facillitate undo.
	private Point oldP;
	private Point newP;
	//String field to store the type of cable whether horizontal or vertical.
	private String plane;
	
	public CableLabel(String text){
		super(text);
		activate();
		oldP = new Point();
		newP = new Point(0,0);
		plane = "";
	}
	
	/**
	 * 
	 * @return NetworkLink object.
	 */
	public NetworkLink getLink(){
		return link;
	}
	
	/**
	 * Initialise the  NetworkLink of the CableLabel.
	 */
	public void activate(){
		link = new NetworkLink(this);
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

	/**
	 * passes the string value reprsenting the plane into plane
	 * @param string
	 */
	public void setPlane(String string) {
		plane = string;
	}
	
	/**
	 * retrieves the plane string value.
	 * @return String.
	 */
	public String getPlane(){
		return plane;
	}
}
