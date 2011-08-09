package com.philipcote.fallingblocks;

import com.philipcote.gamelib.*;
import javax.swing.*;
import java.util.concurrent.*;
import java.awt.*;


// Class Description: Point where all the classes get tied together and make the
// game run..
public class MainApp {

	public static void main(String[] args) {
		// Initialize the panel that both displays game progress and handles 
		// updates of Game Logic
		FallingBlocksPanel fallingBlocksPanel = new FallingBlocksPanel();
		// setup animation cycle to handle the animation cycle of the Falling Blocks 
		// and work to maintain a 50 FPS framerate.
		AnimationCycle animationCycle = new AnimationCycle(fallingBlocksPanel, 50 );
		@SuppressWarnings("unused") 
		JFrame mainFrame = setupMainFrame(fallingBlocksPanel);
		// Submit the animatio cycle to the thread pool and kick it into gear.
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute( animationCycle );
	}

	/*Primarilty sets up a 400 by 400 window, enables it to listen to keyboard
	 * events, and lays out the Falling Blocks panel into a display that can be
	 * seen */
	private static JFrame setupMainFrame(FallingBlocksPanel fallingBlocksPanel) {
		JFrame mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout( new BorderLayout() );
		mainFrame.getContentPane().add( fallingBlocksPanel, BorderLayout.CENTER );
		mainFrame.addKeyListener( fallingBlocksPanel );
		mainFrame.setTitle( "Falling Blocks" );
		mainFrame.setSize( 500, 400 );
		mainFrame.requestFocus();
		mainFrame.setVisible( true );
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		return mainFrame;
	}
}