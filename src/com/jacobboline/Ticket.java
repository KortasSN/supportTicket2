package com.jacobboline;

import java.util.Date;

/**
 * Created by g1zmo on 4/8/2016.
 */
public class Ticket {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private static int staticTicketIDCounter = 1;
    protected int ticketID;

    //We can auto-generate get and set methods if and when we need

    //A constructor would be useful

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    protected int getPriority(){
        return priority;
    }

    protected int getTicketID(){
        return this.ticketID;
    }

    //Called automatically if a Ticket object is an argument to System.out.println
    public String toString(){
        return("Ticket ID: " + this.ticketID + " Description : " + this.description + " Priority: " + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }


}
