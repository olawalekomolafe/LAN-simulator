package gui_interface;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


/**
 * ComponentListener class to listen for changes 
 * in a component and creates an undoable event for those actions.
 * 
 * @author Olawale
 *
 */
class ComponentEventAdapter implements ComponentListener {
	private UndoManager undoManager = new UndoManager();
	private JPanel modelPanel;
	private LAN_Simulator lanSim;
	
	public ComponentEventAdapter(LAN_Simulator lanSim, JPanel modelPanel){
		this.modelPanel = modelPanel;
		this.lanSim = lanSim;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		//do nothing.
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		Point oldP = new Point();
		Point newP = new Point();
		
		if(e.getComponent() instanceof ComputerLabel){
			ComputerLabel c = (ComputerLabel) e.getComponent();
			oldP = c.getPreviousLocation();
			newP = c.getLocation();
		}
		if(e.getComponent() instanceof CableLabel){
			CableLabel c = (CableLabel) e.getComponent();
			oldP = c.getPreviousLocation();
			newP = c.getLocation();
		}
		if(e.getComponent() instanceof HubLabel){
			HubLabel c = (HubLabel) e.getComponent();
			oldP = c.getPreviousLocation();
			newP = c.getLocation();
		}
		if(e.getComponent() instanceof SwitchLabel){
			SwitchLabel c = (SwitchLabel) e.getComponent();
			oldP = c.getPreviousLocation();
			newP = c.getLocation();
		}
		
		undoManager.undoableEditHappened(new UndoableEditEvent(modelPanel, new ComponentMovedEdit(e.getComponent(),oldP , newP, modelPanel)));
		lanSim.updateButtons(undoManager);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// Do nothing.
		
	}

	@Override
	public void componentShown(ComponentEvent e) {			
		undoManager.undoableEditHappened(new UndoableEditEvent(modelPanel, new ComponentAddedEdit(e.getComponent(),e.getComponent().getLocation(), modelPanel)));
		lanSim.updateButtons(undoManager);
	}
	
}


/**
 * an Undoable edit class for when coponents added
 * to the model panel.
 * 
 * @author Olawale
 *
 */
class ComponentAddedEdit extends AbstractUndoableEdit{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Component component;
	Point point;
	JPanel modelPanel;
	
	public ComponentAddedEdit(Component c, Point p, JPanel panel){
		component = c;
		point = p;
		modelPanel = panel;
	}
	
	public void undo()throws CannotUndoException{
		super.undo();
		modelPanel.remove(component);
		modelPanel.revalidate();
		modelPanel.repaint();
	}
	
	public void redo()throws CannotRedoException{
		super.redo();
		modelPanel.add(component);
		modelPanel.revalidate();
		modelPanel.repaint();
	}
}

/**
 * an Undoable edit class for when coponents are
 * moved
 * 
 * @author Olawale
 *
 */
class ComponentMovedEdit extends AbstractUndoableEdit{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Component component;
	Point oldPoint;
	Point newPoint;
	JPanel modelPanel;
	
	public ComponentMovedEdit(Component c, Point oldPoint, Point newPoint, JPanel panel){
		component = c;
		this.oldPoint = oldPoint;
		this.newPoint = newPoint;
		modelPanel = panel;
	}
	
	public void undo()throws CannotUndoException{
		super.undo();
		component.setLocation(oldPoint);
		modelPanel.revalidate();
		modelPanel.repaint();
	}
	
	public void redo()throws CannotRedoException{
		super.redo();
		component.setLocation(newPoint);
		modelPanel.revalidate();
		modelPanel.repaint();
	}
}	
