import java.io.*;
import java.net.*;
import javax.swing.*;
	
import app.*;
	

public class   MasteringApplication
       extends MultimediaApplication
{
    /**
     * The entry-point of the application
     *
     * @param args    The command-line arguments
     */
    public static void main(String[] args) throws Exception
    {
       SwingUtilities.invokeAndWait(
          new MasteringApplication(args, 500, 700));
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
}
