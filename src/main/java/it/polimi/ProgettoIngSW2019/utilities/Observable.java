package it.polimi.ProgettoIngSW2019.utilities;

import java.util.ArrayList;


public abstract class Observable <T>{

    private ArrayList<Observer<T>> observers;


    public void addObserver(Observer<T> observer){
        /*
        synchronized (observers) {
            observers.add(observer);
        }
        */
    }

    public void removeObserver(Observer<T> observer){
        /*
        synchronized (observers) {
            observers.remove(observer);
        }
        */
    }

    protected void notify(T message){
        /*
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
        */
    }

}
