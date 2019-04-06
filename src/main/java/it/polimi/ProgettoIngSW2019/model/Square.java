package it.polimi.ProgettoIngSW2019.model;

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

    public int getPositionX(){

    }

    public int getPositionY(){

    }

    public InfoCardinal getSideInfo() {
        //TODO
    }
}
