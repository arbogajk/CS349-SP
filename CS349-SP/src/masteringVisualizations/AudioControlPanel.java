package masteringVisualizations;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import auditory.sampled.BoomBox;
import auditory.sampled.BufferedSound;
import auditory.sampled.BufferedSoundFactory;
import auditory.sampled.FIRFilter;
import auditory.sampled.FIRFilterOp;
import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;

public class AudioControlPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	private Color jmuPurple;
	private JButton playbutton,pausebutton,stopbutton;
	private JComboBox files;
	private int position=0;
	private boolean isPlaying = false;
	private Clip clip; 
	
	public AudioControlPanel(){
		super();
		setLayout(null);
		
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
    	
    	this.setBounds(0, 550, 500, 150);
    	files = buildDropDown();
    	files.setBounds((int)(this.getBounds().getWidth()/2)-((int)files.getBounds().getWidth()/2), 5, 200, 100);
    	
    	playbutton = new JButton(play);
    	playbutton.setBounds(70,(int)files.getBounds().getMaxY() - 20,60,60);
    	
    	playbutton.addActionListener(this);
    	
    	pausebutton = new JButton(pause);
    	pausebutton.setBounds(135,(int)files.getBounds().getMaxY() - 20,60,60);
    	pausebutton.addActionListener(this);
    	playbutton.setBackground(jmuPurple);
    	
    	stopbutton = new JButton(stop);
    	stopbutton.setBounds(200,(int)files.getBounds().getMaxY() - 20,60,60);
    	stopbutton.addActionListener(this);
    	try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	add(files);
    	add(playbutton);
    	add(pausebutton);
    	add(stopbutton);
    	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String	audioFile = files.getSelectedItem().toString();
		if(clip.isActive() && e.getSource().equals(files))
			clip.stop();
		InputStream is = finder.findInputStream("../audio_src/" + audioFile);
		BufferedInputStream bis = new BufferedInputStream(is);
		
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
			BufferedSoundFactory factory = new BufferedSoundFactory(finder);
			//BufferedSound sound = factory.createBufferedSound(ais);
			if(!clip.isActive())
				clip = AudioSystem.getClip();
			
			
			if(e.getSource().equals(playbutton))
			{
				clip.open(ais);
				clip.setFramePosition(position);
				clip.start();

			}
			else if(e.getSource().equals(pausebutton))
			{
				position = clip.getFramePosition();
				clip.stop();
			}
			else if(e.getSource().equals(stopbutton)){
				if(clip.isRunning()){
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
		String audioFiles[] ={"underminers-drumloop.wav","VeilofShadows-Outro.wav"};
		JComboBox fileSelect = new JComboBox(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
		
	}
}
