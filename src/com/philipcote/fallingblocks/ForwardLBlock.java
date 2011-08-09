package com.philipcote.fallingblocks;
import java.awt.image.*;

// Implementation details of how a L-shaped block should look when rotated.
public class ForwardLBlock extends TetrisBlock{


	public ForwardLBlock(){}
	
	public ForwardLBlock( BufferedImage im ){
		super( im ); // initialize the sub blocks
		orientRight();
	}
	
	protected void orientLeft() {
		subBlocks[0].setPosition( leadX, leadY );
		subBlocks[1].setPosition( leadX, leadY + 20 );
		subBlocks[2].setPosition( leadX, leadY + 40 );
		subBlocks[3].setPosition( leadX - 20, leadY );
	}

	protected void orientDown() {
		subBlocks[0].setPosition( leadX, leadY );
		subBlocks[1].setPosition( leadX + 20, leadY );
		subBlocks[2].setPosition( leadX + 40, leadY );
		subBlocks[3].setPosition( leadX, leadY + 20 );
	}

	protected void orientUp() {
		subBlocks[0].setPosition( leadX, leadY );
		subBlocks[1].setPosition( leadX - 20, leadY );
		subBlocks[2].setPosition( leadX - 40, leadY );
		subBlocks[3].setPosition( leadX, leadY - 20 ); 
	}

	protected void orientRight() {
		subBlocks[0].setPosition( leadX, leadY );
		subBlocks[1].setPosition( leadX + 20, leadY );
		subBlocks[2].setPosition( leadX, leadY - 20 );
		subBlocks[3].setPosition( leadX, leadY - 40 );
	}
}
