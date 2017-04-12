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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visual.statik.sampled.ImageFactory;
import auditory.sampled.AddOp;
import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import auditory.sampled.FIRFilter;
import auditory.sampled.FIRFilterOp;
import io.ResourceFinder;


import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.SamplePlayer;



public class AudioControlPanel extends JPanel implements ActionListener,ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	
	private Color jmuPurple = new Color(69,0,132);
	private Color jmuGold = new Color(203,182,119);
	private Font font = new Font("Times New Roman",Font.BOLD,16);
	private Font fontVolume = new Font("Times New Roman",Font.BOLD,16);
	private Font title = new Font("Times New Roman",Font.BOLD,18);


	private int currVol, prevVol;
	

	private JPanel controlsPanel;
	
	private JSlider volumeSlider;
	private EQPanel eqPanel;
	private JProgressBar volume;
	private JComboBox<String> files;
	
	private JToggleButton playbutton, pausebutton, stopbutton;

	

	private static AudioContext ac;				//An audio context
	private static SamplePlayer sp;				//The audio player that takes sampled content
	private Gain g;							//Gain object for the overall master volume
		
	private final int WIDTH;
	private final int HEIGHT;
	
	public AudioControlPanel(int width, int height){
		super();	
		WIDTH = width;
		HEIGHT = height;
		
		setLayout(null);
		setBounds(0,0, WIDTH, HEIGHT);
		setBackground(jmuPurple);
		

    	finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image pIcon = imgFactory.createBufferedImage("/img/playButton.png", 4);
    	Image pauseIcon = imgFactory.createBufferedImage("/img/pauseButton.png",4);
    	Image stopIcon = imgFactory.createBufferedImage("/img/stopButton.png",4);
    	
    	Icon play = new ImageIcon(pIcon.getScaledInstance(50, 50, 0));
    	Icon pause = new ImageIcon(pauseIcon.getScaledInstance(50, 50, 0));
    	Icon stop = new ImageIcon(stopIcon.getScaledInstance(50, 50, 0));
    	
    	
    	volume = new JProgressBar(JProgressBar.VERTICAL,0,1);
    	volume.setValue(0);
    	volume.setBounds(WIDTH - 50, HEIGHT - 200, 40, 150);
 
    	volumeSlider = new JSlider(JSlider.VERTICAL,0,10,5);
    	volumeSlider.addChangeListener(this);
    	volumeSlider.setMajorTickSpacing(1);
    	volumeSlider.setSnapToTicks(true);
    	volumeSlider.setPaintTicks(true);
    	volumeSlider.setPaintLabels(true);
    	volumeSlider.setFont(fontVolume);
    	volumeSlider.setBorder(new LineBorder(jmuGold,1,true));
    	volumeSlider.setBackground(jmuPurple);
    	volumeSlider.setBounds((int)volume.getBounds().getMinX() - 140, (int)volume.getBounds().getY(), 100, 150);
    	volumeSlider.setForeground(jmuGold);
    	
     	files = buildDropDown();
    	files.setBounds((int)volumeSlider.getBounds().getMinX() - 250, HEIGHT - 100, 200, 20);
    	
    	playbutton = new JToggleButton(play);
    	playbutton.setBounds(5,(int)files.getBounds().getMaxY() - 20,60,60);
    	
    	playbutton.addActionListener(this);
    	
    	pausebutton = new JToggleButton(pause);
    	pausebutton.setBounds(65,(int)files.getBounds().getMaxY() - 20,60,60);
    	pausebutton.addActionListener(this);
    	playbutton.setBackground(jmuPurple);
    	
    	stopbutton = new JToggleButton(stop);
    	stopbutton.setBounds(125,(int)files.getBounds().getMaxY() - 20,60,60);
    	stopbutton.addActionListener(this);
    	

    	JLabel volumeLabel = new JLabel("Volume");
    	volumeLabel.setFont(font);
    	volumeLabel.setForeground(jmuGold);
    	volumeLabel.setBounds((int)volumeSlider.getBounds().getCenterX() - 20,(int)volumeSlider.getBounds().getMaxY() + 10,70,30);
    	
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
		AudioInputStream as = null;
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
		g = new Gain(ac, 2, 0.50f);
		g.addInput(sp);
		ac.out.addInput(g);
		
	    sp.setKillOnEnd(false);
	    
		
	
	}
	
			
	public JComboBox<String> buildDropDown(){
		String audioFiles[] ={"GeneralGrevious.wav", "underminers-drumloop.wav","VeilofShadows-Outro.wav"};

		JComboBox<String> fileSelect = new JComboBox<>(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
	
	}
	
	
	
	
	
	/** This method should toggle the color of the hpf and lpf buttons
	 *  when they are clicked and set the color back to default when clicked again.
	 * @param button
	 */
	public void toggleFilterButtons(JButton button){


		
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
			}	
			System.out.println("Initial Gain" + g.getGain());
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
		
		/************************************* EQ Low/High Pass Filters ********************************************************/
		
	}
	

/**
 * This method needs some work, it works mostly, but needs to be fine tuned a bit
 * should change the volume of the music sample.
 */

	@Override
	public void stateChanged(ChangeEvent e) {
	
		if(e.getSource().equals(volumeSlider)){
			currVol = volumeSlider.getValue();
			if(prevVol < currVol){
				if(g.getGain() < 0.9f)
					g.setGain(g.getGain() + 0.10f);
			}
			else
			{
				if(g.getGain() > 0){
					g.setGain(g.getGain()  - 0.10f);
					
				}
			}
		}

		System.out.println(g.getGain());

	}

	public void updateLevel(float level){
		
		
		
	}
	
	/**
	 * Getter for the AudioContext
	 * 
	 * @return the AudioContext
	 */
	public static AudioContext getAC()
	{
		return ac;
	}
	
	/**
	 * Getter for the AudioContext
	 * 
	 * @return the AudioContext
	 */
	public static SamplePlayer getSP()
	{
		return sp;
	}
	
}
