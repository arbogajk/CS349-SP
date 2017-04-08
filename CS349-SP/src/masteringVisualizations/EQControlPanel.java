package masteringVisualizations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jtransforms.fft.DoubleFFT_1D;

import auditory.sampled.BufferedSound;
import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;


public class EQControlPanel extends JPanel implements ChangeListener, ActionListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider s250,s800,s25,s8;
	private Color jmuPurple,jmuGold;
	private Font title = new Font("Arial",Font.BOLD,18);
	private JButton lpfButton,hpfButton;
	private ResourceFinder finder;
	
	
	public EQControlPanel(){
		super();
	
		jmuPurple = new Color(69,0,132);
	 	jmuGold = new Color(203,182,119);
    	
		setBackground(jmuGold);
		setLayout(null);
		setBounds(0,300,600,300);
		setBorder(new LineBorder(jmuPurple,5));
		
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
		
		add(panelTitle);
		sliderPanel.add(s250);
		sliderPanel.add(s800);
		sliderPanel.add(s25);
		sliderPanel.add(s8);
		sliderPanel.add(lpfButton);
		sliderPanel.add(hpfButton);
		add(sliderPanel);
	}
	
	
	
	
	
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Iterator<double[]> it = AudioControlPanel.getBufferedSound().getSignals();
	
		if(e.getActionCommand().equals("LPF"))
		{
			while(it.hasNext()){
				
			}
			  
		}
		else if(e.getActionCommand().equals("HPF"))
		{
			
		}
	}

}
