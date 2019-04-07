package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.InfoCardinal;
import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

public abstract class Square {
    private RoomColor roomColor;
    private boolean isSpowingPoint;
    private InfoCardinal weastSide;
    private InfoCardinal eastSide;
    private InfoCardinal northSide;
    private InfoCardinal sudSide;

    Square(RoomColor roomColor, boolean isSpowingPoint, InfoCardinal weastSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal sudSide){
        this.roomColor = roomColor;
        this.isSpowingPoint = isSpowingPoint;
        this.weastSide = weastSide;
        this.eastSide = eastSide;
        this.northSide = northSide;
        this.sudSide = sudSide;
    }

    public RoomColor getRoomColor(){
        return this.roomColor;
    }

    public InfoCardinal getSideInfo() {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        InfoCardinal temporaryVariable = InfoCardinal.DOOR;
        return temporaryVariable;
    }
}
