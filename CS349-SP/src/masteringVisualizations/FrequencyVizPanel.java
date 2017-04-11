package masteringVisualizations;

import javax.swing.JPanel;

import visual.VisualizationView;
import visual.dynamic.described.Stage;

/**
 * JPanel that allows switches between visualizations
 * 
 * @author Isaac Sumner
 *
 */
public class FrequencyVizPanel extends JPanel 
{
	public FrequencyVizPanel()
	{
		super();
		setLayout(null);
		
		// Setup the default Visualization
  	Stage stage = new SpectrumAnimationStage(1);
  	VisualizationView view = stage.getView();
  	view.setBounds(0, 0, 600, 300);
  	add(view);
  	stage.start();
	}
}
