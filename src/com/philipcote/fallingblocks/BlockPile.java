package com.philipcote.fallingblocks;
import java.util.*;
import java.awt.*;



public class BlockPile {
	private Collection<SubBlock> blockPileCollection;
	private Image blockPileImage;
	private int bpImageWidth, bpImageHeight;
	
	public BlockPile(int width, int height) {
		
		blockPileCollection = new ArrayList<SubBlock>();
		bpImageWidth = width;
		bpImageHeight = height;
	}

	/* Note: the two methods below deal with the initialization of the blockPileImage.
	 * Initialization is not done in the FallingBlocksPanel constructor because 
	 * calls to createImage will fail everytime in at the panel construction phase.
	 * */
	public boolean imageBufferInitialized(){
		if( blockPileImage == null ){
			return false;
		}
		return true;
	}
	
	public void initializeBuffer( Image im ){
		blockPileImage = im;
	}
	
	// Adds a tetris block's sub block to the existing set of sub blocks
	public synchronized void add( TetrisBlock blockToAdd ){
		SubBlock[] subBlocks = blockToAdd.getSubBlocks();
		for( SubBlock subBlock: subBlocks )
			blockPileCollection.add( subBlock );
		
		this.updateBPImage(); // after block added, the existing blockpile is 
							  // out of date and needs to be updated.
	}
	
	// Checks for collisions between a tetris block and the blockpile.
	public synchronized boolean collidesWith( TetrisBlock tetrisBlock ){
		// check for collisions with the button tetris block
		SubBlock lowestBlock = tetrisBlock.getLowestSubBlock();
		if( subBlockCollides( lowestBlock ) ) return true;
		// check for collisions with the right most tetris block
		SubBlock rightMostBlock = tetrisBlock.getRightMostSubBlock();
		if( subBlockCollides( rightMostBlock ) ) return true;
		// check for collisions with the left most tetris block.
		SubBlock leftMostBlock = tetrisBlock.getLeftMostSubBlock();
		if( subBlockCollides( leftMostBlock ) ) return true;
		
		return false;
	}
	
	// helper method that looks for collisions between one of the sub blocks 
	// of a tetris block and the block pile itself.
	private boolean subBlockCollides( SubBlock subBlock ){
		for( SubBlock blockInPile: blockPileCollection ){
			if( subBlock.getXPos() == blockInPile.getXPos() 
				&& subBlock.getYPos() + subBlock.getWidth() == blockInPile.getYPos() )
			{
				return true;
			}
		}
		
		return false;
	}
	
	// Use the image depiction of the blockpile to draw the image
	public synchronized void draw( Graphics g ){
		if( this.blockPileCollection.isEmpty() ) return; // don't draw if there's nothing there
		if( blockPileImage != null )
			g.drawImage( blockPileImage, 0, 0, bpImageWidth, bpImageHeight, null );
	}
	
	// Called after a tetris block is added to the block pile because, after a 
	// Tetris Block's sub blocks are added, the existing image depication is
	// obsolete.
	private void updateBPImage(){
		Graphics g = blockPileImage.getGraphics();
		g.setColor( Color.black );
		g.fillRect( 0, 0, bpImageWidth, bpImageHeight );
		
		if( g != null ){
			for( SubBlock subBlock: blockPileCollection )
				subBlock.draw( g );
		}
	}

	/* The Two methods below are useful methods for when a Tetris Block 
	  tries to move to the left or right but blocks from the 
	  block pile are obstructing the attempt to move.*/ 
	public synchronized boolean obstructionFromLeft( TetrisBlock tetrisBlock ){
		
		SubBlock[] tetrisSubBlocks = tetrisBlock.getSubBlocks();
		
		for( SubBlock blockInPile: blockPileCollection ){
			for( SubBlock tetrisSubBlock: tetrisSubBlocks ){
			   if( tetrisSubBlock.getYPos() == blockInPile.getYPos() && 
				  tetrisSubBlock.getXPos() == blockInPile.getXPos() + 20 ){
				     return true;
			   }
			}
		}
		
		return false;
	}
	
