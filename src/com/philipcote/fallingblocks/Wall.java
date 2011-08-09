package com.philipcote.fallingblocks;
import java.awt.*;

import com.philipcote.gamelib.*;

public class Wall extends Sprite{
	
	public Wall(){
		this.width = 20;
		this.height = 340;
	}
	
	public Wall( int x, int y ){
		this();
		xPos = x;
		yPos = y;
	}

	@Override
	public void draw(Graphics g ) {
		Color oldColor = g.getColor();
		g.setColor( Color.red );
		g.fillRect( xPos, yPos, width, height );
		g.setColor( oldColor );
	}

	@Override
	public void move() {}

	@Override
	public void die() {}

	public boolean intersectsWith(TetrisBlock currentBlock) {
		SubBlock[] subBlocks = currentBlock.getSubBlocks();
		
		for( SubBlock subBlock: subBlocks ){
			if( subBlock.getXPos() == this.xPos )
				return true;
		}
		return false;
	}

}
