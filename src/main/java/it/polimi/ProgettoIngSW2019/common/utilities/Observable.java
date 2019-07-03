package it.polimi.ProgettoIngSW2019.common.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Observable <T>{

    private List<Observer<T>> observers = new ArrayList<>();


    public void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    protected void notify(T message){
        Iterator<Observer<T>> it = observers.iterator();
        while(it.hasNext()){
            Observer<T> obs = it.next();
            obs.update(message);
        }
    }

}
