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


public class      MasteringApp 
       extends    AbstractMultimediaApp 
       implements ActionListener, ChangeListener
{
	private JPanel contentPane;
	private VisualizationView specView;
	private ResourceFinder finder;
	private Color jmuPurple;
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
    	JButton playbutton,pausebutton,stopbutton;
    	finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image pIcon = imgFactory.createBufferedImage("img/playButton.png", 4);
    	Image pauseIcon = imgFactory.createBufferedImage("img/pauseButton.png",4);
    	Image stopIcon = imgFactory.createBufferedImage("img/stopButton.png",4);
    	Icon play = new ImageIcon(pIcon.getScaledInstance(50, 50, 0));
    	Icon pause = new ImageIcon(pauseIcon.getScaledInstance(50, 50, 0));
    	Icon stop = new ImageIcon(stopIcon.getScaledInstance(50, 50, 0));
    	
    	jmuPurple = new Color(69,0,132);
    	
    	JPanel audioControls = new JPanel();
    	audioControls.setBounds(0, 550, 500, 75);
    	
    	
    	playbutton = new JButton(play);
    	playbutton.setBounds(1,550,60,60);
    	
    	pausebutton = new JButton(pause);
    	pausebutton.setBounds(65,550,60,60);
    	playbutton.setBackground(jmuPurple);
    	stopbutton = new JButton(stop);
    	stopbutton.setBounds(75,550,60,60);
    	audioControls.setBackground(jmuPurple);
    	audioControls.add(playbutton);
    	audioControls.add(pausebutton);
    	audioControls.add(stopbutton);
    	contentPane.add(audioControls);
    	
         
    }





	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
}
