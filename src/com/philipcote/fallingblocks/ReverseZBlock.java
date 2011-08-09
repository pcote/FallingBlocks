package com.philipcote.fallingblocks;

import java.awt.image.BufferedImage;
//Implementation details of how a reversed Z-block should look when rotated.
public class ReverseZBlock extends TetrisBlock {

	public ReverseZBlock(){}
	
	public ReverseZBlock(BufferedImage bi ) {
		super( bi );
		orientRight();
	}

	@Override
	protected void orientRight() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX - 20, leadY - 20 );
		subBlocks[ 3 ].setPosition( leadX + 20, leadY );
	}
	
	@Override
	protected void orientUp() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX - 20, leadY );
		subBlocks[ 3 ].setPosition( leadX - 20, leadY + 20 );
	}
	
	@Override
	protected void orientLeft() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX - 20, leadY - 20 );
		subBlocks[ 3 ].setPosition( leadX + 20, leadY );
	}

	@Override
	protected void orientDown() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX - 20, leadY );
		subBlocks[ 3 ].setPosition( leadX - 20, leadY + 20 );
	}

}
