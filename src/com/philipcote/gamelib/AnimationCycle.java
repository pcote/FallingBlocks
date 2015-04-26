package com.philipcote.gamelib;

/* Class whose primary concern is maintaining a consistent frame rate.
 To give credit where credit is due, much of this code is just a refactored 
 rendition of code found in the book "Killer Game Programming in Java" by 
 Andrew Davidson.  However, I split the code up so that issues concerning frame-rate
 and issues concerning game logic and rendering are dealt with seperately. 
 
 This is a threaded class. Use a thread or thread pool to run this. 
 */
public class AnimationCycle implements Runnable{
	 private static final int NO_DELAYS_PER_YIELD = 16;
	 private static int MAX_FRAME_SKIPS = 5;   // was 2;
	 private boolean running = false;   // used to stop the animation thread
	 //protected long period = 20L;                // period between drawing in _nanosecs_
	 private GamePanel gamePanel;
	 private long period;
	 private long beforeTime;
	 private long afterTime;
	 private long timeDiff;
	 private long sleepTime;
	 private long excess = 0L;
	 private long overSleepTime = 0L;
	 private int noDelays = 0;
	 
	 // use the frame rate to calculate the frame rate.
	 // GamePanel is needed for calls to update, render, and paint
	 // that need to happen within the animation cycle itself.
	 public AnimationCycle( GamePanel gp, long frameRate ){
		 gamePanel = gp;
		 period = (1000 / frameRate ) * 1000000;
	 }

	 public void run(){
			running = true;

			while( running ) {
			  beforeTime = System.nanoTime();
			  gamePanel.gameUpdate();
			  gamePanel.gameRender();
			  gamePanel.paintScreen();
		      afterTime = System.nanoTime();
		      sleepAppropriately();
		      skipNecessaryFrames();
			}

		    System.exit(0);   // so window disappears
	 }

	 // Determine how long, if at all, sleep mode should last.  Then sleep.
	 private void sleepAppropriately() {
		timeDiff = afterTime - beforeTime;
		  sleepTime = ( period - timeDiff) - overSleepTime;  
		  
		  if (sleepTime > 0) {   // some time left in this cycle
		    try {
		      Thread.sleep(sleepTime/1000000L);  // nano -> ms
		    }
		    catch(InterruptedException ex){}
		    overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
		  }
		  else {    // sleepTime <= 0; the frame took longer than the period
		    excess -= sleepTime;  // store excess time value
		    overSleepTime = 0L;

		    if (++noDelays >= NO_DELAYS_PER_YIELD) {
		      Thread.yield();   // give another thread a chance to run
		      noDelays = 0;
		    }
		  }
	}

	private void skipNecessaryFrames() {
		/* If frame animation is taking too long, update the game state
		     without rendering it, to get the updates/sec nearer to
		     the required FPS. */
		  int skips = 0;      
		  while((excess > (period*1000000)) && (skips < MAX_FRAME_SKIPS)) {
		    excess -= period;
		    gamePanel.gameUpdate();    // update state but don't render
		    skips++;
		  }
	}
}
