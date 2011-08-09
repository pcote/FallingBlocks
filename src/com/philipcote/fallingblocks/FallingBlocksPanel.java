package com.philipcote.fallingblocks;
import com.philipcote.gamelib.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import java.applet.*;
import static javax.swing.JOptionPane.*;

/* FallingBlocksPanel wears a few hats.  As a child of GamePanel, it deals with 
 * game rendering and has is required to override gameUpdate.  However, gameUpdate is
 * left empty because just about all of the game logic is processed from within the 
 * GravityTask inner class.  
 * These are the jobs that FallingBlocksPanel itself does.
 * 1.) Initializes and Renders the game components and music.
 * 2.) Handles keyboard inputs for moving the blocks.*/
public class FallingBlocksPanel extends GamePanel implements KeyListener{

	// Class-wide properties
	private static final long serialVersionUID = -1845817539986853808L;
	private Timer gravityTimer; // used to call method GravityTask which drops blocks.
	private Score score = new Score(); // Keeps track of how many lines completed
	private final int LEFT_BOUND = 40;
	private final int RIGHT_BOUND = 340;
	private final int BOTTOM_BOUND = 340;
	private TetrisBlock currentBlock = null, nextBlock = null;
	private int fallRate = 1000; // move down one unit every second (by default)
	private BlockPile blockPile; // place where tetris block pieces pile up.
	
	// Used to make the sub blocks of the Tetris Blocks look pretty.
	private BufferedImage redBlockImage, yellowBlockImage, purpleBlockImage, 
						  greenBlockImage, blueBlockImage, lightBlueBlockImage;
	private Wall leftWall, rightWall;
	private boolean gameOver = false;
	private AudioClip musicClip; // plays that funky Russian tune during the game
	
	/*GavityTask is the inner class that handles the actual game logic updates that are
	 * a consequence of gravity pushing down on a falling block.  The consequences it
	 * deals with are
	 * 1.) Where the new block moves to.
	 * 2.) What do do when the block lands on the block pile.*/
	private class GravityTask extends TimerTask
	{
		public void run(){
			if( blockPile.collidesWith( currentBlock ) || blockHitsGround() ){
				blockPile.add( currentBlock );
				// check for complete rows and change the blockpile should there
				// be any.
				ArrayList<Integer> completedRows = blockPile.getCompletedRows( currentBlock );
				if( !completedRows.isEmpty() ){ // if there are more than 0 completed rows
					for( int completedRow: completedRows ){ // clear out each of them 
						blockPile.removeRow( completedRow ); // and raise the score
						score.raiseScore();
						// increase fall speed once every 10 completed lines
						if( score.getCurrentScore() % 10 == 0 )
							increaseFallSpeed();
					}
				}
				
				dropInNextBlock();
			}
			
			else{
				// backoff from moving down if it intersects with the blockpile.
				currentBlock.moveDown();
				if( blockPile.intersectsWith( currentBlock ) )
					currentBlock.moveUp();
			}
		}

		// Puts the next block into play and puts a new block into waiting.
		private void dropInNextBlock() {
			currentBlock = nextBlock;
			currentBlock.setPosition( 100, 40 );
			nextBlock = setTetrisBlock();
			nextBlock.setPosition( 400, 40 );
			
			if( blockPile.intersectsWith( currentBlock ) ){
				endGame();
			}
		}

		private void endGame() {
			gameOver = true;
			musicClip.stop();
			gravityTimer.cancel(); // stops more blocks from being added.
		}

		// Resets the timer so that blocks fall 100 ms faster than before.
		private void increaseFallSpeed() {
			gravityTimer.cancel();
			gravityTimer = new Timer();
			fallRate -= 100;
			gravityTimer.scheduleAtFixedRate( new GravityTask(), 0, fallRate );
		}
	}
	
	
	public FallingBlocksPanel(){
		try
		{
		initializeBlockImages();
		}
		catch( IOException ioe )
		{
			showMessageDialog(null, "Image Load Failure in FallingBlocksPanel.  Exiting" );
			System.exit( 1 );
		}
		// sets panel dimensions. ( protected variable declared in GamePanel parent.
		width = 500;
		height = 400;
		
		// instantiates, walls, tetris blocks, and the block pile.
		initializeSprites(); 
		// make the timer cause blocks to start falling.
		gravityTimer = new Timer();
		gravityTimer.scheduleAtFixedRate( new GravityTask(), 0, fallRate );
		startMusic();
	}

	// Instantiates tetris blocks, walls, and the block pile.
	private void initializeSprites() {
		currentBlock = setTetrisBlock(); // initialize the current block to a random type
		nextBlock = setTetrisBlock();
		nextBlock.setPosition( 400, 40 );
		leftWall = new Wall( LEFT_BOUND - 20, 40 );
		rightWall = new Wall( RIGHT_BOUND, 40 );
		blockPile = new BlockPile( width, height );
	}

	private void startMusic() {
		// start the music
		musicClip = Applet.newAudioClip( getClass().getResource("resources/tetris1.mid"));
		musicClip.loop();
	}

	private void initializeBlockImages() throws IOException {
		redBlockImage = ImageIO.read( getClass().getResource("resources/red_block.jpg"));
		yellowBlockImage = ImageIO.read( getClass().getResource("resources/yellow_block.jpg"));
		purpleBlockImage = ImageIO.read( getClass().getResource("resources/purple_block.jpg"));
		greenBlockImage = ImageIO.read( getClass().getResource("resources/green_block.jpg"));
		blueBlockImage = ImageIO.read( getClass().getResource("resources/blue_block.jpg"));
		lightBlueBlockImage = ImageIO.read( getClass().getResource( "resources/light_blue_block.jpg") );
	}
	
