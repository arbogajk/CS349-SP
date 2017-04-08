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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.utils.IOUtils;

import auditory.sampled.AddOp;
import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import auditory.sampled.FIRFilter;
import auditory.sampled.FIRFilterOp;
import io.ResourceFinder;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.AudioIO;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.SamplePlayer;
import visual.statik.sampled.ImageFactory;

public class AudioControlPanel extends JPanel implements ActionListener,ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	
	private Color jmuPurple,jmuGold;
	private Font font = new Font("Times New Roman",Font.BOLD,16);;
	private Font title = new Font("Times New Roman",Font.BOLD,18);

	private int position=0;
	private boolean isPlaying = false;
	
	public static Clip clip; 
	public static BufferedSound bs;
	
	private JSlider volumeSlider;
	private JSlider s250,s800,s25,s8;
	private JProgressBar volume;
	private JComboBox<String> files;
	private JButton lpfButton, hpfButton;
	private JButton playbutton, pausebutton, stopbutton;
	
    private AudioContext ac;
    private SamplePlayer sp;
	private Gain g;
	private OnePoleFilter lpf;			//for low pass filter
	private SampleManager contentManager;
	private JPanel eqPanel;
	
	
	
	
	public AudioControlPanel(){
		super();
		setLayout(null);
		setBounds(0, 520, 800, 250);
		
	
    	finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image pIcon = imgFactory.createBufferedImage("../img/playButton.png", 4);
    	Image pauseIcon = imgFactory.createBufferedImage("../img/pauseButton.png",4);
    	Image stopIcon = imgFactory.createBufferedImage("../img/stopButton.png",4);
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
    	
    	volumeSlider = new JSlider(JSlider.VERTICAL,0,6,3);
    	volumeSlider.setMajorTickSpacing(1);
    	volumeSlider.setSnapToTicks(true);
    	volumeSlider.setPaintTicks(true);
    	volumeSlider.setPaintLabels(true);
    	volumeSlider.setFont(font);
    	volumeSlider.setBorder(new LineBorder(jmuGold,1,true));
    	volumeSlider.setBackground(jmuPurple);
    	
    	JLabel volumeLabel = new JLabel("Volume");
    	volumeLabel.setFont(font);
    	volumeSlider.setBounds(425, 20, 100, 150);
    	volumeSlider.addChangeListener(this);
    	volumeLabel.setBounds(450,170,70,30);
    	
    	volume = new JProgressBar(JProgressBar.VERTICAL,0,1);
    	volume.setValue(0);
    	volume.setBounds(550, 20, 40, 150);
 
//    	try {
//			clip = AudioSystem.getClip();
//		} catch (LineUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
		String	audioFile = files.getSelectedItem().toString();
		String sourceFile ="audio/" + audioFile;
		Sample sample = null;
		try {
			sample = new Sample(sourceFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ac = new AudioContext();
		sp = new SamplePlayer(ac, sample);
		g = new Gain(ac, 2, 0.2f);
		g.addInput(sp);
		ac.out.addInput(g);
		
		  
	    sp.setKillOnEnd(false);
		lpf = new OnePoleFilter(ac,700.0f);
		lpf.addInput(sp);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String	audioFile = "audio/" + files.getSelectedItem().toString();
		Sample sample = null;
	    if(files.hasFocus()){
	    	ac.stop();
	    	
	    }
		
		sample = SampleManager.fromGroup(audioFile, 1);
		//sample = new Sample(audioFile);
	
		if(files.hasFocus()){
			ac.stop();
			sp.setSample(sample);
		    
		}
			
			if(e.getSource().equals(playbutton))
			{
				if(ac != null && !ac.isRunning())
				{
					ac.start();

				
				}

				
			}
			else if(e.getSource().equals(pausebutton))
			{
				ac.stop();
				
			}
			else if(e.getSource().equals(stopbutton))
			{
				ac.stop();
				ac.reset();

				
			}
		
			
/************************************* EQ LISTENERS ********************************************************/
			else if(e.getActionCommand().equals("LPF"))
			{
			
				lpf.setFrequency(500);
				sp.update();
				lpf.start();
				lpfButton.setBackground(jmuPurple);
				this.repaint();
				
				super.repaint();
			}
			else if(e.getActionCommand().equals("HPF"))
			{
				
			}
			else
			{
				
			}
		}
	
			
	public JComboBox buildDropDown(){
		String audioFiles[] ={"General Grevious.wav", "underminers-drumloop.wav","VeilofShadows-Outro.wav"};
		contentManager = new SampleManager();
		for(int i = 0; i < audioFiles.length;i++){
			try {
				contentManager.addToGroup("audio/"+audioFiles[i], new Sample("audio/"+audioFiles[i]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JComboBox fileSelect = new JComboBox(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
	}

	public void updateLevel(float level){
		
		g.setGain(level);
		
	}

	
	
	public JPanel eqControlPanel(){
		eqPanel = new JPanel();
	    	
		eqPanel.setBackground(jmuGold);
		eqPanel.setLayout(null);
		eqPanel.setBounds(0,300,600,300);
		eqPanel.setBorder(new LineBorder(jmuPurple,5));
		
		finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image eqIcon= imgFactory.createBufferedImage("../img/eqText.png", 2);
    	
    	Icon eqImg = new ImageIcon(eqIcon.getScaledInstance(100,70, 0));
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(null);
		sliderPanel.setBounds(20,30,550,235);
   
    	JLabel panelTitle = new JLabel(eqImg);
    	panelTitle.setBounds(((int)sliderPanel.getBounds().getCenterX()) - 75,15,110,80);
    	
		s250 = new JSlider(JSlider.VERTICAL,-12,12,0);
		s250.setMajorTickSpacing(3);
		s250.setPaintLabels(true);
		s250.setPaintTicks(true);
		s250.setSnapToTicks(true);
		s250.addChangeListener(this);
		s250.setBounds(40,50,50,170);
		s250.setForeground(jmuPurple);
		
		s800 = new JSlider(JSlider.VERTICAL,-12,12,0);
		s800.setMajorTickSpacing(3);
		s800.setPaintLabels(true);
		s800.setPaintTicks(true);
		s800.setSnapToTicks(true);
		s800.addChangeListener(this);
		s800.setForeground(jmuPurple);
		s800.setBounds(120,50,50,170);
		
		s25= new JSlider(JSlider.VERTICAL,-12,12,0);
		s25.setMajorTickSpacing(3);
		s25.setPaintLabels(true);
		s25.setPaintTicks(true);
		s25.setSnapToTicks(true);
		s25.addChangeListener(this);
		s25.setForeground(jmuPurple);
		s25.setBounds(200,50,50,170);
		
		s8= new JSlider(JSlider.VERTICAL,-12,12,0);
		s8.setMajorTickSpacing(3);
		s8.setPaintLabels(true);
		s8.setPaintTicks(true);
		s8.setSnapToTicks(true);
		s8.addChangeListener(this);
		s8.setForeground(jmuPurple);
		s8.setBounds(270,50,50,170);
		
		lpfButton = new JButton("LPF");
		hpfButton = new JButton("HPF");
		lpfButton.addActionListener(this);
		hpfButton.addActionListener(this);
		lpfButton.setBounds(350,20,100,30);
		hpfButton.setBounds(350,80,100,30);
		
		eqPanel.add(panelTitle);
		sliderPanel.add(s250);
		sliderPanel.add(s800);
		sliderPanel.add(s25);
		sliderPanel.add(s8);
		sliderPanel.add(lpfButton);
		sliderPanel.add(hpfButton);
		eqPanel.add(sliderPanel);
		
		return eqPanel;
	}
	




	
	
	
	
	
	
	
	
	

}
