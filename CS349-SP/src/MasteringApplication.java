import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import javax.swing.*;
	
import app.*;
	

public class   MasteringApplication
       extends MultimediaApplication
{
	public static double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    /**
     * The entry-point of the application
     *
     * @param args    The command-line arguments
     */
    public static void main(String[] args) throws Exception
    {
       WIDTH = WIDTH / 3;
       HEIGHT = HEIGHT - 100;
       SwingUtilities.invokeAndWait(
          new MasteringApplication(args, (int)(WIDTH), (int)HEIGHT));
    }
	
    /**
     * Explicit Value Constructor
     *
     * @param args    The command-line arguments
     * @param width   The width of the content (in pixels)
     * @param height  The height of the content (in pixels)
     */
    public MasteringApplication(String[] args,
                                    int width, int height)
    {
       super(args, new MasteringApp(), width, height);
    }
    
    public static double getWidth(){
    	return WIDTH;
    }
    
    public static double getHeight(){
    	return HEIGHT;
    }
}
