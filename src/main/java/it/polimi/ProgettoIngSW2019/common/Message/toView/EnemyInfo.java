package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.List;

public class EnemyInfo {
    private int id;
    private String name;
    int[] position;
    private List<int[]> movement;

    public EnemyInfo(int id, String name, int[] position, List<int[]> movement){
        this.id = id;
        this.name = name;
        this.position = position;
        this.movement = movement;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<int[]> getMovement() {
        return movement;
    }
}
