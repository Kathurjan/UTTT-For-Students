package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class jenBot implements IBot {
    private static final String BOTNAME = "Jen's Bot";

    protected int[][] preferredMoves = {
            {1, 1}, //Center
            {0, 0}, {2, 2}, {0, 2}, {2, 0},  //Corners ordered across
            {0, 1}, {2, 1}, {1, 0}, {1, 2}}; //Outer Middles ordered across

    @Override
    public IMove doMove(IGameState state) {
        IField field = state.getField();

        List<IMove> winningMoves = getWinningMoves(state);
        if(!winningMoves.isEmpty()) {
            return winningMoves.get(0);
        }

        for(int[] move : preferredMoves) {
            if(field.getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD)) {

                for(int[] selectedMove : preferredMoves) {
                    int x = move[0]*3 + selectedMove[0];
                    int y = move[0]*3 + selectedMove[1];
                    if(field.getBoard()[x][y].equals(IField.EMPTY_FIELD)) {
                        return new Move(x,y);
                    }
                }
            }
        }

        return field.getAvailableMoves().get(0);
    }

    //a list of all available winning moves
    private List<IMove> getWinningMoves(IGameState state){
        String player = "1";
        if(state.getMoveNumber()%2==0)
            player="0";

        List<IMove> availableMoves = state.getField().getAvailableMoves();

        List<IMove> winningMoves = new ArrayList<>();
        for (IMove move:availableMoves) {
            if(isWinningMoveAlternative(state,move,player))
                winningMoves.add(move);
        }
        return winningMoves;
    }

    private boolean isWinningMoveAlternative(IGameState state, IMove move, String player){

        // a clone of the array and all the values being transfered to the new array.
        String[][] board = Arrays.stream(state.getField().getBoard()).map(String[]::clone).toArray(String[][]::new);

        board[move.getX()][move.getY()] = player;

        int startX = move.getX()-(move.getX()%3);
        if(board[startX][move.getY()]==player)
            if (board[startX][move.getY()] == board[startX+1][move.getY()] &&
                    board[startX+1][move.getY()] == board[startX+2][move.getY()])
                return true;

        int startY = move.getY()-(move.getY()%3);
        if(board[move.getX()][startY]==player)
            if (board[move.getX()][startY] == board[move.getX()][startY+1] &&
                    board[move.getX()][startY+1] == board[move.getX()][startY+2])
                return true;


        if(board[startX][startY]==player)
            if (board[startX][startY] == board[startX+1][startY+1] &&
                    board[startX+1][startY+1] == board[startX+2][startY+2])
                return true;

        if(board[startX][startY+2]==player)
            if (board[startX][startY+2] == board[startX+1][startY+1] &&
                    board[startX+1][startY+1] == board[startX+2][startY])
                return true;

        return false;
    }

    @Override
    public String getBotName() {
        return BOTNAME;
    }
}


