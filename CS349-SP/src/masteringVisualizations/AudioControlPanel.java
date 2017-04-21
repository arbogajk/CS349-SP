package masteringVisualizations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;


import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visual.statik.sampled.ImageFactory;
import io.ResourceFinder;
import net.beadsproject.beads.analysis.featureextractors.Power;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Clip;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.RMS;
import net.beadsproject.beads.ugens.SamplePlayer;



public class AudioControlPanel extends JPanel implements ActionListener,ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	
	private Color jmuPurple = new Color(69,0,132);
	private Color jmuGold = new Color(203,182,119);
	private Font font, fontVolume, title;

	private int currVol, prevVol;
	

	private JPanel controlsPanel;
	
	private JSlider volumeSlider;
	private EQPanel eqPanel;
	private JProgressBar volume;
	private JComboBox<String> files;
	
	private JToggleButton playbutton, pausebutton, stopbutton;

	

	private AudioContext ac;				//An audio context
	private SamplePlayer sp;				//The audio player that takes sampled content
	private Power power;
	private ShortFrameSegmenter sfs;
	private RMS rms;
	private static Gain masterGain;							//Gain object for the overall master volume
	
	private final int WIDTH;
	private final int HEIGHT;
	private final int MAX_HEIGHT;
	
	public AudioControlPanel(int width, int max_height, int height){
		super();	
		WIDTH = width;
		HEIGHT = height;
		MAX_HEIGHT = max_height;
		
		font = new Font("Times New Roman",Font.BOLD,(int)(WIDTH * 0.03));
		fontVolume = new Font("Times New Roman",Font.BOLD,(int)(WIDTH * 0.03));
		title = new Font("Times New Roman",Font.BOLD,(int)(WIDTH * 0.03));

		
		
		setLayout(null);
		setBounds(0,0, WIDTH, HEIGHT);
		setBackground(jmuPurple);
		

    	finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image pIcon = imgFactory.createBufferedImage("/img/playButton.png", 4);
    	Image pauseIcon = imgFactory.createBufferedImage("/img/pauseButton.png",4);
    	Image stopIcon = imgFactory.createBufferedImage("/img/stopButton.png",4);
    	
    	Icon play = new ImageIcon(pIcon.getScaledInstance((int)(WIDTH * 0.085), (int)(WIDTH * 0.085), 0));
    	Icon pause = new ImageIcon(pauseIcon.getScaledInstance((int)(WIDTH * 0.085), (int)(WIDTH * 0.085), 0));
    	Icon stop = new ImageIcon(stopIcon.getScaledInstance((int)(WIDTH * 0.085), (int)(WIDTH * 0.085), 0));
    	
    	
    	volume = new JProgressBar(JProgressBar.VERTICAL,0,2000);
    	volume.setValue(0);
    	volume.setBounds((int)(this.getBounds().getMaxX() - (int)WIDTH*0.1) - 10, 5, (int)(WIDTH * 0.1),
    			(int)this.getBounds().getHeight() - 25);
 
    	volumeSlider = new JSlider(JSlider.VERTICAL,0,10,5);
    	volumeSlider.addChangeListener(this);
    	volumeSlider.setMajorTickSpacing(1);
    	volumeSlider.setSnapToTicks(true);
    	volumeSlider.setPaintTicks(true);
    	volumeSlider.setPaintLabels(true);
    	volumeSlider.setFont(fontVolume);
    	volumeSlider.setBorder(new LineBorder(jmuGold,1,true));
    	volumeSlider.setBackground(jmuPurple);
    	volumeSlider.setBounds((int)(volume.getBounds().getMinX() - (int)(WIDTH * 0.15) - 10), 
    			5, (int)(WIDTH * 0.15), (int)this.getBounds().getHeight() - 25);
    	volumeSlider.setForeground(jmuGold);
    	
     	files = buildDropDown();
    	files.setBounds(10, HEIGHT - 200, 200, 20);
    	
    	/* Audio playback buttons */
    	playbutton = new JToggleButton(play);
    	playbutton.setBounds(5,(int)files.getBounds().getMaxY() + 100,60,60);
    	
    	playbutton.addActionListener(this);
    	
    	pausebutton = new JToggleButton(pause);
    	pausebutton.setBounds(65,(int)files.getBounds().getMaxY() + 100,60,60);
    	pausebutton.addActionListener(this);
    	playbutton.setBackground(jmuPurple);
    	
    	stopbutton = new JToggleButton(stop);
    	stopbutton.setBounds(125,(int)files.getBounds().getMaxY() + 100,60,60);
    	stopbutton.addActionListener(this);
    	

    	JLabel volumeLabel = new JLabel("Volume",SwingConstants.CENTER);
    	
    	volumeLabel.setFont(font);
    	volumeLabel.setForeground(jmuGold);
    	volumeLabel.setBounds((int)volumeSlider.getBounds().getMaxX() - 80,
    			(int)volumeSlider.getBounds().getMaxY() ,(int)(WIDTH * 0.1),20);
    	System.out.println("Center volume slider " +(int)volumeSlider.getBounds().getCenterX());
    	System.out.println("Center volume label " + (int)volumeLabel.getBounds().getCenterX());
    	samplePlayerInit();
    	
    	add(volumeSlider);
    	add(volumeLabel);
    	add(files);
    	add(volume);
    	add(playbutton);
    	add(pausebutton);
    	add(stopbutton);
    
	}
	
	public void samplePlayerInit(){
		
		String	audioFile = files.getSelectedItem().toString();
		InputStream sourceStream = finder.findInputStream("/audio/"+audioFile);
		
		Sample sample = null;
		try {
			sample = new Sample(sourceStream);
			System.out.println(sample.getLength());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("could not find " + audioFile );
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac = new AudioContext();
		sp = new SamplePlayer(ac, sample);
		

		masterGain = new Gain(ac, 1, 0.0f);
	
		masterGain.addInput(sp);
		ac.out.addInput(masterGain);
		ac.out.setGain(0.08f);
		masterGain.setGain(volumeSlider.getValue() );
	    sp.setKillOnEnd(false);
	    
	    rms = new RMS(ac,2,1024);
	    power = new Power();
	    rms.addInput(ac.out);
	    ac.out.addDependent(rms);
	    
		
	
	}
	
			
	public JComboBox<String> buildDropDown(){
		String audioFiles[] ={"VeilofShadows.wav","GeneralGrevious.wav", 
				"underminers-drumloop.wav","VeilofShadows-Outro.wav"};

		JComboBox<String> fileSelect = new JComboBox<>(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
	
	}
	
	
	/**
	 * Performs actions based on button clicks
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    String	audioFile = files.getSelectedItem().toString();
	    InputStream sourceStream = finder.findInputStream("/audio/"+audioFile);

		
		Sample sample = null;
		try {
			sample = new Sample(sourceStream);
		} catch (IOException | UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		if(files.hasFocus()){
			ac.stop();
			sp.reset();
			sp.setSample(sample);
			EQPanel.resetFilters();
		}
	
			
		if(e.getSource().equals(playbutton))
		{
			if(ac != null && !ac.isRunning())
			{
				ac.start();
				currVol = volumeSlider.getValue();
				prevVol = currVol;
				updateRMS();
			}	
			System.out.println("Initial Gain" + masterGain.getGain());
		}
		else if(e.getSource().equals(pausebutton))
		{
			ac.stop();
			
		}
		else if(e.getSource().equals(stopbutton))
		{
			ac.stop();
			sp.reset();
			ac.reset();
		}
	}
	

/**
 * This method needs some work, it works mostly, but needs to be fine tuned a bit
 * should change the volume of the music sample.
 */

	@Override
	public void stateChanged(ChangeEvent e)
	{
	
		if(e.getSource().equals(volumeSlider))
		{
			if(volumeSlider.getValue() == 0){
				ac.out.setGain(0.0f);
			}
			else{
				ac.out.setGain(0.08f);
				masterGain.setGain(volumeSlider.getValue());	
				
			}
					
			System.out.println("Gain: " + ac.out.getGain());
		
	
		}
	
	}

	public void updateRMS()
	{
		
		 	Thread thread = new Thread(){
			public void run()
			{
				while(getAC().isRunning())
				{
					float value = rms.getValue() * 10000;
					//System.out.println((int)value);
					synchronized(this){
						volume.setValue((int)value);
					}
					
				}
				
			}
		};
		thread.start();
		
	}
	
	/**
	 * Getter for the AudioContext
	 * 
	 * @return the AudioContext
	 */
	public AudioContext getAC()
	{
		return ac;
	}
	
	/**
	 * Getter for the AudioContext
	 * 
	 * @return the AudioContext
	 */
	public SamplePlayer getSP()
	{
		return sp;
	}
	public static Gain getMainGain(){
		return masterGain;
	}
}
