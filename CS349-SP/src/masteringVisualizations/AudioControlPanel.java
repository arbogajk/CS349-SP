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

import auditory.sampled.AddOp;
import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import auditory.sampled.FIRFilter;
import auditory.sampled.FIRFilterOp;
import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;

public class AudioControlPanel extends JPanel implements ActionListener,ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	private Color jmuPurple,jmuGold;
	
	private JButton playbutton,pausebutton,stopbutton;
	private JComboBox files;
	private int position=0;
	private boolean isPlaying = false;
	public static Clip clip; 
	private JSlider volumeSlider;
	private JProgressBar volume;
	private int currentAudioLevel = 0;
	private FloatControl gainControl;
	public static BufferedSound copySound;
	private Font font;
	
	public AudioControlPanel(){
		super();
		setLayout(null);
		setBounds(0, 520, 800, 250);
		
		font = new Font("Times New Roman",Font.BOLD,16);
		
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
 
    	try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	add(volumeSlider);
    	add(volumeLabel);
    	add(files);
    	add(volume);
    	add(playbutton);
    	add(pausebutton);
    	add(stopbutton);
    	
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			
			AudioInputStream stream;
			if(clip.isActive() && files.hasFocus())
			{
				clip.stop();
			}
			String	audioFile = files.getSelectedItem().toString();
		
			InputStream is = finder.findInputStream("../audio_src/" + audioFile);
		    BufferedSoundFactory soundFactory = new BufferedSoundFactory(finder);
			BufferedSound bs = soundFactory.createBufferedSound("../audio_src/"+audioFile);
			stream = AudioSystem.getAudioInputStream(is);
		
			
			
			if(e.getSource().equals(playbutton))
			{
				if(clip.isActive() == false)
				{
					clip = AudioSystem.getClip();
					clip.open(stream);
					clip.setFramePosition(position);
					clip.start();
				}
				
			}
			else if(e.getSource().equals(pausebutton))
			{
				if(clip.isActive() == true){
					position = clip.getFramePosition();
					clip.stop();
					
				}
				else
				{
					clip.setFramePosition(position);
					clip.start();
				}
				
			}
			else if(e.getSource().equals(stopbutton)){
				if(clip.isActive()){
					clip.stop();
					position = 0;
				
					
				}
				
			}
				
			
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnsupportedAudioFileException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// TODO Auto-generated method stub
		
		
	}
			
	public JComboBox buildDropDown(){
		String audioFiles[] ={"General Grevious.wav", "underminers-drumloop.wav","VeilofShadows-Outro.wav"};
		JComboBox fileSelect = new JComboBox(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
		
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		
		
	
		
		
		
		
	}
	public void updateLevel(float level){
		
		
		
	}
	

}
