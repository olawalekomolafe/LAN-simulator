package simulation;

 

import java.util.ArrayList;

/**
 *The simulation engine class is used torun 
 *the network simulation. It can stop a 
 *running simulation and it also collates 
 *the log and statistic reports. 
 * 
 * @author Olawale
 *
 */
public class SimulationEngine {
	//list of nodes in the simulation model
	private ArrayList<Node> nodes;
	//field to represent a protocol
	private int activeProtocolIndex;
	//a statistics object.
	private Statistics summaryStats;
	//a list of running threads.
	private ArrayList<Thread> nodeThreads;
	// a list of network links in the simulation model.
	ArrayList<NetworkLink> links = new ArrayList<>();
	
	
	public SimulationEngine(){
		nodes = new ArrayList<>();
		nodeThreads = new ArrayList<>();
		summaryStats = new Statistics(0,0,0,0,0);
		activeProtocolIndex = 0;
	}
	
	/**
	 * add a node to the node list
	 * @param node
	 */
	public void addNodes(Node node){
		if(node != null ){			
			nodes.add(node);
		}
	}
	/**
	 * add the nodes from an arraylist to the list
	 * @param nodes
	 */
	public void addNodes(ArrayList<Node> nodes){
		for(Node node: nodes){
			if(node != null && node.hasLink()){
				this.nodes.add(node);
			}
		}
	}
	
	/**
	 * run the simulation.
	 * @param activeProtocolIndex
	 */
	public void run(int activeProtocolIndex){
		reset();
		this.activeProtocolIndex = activeProtocolIndex;
		
		for(Node node: nodes){	
			Thread t = new Thread(new Runnable() {
				public void run() {
					try{
						nodeRun(node);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			nodeThreads.add(t);
			node.setThread(t);
			t.start();
		}		
	}

	/**
	 * tells a node to start transmitting
	 * @param node
	 */
	private void nodeRun(Node node) {
		node.reset();
		node.getLink().reset();
		node.setActiveProtocol(activeProtocolIndex);
		if(node.getDevice() instanceof Computer)
		node.sendFrame(100);
		node.interruptThread();
	}
		
	/**
	 *Reset statistics objects.  
	 */
	public void reset(){
		summaryStats.reset();
	}
				
	/**
	 * checks if a thread is running.
	 * @return
	 */
	public boolean threadStatus(){
		for(Thread t: nodeThreads){
			if(t.isAlive())
				return true;			
		}
		return false;
	}

	/**
	 * stop a running simulation.
	 */
	public void stop() {
		reset();
		for(Node node :nodes){
			if(node.getThread().isAlive())
			node.getThread().interrupt();
		}
	}
	
	/**
	 * generate summary statistics
	 * @return
	 */
	public Statistics generateSummaryStats(){
		reset();
		for(Node node :nodes){
			summaryStats.addStats(node.getStats());
		}
		return summaryStats;
	}
	
	/**
	 * empty the nodes list
	 */
	public void empty() {
		nodes.clear();
	}
	
	/**
	 * Collate log.
	 * @return
	 */
	public String collateLog() {
		String collatedLog = ""; 		
		for(Node node: nodes){
			if(!links.contains(node.getLink())){
				collatedLog = collatedLog + "\r\n\r\n"+ node.getLink().getLog();
				links.add(node.getLink());
			}
		}
		//empty the link list.
		links.clear();
		return collatedLog;
	}	
}
