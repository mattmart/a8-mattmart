package a8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;








public class GameOfLifeWidget extends JPanel implements ActionListener, SpotListener, ChangeListener {
	
	private static final int FPS_MIN = 0;
	private static final int FPS_MAX = 100;
	private static final int FPS_INIT = 10;
	
	private JSpotBoard _board;	/* SpotBoard playing area. */
	private JSpotBoard boardCopy;
	private JSpotBoard emptyBoard;
	private JLabel _message;		/* Label for messages. */
	private JButton reset_button, fillRandom, advButton;
	private JSlider boardSize;
	//private int count;
	private int widthBoard = 10;
	private int heightBoard = 10;
	private JSpot[][] tempSpot2;
	
	public GameOfLifeWidget() {
		
		
		_board = new JSpotBoard(widthBoard ,heightBoard);
		boardCopy = new JSpotBoard(widthBoard, heightBoard);
		emptyBoard = new JSpotBoard(2,2);
		_message = new JLabel();
		
		/* Set layout and place SpotBoard at center. */
		setLayout(new BorderLayout());
		add(boardCopy, BorderLayout.CENTER);
		add(_board, BorderLayout.CENTER);
		//add(boardCopy, BorderLayout.CENTER);
		
		/* Create subpanel for message area and reset button. */
		
		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());
		
		/* Reset button. Add ourselves as the action listener. */
		
		reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		//reset_message_panel.add(_message, BorderLayout.CENTER);
		
		// fill button. Add ourselves as the action listener
		
		fillRandom = new JButton("Fill");
		fillRandom.addActionListener(this);
		reset_message_panel.add(fillRandom, BorderLayout.WEST);
		
		// play button
		
//		playButton = new JButton("Play");
//		playButton.addActionListener(this);
//		reset_message_panel.add(playButton, BorderLayout.NORTH);
		
		
		// advance button
		
		advButton = new JButton("Advance");
		advButton.addActionListener(this);
		reset_message_panel.add(advButton, BorderLayout.CENTER);
		
		// board size slider
		boardSize = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
		boardSize.setMajorTickSpacing(10);
		boardSize.setMinorTickSpacing(5);
		boardSize.setPaintTicks(true);
		boardSize.setPaintLabels(true);
		
		
		//boardSize.addActionListener(this);
		reset_message_panel.add(boardSize, BorderLayout.SOUTH);
		
		/* Add subpanel in south area of layout. */
		
		add(reset_message_panel, BorderLayout.SOUTH);

		/* Add ourselves as a spot listener for all of the
		 * spots on the spot board.
		 */
		_board.addSpotListener(this);
		boardCopy.addSpotListener(this);

