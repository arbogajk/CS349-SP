// Java Library
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;




// Multimedia Library
import app.*;
import io.*;
import masteringVisualizations.AudioControlPanel;
import masteringVisualizations.EQPanel;
import masteringVisualizations.FrequencyVizPanel;
import masteringVisualizations.SpectrumAnimationStage;
import visual.*;
import visual.dynamic.described.DescribedSprite;
import visual.dynamic.described.SampledSprite;
import visual.dynamic.described.Stage;
import visual.dynamic.sampled.Screen;
import visual.statik.described.*;
import visual.statik.sampled.BufferedImageOpFactory;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;


public class MasteringApp extends AbstractMultimediaApp      
{
	private JPanel contentPane;
	
	public static int MAX_WIDTH = (int)MasteringApplication.getWidth();
	public static int MAX_HEIGHT = (int)MasteringApplication.getHeight();
	public static int PANELHEIGHT;
	
    /**
     * Handle actionPerformed messages
     *
     * @param ae  The ActionEvent that generated the message
     */
    public void actionPerformed(ActionEvent ae)
    {
       String       ac;
       
       ac = ae.getActionCommand();
      
    }

    /**
     * The entry-point
     */
    public void init()
    {
    
    	contentPane = (JPanel)rootPaneContainer.getContentPane();
    	contentPane.setLayout(null);
    	contentPane.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT);
    
    	PANELHEIGHT = (int) MAX_HEIGHT /3;
    	AudioControlPanel audioControls = new AudioControlPanel(MAX_WIDTH, PANELHEIGHT);
    	
    	
    	System.out.println("Max width " + MAX_WIDTH);
    	System.out.println("Max HEIGHT " + MAX_HEIGHT);

    	System.out.println("Max 3RDS PANEL HEIGHT" + PANELHEIGHT);
    	
       	contentPane.add(audioControls);

    	JPanel fvp = new FrequencyVizPanel(MAX_WIDTH,PANELHEIGHT);
    	
    	fvp.setBounds(0, 0, MAX_WIDTH, PANELHEIGHT);
     	
     	
    	contentPane.add(fvp);
    	
    	
    	EQPanel eqPanel = new EQPanel(MAX_WIDTH,PANELHEIGHT);
    	eqPanel.setLayout(null);
    	eqPanel.setBounds(0,(int)fvp.getBounds().getMaxY(),MAX_WIDTH,PANELHEIGHT);
      	contentPane.add(eqPanel);
      	audioControls.setBounds(0, (int)eqPanel.getBounds().getMaxY(), MAX_WIDTH, PANELHEIGHT );
      	
    	
    }	

}
