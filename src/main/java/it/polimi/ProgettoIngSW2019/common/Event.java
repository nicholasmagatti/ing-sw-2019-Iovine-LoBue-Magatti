package it.polimi.ProgettoIngSW2019.common;

import it.polimi.ProgettoIngSW2019.common.enums.EventType;

import java.io.Serializable;

/**
 * Event use to send information through the network
 */
public class Event implements Serializable {
    private EventType command;
    private String messageInJsonFormat;

    /**
     * Constructor
     * @param command
     * @param messageInJsonFormat
     */
    public Event(EventType command, String messageInJsonFormat){
        this.command = command;
        this.messageInJsonFormat = messageInJsonFormat;
    }

    /**
     * Get the event type to identify the event
     * @return event type to identify the event
     */
    public EventType getCommand(){
        return this.command;
    }

    /**
     * Json message to convert in an object when arrived at destination
     * @return json message
     */
    public String getMessageInJsonFormat(){
        return messageInJsonFormat;
    }
}