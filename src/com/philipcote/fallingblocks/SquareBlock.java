package com.philipcote.fallingblocks;
import java.awt.image.*;
//Implementation details of how a Square block should look when rotated.
public class SquareBlock extends TetrisBlock {

	public SquareBlock( BufferedImage im ){
		super( im );
		squareRotate();
	}

	// NOTE: The beauty of a square block is that it looks the same no matter
	// how it's faced.  Saves me quite a bit of code  :-)
	private void squareRotate() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX + 20, leadY );
		subBlocks[ 2 ].setPosition( leadX, leadY + 20 );
		subBlocks[ 3 ].setPosition( leadX + 20, leadY + 20 );
	}
	
	@Override
	protected void orientLeft() {
		squareRotate();
	}
	
	@Override
	protected void orientRight() {
		squareRotate();
	}
	
	@Override
	protected void orientUp() {
		squareRotate();
	}
	
	@Override
	protected void orientDown() {
		squareRotate();
	}

}