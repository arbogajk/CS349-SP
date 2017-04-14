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
import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.featureextractors.SpectralPeaks;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
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
	private SpinnerNumberModel filterFreqLPF, filterFreqHPF;
	private JSpinner lpfSpinner, hpfSpinner;
	private JPanel sliderPanel, eqPanel;
	private BiquadFilter peakFilter250, peakFilter800, peakFilter25, peakFilter8;
	
	/**These are static so the reset method can be called in the AudioControlPanel class */
	private static OnePoleFilter lpf,hpf;
	private static Gain mainGain = AudioControlPanel.getMainGain();
	private static JSlider s250,s800,s25,s8;
	private static JToggleButton lpfButton, hpfButton,highShelfButton, lowShelfButton;
	private static int lpfOn =0;
	private static int hpfOn =0;
	private static int hShelfOn = 0;
	private static int lShelfOn = 0;
	private static Gain lpfGain, hpfGain, gain250, gain800, gain25, gain8, highShelfGain, lowShelfGain;	
	private static Glide lpfGlide, hpfGlide;
	private static BiquadFilter lowShelf, highShelf;
		


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
    	
		s250 = new JSlider(JSlider.VERTICAL,-9,9,0);
		s250.setMajorTickSpacing(3);
		s250.setPaintLabels(true);
		s250.setPaintTicks(true);
		s250.setSnapToTicks(true);
		s250.addChangeListener(this);
		s250.setBounds(5,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
				(int)(sliderPanel.getBounds().getMaxY()/1.5));
		s250.setForeground(jmuPurple);
		f250.setBounds((int)s250.getBounds().getMinX() + 6,(int)s250.getBounds().getMaxY() + 6,40,20);
		
		s800 = new JSlider(JSlider.VERTICAL,-9,9,0);
		s800.setMajorTickSpacing(3);
		s800.setPaintLabels(true);
		s800.setPaintTicks(true);
		s800.setSnapToTicks(true);
		s800.addChangeListener(this);
		s800.setForeground(jmuPurple);
		s800.setBounds((int)s250.getBounds().getMaxX() + 20,(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
				(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f800.setBounds((int)s800.getBounds().getMinX() + 6,(int)s800.getBounds().getMaxY() + 1,50,30);
		
		s25= new JSlider(JSlider.VERTICAL,-9,9,0);
		s25.setMajorTickSpacing(3);
		s25.setPaintLabels(true);
		s25.setPaintTicks(true);
		s25.setSnapToTicks(true);
		s25.addChangeListener(this);
		s25.setForeground(jmuPurple);
		s25.setBounds((int)s800.getBounds().getMaxX() + 20,
				(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
		(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f25.setBounds((int)s25.getBounds().getMinX() + 6,
				(int)s25.getBounds().getMaxY() + 1,50,30);
		
		s8= new JSlider(JSlider.VERTICAL,-9,9,0);
		s8.setMajorTickSpacing(3);
		s8.setPaintLabels(true);
		s8.setPaintTicks(true);
		s8.setSnapToTicks(true);
		s8.addChangeListener(this);
		s8.setForeground(jmuPurple);
		s8.setBounds((int)s25.getBounds().getMaxX() +20,
				(int)sliderPanel.getBounds().getMinY() + 30,(int)(WIDTH *0.07),
				(int)(sliderPanel.getBounds().getMaxY()/1.5));
		f8.setBounds((int)s8.getBounds().getMinX() + 6,
				(int)s8.getBounds().getMaxY() + 1,50,30);
		
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
		
		highShelfButton = new JToggleButton("High Shelf");
		lowShelfButton = new JToggleButton("Low Shelf");
		
		highShelfButton.setBounds((int)hpfButton.getBounds().getX(), (int)hpfButton.getBounds().getMaxY() + 6, 85,30);
		lowShelfButton.setBounds((int)hpfButton.getBounds().getX(), (int)highShelfButton.getBounds().getMaxY() + 6, 85,30);
		
		
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
		sliderPanel.add(highShelfButton);
		sliderPanel.add(lowShelfButton);
		sliderPanel.add(lpfSpinner);
		sliderPanel.add(hpfSpinner);
		eqPanel.add(panelTitle);
		eqPanel.add(sliderPanel);
	}
	
	
	public void initFilters()
	{
		
		/* Low Pass High pass*/
		lpf = new OnePoleFilter(ac,0.0f);
		hpf = new OnePoleFilter(ac,0.0f);
		
		lowShelf = new BiquadFilter(ac,2,BiquadFilter.LOW_SHELF);
		lowShelf.setFrequency(80.0f);
		highShelf = new BiquadFilter(ac, 2, BiquadFilter.HIGH_SHELF);
		highShelf.setFrequency(9000.0f);
		
		//add the sample player as an input to the filters
		lpf.addInput(sp);
		hpf.addInput(sp);
		lowShelf.addInput(sp);
		highShelf.addInput(sp);
		
		lpfGlide = new Glide(ac, 0.1f, 20);
		hpfGlide = new Glide(ac, 0.1f, 20);
		lpfGain = new Gain(ac, 2, lpfGlide);
		hpfGain = new Gain(ac, 2, hpfGlide);
		lpfGain.addInput(lpf);
		hpfGain.addInput(hpf);
		
		lowShelfGain = new Gain(ac, 2, 0.0f);
		highShelfGain = new Gain(ac, 2, 0.0f);
		
		lowShelfGain.addInput(lowShelf);
		highShelfGain.addInput(highShelf);
		
		ac.out.addInput(lowShelfGain);
		ac.out.addInput(highShelfGain);
		ac.out.addInput(lpfGain);
		ac.out.addInput(hpfGain);
		
		/* Peak Filters */
		peakFilter250 = new BiquadFilter(ac, 2, BiquadFilter.PEAKING_EQ);
		peakFilter250.setFrequency(250.0f);
		peakFilter250.setQ(1.0f);
		peakFilter250.setGain(0.0f);
		peakFilter250.addInput(sp);	
	
		gain250 = new Gain(ac,2,0.0f);
		gain250.addInput(peakFilter250);
		
		//800 Hz ************************
		peakFilter800 = new BiquadFilter(ac, 2, BiquadFilter.PEAKING_EQ);
		peakFilter800.setFrequency(800.0f);
		peakFilter800.setQ(1.0f);
		peakFilter800.setGain(0.0f);
		peakFilter800.addInput(sp);
	
		gain800 = new Gain(ac,2,0.0f);
		gain800.addInput(peakFilter800);
		
		//2.5kHz ********************************
		peakFilter25 = new BiquadFilter(ac, 2, BiquadFilter.PEAKING_EQ);
		peakFilter25.setFrequency(800.0f);
		peakFilter25.setQ(1.0f);
		peakFilter25.setGain(0.0f);
		peakFilter25.addInput(sp);
	
		gain25 = new Gain(ac,2,0.0f);
		gain25.addInput(peakFilter25);
		
		// 8 kHz ********************************
		peakFilter8 = new BiquadFilter(ac, 2, BiquadFilter.PEAKING_EQ);
		peakFilter8.setFrequency(800.0f);
		peakFilter8.setQ(1.0f);
		peakFilter8.setGain(0.0f);
		peakFilter8.addInput(sp);
	
		gain8 = new Gain(ac,2,0.0f);
		gain8.addInput(peakFilter8);
		
		/*** Add the Gains for the frequencies to the audio context output ***/
		ac.out.addInput(gain800);
		ac.out.addInput(gain250);
		ac.out.addInput(gain25);
		ac.out.addInput(gain8);

	}
	
	public static void resetFilters()
	{
		lpf.setFrequency(0.0f);
		hpf.setFrequency(0.0f);
		lpf.setValue(0.0f);
		hpf.setValue(0.0f);
		lpfGain.setValue(0.0f);
		hpfGain.setValue(0.0f);
		lpfOn = 0;
		hpfOn = 0;
		lpfButton.setSelected(false);
		hpfButton.setSelected(false);
		s250.setValue(0);
		s800.setValue(0);
		s25.setValue(0);
		s8.setValue(0);
		gain250.setGain(0.0f);
		gain800.setGain(0.0f);
		gain25.setGain(0.0f);
		gain8.setGain(0.0f);
	}
	
	
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{

			if(e.getSource().equals(s250))
			{
				
					peakFilter250.setGain(s250.getValue());
					gain250.setGain(s250.getValue() * 0.5f);
					System.out.println("250Gain is " + gain250.getGain());
	
			
			}
			else if(e.getSource().equals(s800)){
				peakFilter800.setGain(s800.getValue());
				gain800.setGain(s800.getValue() * 0.5f);
			}
			else if(e.getSource().equals(s25)){
				peakFilter25.setGain(s25.getValue() );
				gain25.setGain(s25.getValue() * 0.5f);
			}
			else if(e.getSource().equals(s8)){
				peakFilter8.setGain(s8.getValue());
				gain8.setGain(s8.getValue() * 0.5f);
			}
		

		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(lpfButton))
		{
		
			if(lpfOn == 0){
				System.out.println("armed");
				float frequency = filterFreqLPF.getNumber().floatValue();
				lpf.setFrequency(frequency);
			
				lpfGlide.setValue(3.0f);
			
				lpfOn = 1;
			}
			else
			{
				lpf.setFrequency(0.0f);
				lpfGlide.setValue(0.0f);
		
				lpfOn = 0;
			}

		}
		else if(e.getSource().equals(hpfButton))
		{
			if(hpfOn == 0)
			{
				System.out.println("armed");
				float frequency = filterFreqHPF.getNumber().floatValue();
				hpf.setFrequency(frequency);
				hpfGlide.setValue(1.0f);
				hpfOn = 1;
			}
			else
			{
				hpf.setFrequency(0.0f);
				hpfGlide.setValue(0.0f);
			
				hpfOn = 0;
			}
		}
		else if(e.getSource().equals(lowShelfButton))
		{
			if(lShelfOn == 0){
				lowShelfGain.setGain(4.0f);
				lShelfOn = 1;
			}
			else{
				lowShelfGain.setGain(0.0f);
				lShelfOn = 0;
			}
		}
		else if(e.getSource().equals(highShelfButton))
		{
			if(hShelfOn == 0){
				highShelfGain.setGain(4.0f);
				hShelfOn = 1;
			}
			else{				
				highShelfGain.setGain(0.0f);
				hShelfOn = 0;
			}
		}
	}
}
