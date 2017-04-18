package masteringVisualizations;

import javax.swing.JComboBox;
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
	private JComboBox<String> cbox;
	
	public FrequencyVizPanel(int width, int height)
	{
		super();
		setLayout(null);
		
		// Setup the default Visualization
  	//Stage stage = new DropletsAnimationStage(1, width, height - 10);
		Stage stage = new SpectrumAnimationStage(1, width, height - 10);
  	VisualizationView view = stage.getView();
  	view.setBounds(0, 0, width , height - 20);
  	add(view);
  	stage.start();
  	
  	// Create the combo box to select the animation
  	String[] animations = {"Spectrum", "Droplets"};
  	JComboBox<String> cbox = new JComboBox<>(animations);
  	//cbox.addActionListener(this);
  	cbox.setBounds(5, height - 20, 100, 20);
  	add(cbox);
	}
}