	// tetris block initializer randomly picks from one of 7 different child classes
	private TetrisBlock setTetrisBlock(){
		TetrisBlock newBlock;
		Random random = new Random();
		random.setSeed( System.currentTimeMillis() );
		int blockType = 1 + random.nextInt( 7 );
		
		switch( blockType )
		{
		case 1:
			newBlock = new LongBlock( redBlockImage );
			break;
		case 2:
			newBlock = new ReverseLBlock( yellowBlockImage );
			break;
		case 3:
			newBlock = new SquareBlock( greenBlockImage );
			break;
		case 4:
			newBlock = new TBlock( blueBlockImage );
			break;
		case 5:
			newBlock = new ForwardLBlock( purpleBlockImage );
			break;
		case 6:
			newBlock = new ZBlock( lightBlueBlockImage );
			break;
		case 7:
			newBlock = new ReverseZBlock( redBlockImage );
			break;
		default:
			System.err.println("Error in block instantiation. Type: " + blockType + 
							   " is invalid");
			newBlock = null; // this should never happen
			break;
		}
		return newBlock;
	}
	
	@Override
	protected void gameUpdate() {/* Most game update logic in the GravityTask class*/}

	private boolean blockHitsGround() {
		// find which sub-block is lowest to the ground
		SubBlock lowestBlock = currentBlock.getLowestSubBlock();
		
		if( lowestBlock.getYPos() > BOTTOM_BOUND )
			return true;
		
		return false;
	}

	@Override
	protected synchronized void gameRender() {
		Graphics g = initializeGraphicsContext(); // keep graphics context up to date
		if( !blockPile.imageBufferInitialized() ){
			blockPile.initializeBuffer( createImage( width, height ) );
		}
		
		if( bufferImage != null && g != null ){
			g.setColor( Color.black );
			g.fillRect( 0, 0, width, height ); // draw background
			// TODO: Check and see if these are all child classes to Sprite.  
			// Conceivably, I could just loop through this as a collection.
			blockPile.draw( g );
			leftWall.draw( g );
			rightWall.draw( g );
			currentBlock.draw( g );
			nextBlock.draw( g );
			score.draw( g );
			
			if( gameOver )
				this.showGameOverMessage( g );
		}
		
		
	}

	private void showGameOverMessage( Graphics g ) {
		Color oldColor = g.getColor();
		g.setColor( Color.blue );
		g.drawString( "Game Over", 400, 200 );
		g.setColor( oldColor );
	}

	public void keyTyped(KeyEvent e ) {/* Mandatory Override */}

	
	public void keyPressed(KeyEvent e ) {
//		 If the game is over, the only recognizable key should be escape
		if( e.getKeyCode() == KeyEvent.VK_ESCAPE ){
			System.exit( 0 );
		}	
		
		if( gameOver ) return;
		
		if( e.getKeyCode() == KeyEvent.VK_UP ){
			attemptRotateUp();
		}
		
		else if( e.getKeyCode() == KeyEvent.VK_LEFT ){
			attemptMoveLeft();
		}
		
		else if( e.getKeyCode() == KeyEvent.VK_RIGHT ){
			attemptMoveRight();
		}
		
		else if( e.getKeyCode() == KeyEvent.VK_DOWN ){
			attemptPushBlockDown();
		}
		
	}
	
	public void keyReleased(KeyEvent e ) {
		
		
	}

	/************************"ATTEMPT" METHODS*************************/
	/* The "attempt" support methods are designed to make sure that any movements
	 * or rotations that can occur do so safely without inappropriately intersecting
	 * with a block pile falling through the floor, ect.  
	 * */
	private void attemptPushBlockDown() {
		currentBlock.moveDown();
		
		if( blockPile.intersectsWith( currentBlock ) ){
			currentBlock.moveUp();
		}
		else if( currentBlock.getLowestSubBlock().getYPos() > this.BOTTOM_BOUND ){
			currentBlock.moveUp();
		}
	}

	private void attemptMoveRight() {
		if( !blockPile.obstructionFromRight( currentBlock ) &&
			rightMoveWithinBounds() )
		   currentBlock.moveRight();
	}

	private void attemptMoveLeft() {
		if( !blockPile.obstructionFromLeft( currentBlock ) && 
			leftMoveWithinBounds() )
		   currentBlock.moveLeft();
	}

	private void attemptRotateUp() {
		currentBlock.rotate();
		if( blockPile.intersectsWith( currentBlock ) ){
			undoRotation();
		}
		else if( leftWall.intersectsWith( currentBlock ) || rightWall.intersectsWith( currentBlock ) ){
			undoRotation();
		}
	}

	private void undoRotation() {
		currentBlock.rotate();
		currentBlock.rotate();
		currentBlock.rotate();
	}

	private boolean leftMoveWithinBounds() {
		SubBlock leftMostBlock = currentBlock.getLeftMostSubBlock();
		if( leftMostBlock.getXPos() == this.LEFT_BOUND )
		   return false;
		
		return true;
	}
	
	private boolean rightMoveWithinBounds(){
		SubBlock rightMostBlock = currentBlock.getRightMostSubBlock();
		if( rightMostBlock.getXPos() + 20 == this.RIGHT_BOUND )
			return false;
		
		return true;
	}
	
	/************************END OF "ATTEMPT" METHODS*************************/
}
