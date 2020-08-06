import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

import java.util.Hashtable;
import java.lang.Double;

class WaveDraw extends JPanel {
    protected static final int points = 2000;
    
    public boolean show = false;
    
    private Color gridColor = new Color(200, 200, 200, 200);
    
    private Color strokeColor;
    
    protected static final Stroke GRAPH_STROKE = new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    
    protected static final int xpadding = 30, ypadding = 20, ylabelp = 10, xlabelp = 20;
	
	protected static final Stroke BORDER_STROKE = new BasicStroke(1f);
	
    protected double cycles = 0, amp = 0, XRange = 0.2;
    protected int YRange = 6;

    protected double[] sines = new double[points];

    protected int[] pts = new int[points];

    public WaveDraw(double n, double a, Color sc) {
    	strokeColor = sc;
    	amp = a;
    	for (int i = 0;i < points;i++)
    		pts[i] = i;
    }
    public void setCycles(double newCycles) {
    	if (cycles == 0.0)
    		show = false;
    	else
    		show = true;
        cycles = newCycles;
        for (int i = 0; i < points; i++) {
            double radians = (Math.PI / points) * i * 2 * cycles;
            sines[i] = Math.sin(radians) * amp;
        }
        repaint();
    }
    
    public void setVisible(boolean b) {
   		show = b;
   		repaint(); 
   	}
    
    public void setAmp(double newAmp) {
    	if (cycles == 0.0)
    		show = false;
    	else
    		show = true;
        amp = newAmp;
        for (int i = 0; i < points; i++) {
            double radians = (Math.PI / points) * i * 2 * cycles;
            sines[i] = Math.sin(radians) * amp;
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int maxWidth = getWidth() - xpadding - xlabelp;
        double hstep = (double) maxWidth / (double) points;
        int maxHeight = getHeight() - ypadding - ylabelp;
        
        g2.setColor(Color.WHITE);
        g2.fillRect(xpadding, ylabelp, maxWidth, maxHeight);
        
        g2.setColor(gridColor);
        for (int i = 1;i < 24;i++) {
        	g2.drawLine(xpadding + i * maxWidth / 24, ylabelp, xpadding + i * maxWidth / 24, ylabelp + maxHeight - 1);
       	}
       	for (int i = 1;i < 6;i++) {
        	g2.drawLine(xpadding, ylabelp + i * maxHeight / 6, xpadding + maxWidth - 1, ylabelp + i * maxHeight / 6);
       	}
        g2.setStroke(BORDER_STROKE);
        
        g2.drawLine(xpadding, ylabelp, xpadding + maxWidth - 1, ylabelp);
        g2.drawLine(xpadding + maxWidth - 1, ylabelp, xpadding + maxWidth - 1, ylabelp + maxHeight - 1);
        g2.setColor(Color.BLACK);
        g2.drawLine(xpadding, ylabelp, xpadding, ylabelp + maxHeight - 1);
        g2.drawLine(xpadding, ylabelp + maxHeight - 1, xpadding + maxWidth - 1, ylabelp + maxHeight - 1);
        
        for (int i = 0;i <= 6;i++) {
        	String str = "" + (YRange - i * YRange / 3);
        	FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(str);
        	g2.drawString(str, xpadding - labelWidth - 5, ylabelp + 5 + i * maxHeight / 6);
       	}
       	
       	for (int i = 0;i < 6;i++) {
       		g2.drawLine(xpadding , ylabelp + i * maxHeight / 6, xpadding + 5, ylabelp + i * maxHeight / 6);
       	}
       	
       	for (int i = 0;i <= 24;i += 2) {
        	String str = "" + (int)(i * XRange * 1000 / 24) / 1000.0;
        	if (i == 0)
        		str = "0";
        	FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(str);
        	g2.drawString(str, xpadding - labelWidth / 2 + i * maxWidth / 24, ylabelp + 15 + maxHeight);
       	}
       	
       	for (int i = 2;i <= 24;i += 2) {
       		g2.drawLine(xpadding + i * maxWidth / 24, ylabelp + maxHeight - 6, xpadding + i * maxWidth / 24, ylabelp + maxHeight - 1);
       	}
        
        for (int i = 0; i < points; i++)
            pts[i] = (int) (sines[i] * maxHeight * -5 / 12 + maxHeight / 2);
        g2.setColor(strokeColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 1; i < points && show; i++) {
            int x1 = xpadding + (int) ((i - 1) * hstep);
            int x2 = xpadding + (int) (i * hstep);
            int y1 = ylabelp + pts[i - 1];
            int y2 = ylabelp + pts[i];
            g2.drawLine(x1, y1, x2, y2);
        }
    }
}

class ModWaveDraw extends WaveDraw {
	public ModWaveDraw(WaveDraw a, WaveDraw b, Color sc) {
		super(1, 1, sc);
    	YRange = 30;
    	
    }
    
