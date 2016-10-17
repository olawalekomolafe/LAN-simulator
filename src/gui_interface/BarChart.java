package gui_interface;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import java.util.ArrayList;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JPanel;
import java.awt.BorderLayout;
/**
 * The below code was found from the following source:
 * http://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
 * 
 * modified by Nasra Al-Barwani on 28th of July-2016.
 */

public class BarChart
{
    ArrayList<Integer> intList = new ArrayList<Integer>();
    ArrayList<String> stringList = new ArrayList<String>();
    
   public BarChart(String chartTitle, JPanel panel, String xAxis, String yAxis, ArrayList<String> stringList, 
		   ArrayList<Integer> intList)
   {
	   this.intList = intList;
	   this.stringList = stringList;
	   JFreeChart barChart = ChartFactory.createBarChart3D(
         chartTitle,
         xAxis,yAxis,
         graphValues(),
         PlotOrientation.VERTICAL,
         true,true,false);
        
	   ChartPanel chartPanel = new ChartPanel( barChart );
	   chartPanel.setPreferredSize( new java.awt.Dimension( 340 , 223 ) );
	   panel.setLayout(new BorderLayout(0,0));
	   panel.add( chartPanel, BorderLayout.CENTER );
    }
   
       
     private DefaultCategoryDataset graphValues( )
   {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      dataset.addValue( intList.get(0) , "Frames" ,  stringList.get(0));
      dataset.addValue( intList.get(1) , "Frames" ,  stringList.get(1));
      return dataset;
   }
}