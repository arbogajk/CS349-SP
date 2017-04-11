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
    	AudioControlPanel audioControls = new AudioControlPanel();
    	audioControls.setBounds(0,600,600,200);
    	//EQControlPanel eqPanel = new EQControlPanel();
    	contentPane.add(audioControls);
    	
    	JPanel eq = audioControls.eqControlPanel();
    	contentPane.add(eq);
    	
    	JPanel fvp = new FrequencyVizPanel();
    	fvp.setBounds(0, 0, 600, 300);
    	contentPane.add(fvp);
    }	
}
