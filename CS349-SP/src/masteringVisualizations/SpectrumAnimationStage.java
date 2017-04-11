package masteringVisualizations;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import visual.dynamic.described.DescribedSprite;
import visual.dynamic.described.Stage;
import visual.dynamic.described.TweeningSprite;
import visual.statik.TransformableContent;
import visual.statik.described.AggregateContent;
import visual.statik.described.Content;


/**
 * The Stage representing the oscilloscope animation.
 * 
 * @author Isaac Sumner
 * @version V1 4.9.17
 *
 */
public class SpectrumAnimationStage extends Stage  
{
	
	public static final int VIEW_WIDTH = 600;
	public static final int VIEW_HEIGHT = 300;
	
	private PowerSpectrum ps;
	private AudioContext ac;
	private Content bg;
	
	public SpectrumAnimationStage(int arg0) 
	{
		super(arg0);
		
		bg = new Content(createBackground(), null, Color.BLACK, null);
		add(bg);
		
		
		ac = AudioControlPanel.getAC();
		
		// set up a master gain object
		Gain g = new Gain(ac, 2, 0.3f);
		ac.out.addInput(g);
		SamplePlayer sp = AudioControlPanel.getSP();
		g.addInput(sp);
		
		ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
		sfs.addInput(ac.out);
		
		FFT fft = new FFT();
		sfs.addListener(fft);
		
		ps = new PowerSpectrum();
		fft.addListener(ps);
		
		ac.out.addDependent(sfs);
		draw();
	}
	
	/**
	 * Draws the bars after a new tick occurs
	 */
	public void handleTick(int time)
	{
		clear();
		add(bg);
		draw();
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
  private Content createThinBar(Paint p, double x1, double y1, double x2, double y2)
  {
  	Path2D.Float path = new Path2D.Float();
  	path.moveTo(x1, y1);
  	path.lineTo(x2, y2);
  	path.lineTo(x2+1.0, y2);
  	path.lineTo(x2+1.0, y1);
  	path.lineTo(x1, y1);
  	path.closePath();
  	
  	return new Content(path, null, p, null);
  }
  
  /**
   * Routine to draw the features
   */
  private void draw()
  {
  	float[] features = ps.getFeatures();
  	System.out.println("BEFORE DRAWING FEATURES");
  	if (features != null)
  	{
  		System.out.println("THE FEATURES ARE NOT NULL");
  		for(int x = 0; x < VIEW_WIDTH; x++)
  		{
  			// figure out which featureIndex corresponds to this x-
  			// position
  			int featureIndex = (x * features.length) / VIEW_WIDTH;
  			
  			// calculate the bar height for this feature
  			int barHeight = Math.min((int)(features[featureIndex] *
  					VIEW_HEIGHT), VIEW_HEIGHT - 1);
  			
  			// Create the GradientPaint
  			GradientPaint gp = new GradientPaint(x, VIEW_HEIGHT, Color.GREEN,
  															x, VIEW_HEIGHT - barHeight, Color.RED);
  			
  			
  			
  			// draw a vertical line corresponding to the frequency
  			// represented by this x-position
  			add(createThinBar(gp, x, VIEW_HEIGHT, x, VIEW_HEIGHT - barHeight));
  		}
  	}
  	System.out.println("AFTER DRAWING FEATURES");
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
}