    public void refresh(WaveDraw a, WaveDraw b) {
    	if (a.show && b.show)
    		this.show = true;
    	else
    		this.show = false;
        for (int i = 0; i < points; i++) {
            double radians1 = (Math.PI / points) * i * 2 * a.cycles;
            double radians2 = (Math.PI / points) * i * 2 * b.cycles;
            sines[i] = Math.sin(radians1) * Math.sin(radians2) * a.amp * b.amp;
        }
        repaint();
    }
}

class MySlider extends JSlider {
	public MySlider(int a, int b, int c) {
		super(a, b, c);
		int sp = (b - a) / 5;
        setMajorTickSpacing(sp);
        setPaintTicks(true);
        setPaintLabels(true);
        Hashtable position = new Hashtable();
        for (int i = 0;i < 6;i++) {
        	position.put(i * sp, new JLabel(String.valueOf(i * sp / 10)));
        }
        setLabelTable(position);
        setBackground(new Color(225, 225, 225, 255));
	}
}

class ControlBlock extends JPanel {
	public ControlBlock(JSlider S, String str) {
		super(new GridLayout(3, 1));
		setBackground(new Color(225, 225, 225, 255));
		JLabel Title = new JLabel(str, SwingConstants.CENTER);
		Font f = new Font("", Font.BOLD, 15);
		Title.setFont(f);
		JTextField textInput = new JTextField();
		setBorder(new EmptyBorder(10, 8, 10, 8));
		add(Title);
		add(textInput);
		add(S);
		S.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	if (((JSlider)e.getSource()).getValue() != 0)
                	textInput.setText("" + ((JSlider)e.getSource()).getValue() / 10.0);
                else
                	textInput.setText("");
            }
        });
        textInput.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    double v = Double.parseDouble(textInput.getText());
        	    S.setValue((int)(v * 10));
        	}
    	});
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int maxWidth = getWidth();
        int maxHeight = getHeight();

        g2.setColor(new Color(100, 100, 100, 255));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(0, 0, 0, maxHeight - 1);
        g2.drawLine(0, 0, maxWidth - 1, 0);
        g2.drawLine(maxWidth - 1, 0, maxWidth - 1, maxHeight - 1);
        g2.drawLine(0, maxHeight - 1, maxWidth - 1, maxHeight - 1);
    }
}

public class VLab1 extends JPanel {
    private WaveDraw baseband = new WaveDraw(0, 0, new Color(255, 0, 0, 200));
    private WaveDraw carrier = new WaveDraw(0, 0, new Color(0, 0, 255, 200));
    private ModWaveDraw modform = new ModWaveDraw(baseband, carrier, new Color(175, 0, 175, 200));

    private JSlider adjustCycles1 = new MySlider(0, 1000, 0);
    private JSlider adjustCycles2 = new MySlider(0, 1000, 0);
    private JSlider adjustAmp1 = new MySlider(0, 50, 0);
    private JSlider adjustAmp2 = new MySlider(0, 50, 0);
    
    private JPanel control = new JPanel();
    private JPanel waveView = new JPanel();
    private JPanel baseView = new JPanel(new BorderLayout());
    private JPanel carrView = new JPanel(new BorderLayout());
    private JPanel modlView = new JPanel(new BorderLayout());
    
    private JLabel baseText = new JLabel("Baseband Signal Waveform", SwingConstants.CENTER);
    private JLabel carrText = new JLabel("Carrier Signal Waveform", SwingConstants.CENTER);
    private JLabel modlText = new JLabel("Modulated Signal Waveform", SwingConstants.CENTER);

