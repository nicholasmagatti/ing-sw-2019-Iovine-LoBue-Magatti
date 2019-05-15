package it.polimi.ProgettoIngSW2019.common;

import it.polimi.ProgettoIngSW2019.common.enums.EventType;

import java.io.Serializable;

public class Event implements Serializable {
    private EventType command;
    private String messageInJsonFormat;

    public Event(EventType command, String messageInJsonFormat){
        this.command = command;
        this.messageInJsonFormat = messageInJsonFormat;
    }

    public EventType getCommand(){
        return this.command;
    }

    public String getMessageInJsonFormat(){
        return messageInJsonFormat;
    }
}