package it.polimi.ProgettoIngSW2019.model;

public abstract class Square {
    private RoomColor roomColor;
    private int positionX;
    private int positionY;
    private InfoCardinal weastSide;
    private InfoCardinal eastSide;
    private InfoCardinal northSide;
    private InfoCardinal sudSide;

    Square(RoomColor roomColor, int positionX, int positionY, InfoCardinal weastSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal sudSide){
        this.roomColor = roomColor;
        this.positionX = positionX;
        this.positionY = positionY;
        this.weastSide = weastSide;
        this.eastSide = eastSide;
        this.northSide = northSide;
        this.sudSide = sudSide;
    }

    public RoomColor getRoomColor(){
        return this.roomColor;
    }

    public int getPositionX(){
        return this.positionX;
    }

    public int getPositionY(){
        return this.positionY;
    }

    public InfoCardinal getSideInfo() {
        //TODO
    }
}
