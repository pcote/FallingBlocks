package com.philipcote.fallingblocks;
import java.awt.Graphics;
import java.awt.image.*;
import com.philipcote.gamelib.*;
/* TetrisBlock is the abstract parent to all 7 types of Tetris Blocks.  
 * */
public abstract class TetrisBlock extends Sprite {
	
	protected int leadX = 100, leadY = 40;
	protected SubBlock[] subBlocks;
	protected Direction currentDirection = Direction.RIGHT;
	private final int NUMBER_OF_SUBBLOCKS = 4;
	
	public TetrisBlock(){}
	
	public TetrisBlock( BufferedImage im ){
		// initialize the subblocks
		subBlocks = new SubBlock[ NUMBER_OF_SUBBLOCKS ];
		for( int i = 0; i < subBlocks.length; i++ ){
			subBlocks[ i ] = new SubBlock( im );
		}
	}
	
	public void setPosition( int x, int y ){
		leadX = x;
		leadY = y;
		reinitializeSubBlocks(); 
	}
	
	
	
	/*Called whenever the Tetris block is moved.
	 * */
	private void reinitializeSubBlocks() {
		if( currentDirection == Direction.LEFT )
			orientLeft();
		else if( currentDirection == Direction.RIGHT )
			orientRight();
		else if( currentDirection == Direction.UP )
			orientUp();
		else if( currentDirection == Direction.DOWN )
			orientDown();
	}

	/****************move methods*******************/
	/* These methods work by changing the position of the lead block
	 * and then reinitalizing the array of blocks so the sub blocks fall into
	 * line with respect to where the leadX and leadY are.
	 * */
	protected final void moveLeft(){
		leadX -= 20;
		reinitializeSubBlocks();
	}
	
	protected final void moveRight(){
		leadX += 20;
		reinitializeSubBlocks();
	}
	
	protected final void moveDown(){
		leadY += 20;
		reinitializeSubBlocks();
	}
	
	protected final void moveUp(){
		leadY -= 20;
		reinitializeSubBlocks();
	}
	
	/*******************end of move methods*******************************/
	
	public void draw(Graphics g ){
		for( SubBlock subBlock: subBlocks )
			subBlock.draw( g );
	} 

	// Rotate the block in a counter-clockwise position
	public final void rotate(){
		// set the direction first off
		if( currentDirection == Direction.RIGHT ){
			currentDirection = Direction.UP;
		}
		else if( currentDirection == Direction.UP ){
			currentDirection = Direction.LEFT;
		}
		else if( currentDirection == Direction.LEFT ){
			currentDirection = Direction.DOWN;
		}
		else if( currentDirection == Direction.DOWN ){
			currentDirection = Direction.RIGHT;
		}
		
		// now do the actual rotation (rearrange the subblocks ).
		if( currentDirection == Direction.RIGHT ){
			orientRight();
		}
		else if( currentDirection == Direction.UP ){
			orientUp();
		}
		else if( currentDirection == Direction.LEFT ){
			orientLeft();
		}
		else if( currentDirection == Direction.DOWN ){
			orientDown();
		}
	}
	
	@Override
	public void move() {/* mandatory override */}
	
	/* Orient methods dictate how the blocks look when they face up, down, ect.  How
	 * they look in those positions depends on the child class in question.*/
	protected abstract void orientLeft();
	protected abstract void orientRight();
	protected abstract void orientUp();
	protected abstract void orientDown();	
	public void die(){/*mandatory override*/}

	public SubBlock[] getSubBlocks() {
		return subBlocks;
	}
	
	/*
	 * The 3 methods below are used to determine if any collision detection is 
	 * taking place.
	 * */
	public SubBlock getLowestSubBlock(){
		SubBlock lowestSubBlock = subBlocks[ 0 ];
		for( SubBlock subBlock: subBlocks ){
			if( subBlock.getYPos() > lowestSubBlock.getYPos() ){
				lowestSubBlock = subBlock;
			}
		}
		
		return lowestSubBlock;
	}
	
	public SubBlock getRightMostSubBlock(){
		SubBlock rightMostSubBlock = subBlocks[ 0 ];
		for( SubBlock subBlock: subBlocks ){
			if( subBlock.getXPos() > rightMostSubBlock.getXPos() ){
				rightMostSubBlock = subBlock;
			}
		}
		
		return rightMostSubBlock;
	}
	
	public SubBlock getLeftMostSubBlock(){
		SubBlock leftMostSubBlock = subBlocks[ 0 ];
		for( SubBlock subBlock: subBlocks ){
			if( subBlock.getXPos() < leftMostSubBlock.getXPos() ){
				leftMostSubBlock = subBlock;
			}
		}
		return leftMostSubBlock;
	}

	
}