    public VLab1() {
      super(new BorderLayout());

        control.setLayout(new GridLayout(4, 1, 0, 20));
        control.add(new ControlBlock(adjustCycles1, "Baseband Frequency (Hz)"));
        control.add(new ControlBlock(adjustAmp1, "Baseband Amplitude (V)"));
        control.add(new ControlBlock(adjustCycles2, "Carrier Frequency (Hz)"));
        control.add(new ControlBlock(adjustAmp2, "Carrier Amplitude (V)"));
        
        JButton resetBtn = new JButton("Reset");
        JButton exportBtn = new JButton("Export");
        
        JPanel ControlBox = new JPanel(new BorderLayout());
        JPanel ButtonBox = new JPanel(new GridLayout(1, 2, 10, 0));
        ButtonBox.add(resetBtn);
        ButtonBox.add(exportBtn);
        ButtonBox.setBorder(new EmptyBorder(18, 0, 0, 0));
        
        ControlBox.setBorder(new EmptyBorder(18, 25, 32, 25));//(18, 25, 32, 25)
        ControlBox.add(BorderLayout.CENTER, control);
        ControlBox.add(BorderLayout.SOUTH, ButtonBox);
		
		Font f = new Font("", Font.BOLD, 15);
		baseText.setFont(f);
		carrText.setFont(f);
		modlText.setFont(f);
		
        baseView.add(BorderLayout.CENTER, baseband);
        baseView.add(BorderLayout.SOUTH, baseText);
        
        carrView.add(BorderLayout.CENTER, carrier);
        carrView.add(BorderLayout.SOUTH, carrText);
        
        modlView.add(BorderLayout.CENTER, modform);
        modlView.add(BorderLayout.SOUTH, modlText);
		
        waveView.setLayout(new GridLayout(3, 1, 0, 10));
        waveView.setBorder(new EmptyBorder(18, 10, 18, 25));
        waveView.add(baseView);
        waveView.add(carrView);
        waveView.add(modlView);
		
		setBackground(new Color(225, 225, 225, 255));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		add(BorderLayout.WEST, ControlBox);
		add(BorderLayout.CENTER, waveView);

        adjustCycles1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	double c = (double)(((JSlider) e.getSource()).getValue()) / 50;
               	baseband.setCycles(c);
               	modform.refresh(baseband, carrier);
               	if (baseband.show) {
               		baseText.setText("Baseband Signal Waveform (freq = " + adjustCycles1.getValue() / 10.0 + "Hz, amp = " + adjustAmp1.getValue() / 10.0 + "V)");
               	}
               	else {
               		baseText.setText("Carrier Signal Waveform");
               	}
            }
        });
        
        adjustAmp1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	double c = (double)(((JSlider) e.getSource()).getValue()) / 50;
               	baseband.setAmp(c);
               	modform.refresh(baseband, carrier);
               	if (baseband.show) {
               		baseText.setText("Baseband Signal Waveform (freq = " + adjustCycles1.getValue() / 10.0 + "Hz, amp = " + adjustAmp1.getValue() / 10.0 + "V)");
               	}
               	else {
               		baseText.setText("Carrier Signal Waveform");
               	}
            }
        });

        adjustCycles2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
               	double c = (double)(((JSlider) e.getSource()).getValue()) / 50;
               	carrier.setCycles(c);
               	modform.refresh(baseband, carrier);
               	if (carrier.show) {
               		carrText.setText("Carrier Signal Waveform (freq = " + adjustCycles2.getValue() / 10.0 + "Hz, amp = " + adjustAmp2.getValue() / 10.0 + "V)");
               	}
               	else {
               		carrText.setText("Carrier Signal Waveform");
               	}
            }
        });
        
        adjustAmp2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	double c = (double)(((JSlider) e.getSource()).getValue()) / 50;
               	carrier.setAmp(c);
               	modform.refresh(baseband, carrier);
               	if (carrier.show) {
               		carrText.setText("Carrier Signal Waveform (freq = " + adjustCycles2.getValue() / 10.0 + "Hz, amp = " + adjustAmp2.getValue() / 10.0 + "V)");
               	}
               	else {
               		carrText.setText("Carrier Signal Waveform");
               	}
            }
        });
        
        resetBtn.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				baseband.setVisible(false);
  				carrier.setVisible(false);
  				modform.setVisible(false);
    			adjustCycles1.setValue(0);
    			adjustCycles2.setValue(0);
    			adjustAmp1.setValue(0);
    			adjustAmp2.setValue(0);
  			}
		});
		
		exportBtn.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				createImage(waveView);
  			}
		});
    }
    
    public void createImage(JPanel panel) {
    	int w = panel.getWidth();
    	int h = panel.getHeight();
    	BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    	Graphics2D g = bi.createGraphics();
    	panel.print(g);
    	g.dispose();
    	try {
             ImageIO.write(bi, "jpg", new File("./image.jpg"));
 
         } catch (IOException e) {
               System.out.println("Exception occured :" + e.getMessage());
         }
	}

    public static void main(String[] args) {
        JPanel p = new VLab1();
        JFrame frame = new JFrame("Generation of Amplitude Modulated Waveform (DSB-SC)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(p);
        frame.setSize(1408,792);
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
