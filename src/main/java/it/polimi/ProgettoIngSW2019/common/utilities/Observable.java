package it.polimi.ProgettoIngSW2019.common.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Calss used to notify all the different observers linked to the observable
 * @param <T>
 */
public abstract class Observable <T>{

    private List<Observer<T>> observers = new ArrayList<>();


    /**
     * Add an observer
     * @param observer
     */
    public synchronized void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    /**
     * Notify all the observers linked to this observable
     * @param message
     */
    protected synchronized void notify(T message){
        List<Observer<T>> copyList = copy(observers);

        for (Observer<T> observer: copyList) {
            observer.update(message);
        }
    }


    /**
     * Return a copy of the list of observers.
     * @param obsList
     * @return a copy of the list of observers
     */
    public List<Observer<T>> copy(List<Observer<T>> obsList){
        List<Observer<T>> cloneList = new ArrayList<>();
        cloneList.addAll(obsList);

        return cloneList;
    }
}
