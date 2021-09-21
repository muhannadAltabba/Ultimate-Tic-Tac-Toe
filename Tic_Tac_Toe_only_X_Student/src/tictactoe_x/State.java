package tictactoe_x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dallal, Z
 */
public class State {

    private int width = 3;

    public char[][] board;

//    private int last_move_r = -1;
//    private int last_move_c = -1;

    public State(int width) {
        this.width = width;
        board = new char[width][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public State(State state) {
        this.width = state.width;
        board = new char[width][width];
        for (int i = 0; i < width; i++) {
            System.arraycopy(state.board[i], 0, board[i], 0, width);
        }

    }

    public int getWidth() {
        return this.width;
    }

    public char getCill(int row, int col) {
        return board[row][col];
    }

    public void setCill(int row, int col, char c) {
        board[row][col] = c;
    }

    public void playMove(int x, int y, char c) {
        if (board[x][y] == ' ') {
            board[x][y] = c;
        }
//        this.last_move_r = x;
//        this.last_move_c = y;
    }

    public void playUser(int x, int y) {
        playMove(x, y, 'X');
    }

    public void playComputer(int x, int y) {
        playMove(x, y, 'O');
    }

//    public List<State> allNextMoves() {
//        List<State> nextBoards = new LinkedList<>();
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < width; j++) {
//                if (board[i][j] == ' ') {
//                    State nextBoard = new State(this);
//                    nextBoard.playComputer(i, j);
//                    nextBoards.add(nextBoard);
//                }
//        }
//        return nextBoards;
//    }

    static public List<ArrayList<Integer>> getAllNextMoves(State[][] s, int row, int col) {
        if (!s[row][col].isLoseOnLastMove()) {
            return allNextMovesInSmallPiece(s, row, col);
        }
        List<ArrayList<Integer>> nextBoards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (s[i][j].isLoseOnLastMove()) {
                    continue;
                }
                nextBoards.addAll(allNextMovesInSmallPiece(s, i, j));
            }
        }
        return nextBoards;
    }

    static public List<ArrayList<Integer>> allNextMovesInSmallPiece(State[][] s, int row, int col) {
        List<ArrayList<Integer>> nextBoards = new ArrayList<>();
        for (int i = 0; i < s[row][col].width; i++) {
            for (int j = 0; j < s[row][col].width; j++) {
                if (s[row][col].board[i][j] == ' ') {
                    //State nextBoard = new State(s[row][col]);
                    //nextBoard.playComputer(i, j);
                    ArrayList<Integer> curMove = new ArrayList<>();
                    curMove.add(row);curMove.add(col);
                    curMove.add(i);curMove.add(j);
                    nextBoards.add(curMove);
                }
            }
        }
        return nextBoards;
    }

    static public State[][] sonTable(State[][] s, int row, int col, State newstate) {
        State[][] temp = new State[4][4];
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == row && j == col) {
                    temp[i][j] = new State(newstate);
                } else {
                    temp[i][j] = new State(s[i][j]);
                }
            }
        }
        return temp;
    }

    public int CountLinesHave_c_n_Times(char c, int n) {
        int count = 0;
        // count full culumns
        for (int i = 0; i < width; i++) {
            if (CountCharInLine(i, 0, 0, 1, c) == n) {
                count++;
            }
        }
        // count full rows
        for (int i = 0; i < width; i++) {
            if (CountCharInLine(0, i, 1, 0, c) == n) {
                count++;
            }
        }
        // count full main diagonals
        if (CountCharInLine(0, 0, 1, 1, c) == n) {
            count++;
        }
        // coubt full second diagonals
        if (CountCharInLine(2, 0, -1, 1, c) == n) {
            count++;
        }
        return count;
    }

    public int prospectToWin() {
        return CountLinesHave_c_n_Times('X', 0);
    }

    public int prospectToLoss() {
        return CountLinesHave_c_n_Times('O', 0);
    }

    public int eval() {

        if (isLoseOnLastMove('X')) {
            return -10;
        }
        if (isLoseOnLastMove('O')) {
            return 10;
        }
        int w = prospectToWin();
        int l = prospectToLoss();

        return w - l;
    }

    static public int evalForAllTable(State[][] state) {
        int sum = 0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                sum += state[i][j].eval();
                //   System.out.println("ddddd");
                //   System.out.println(state[i][j]);
            }
        }

        sum += 100 * bigboard(state).eval();
        // System.out.println(this);
        //System.out.println(sum);
        return sum;
    }

    static public State bigboard(State[][] state) {
        State s = new State(3);
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (state[i][j].isLoseOnLastMove('X')) {
                    s.board[i - 1][j - 1] = 'X';
                } else if (state[i][j].isLoseOnLastMove('O')) {
                    s.board[i - 1][j - 1] = 'O';
                } else {
                    s.board[i - 1][j - 1] = ' ';
                }
            }
        }

        return s;
    }

    public boolean isLoseOnLastMove() {
        if (isLoseOnLastMove('X')) {
            return true;
        }
        if (isLoseOnLastMove('O')) {
            return true;
        }
        return false;
    }

    public boolean isLoseOnLastMove(char c) {
        return CountLinesHave_c_n_Times(c, 3) > 0;
    }

    private int CountCharInLine(int iStart, int jStart, int iDirection, int jDirection, char c) {
        int count = 0;
        for(int x=0;x<3;x++){
            if(board[iStart][jStart]==c)count++;
            iStart+=iDirection;
            jStart+=jDirection;
        }
        return count;
    }

    private boolean all_X(List<Character> list, char xory) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != xory) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 0; i < width; i++) {
            sb.append(i + 1);
            sb.append(" | ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("\n");
        sb.append("   ");
        for (int i = 0; i < width; i++) {
            sb.append("....");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("\n");
        for (int i = 0; i < width; i++) {
            sb.append(i + 1);
            sb.append(": ");
            for (int j = 0; j < width; j++) {
                sb.append(board[i][j]);
                sb.append(" | ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append('\n');
        }
        return sb.toString();
    }

}
