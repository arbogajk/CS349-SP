package masteringVisualizations;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.described.Content;

/**
 * This class creates a view with the frequency based audio
 * visualization.
 * 
 * @author Isaac Sumner
 * @version V1 4.9.17
 */
public class SpectrumOscViewMaker  
{
	public static final int VIEW_WIDTH = 600;
	public static final int VIEW_HEIGHT = 600;
	
	private VisualizationView view;
	
	public SpectrumOscViewMaker(Stage stage)
	{
		// Make the background
		Content bg = new Content(createBackground(), null, Color.BLACK, null);
		stage.add(bg);
		
		stage.setRestartTime(400);
		//OscAnimation osc = new OscAnimation();
		//stage.add(osc);
		
		view = stage.getView();
	}
	
	/**
   * Creates the background shape for the view
   * 
   * @return a rectangle representing the view background
   */
	private Rectangle2D.Float createBackground()
	{
		return new Rectangle2D.Float(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
  }
	
  /**
   * Getter for the View
   * 
   * @return The current view
   */
  public VisualizationView getView()
  {
    return view;
  }
}
