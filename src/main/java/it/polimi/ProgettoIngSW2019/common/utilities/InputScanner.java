package it.polimi.ProgettoIngSW2019.common.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputScanner {
    private String input = "";
    private boolean timeExpired = false;
    private Thread inputThread = new Thread(new UserInputRunnable());
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(isr);

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
            inputThread.start();
            inputThread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Interrupt the thread and block user input
     *
     * @author: Luca Iovine
     */
    public void close(){
        try{
            inputThread.interrupt();
        }catch(Exception e){
            e.printStackTrace();
        }
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