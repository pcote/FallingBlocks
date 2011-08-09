package com.philipcote.fallingblocks;

import java.awt.image.*;
//Implementation details of how a Long block should look when rotated.
public class LongBlock extends TetrisBlock{

	public LongBlock(){}
	
	public LongBlock( BufferedImage im ){
		super( im );
		orientRight();
	}

	// long blocks only have two looks depending on whether they 
	// are vertical or horizontal.  "orient" methods are overriden
	// accordingly.  
	private void makeHorizontal() {
		subBlocks[ 0 ].setPosition( leadX, leadY  );
		subBlocks[ 1 ].setPosition( leadX - 20, leadY );
		subBlocks[ 2 ].setPosition( leadX + 20, leadY );
		subBlocks[ 3 ].setPosition( leadX + 40, leadY );
	}

	private void makeVertical() {
		subBlocks[ 0 ].setPosition( leadX, leadY );
		subBlocks[ 1 ].setPosition( leadX, leadY - 20 );
		subBlocks[ 2 ].setPosition( leadX, leadY + 20 );
		subBlocks[ 3 ].setPosition( leadX, leadY + 40 );
	}

	@Override
	protected void orientLeft() {
		makeHorizontal();
	}

	@Override
	protected void orientRight() {
		makeHorizontal();
	}

	@Override
	protected void orientUp() {
		makeVertical();
	}

	@Override
	protected void orientDown() {
		makeVertical();
	}
}
