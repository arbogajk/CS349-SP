package masteringVisualizations;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	public EQControlPanel(){
		super();
		jmuPurple = new Color(69,0,132);
	 	jmuGold = new Color(203,182,119);
    	setBackground(jmuGold);
		setLayout(null);
		setBounds(0,250,500,300);
		setBorder(new LineBorder(jmuPurple,5));
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(null);
		sliderPanel.setBounds(20,20,460,235);
   
    	JLabel panelTitle = new JLabel("EQ");
    	panelTitle.setFont(title);
    	panelTitle.setForeground(jmuPurple);
    	panelTitle.setBounds(250,10,100,50);
    	
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
		// TODO Auto-generated method stub
		
	}






	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
