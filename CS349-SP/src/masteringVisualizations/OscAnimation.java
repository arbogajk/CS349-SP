package masteringVisualizations;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import visual.dynamic.described.DescribedSprite;
import visual.statik.described.AggregateContent;
import visual.statik.described.Content;

/**
 * The Sprite representing the oscilloscope animation.
 * 
 * @author Isaac Sumner
 * @version V1 4.9.17
 *
 */
public class OscAnimation extends DescribedSprite 
{
	public OscAnimation() 
	{
		super();
		AggregateContent line = new AggregateContent();
		line.add(createLine(0, 150, 600, 150));
		addKeyTime(500, 0.0, 0.0, 0.0, line);
		setEndState(REMAIN);
	}
	
	/**
   * Add a key time
   *
   * @param time    The key time
   * @param x       The x position
   * @param y       The y position
   * @param r       The rotation angle
   * @param content The static visual content
   */
  private void addKeyTime(int time, double x, double y,
                           double r, AggregateContent content)
  {
     addKeyTime(time, new Point2D.Double(x, y), new Double(r), 
                new Double(1.0), content);
  }
  
  /**
   * Makes a line given two points.
   * 
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return A content representation of a line
   */
  private Content createLine(double x1, double y1, double x2, double y2)
  {
  	Path2D.Float path = new Path2D.Float();
  	path.moveTo(x1, y1);
  	path.lineTo(x2, y2);
  	path.closePath();
  	
  	return new Content(path, Color.WHITE, Color.WHITE, null);
  }
}
