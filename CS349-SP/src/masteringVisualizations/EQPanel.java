package masteringVisualizations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.ResourceFinder;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.OnePoleFilter;
import net.beadsproject.beads.ugens.SamplePlayer;
import visual.statik.sampled.ImageFactory;

public class EQPanel extends JPanel implements ActionListener, ChangeListener {
private ResourceFinder finder;
	
	private Color jmuPurple = new Color(69,0,132);
	private Color jmuGold = new Color(203,182,119);
	private Font font = new Font("Times New Roman",Font.BOLD,16);
	private Font fontVolume = new Font("Times New Roman",Font.BOLD,16);
	private Font title = new Font("Times New Roman",Font.BOLD,18);
	private static int lpfOn =0;
	private static int hpfOn =0;
	private JSlider s250,s800,s25,s8;
	private static JToggleButton lpfButton, hpfButton;
	private SpinnerNumberModel filterFreqLPF, filterFreqHPF;
	private JSpinner lpfSpinner, hpfSpinner;
	private static OnePoleFilter lpf,hpf;	
	private JPanel sliderPanel,eqPanel;
	private static Gain lpfGain, hpfGain;	
	
	public static AudioContext ac;
	public static SamplePlayer sp;
	
	private final int WIDTH;
	private final int HEIGHT;
	
	public EQPanel(int width, int height)
	{
		super();
		WIDTH = width;
		HEIGHT = height;
		
		setBackground(jmuGold);
		setLayout(null);
		setBounds(0,0,WIDTH,HEIGHT/2);
		setBorder(new LineBorder(jmuPurple,5));
		
		buildEQSliders();
		
		ac = AudioControlPanel.getAC();
	
		sp = AudioControlPanel.getSP();
		
		initFilters();
				
		add(eqPanel);

	}
	
	public void buildEQSliders()
	{
		eqPanel = new JPanel();
		eqPanel.setBackground(jmuGold);
		eqPanel.setLayout(null);
		eqPanel.setBounds(0,0,WIDTH,HEIGHT);
		eqPanel.setBorder(new LineBorder(jmuPurple,5));
		
		finder = ResourceFinder.createInstance(this);
    	ImageFactory imgFactory = new ImageFactory(finder);
    	Image eqIcon= imgFactory.createBufferedImage("/img/eqText.png", 2);
    	
    	Icon eqImg = new ImageIcon(eqIcon.getScaledInstance(100,70, 0));
		
		sliderPanel = new JPanel();
		sliderPanel.setLayout(null);
		sliderPanel.setBounds(20,20,WIDTH - 40, HEIGHT -40);
   
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
		s250.setBounds(5,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),(int)(sliderPanel.getBounds().getMaxY()/1.5));
		s250.setForeground(jmuPurple);
		f250.setBounds((int)s250.getBounds().getMinX() + 6,(int)s250.getBounds().getMaxY() + 6,40,20);
		
		s800 = new JSlider(JSlider.VERTICAL,-12,12,0);
		s800.setMajorTickSpacing(3);
		s800.setPaintLabels(true);
		s800.setPaintTicks(true);
		s800.setSnapToTicks(true);
		s800.addChangeListener(this);
		s800.setForeground(jmuPurple);
		s800.setBounds((int)s250.getBounds().getMaxX() + 20,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
				(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f800.setBounds((int)s800.getBounds().getMinX() + 6,(int)s800.getBounds().getMaxY() + 1,50,30);
		
		s25= new JSlider(JSlider.VERTICAL,-12,12,0);
		s25.setMajorTickSpacing(3);
		s25.setPaintLabels(true);
		s25.setPaintTicks(true);
		s25.setSnapToTicks(true);
		s25.addChangeListener(this);
		s25.setForeground(jmuPurple);
		s25.setBounds((int)s800.getBounds().getMaxX() + 20,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
		(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f25.setBounds((int)s25.getBounds().getMinX() + 6,(int)s25.getBounds().getMaxY() + 1,50,30);
		
		s8= new JSlider(JSlider.VERTICAL,-12,12,0);
		s8.setMajorTickSpacing(3);
		s8.setPaintLabels(true);
		s8.setPaintTicks(true);
		s8.setSnapToTicks(true);
		s8.addChangeListener(this);
		s8.setForeground(jmuPurple);
		s8.setBounds((int)s25.getBounds().getMaxX() +20,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
				(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f8.setBounds((int)s8.getBounds().getMinX() + 6,(int)s8.getBounds().getMaxY() + 1,50,30);
		
		lpfButton = new JToggleButton("LPF");
		hpfButton = new JToggleButton("HPF");
		lpfButton.addActionListener(this);
		hpfButton.addActionListener(this);
		
		lpfButton.setBounds(WIDTH - 200,20,60,30);
		hpfButton.setBounds(WIDTH - 200,80,60,30);
		
		hpfSpinner = new JSpinner();
		lpfSpinner = new JSpinner();
		filterFreqLPF = new SpinnerNumberModel(100,20, 1000,10);
		filterFreqHPF = new SpinnerNumberModel(16000,1000,20000,100);
	
		lpfSpinner.setModel(filterFreqLPF);
		lpfSpinner.setBounds((int)lpfButton.getBounds().getMaxX() + 6, 
				(int)lpfButton.getBounds().getY(),70,30);
		
		hpfSpinner.setModel(filterFreqHPF);
		hpfSpinner.setBounds((int)hpfButton.getBounds().getMaxX() + 6,
				(int)hpfButton.getBounds().getY(),70,30);
		
		
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
		eqPanel.add(panelTitle);
		eqPanel.add(sliderPanel);
	}
	
	
	public void initFilters()
	{
		lpfGain = new Gain(ac,2,0.0f);
		hpfGain = new Gain(ac,2,0.0f);
		lpf = new OnePoleFilter(ac,0.0f);
		hpf = new OnePoleFilter(ac,0.0f);
		lpf.addInput(lpfGain);
		hpf.addInput(hpfGain);
		lpf.addInput(sp);
		hpf.addInput(sp);
		ac.out.addInput(lpf);
		ac.out.addInput(hpf);
	}
	
	public static void resetFilters(){
		lpf.setFrequency(0.0f);
		hpf.setFrequency(0.0f);
		lpf.setValue(0.0f);
		hpf.setValue(0.0f);
		lpfOn = 0;
		hpfOn = 0;
		lpfButton.setSelected(false);
		hpfButton.setSelected(false);
	
	}
	
	
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(lpfButton))
		{
		
			if(lpfOn == 0){
				System.out.println("armed");
				float frequency = filterFreqLPF.getNumber().floatValue();
				lpf.setFrequency(frequency);
			
				lpfGain.setGain(10.0f);
				lpfGain.start();
				lpfOn = 1;
			
			}
			else
			{
				lpf.setFrequency(0.0f);
				lpfGain.setGain(0.0f);
				lpfGain.start();
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
				
				hpfGain.setGain(-10.0f);
					
			
				hpfGain.start();
			
				hpfOn = 1;
			}
			else{
				hpf.setFrequency(0.0f);
				hpfGain.setGain(0.0f);
		
				hpfGain.start();
				hpfOn = 0;
			}
			sp.start();

		}
		
	}
}
