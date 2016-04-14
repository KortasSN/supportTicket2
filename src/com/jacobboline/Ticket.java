package com.jacobboline;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Ticket implements Serializable {


    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private static int staticTicketIDCounter = 1;
    protected int ticketID;
    private Date dateResolved;
    private String resolutionDescription;

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

    protected String getDescription() {
        return description;
    }

    protected void setDateResolved(Date newDate) {
        this.dateResolved = newDate;
    }

    protected Date getDateResolved() {
        return this.dateResolved;
    }

    protected void setResolutionDescription(String resolutionDescription) {
        this.resolutionDescription = resolutionDescription;
    }

    protected String getResolutionDescription() {
        return this.resolutionDescription;
    }

    protected String getReporter() {
        return reporter;
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

    public void addToResolutionList(ArrayList<Ticket> resolvedTicketList) {

        Scanner resolvedScanner = new Scanner(System.in);
        System.out.println("Enter a description of why the ticket is being resolved");
        String resolution = resolvedScanner.next();
        while (resolution.isEmpty()) {
            System.out.println("An empty description is not a description.");
            resolvedScanner.hasNext();

        this.setResolutionDescription(resolution);
        //Date newDate = new Date();
        this.dateResolved = new Date();
        }


    }


}
