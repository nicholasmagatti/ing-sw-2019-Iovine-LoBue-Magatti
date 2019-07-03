package it.polimi.ProgettoIngSW2019.common.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Observable <T>{

    private List<Observer<T>> observers = new ArrayList<>();


    public synchronized void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    protected synchronized void notify(T message){
        List<Observer<T>> copyList = copy(observers);

        for (Observer<T> observer: copyList) {
            observer.update(message);
        }
    }


    public List<Observer<T>> copy(List<Observer<T>> obsList){
        List<Observer<T>> cloneList = new ArrayList<>();
        cloneList.addAll(obsList);

        return cloneList;
    }
}
