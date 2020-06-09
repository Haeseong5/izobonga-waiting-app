package com.example.izobonga_waiting_app.models;

import java.util.ArrayList;

public class Ticket {
    private int ticket;
    private ArrayList<String> queue;

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public ArrayList<String> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<String> queue) {
        this.queue = queue;
    }
}
