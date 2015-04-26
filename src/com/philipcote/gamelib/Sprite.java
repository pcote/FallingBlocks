package com.philipcote.gamelib;
import java.awt.*;

/**
 * @author      Phil Cote 
 */

// Parent class to the players and objects in a game.
public abstract class Sprite {
	
	protected int xPos = 0 ;
	protected int yPos = 0 ;
	protected int width = 0;
	protected int height = 0;
	protected Direction direction;
	protected boolean alive = true;
	public abstract void draw( Graphics g );
	public abstract void move();
	
	public int getXPos(){
		return xPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setPosition( int x, int y ){
			xPos = x;
			yPos = y;
	}
	
	
	// bounding box collision detection method
	public boolean collidesWith( Sprite otherSprite ){
		if( this.xPos > otherSprite.xPos + otherSprite.width )
			return false;
		else if( this.xPos + this.width < otherSprite.xPos )
			return false;
		else if( this.yPos + this.width < otherSprite.yPos )
			return false;
		else if( this.yPos > otherSprite.yPos + otherSprite.height )
			return false;
		
       return true;
	}
	
	
	public Direction getDirection() {
		return direction;
	}
	
	public abstract void die();
			
	public boolean isAlive(){
		return alive;
	}
	
	public void setAlive(boolean lifeState) {
		alive = lifeState;
	}
			
}
