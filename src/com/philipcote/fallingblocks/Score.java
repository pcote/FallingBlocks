package com.philipcote.fallingblocks;
import java.awt.*;

// Tracks and displays how many lines have been done completed so far.
public class Score {

	private int currentScore = 0;
	
	public void draw( Graphics g ){
		Color oldColor = g.getColor();
		g.setColor( Color.white );
		g.drawString( "Lines: " + currentScore,
					  300, 20 );
		g.setColor( oldColor );
	}
	
	public void raiseScore(){
		currentScore+= 1;
	}
	
	public int getCurrentScore(){
		return currentScore;
	}

}
