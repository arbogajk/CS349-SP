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
	
	private Color jmuPurple,jmuGold;
	private Font font = new Font("Times New Roman",Font.BOLD,16);;
	private Font title = new Font("Times New Roman",Font.BOLD,18);


	private int currVol, prevVol;
	private int lpfOn =0;
	private int hpfOn =0;
	public static Clip clip; 
	public static BufferedSound bs;
	
	
	private JSlider volumeSlider;
	private JSlider s250,s800,s25,s8;
	private JProgressBar volume;
	private JComboBox<String> files;
	private JToggleButton lpfButton, hpfButton;
	private JButton playbutton, pausebutton, stopbutton;

	
    private AudioContext ac;
    private SamplePlayer sp;
	private Gain g;
	private OnePoleFilter lpf,hpf;			//for low pass filter
	private SampleManager contentManager;
	private Glide lpfGain, hpfGain;
	private JPanel eqPanel;
	
	private SpinnerNumberModel filterFreqLPF, filterFreqHPF;
	private JSpinner lpfSpinner, hpfSpinner;
	
	public AudioControlPanel(){
		super();
		setLayout(null);
		setBounds(0, 520, 800, 250);
		
	
    	finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image pIcon = imgFactory.createBufferedImage("/img/playButton.png", 4);
    	Image pauseIcon = imgFactory.createBufferedImage("/img/pauseButton.png",4);
    	Image stopIcon = imgFactory.createBufferedImage("/img/stopButton.png",4);
    	
    	Icon play = new ImageIcon(pIcon.getScaledInstance(50, 50, 0));
    	Icon pause = new ImageIcon(pauseIcon.getScaledInstance(50, 50, 0));
    	Icon stop = new ImageIcon(stopIcon.getScaledInstance(50, 50, 0));
    	
    	jmuPurple = new Color(69,0,132);
    	this.setBackground(jmuPurple);
    	jmuGold = new Color(203,182,119);
    	
    	
    	files = buildDropDown();
    	files.setBounds(210, 75, 200, 20);
    	
    	playbutton = new JButton(play);
    	playbutton.setBounds(5,(int)files.getBounds().getMaxY() - 20,60,60);
    	
    	playbutton.addActionListener(this);
    	
    	pausebutton = new JButton(pause);
    	pausebutton.setBounds(65,(int)files.getBounds().getMaxY() - 20,60,60);
    	pausebutton.addActionListener(this);
    	playbutton.setBackground(jmuPurple);
    	
    	stopbutton = new JButton(stop);
    	stopbutton.setBounds(125,(int)files.getBounds().getMaxY() - 20,60,60);
    	stopbutton.addActionListener(this);
    	
    	volumeSlider = new JSlider(JSlider.VERTICAL,0,10,5);
    	volumeSlider.addChangeListener(this);
    	volumeSlider.setMajorTickSpacing(1);
    	volumeSlider.setSnapToTicks(true);
    	volumeSlider.setPaintTicks(true);
    	volumeSlider.setPaintLabels(true);
    	volumeSlider.setFont(font);
    	volumeSlider.setBorder(new LineBorder(jmuGold,1,true));
    	volumeSlider.setBackground(jmuPurple);
    	volumeSlider.setBounds(425, 20, 100, 150);
    	
    	JLabel volumeLabel = new JLabel("Volume");
    	volumeLabel.setFont(font);
    	volumeLabel.setBounds(450,170,70,30);
    	
    	volume = new JProgressBar(JProgressBar.VERTICAL,0,1);
    	volume.setValue(0);
    	volume.setBounds(550, 20, 40, 150);
 
    
    	add(volumeSlider);
    	add(volumeLabel);
    	add(files);
    	add(volume);
    	add(playbutton);
    	add(pausebutton);
    	add(stopbutton);
    	
    	samplePlayerInit();

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
		g = new Gain(ac, 2, 0.25f);
		g.addInput(sp);
		ac.out.addInput(g);
		lpfGain = new Glide(ac,0.2f);
		hpfGain = new Glide(ac,0.2f);
	    sp.setKillOnEnd(false);
	    
		lpf = new OnePoleFilter(ac,0.0f);
		hpf = new OnePoleFilter(ac,0.0f);
		lpf.addInput(lpfGain);
		hpf.addInput(hpfGain);
		lpf.addInput(sp);
		hpf.addInput(sp);
		ac.out.addInput(lpf);
		ac.out.addInput(hpf);
		
	}
	
			
	public JComboBox<String> buildDropDown(){
		String audioFiles[] ={"GeneralGrevious.wav", "underminers-drumloop.wav","VeilofShadows-Outro.wav"};

		JComboBox<String> fileSelect = new JComboBox<>(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
	
	}
	
	
	
	public JPanel eqControlPanel(){
		eqPanel = new JPanel();
	    	
		eqPanel.setBackground(jmuGold);
		eqPanel.setLayout(null);
		eqPanel.setBounds(0,300,600,300);
		eqPanel.setBorder(new LineBorder(jmuPurple,5));
		
		finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image eqIcon= imgFactory.createBufferedImage("/img/eqText.png", 2);
    	
    	Icon eqImg = new ImageIcon(eqIcon.getScaledInstance(100,70, 0));
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(null);
		sliderPanel.setBounds(20,30,550,235);
   
    	JLabel panelTitle = new JLabel(eqImg);
    	panelTitle.setBounds(((int)sliderPanel.getBounds().getCenterX()) - 75,15,110,80);
    	JLabel f250,f800,f25,f8;
    	
    	f250 = new JLabel("250 Hz");
    	f800 = new JLabel("800 Hz");
    	f25 = new JLabel("2.5 kHz");
    	f8 = new JLabel("8 kHz");
    	
    	
		s250 = new JSlider(JSlider.VERTICAL,-12,12,0);
		s250.setMajorTickSpacing(3);
		s250.setPaintLabels(true);
		s250.setPaintTicks(true);
		s250.setSnapToTicks(true);
		s250.addChangeListener(this);
		s250.setBounds(40,50,50,170);
		s250.setForeground(jmuPurple);
		f250.setBounds((int)s250.getBounds().getMinX() + 4,(int)s250.getBounds().getMaxY() -6,50,30);
		
		
		
		s800 = new JSlider(JSlider.VERTICAL,-12,12,0);
		s800.setMajorTickSpacing(3);
		s800.setPaintLabels(true);
		s800.setPaintTicks(true);
		s800.setSnapToTicks(true);
		s800.addChangeListener(this);
		s800.setForeground(jmuPurple);
		s800.setBounds(120,50,50,170);
		f800.setBounds((int)s800.getBounds().getMinX() + 4,(int)s800.getBounds().getMaxY() - 6,50,30);
		
		s25= new JSlider(JSlider.VERTICAL,-12,12,0);
		s25.setMajorTickSpacing(3);
		s25.setPaintLabels(true);
		s25.setPaintTicks(true);
		s25.setSnapToTicks(true);
		s25.addChangeListener(this);
		s25.setForeground(jmuPurple);
		s25.setBounds(200,50,50,170);
		f25.setBounds((int)s25.getBounds().getMinX() + 4,(int)s25.getBounds().getMaxY() - 6,50,30);
		s8= new JSlider(JSlider.VERTICAL,-12,12,0);
		s8.setMajorTickSpacing(3);
		s8.setPaintLabels(true);
		s8.setPaintTicks(true);
		s8.setSnapToTicks(true);
		s8.addChangeListener(this);
		s8.setForeground(jmuPurple);
		s8.setBounds(270,50,50,170);
		f8.setBounds((int)s8.getBounds().getMinX() + 4,(int)s8.getBounds().getMaxY() - 6,50,30);
		
		lpfButton = new JToggleButton("LPF");
		hpfButton = new JToggleButton("HPF");
		lpfButton.addActionListener(this);
		hpfButton.addActionListener(this);
		
		lpfButton.setBounds(350,20,100,30);
		hpfButton.setBounds(350,80,100,30);
		
		hpfSpinner = new JSpinner();
		lpfSpinner = new JSpinner();
		filterFreqLPF = new SpinnerNumberModel(100,20, 1000,10);
		filterFreqHPF = new SpinnerNumberModel(16000,1000,20000,100);
	
		lpfSpinner.setModel(filterFreqLPF);
		lpfSpinner.setBounds((int)lpfButton.getBounds().getMaxX() + 6, (int)lpfButton.getBounds().getY(),70,30);
		hpfSpinner.setModel(filterFreqHPF);
		hpfSpinner.setBounds((int)hpfButton.getBounds().getMaxX() + 6, (int)hpfButton.getBounds().getY(),70,30);
		
		
		eqPanel.add(panelTitle);
		sliderPanel.add(s250);
		sliderPanel.add(f250);
		sliderPanel.add(s800);
		sliderPanel.add(f800);
		sliderPanel.add(s25);
		sliderPanel.add(f25);
		sliderPanel.add(s8);
		sliderPanel.add(f8);
		sliderPanel.add(lpfButton);
		sliderPanel.add(hpfButton);
		sliderPanel.add(lpfSpinner);
		sliderPanel.add(hpfSpinner);
		eqPanel.add(sliderPanel);
		
		return eqPanel;
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
			lpf.setFrequency(0.0f);
			hpf.setFrequency(0.0f);
			lpf.setValue(0.0f);
			hpf.setValue(0.0f);
			sp.reset();
			lpf.setFrequency(0.0f);
			hpf.setFrequency(0.0f);
			sp.setSample(sample);
		    
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
		else if(e.getSource().equals(lpfButton))
		{
		
			if(lpfOn == 0){
				System.out.println("armed");
				float frequency = filterFreqLPF.getNumber().floatValue();
				lpf.setFrequency(frequency);
				
			
				lpfOn = 1;
			
			}
			else
			{
				
				lpf.setFrequency(0.0f);
			
				lpfOn = 0;
			}
		
			sp.start();

		}
		else if(e.getSource().equals(hpfButton))
		{
			if(hpfOn == 0){
				System.out.println("armed");
				float frequency = filterFreqHPF.getNumber().floatValue();
				hpf.setFrequency(frequency);

				System.out.println("HPF Gain value " + hpfGain.getValue());
				hpfOn = 1;
			}
			else{
				hpf.setFrequency(0.0f);
				hpfGain.setValue(hpfGain.getValue() - 0.80f);
				hpfOn = 0;
			}
			sp.start();
		}
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
				
					g.setGain(g.getGain()  - 0.10f);
			}
		}

		System.out.println(g.getGain());

	}

	public void updateLevel(float level){
		
		
		
	}
	
}
