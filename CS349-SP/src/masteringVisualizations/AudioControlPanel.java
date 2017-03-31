package masteringVisualizations;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;

public class AudioControlPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourceFinder finder;
	private Color jmuPurple;
	public AudioControlPanel(){
		super();
		setLayout(null);
		JButton playbutton,pausebutton,stopbutton;
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
    	JComboBox files = buildDropDown();
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
    	
    	
    	add(files);
    	add(playbutton);
    	add(pausebutton);
    	add(stopbutton);
    	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("test");
	}
			
	public JComboBox buildDropDown(){
		String audioFiles[] ={"underminers-drumloop.wav","VeilofShadows-Outro.wave"};
		JComboBox fileSelect = new JComboBox(audioFiles);
		fileSelect.addActionListener(this);
		return fileSelect;
		
	}
}
