package com.philipcote.gamelib;


// WormPanel.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The game's drawing surface. It shows:
     - the moving worm
     - the obstacles (blue boxes)
     - the current average FPS and UPS
*/

import javax.swing.*;
import java.awt.*;

//import com.sun.j3d.utils.timer.J3DTimer;

/*
 * GamePanel lifts some of the detail burdens off of the programmer's shoulders.
 * By extending this class, you get a handy panel that handles a healthy chunk
 * of the double buffering details for you.  
 * */
public abstract class GamePanel extends JPanel
{
	private static final long serialVersionUID = 2069961672568713289L;
    protected int width = 500;   // size of panel
    protected int height = 400; 
    protected long period = 20L; // period between drawing in _nanosecs_

   // off screen rendering
   protected Graphics dbg; 
   protected Image bufferImage = null;

   public void addNotify()
   // wait for the JPanel to be added to the JFrame before starting
   { 
	   super.addNotify();   // creates the peer
   }
  
  /*Overriden for updating the logic of the specific game.*/
  protected abstract void gameUpdate();
  
  /*The first half of the page flipping process.  
   * It's a good idea to make sure the back buffer and graphics context are
   * properly initialized before doing anything here.
   * TODO: Look into the possibility of putting that check into this method.
   * 
   * It would be nice if Java allowed the programmer to put in common code detail
   * in here and still require sub classers to override it.  Of course, this would
   * mean that the programmer would have to invoke super() but still....*/
  protected abstract void gameRender();

  
  /*Takes care of the other half of the double buffering process by 
   * flipping the rendered image on the back bufferImage to the gamepanel display*/
  protected void paintScreen()
  { 
    Graphics g;
    try {
      g = this.getGraphics();
      if ((g != null) && ( bufferImage != null))
        g.drawImage( bufferImage, 0, 0, null);
      g.dispose();
    }
    catch (Exception e)
    { System.out.println("Graphics context error: " + e);  }
  } // end of paintScreen()

  
protected Graphics initializeGraphicsContext() {
	Graphics g = null;
	
	if( this.bufferImage == null ){
		bufferImage = createImage( width, height );
	}
	
	if( bufferImage != null ){
		g = bufferImage.getGraphics();
	}
	return g;
}

}  // end of WormPanel class
