package gui_interface;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
/**
 * A class that subclasses panel used to display the log
 * to the user.
 * @author Olawale
 *
 */
public class LogReader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//A text area that displays the log as text
	private JTextArea textArea;

	/**
	 * Create the panel.
	 */
	public LogReader() {
		setLayout(new BorderLayout(0, 0));
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textArea);
		add(scrollPane);
	}
	
	/**
	 * Takes in a String and adds it to the text 
	 * diplayed on te textArea.
	 * @param s
	 */
	public void updateTextArea(String s){
		String log = textArea.getText() + "\r\n" + s;
		textArea.setText(log);
	}

}
