package com.philipcote.fallingblocks;
import java.awt.*;
import java.awt.image.*;
import com.philipcote.gamelib.*;
public class SubBlock extends Sprite {

	private final int SUBBLOCK_SIDE_LENGTH = 20;
	private BufferedImage blockImage;
	
	public SubBlock(){
		width = SUBBLOCK_SIDE_LENGTH;
		height = SUBBLOCK_SIDE_LENGTH;
		xPos = 0;
		yPos = 0;
	}
	
	public SubBlock( BufferedImage blockImage ){
		this();
		this.blockImage = blockImage;
	}
	
	
	public void setPosition( int x, int y ){
		// NOTE: Intentional override of parent done to kill redundancy.
		xPos = x;
		yPos = y;
	}
	
	@Override
	public void draw(Graphics g ) {
		g.drawImage( blockImage, xPos, yPos, width, height, null );
	}
	
	public void move() {}
	@Override
	public void die() {}

}
