package it.polimi.ProgettoIngSW2019.common.utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable <T>{

    private List<Observer<T>> observers = new ArrayList<>();


    public void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    protected void notify(T message){

        for(Observer<T> observer : observers){
            observer.update(message);
        }
    }

}
