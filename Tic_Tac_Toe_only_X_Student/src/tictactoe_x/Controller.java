package tictactoe_x;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

import javax.management.RuntimeErrorException;

/**
 *
 * @author Mouaz, Z
 */
public class Controller {

    static final int OO = (int) 1e9;
    static final int max = 6;

    public static void main(String[] args) {
        Controller g = new Controller();
        g.gameOn();
    }

    int totLeafs;
    State board = new State(3);
    State[][] state = new State[4][4];
    int nextMoveRow;
    int nextMoveCol;

    public void gameOn() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                state[i][j] = new State(3);
            }
        }
        //System.out.println(board);
        pritnStates();

        Scanner s = new Scanner(System.in);
        System.out.print("Enter row for big table: ");
        nextMoveRow = s.nextInt();
        System.out.print("Enter col for big table: ");
        nextMoveCol = s.nextInt();

        while (true) {
            System.out.println("************USER************");
            getUserMove();
            pritnStates();
            System.out.println(board);
            if (board.isLoseOnLastMove()) {
                System.out.println("USER Wins");
                break;
            }
            System.out.println("row " + nextMoveRow);
            System.out.println("col " + nextMoveCol);

            System.out.println("end user");

            System.out.println("************Computer************");
            getComputerMove();
            pritnStates();
            System.out.println("totLeafs: " + totLeafs);
            System.out.println(board);
            if (board.isLoseOnLastMove()) {
                System.out.println("Computer Wins");
                break;
            }
            System.out.println("row " + nextMoveRow);
            System.out.println("col " + nextMoveCol);

        }

    }

    /**
     *
     */
    public void pritnStates() {
        pritnStates(this.state);
    }

    /**
     *
     * @param state
     */
    static public void pritnStates(State[][] state) {
        System.out.print("   ");
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                System.out.print(i + 1);
                System.out.print(" | ");
            }
            System.out.print("   ");
        }
        System.out.print("\n");
        System.out.print("   ");
        for (int i = 0; i < 9; i++) {
            System.out.print("....");
        }
        System.out.print("\n");
        for (int j = 1; j <= 3; j++) {
            for (int i = 0; i < 3; i++) {
                System.out.print(i + 1);
                System.out.print(": ");
                for (int k = 1; k <= 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        System.out.print(state[j][k].board[i][l]);
                        System.out.print(" | ");
                    }
                    System.out.print("   ");
                }
                System.out.print("\n");
            }
            System.out.print("   ");
            for (int i = 0; i < 9; i++) {
                System.out.print("....");
            }
            System.out.print("\n");

        }

    }

    private void getUserMove() {
        Scanner s = new Scanner(System.in);

        while (state[nextMoveRow][nextMoveCol].isLoseOnLastMove()) {
            System.out.print("Enter row for big table: ");
            nextMoveRow = s.nextInt();
            System.out.print("Enter col for big table: ");
            nextMoveCol = s.nextInt();
        }
        int row;
        int col;
        while (true) {

            while (true) {
                System.out.print("Enter row: ");
                row = s.nextInt();
                System.out.println();
                if ((row > 0) && (row < board.getWidth() + 1)) {
                    break;
                }
            }

            while (true) {
                System.out.print("Enter column: ");
                col = s.nextInt();
                System.out.println();
                if ((col > 0) && (col < board.getWidth() + 1)) {
                    break;
                }
            }
            if (state[nextMoveRow][nextMoveCol].getCill(row - 1, col - 1) == ' ') {
                break;
            }
        }
        //board.playUser(row - 1, col - 1);
        state[nextMoveRow][nextMoveCol].playUser(row - 1, col - 1);
        //cheak if the cill finsh
        if (state[nextMoveRow][nextMoveCol].isLoseOnLastMove()) {
            board.setCill(nextMoveRow - 1, nextMoveCol - 1, 'X');
        }
        nextMoveCol = col;
        nextMoveRow = row;

    }
    public static ArrayList<Integer> bestMove;
    private void getComputerMove() throws RuntimeErrorException {
        // cheak if this cill finsh and chooce another Best one 
//        if(state[nextMoveRow][nextMoveCol].isLoseOnLastMove()){
//            nextMoveRow = getBestCill().get(0);
//            nextMoveCol = getBestCill().get(1);
//        }

        bestMove = new ArrayList<>();
        totLeafs = 0;
        int eval1 = maxMove(state, 0, nextMoveRow, nextMoveCol);
        ArrayList m1 = bestMove;
//        bestMove = new ArrayList<>();
//        totLeafs = 0;
//        int eval2 = alphaMove(state, 0, nextMoveRow, nextMoveCol);
//        if(eval1 != eval2) {
//            System.out.println("Eval: " + eval1 + " " + eval2);
//            assert(false);
////            throw new RuntimeErrorException();
//        }
//        if(!m1.equals(bestMove)) {
//            System.out.println("Move: " + m1 + " " + bestMove);
//            assert(false);
//        }

        int curRow = bestMove.get(0);
        int curCol = bestMove.get(1);
        state[curRow][curCol].playComputer(bestMove.get(2),bestMove.get(3));

        // if state cill finsh
        if (state[curRow][curCol].isLoseOnLastMove()) {
            board.setCill(curRow - 1, curCol - 1, 'O');
        }
        nextMoveRow = bestMove.get(2) + 1;
        nextMoveCol = bestMove.get(3) + 1;

    }

    private int maxMove(State[][] b, int dep, int moveRow, int moveCol) {
//         ************** YOUR CODE HERE ************            \\
        int best = -OO;
            totLeafs++;
        if (board.isLoseOnLastMove() | dep >= max) {
            //System.out.println("state dep row: " + moveRow + " col: " + moveCol);
            return State.evalForAllTable(b);
        }

        List<ArrayList<Integer>> Moves = State.getAllNextMoves(b, moveRow, moveCol);
        for (ArrayList<Integer> curMove: Moves) {
            State nextBoard = new State(b[curMove.get(0)][curMove.get(1)]);
            nextBoard.playComputer(curMove.get(2), curMove.get(3));

            int curRes = minMove(State.sonTable(b, curMove.get(0), curMove.get(1), nextBoard), dep + 1, curMove.get(2) + 1,curMove.get(3) + 1);
            if (best < curRes) {
                best = curRes;
                if(b == state)
                    bestMove = curMove;
            }
        }
        return best;
    }

    private int minMove(State[][] b, int dep, int moveRow, int moveCol) {
//         ************** YOUR CODE HERE ************            \\
        int best = OO;
            totLeafs++;
        if (board.isLoseOnLastMove() | dep >= max) {
            //System.out.println("state dep row: " + moveRow + " col: " + moveCol);
            return State.evalForAllTable(b);
        }
        List<ArrayList<Integer> > Moves = State.getAllNextMoves(b, moveRow, moveCol);
        for (ArrayList<Integer> curMove : Moves) {
            State nextBoard = new State(b[curMove.get(0)][curMove.get(1)]);
            nextBoard.playComputer(curMove.get(2), curMove.get(3));

            int curRes = maxMove(State.sonTable(b, curMove.get(0), curMove.get(1), nextBoard), dep + 1, curMove.get(2) + 1,curMove.get(3) + 1);
            if (best > curRes){
                best = curRes;
                if(b == state)
                    bestMove = curMove;
            }
        }
        return best;
    }

    private int alphaMove(State[][] b, int dep, int moveRow, int moveCol) {
        return alphaMove(b, dep, moveRow, moveCol, -OO, OO);
    }

    private int betaMove(State[][] b, int dep, int moveRow, int moveCol) {
        return betaMove(b, dep, moveRow, moveCol, -OO, OO);
    }

    private int alphaMove(State[][] b, int dep, int moveRow, int moveCol, int Alpha,int Beta) {
//         ************** YOUR CODE HERE ************            \\
        totLeafs++;
        if (board.isLoseOnLastMove() | dep >= max) {
            //System.out.println("state dep row: " + moveRow + " col: " + moveCol);
            return State.evalForAllTable(b);
        }

        List<ArrayList<Integer>> Moves = State.getAllNextMoves(b, moveRow, moveCol);
        for (ArrayList<Integer> curMove : Moves) {
            State nextBoard = new State(b[curMove.get(0)][curMove.get(1)]);
            nextBoard.playComputer(curMove.get(2), curMove.get(3));

            int curRes = betaMove(State.sonTable(b, curMove.get(0), curMove.get(1), nextBoard), dep + 1, curMove.get(2) + 1,curMove.get(3) + 1,Alpha, Beta);
            if (Alpha < curRes) {
                Alpha = curRes;
                if(b == state){
                    bestMove = curMove;
                }
            }

            if (Alpha >= Beta) {
                break;
            }
        }
        return Alpha;
    }

    private int betaMove(State[][] b, int dep, int moveRow, int moveCol, int Alpha, int Beta) {
//         ************** YOUR CODE HERE ************            \\
        totLeafs++;
        if (board.isLoseOnLastMove() | dep >= max) {
            //System.out.println("state dep row: " + moveRow + " col: " + moveCol);
            return State.evalForAllTable(b);
        }
        List<ArrayList<Integer> > Moves = State.getAllNextMoves(b, moveRow, moveCol);

        for (ArrayList<Integer> curMove : Moves) {
            State nextBoard = new State(b[curMove.get(0)][curMove.get(1)]);
            nextBoard.playComputer(curMove.get(2), curMove.get(3));

            int curRes = alphaMove(State.sonTable(b, curMove.get(0), curMove.get(1), nextBoard), dep + 1, curMove.get(2) + 1,curMove.get(3) + 1,Alpha,Beta);

            if (Beta > curRes) {
                Beta = curRes;
                if(b == state){
                    bestMove = curMove;
                }
            }

            if (Alpha >= Beta ) {
                break;
            }
        }
        return Beta;
    }
    /*
        static private ArrayList<Integer> indexCellChanger(State[][] prevState, State[][] nextState) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (prevState[i][j].board[k][l] != nextState[i][j].board[k][l]) {
                            indexes.add(i);
                            indexes.add(j);
                            indexes.add(k + 1);
                            indexes.add(l + 1);
                        }
                    }
                }
            }
        }
        return indexes;
    }
    private ArrayList<Integer> getBestCill() {
        ArrayList<Integer> indexes = new ArrayList<>();
        int val = -10000;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (state[i][j].isLoseOnLastMove()) {
                    continue;
                } else {
                    if (val <= maxMove(state, 0, i, j).getKey()) {
                        temp = maxMove(state, 0, i, j).getValue();
                        val = maxMove(state, 0, i, j).getKey();
                        indexes = indexCellChanger(temp, state);
                    }

                }
            }
        }
        return indexes;
    
     */
}
