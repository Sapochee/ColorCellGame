package model;

import java.util.Random; 

/**
 * This class extends GameModel and implements the logic of the clear cell game,
 * specifically.
 * 
 * @author Dept of Computer Science, UMCP
 */

public class ClearCellGameModel extends GameModel {

	private Random random;
	private int score;

	/**
	 * Defines a board with empty cells.  It relies on the
	 * super class constructor to define the board.
	 * 
	 * @param rows number of rows in board
	 * @param cols number of columns in board
	 * @param random random number generator to be used during game when
	 * rows are randomly created
	 */
	public ClearCellGameModel(int rows, int cols, Random random) {
		super(rows, cols);
		this.random = random;
		score = 0;	
	}

	/**
	 * The game is over when the last row (the one with index equal
	 * to board.length-1) contains at least one cell that is not empty.
	 */
	public boolean isGameOver() {
		for (int row = 0; row < board[0].length; row++) {
			if (board[board.length - 1][row] != BoardCell.EMPTY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the player's score.  The player should be awarded one point
	 * for each cell that is cleared.
	 * 
	 * @return player's score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * This method must do nothing in the case where the game is over.
	 * 
	 * As long as the game is not over yet, this method will do 
	 * the following:
	 * 
	 * 1. Shift the existing rows down by one position.
	 * 2. Insert a row of random BoardCell objects at the top
	 * of the board. The row will be filled from left to right with cells 
	 * obtained by calling BoardCell.getNonEmptyRandomBoardCell().  (The Random
	 * number generator passed to the constructor of this class should be
	 * passed as the argument to this method call.)
	 */
	public void nextAnimationStep() {
		if(isGameOver()) {
			return;
		}
		for (int row = getRows() - 2; row >= 0; row--) {
			boolean currEmpty = true;
			for (int col = 0; col < getCols(); col++) {
				if (getBoardCell(row, col) != BoardCell.EMPTY) {
					currEmpty = false;
					break;
				}
			}
			if (!currEmpty) {
				for (int col = 0; col < getCols(); col++) {
					setBoardCell(row + 1, col, getBoardCell(row, col));
				}
			}
		}
		for (int col = 0; col < getCols(); col++) {
			setBoardCell(0, col, BoardCell.getNonEmptyRandomBoardCell(random));
		}
	}

	/**
	 * This method is called when the user clicks a cell on the board.
	 * If the selected cell is not empty, it will be set to BoardCell.EMPTY, 
	 * along with any adjacent cells that are the same color as this one.  
	 * (This includes the cells above, below, to the left, to the right, and 
	 * all in all four diagonal directions.)
	 * 
	 * If any rows on the board become empty as a result of the removal of 
	 * cells then those rows will "collapse", meaning that all non-empty 
	 * rows beneath the collapsing row will shift upward. 
	 * 
	 * @throws IllegalArgumentException with message "Invalid row index" for 
	 * invalid row or "Invalid column index" for invalid column.  We check 
	 * for row validity first.
	 */
	public void processCell(int rowIndex, int colIndex) {
		int down = rowIndex+1;
		int up = rowIndex-1;
		int right = colIndex+1;
		int left = colIndex-1;

		if(rowIndex > getRows() || rowIndex < 0) {  
			throw new IllegalArgumentException("Invalid row index");
		}
		if(colIndex > getCols() || colIndex < 0) { 
			throw new IllegalArgumentException("Invalid column index");
		}

		if(board[rowIndex][colIndex] != BoardCell.EMPTY){
			if(up >= 0){
				if(board[up][colIndex] == board[rowIndex][colIndex]){ 
					board[up][colIndex] = BoardCell.EMPTY;
					score++;
				}
				if(left >= 0){
					if(board[up][left] == board[rowIndex][colIndex]){ 
						board[up][left] = BoardCell.EMPTY;
						score++;
					}
				}
				if(right < getCols()){
					if(board[up][right] == board[rowIndex][colIndex]){ 
						board[up][right] = BoardCell.EMPTY;
						score++;
					}
				}
			}
			if(down < getRows()){
				if(board[down][colIndex] == board[rowIndex][colIndex]){ 
					board[down][colIndex] = BoardCell.EMPTY;
					score++;
				}
				if(left >= 0){
					if(board[down][left] == board[rowIndex][colIndex]){ 
						board[down][left] = BoardCell.EMPTY;
						score++;
					}
				}
				if(right < getCols()){
					if(board[down][right] == board[rowIndex][colIndex]){ 
						board[down][right] = BoardCell.EMPTY;
						score++;
					}   
				}
			}
			if(left >= 0){
				if(board[rowIndex][left] == board[rowIndex][colIndex]){ 
					board[rowIndex][left] = BoardCell.EMPTY;
					score++;
				}
			}
			if(right < getCols()){
				if(board[rowIndex][right] == board[rowIndex][colIndex]){ 
					board[rowIndex][right] = BoardCell.EMPTY;
					score++;
				}
			}
			if(board[rowIndex][colIndex] == board[rowIndex][colIndex]){ 
				board[rowIndex][colIndex] = BoardCell.EMPTY;
				score++;
			}
		}

		for(int row = getRows()-2; row >= 0; row--){
			
			int count = 0;
			for(int col = 0; col < getCols(); col++){
				if(getBoardCell(row, col) == BoardCell.EMPTY){
					count++;
				}
			}
			
			if(count == getCols()) { 
				for(int i = row; i < getRows()-1; i++){
					for(int col = 0; col < getCols(); col++){
						board[i][col] = board[i+1][col];
						board[i+1][col] = BoardCell.EMPTY;
					}
				}
			}
		}
	}
}