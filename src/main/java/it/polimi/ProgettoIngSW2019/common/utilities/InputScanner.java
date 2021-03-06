package it.polimi.ProgettoIngSW2019.common.utilities;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Customized instrument to manage the inputs from the user linked to
 * the timer that will block the input when the timer expires
 */
public class InputScanner extends Observable<Event>{
    private String input = "";
    private boolean timeExpired;
    private boolean keepTimerAlive;
    private Thread inputThread;
    private InputStreamReader isr;
    private BufferedReader br;

    /**
     * Constructor
     */
    public InputScanner(){
        timeExpired = false;
        keepTimerAlive = false;
        isr = new InputStreamReader(System.in);
        br = new BufferedReader(isr);
    }
    /**
     * Class UserInputRunnable
     *
     * @author: Luca Iovine
     */
    private class UserInputRunnable implements Runnable{
        /**
         * Run the process which wait for the user input
         *
         * @author: Luca Iovine
         */
        @Override
        public void run() {
            requestInput();
        }
    }

    /**
     * Request the input through a buffer reader.
     * If the thread which call this method has been interrupted, a timeExpired flag go up.
     *
     * @author: Luca Iovine
     */
    private void requestInput(){
        try{
            while(!br.ready())
                Thread.sleep(200);
            input = br.readLine();
        }catch(IOException e){
            System.out.println("An error was occurred with the input");
        }catch (InterruptedException e){
            timeExpired = true;
        }
    }

    /**
     * Run the thread that start the input request
     *
     * @author: Luca Iovine
     */
    public void read(){
        try {
            if(!keepTimerAlive){
                notify(new Event(EventType.START_ACTION_TIMER, ""));
                keepTimerAlive = true;
            }
            inputThread = new Thread(new UserInputRunnable());
            inputThread.start();
            inputThread.join();
        }catch(InterruptedException e){
            System.out.println("Unexpected behaviour, the input has been interrupted");
        }
    }

    /**
     * Interrupt the thread and block user input
     *
     * @author: Luca Iovine
     */
    public void close(){
        keepTimerAlive = false;
        if(!timeExpired)
            notify(new Event(EventType.STOP_ACTION_TIMER, ""));
        if(inputThread.isAlive())
            inputThread.interrupt();
    }

    /**
     * To check if the time is expired
     *
     * @return true if it is expired, false otherwise
     * @author: Luca Iovine
     */
    public boolean isTimeExpired(){
        return timeExpired;
    }

    /**
     * @return input of the user
     * @author: Luca Iovine
     */
    public String getInputValue(){
        return input;
    }
}