	public synchronized boolean obstructionFromRight( TetrisBlock tetrisBlock ){
		SubBlock[] tetrisSubBlocks = tetrisBlock.getSubBlocks();
		
		for( SubBlock blockInPile: blockPileCollection ){
			for( SubBlock tetrisSubBlock: tetrisSubBlocks ){
			   if( tetrisSubBlock.getYPos() == blockInPile.getYPos() && 
				   tetrisSubBlock.getXPos() + tetrisSubBlock.getWidth() == blockInPile.getXPos() ){
				      return true;
			   }
			}
		}
		
		return false;
	}

	/*Basically, this is an error detection method to check for circumstances
	 * for when the sub blocks of a tetris block might coincide with the 
	 * */
	public synchronized boolean intersectsWith(TetrisBlock currentBlock) {
		SubBlock[] tetrisSubBlocks = currentBlock.getSubBlocks();
		
		for( SubBlock tetrisSubBlock: tetrisSubBlocks ){
			for( SubBlock blockInPile: blockPileCollection ){
				if( tetrisSubBlock.getXPos() == blockInPile.getXPos() && 
					tetrisSubBlock.getYPos() == blockInPile.getYPos() ){
					return true;
				}
			}
		}
		
		return false;
	}

	/* When a Tetris Block gets added to the block pile, there is a chance that
	 * it may have created complete rows that will need to be removed.  This method
	 * returns a list detailing where those rows ( if any ) are.
	 * */
	public synchronized ArrayList<Integer> getCompletedRows( TetrisBlock tetrisBlock ) {
		
		// get a list of all the applicable yPosition rows based on the recently
		// landed tetris block
		ArrayList<Integer> affectedRows = new ArrayList<Integer>();
		for( SubBlock subBlock: tetrisBlock.getSubBlocks() ){
			if( !affectedRows.contains( subBlock.getYPos() ) )
				affectedRows.add( subBlock.getYPos() );
		}
		
		ArrayList<Integer> completedRows = new ArrayList<Integer>();
		int blockRowCount = 0; // used to tally the total blocks found in a given row
		// for every affected row, look for blocks in the pile that are also in that 
		// row.
		for( int affectedRow: affectedRows ){ 
			for( SubBlock blockInPile: blockPileCollection ){ 
				if( blockInPile.getYPos() == affectedRow ){
					blockRowCount++;
				}
			}
			
			if( blockRowCount == 15 ){ // 15 blocks found means the row is complete
				completedRows.add( affectedRow );
			}
			
			blockRowCount = 0;	
		}
		
		return completedRows;
	}

	// removes the sub blocks of a specified row.
	public synchronized void removeRow( int completedRow ) {
		removeRowSubBlocks(completedRow);
		dropUnclearedBlocks( completedRow );
		updateBPImage(); // blockpike image obsolete.  time to udpate it.
	}

	// takes the blocks that are part of the completed row out of the block pile
	private void removeRowSubBlocks(int completedRow) {
		// NOTE: The tempBlockArray logic is a workaround that I used because
		// java doesn't allow for removing list elements while you are iterating 
		// it.  :-(
		SubBlock[] tempBlockArray = ( SubBlock[] )blockPileCollection.toArray( new SubBlock[0] );
		blockPileCollection.clear();
		
		for( SubBlock subBlock: tempBlockArray ){
			if( subBlock.getYPos() != completedRow ){
				blockPileCollection.add( subBlock );
			}
		}
	}

	/* After a row has been cleared out, the blocks above it need to be dropped 
	 * down so a gap isn't left behing where the row once was.*/
	private void dropUnclearedBlocks( int clearedRow ) {
		SubBlock[] temp = ( SubBlock[] )blockPileCollection.toArray( new SubBlock[0] );
		blockPileCollection.clear();
		
		for( SubBlock subBlock: temp ){
			if( subBlock.getYPos() < clearedRow ){ // if subBlock above recently cleared row
				// drop the subblock down by one row
				subBlock.setPosition( subBlock.getXPos(), subBlock.getYPos() + 20 );
			}
			blockPileCollection.add( subBlock );
		}
	}
	
}