		/* Reset game. */
		resetGame();
	}

	/* resetGame
	 * 
	 * Resets the game by clearing all the spots on the board,
	 * picking a new secret spot, resetting game status fields, 
	 * and displaying start message.
	 * 
	 */
	
	private void resetBoard() {
		
	}

	private void resetGame() {
		/* Clear all spots on board. Uses the fact that SpotBoard
		 * implements Iterable<Spot> to do this in a for-each loop.
		 */

		for (Spot s : _board) {
			s.clearSpot();
			s.toDead();
		}
		for (Spot s : boardCopy) {
			s.clearSpot();
			s.toDead();
		}
		
		/* Display game start message. */
		
		_message.setText("Welcome to the Game of Life.");
	}
	
	private void fillGame() {
		
		for (Spot s : _board) {
			if (s.getRandNum() > (Math.random())) {
			s.setSpotColor(Color.BLUE);
			s.toggleSpot();
			s.toAlive();
			} else {
				s.clearSpot();
				s.toDead();
			}
			s.setRandNum(Math.random());
		}
		for (Spot s : boardCopy) {
			if (s.getRandNum() > (Math.random())) {
			s.setSpotColor(Color.BLUE);
			s.toggleSpot();
			s.toAlive();
			} else {
				s.clearSpot();
				s.toDead();
			}
			s.setRandNum(Math.random());
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
	    JSlider source = (JSlider)e.getSource();
	   System.out.println("hello");
	    if (!source.getValueIsAdjusting()) {
	        int fps = (int)source.getValue();
	        for (int i=0; i<100; i++) {
	        	if (fps == i) {
	        		widthBoard =i;
	        		heightBoard = i;
	        		resetBoard();
	        	}
	        }
	    } 
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//System.out.println(e.getSource().toString());
		if (e.getSource() == reset_button) {
				resetGame();
		}
		if (e.getSource() == fillRandom) {
			fillGame();
		}
		if (e.getSource() == advButton) {
			// System.out.println("hello");
			gameMechanics();
		}
		
	}

	@Override
	public void spotClicked(Spot s) {
		if (s.isEmpty() == false) {
			_message.setText( "killed life at " + s.getCoordString() + ". ");
			s.clearSpot();
			s.toDead();
			s.toggleSpot();
		}
		if (s.isEmpty()) {
			_message.setText( "created life at " + s.getCoordString() + ". ");
		}
		
		
		s.setSpotColor(Color.BLUE);
		s.toggleSpot();
		s.toAlive();
		
		
	}

	@Override
	public void spotEntered(Spot spot) {
		spot.highlightSpot();
		
	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();
		
	}
	
//	public <T> List<T> spotListGetter(Class<T> klazz) {
//		List<T> list = new ArrayList<>();
//		list.add(klazz.cast(obj))
//	}

	private void spotCheck(Spot s) {
		
		Spot dullSpot = new JSpot(Color.BLACK, Color.YELLOW, Color.RED, emptyBoard, 1, 1);
		int countAlive = 0;
		Spot a1 = dullSpot;
		Spot a2 = dullSpot;
		Spot a3 = dullSpot;
		Spot a4 = dullSpot;
		Spot a5 = dullSpot;
		Spot a6 = dullSpot;
		Spot a7 = dullSpot;
		Spot a8 = dullSpot;
			
		int x = s.getSpotX();
		//System.out.println("the x: " + x);
		int y = s.getSpotY();
		//System.out.println("the x: " + y);
		if (y < heightBoard-1) {
		a1 = _board.getSpotAt(x, y+1); // bottom
		} 
		if (x < widthBoard-1 && y < heightBoard -1) {
		a2 = _board.getSpotAt(x+1, y+1); //bottom right
		}
		if (x < widthBoard-1) {
		a3 = _board.getSpotAt(x+1, y); // right
		}
		if (x < widthBoard-1 && y > 0) {
		a4 = _board.getSpotAt(x+1, y-1); //top right
		}
		if (y > 0) {
		a5 = _board.getSpotAt(x, y-1); // top
		}
		if (x > 0 && y > 0) {
			//System.out.println("to here");
		a6= _board.getSpotAt(x-1, y-1); //top left
		}
		if (x > 0) {
		a7 = _board.getSpotAt(x-1, y); // left
		}
		if (x > 0 && y < heightBoard -1) {
		a8 = _board.getSpotAt(x-1, y+1); //bottom left
		}
		List<Spot> borderSpots = new ArrayList<Spot>();
		borderSpots.add(a1);
		borderSpots.add(a2);
		borderSpots.add(a3);
		borderSpots.add(a4);
		borderSpots.add(a5);
		borderSpots.add(a6);
		borderSpots.add(a7);
		borderSpots.add(a8);
		
		for (Spot spot : borderSpots) {
			if (spot.getLife().equals("A")) {
				countAlive++;
			}
		}
		
		
		// rules that change live cell to dead &&
		// rules that change dead cell to live
				if (tempSpot2[x][y].getLife().contentEquals("A")) {
					//System.out.println("alive: " + countAlive);
					if (countAlive > 3 || countAlive < 2) {
						s.clearSpot();
						s.toDead();	
					} else {
						s.toAlive();
						s.setSpotColor(Color.BLUE);
						s.toggleSpot();
					}
				}

				if (countAlive == 3) {
					s.toAlive();
					s.setSpotColor(Color.BLUE);
					s.toggleSpot();
				}
			
		
//		count++;
//		System.out.println("count: " + count);
	}
	
	private void gameMechanics() {

	//new board
		JSpotBoard aNewBoard = new JSpotBoard(widthBoard, heightBoard);
		tempSpot2 = _board.returnspots();
		
		
	//within the board spot check each 
		for (int i = 0; i < widthBoard ; i++) {
			for (int y = 0; y < heightBoard ; y++) {
				Spot c = aNewBoard.getSpotAt(i, y);
				spotCheck(c);
			}
		}
		JSpot[][] tempSpot = aNewBoard.returnspots();
		
		_board.copy(tempSpot);
		
		for (int i = 0; i < widthBoard ; i++) {
			for (int y = 0; y < heightBoard ; y++) {
				Spot ce = _board.getSpotAt(i, y);
				if (ce.getLife().contentEquals("A")) {
					ce.setSpotColor(Color.BLUE);
					ce.toggleSpot();
				}
			}
		}
//		int count2 = 0;
//		for (int i = 0; i < widthBoard ; i++) {
//			for (int y = 0; y < heightBoard ; y++) {
//				System.out.println(tempSpot[i][y].getLife());
//				count2++;
//				}
//			}
//		System.out.println(count2);
		
	}

	
	
}
