package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private final Board _board;
    /**
     * Current score.
     */
    private int _score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int _maxScore;
    /**
     * True iff game is ended.
     */
    private boolean _gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        _board = new Board(size);
        _score = _maxScore = 0;
        _gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        _board = new Board(rawValues);
        this._score = score;
        this._maxScore = maxScore;
        this._gameOver = gameOver;
    }

    /**
     * Same as above, but gameOver is false. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore) {
        this(rawValues, score, maxScore, false);
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return _board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return _board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (_gameOver) {
            _maxScore = Math.max(_score, _maxScore);
        }
        return _gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return _score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return _maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        _score = 0;
        _gameOver = false;
        _board.clear();
        setChanged();
    }

    /**
     * Allow initial game board to announce a hot start to the GUI.
     */
    public void hotStartAnnounce() {
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        _board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE.
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have
     * the same value, they are merged into one Tile of twice the original
     * value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     * tilt. So each move, every tile will only ever be part of at most one
     * merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     * value, then the leading two tiles in the direction of motion merge,
     * and the trailing tile does not.
     *
     * @return
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        _board.setViewingPerspective(side);

        for (int cols = 0; cols < _board.size(); cols = cols + 1) {
            for (int rows = 3; rows >= 0; rows = rows - 1) {
                    Tile tile1 = _board.tile(cols, rows);
                    if(tile1 != null){ /**This part of the function goes through all the rows and columns in which it tries to determine at any position it iterates trough whether or not there is a tile there  */
                    for (int rows2 = rows - 1; rows2 >= 0; rows2 = rows2 - 1) { /**Same logic as above only expect it tries to iterate NEXT TO the "rows" pointer hence why we do rows-1 w/ same iteration features as 'rows"  */
                        Tile tile2 = _board.tile(cols, rows2); /**This just creates a tile to represent the "ireator position" of the current "pointer location" in the board same as tile1  */
                        if (tile2 != null) { /**Same logic as above   */
                            if (tile1.value() == tile2.value()) { /**IF let's say we have [4,4,x,x] then we proceed with the valid merging mechanism an change of score as appopriate   */
                                _board.move(cols, rows, tile2); /**If equal and adjacent the tile now moves to position "tile2" where tile 2 is BEFORE the merging of 2 tiles   */
                                changed = true; /**Explained in the instructions and Professor Hug's Youtube video of the Tilt method overview*/
                                _score = _score + 2 * tile1.value(); /**Update Score if [16,16,x,x] and we move left >> [32,x,x,x] (just double the score   */
                                rows = rows2;
                                break;
                            }  {

                            }
                        }  {


                }
            }
        }
    }
}
        for (int cols = 0; cols < _board.size(); cols = cols + 1) {
            for (int rows = _board.size() - 1; rows >= 0; rows = rows - 1) {
                Tile tile1 = _board.tile(cols, rows);
                if (tile1 == null) {
                    for (int rows2 = rows - 1; rows2 >= 0; rows2 = rows2 - 1) {
                        Tile tile2 = _board.tile(cols, rows2);
                        if (tile2 != null) {
                            _board.move(cols, rows, tile2);
                            changed = true; /**Same logic as above except it looks if the value of tile1 is empty (null) since no merging is happenng here SCORE IS NOT CHNAGED
                             Just because one moves the board doesnt always mean that any tiles will merge (or that the score will change) */
                            break;
                        }
                    }
                }
            }
        }
        _board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }
    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        _gameOver = checkGameOver(_board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     */

    /** Logic: The important thing we are doing for this function is to check if there any empty spaces at ALL
     * We should do soemthing that checks each space on the board to check if there is an empty space
     * Make a function that transverse each element in the array both in the rows and columns
     * Return true if empty (stored as null)
     *
     * */
    public static boolean emptySpaceExists(Board b) {
        for (int rows = 0; rows < b.size(); rows = rows + 1) /**This part of the function goes through all the rows until the "rows variable" equals the size of the board (before it goes out of bounds)  */
            for(int cols = 0; cols < b.size(); cols = cols + 1) /**This part of the function goes through all the rows until the "rows variable" equals the size of the board (before it goes out of bounds)  */
                if(b.tile(rows, cols) == null) /** checks if any tile on the board is empty (no tile = null) */
                    return true; /** Returns True if there is any empty space (if the index of rows or cols returns a empty space (null value)  */
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */

    /**This function   */
    public static boolean maxTileExists(Board b) {
        for (int rows = 0; rows < b.size(); rows = rows + 1) /**This part of the function goes through all the rows until the "rows variable" equals the size of the board (before it goes out of bounds)  */
            for(int cols = 0; cols < b.size(); cols = cols + 1) /**This part of the function goes through all the rows until the "rows variable" equals the size of the board (before it goes out of bounds)  */
                if(b.tile(cols,rows) != null && b.tile(cols,rows).value() == MAX_PIECE ) /**This part of the function goes through all the rows and columns and checks if there is a tile in any of the rows and columns that are "traversed" through and
                 the second condition checks if the value of the tile on any row/column is equal to MAX_PIECE which is set at 2048*/
                    return true; /** Returns True if there a tile valued at 2048  */
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
        public static boolean atLeastOneMoveExists(Board b) {
            return emptySpaceExists(b) || AValidMerge (b); /** Returns True if there is at least a empty space on the board  OR A valid move exist (menaing that two tiles next too each other have the same value
             (The Valid Move Checker oversees the value of each possible adjancent combination of tiles and checks if the values are equal to each other for a valid merging of tiles*/

        }

    private static boolean AValidMerge (Board b) {
        for (int rows = 0; rows <= b.size()- 1; rows = rows + 1) {
            for (int cols = 0; cols <= b.size()- 2; cols = cols + 1) { /** This basically iterates the some of rows and columns to check if there is a tile there, than compares value of tiles
             other empty values are checked by "emptySpaceExist"*/
                Tile lefttile = b.tile(cols, rows); /** For example (rows: 3, columns: 2) (3,2)  */
                Tile righttile = b.tile(cols + 1, rows);  /** For example (rows: 4, columns: 2) (4,2) (lets assume that the board has a size of over 4) (Rows:0-4 exist for example) (this is just the logic of it)  */
                if (righttile.value() == lefttile.value()) {
                    return true;

                }

                Tile downtile = b.tile(rows, cols); /** ** For example tile at (2,3)  */ /** Think of these two as checking the tile that has inverse coordinates of the one above (coordinates that are next to each other)
                 (ex lefttile is (2,3) than uptile is (3,2) (In short the coordinates are inversed to check the adjancent tile from the desired direction)*/
                Tile uptile = b.tile(rows, cols + 1); /** For example (rows = 3, columns = 3) (3,3) uptile to (2,3 from downtile) */
                if (downtile.value() == uptile.value()) {
                    return true;
                }
            }
        }
        return false;
        }


    /** Returns the model as a string, used for debugging. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    /** Returns whether two models are equal. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    /** Returns hash code of Model’s string. */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
