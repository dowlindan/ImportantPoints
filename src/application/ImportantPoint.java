/**
 * @author 22ddowlin
 * An ImportantPoint is a point with importance based on its surrounding points.
 */

package application;

import java.awt.Point;

public class ImportantPoint extends Point
{
	private static final long serialVersionUID = 1L;
	
	private double importance;
	
	/**
	 * Creates an important point with given x and y values
	 * @param x
	 * @param y
	 */
	public ImportantPoint(int x, int y)
	{
		super(x,y);
		importance = 0.0;
	}
	
	/**
	 * Creates an important point with given point
	 * @param p
	 */
	public ImportantPoint(Point p)
	{
		super(p);
		importance = 0.0;
	}
	
	/**
	 * Calculates importance of each point in a linked list of important points
	 * @param ips
	 */
	private static void calculateImportance(LinkedList ips)
	{
		Node currentprev = ips.getTheTailNode();
		Node current = ips.getTheHeadNode();
		Node currentafter = ips.getTheHeadNode().getNext();
		double minimportance = 9999999;
		for (int i = 0; i < ips.size(); i++)
		{
			
			if (current == null && currentafter == null)
			{
				current = ips.getTheHeadNode();
				currentafter = current.getNext();
			}
			else if (currentafter == null)
			{
				currentafter = ips.getTheHeadNode();
			}
			else if (current == null)
			{
				current = ips.getTheHeadNode();
				currentafter = current.getNext();
			}
			
			ImportantPoint L = (ImportantPoint)currentprev.getData();
			ImportantPoint P = (ImportantPoint)current.getData();
			ImportantPoint R = (ImportantPoint)currentafter.getData();
			double d1 = Math.sqrt(Math.abs(((L.x-P.x)^2)+((L.y-P.y)^2)));
			double d2 = Math.sqrt(Math.abs(((P.x-R.x)^2)+((P.y-R.y)^2)));
			double d3 = Math.sqrt(Math.abs(((L.x-R.x)^2)+((L.y-R.y)^2)));
			double importance = d1 + d2 - d3;
			P.setImportance(importance);
			if (importance < minimportance)
				minimportance = importance;
				
					
			currentprev = current;
			current = currentprev.getNext();
			if (current!=null)
				currentafter = current.getNext();
		}
		
		Node current1 = ips.getTheHeadNode();
		
		while (current1!=null)
		{
			current1 = current1.getNext();
		}
		
	}
	
	/**
	 * Returns linked list of the amount of most important point using importance formula
	 * @param pts
	 * @param amount
	 * @return LinkedList of amount most important points
	 */
	public static LinkedList ImportantPoints(LinkedList pts, int amount)
	{
		//creates linked list of important points
		LinkedList ips = new LinkedList();
		Node current = pts.getTheHeadNode();
		while (current!=null)
		{
			ips.insertBack(new ImportantPoint((Point)current.getData()));
			current=current.getNext();
		}		
	
		while (ips.size()>amount)
		{
			calculateImportance(ips);
			removeLeastImportant(ips);
		}

		
		return ips;
	}
	
	/**
	 * Removes least important point in the list
	 * @param ips
	 */
	private static void removeLeastImportant(LinkedList ips)
	{
		double minimportance = 999999999;
		Node current = ips.getTheHeadNode();
		while (current != null)
        {
            ImportantPoint IP = (ImportantPoint)current.getData();
            if (IP.getImportance()<minimportance)
                minimportance = IP.getImportance();
            current = current.getNext();
        }
		
		current = ips.getTheTailNode();
		Node currentafter = ips.getTheHeadNode();
		for(int i = 0; i<ips.size(); i++)
		{
			ImportantPoint pafter = (ImportantPoint)currentafter.getData();
			
			if (pafter.getImportance()==minimportance)
			{
				if (currentafter==ips.getTheHeadNode())
					ips.removeFront();
				
				else if (currentafter.getData().equals(ips.getTheTailNode().getData()))
					ips.removeBack();
					
				else
					current.setNext(currentafter.getNext());
				//break;
			}
			
			current = currentafter;
			currentafter = current.getNext();
		}
		
	}
	
	/**
	 * Sets point's importance to i
	 * @param i
	 */
	public void setImportance(double i)
	{
		this.importance = i;
	}
	
	/**
	 * @return importance of the point
	 */
	public double getImportance()
	{
		return importance;
	}
	
	public String toString()
	{
		return super.toString() + "[Importance: " + this.importance + "]";
	}
}
