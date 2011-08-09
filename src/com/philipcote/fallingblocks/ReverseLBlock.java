package com.philipcote.fallingblocks;
import java.awt.image.*;
//Implementation details of how a reversed L block should look when rotated.
public class ReverseLBlock extends TetrisBlock {

	public ReverseLBlock(){}
	
	public ReverseLBlock( BufferedImage im ){
		super( im );
		orientRight();
	}
	
	

	@Override
	protected void orientLeft() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX + 20, leadY );
		subBlocks[ 2 ].setPosition( leadX, leadY + 20 );
		subBlocks[ 3 ].setPosition( leadX, leadY + 40 );
	}
	
	@Override
	protected void orientRight() {	
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX - 20, leadY );
		subBlocks[ 2 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 3 ].setPosition( leadX, leadY - 40 );
	}
	
	@Override
	protected void orientUp() {	
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY + 20 );
		subBlocks[ 2 ].setPosition( leadX - 20, leadY );
		subBlocks[ 3 ].setPosition( leadX - 40, leadY );
	}
	
	@Override
	protected void orientDown() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX + 20, leadY );
		subBlocks[ 3 ].setPosition( leadX + 40, leadY );		
	}

}
