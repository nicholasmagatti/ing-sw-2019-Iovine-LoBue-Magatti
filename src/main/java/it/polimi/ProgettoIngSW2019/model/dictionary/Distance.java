package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public abstract class Distance {
    private static Square[][] board;

    protected Distance(Square[][] board){
        this.board = board;
    }

    protected static Square[][] getBoard() {
        return board;
    }

    protected List<Square> getDistance(Square fromPosition){
        return null;
    }
}
