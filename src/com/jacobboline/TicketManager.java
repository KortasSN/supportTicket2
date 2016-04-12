package com.jacobboline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class TicketManager {

    public static void main(String[] args) {



        LinkedList<Ticket> ticketQueue = new LinkedList<>();

        Scanner scan = new Scanner(System.in);

        while(true){

            System.out.println("1. Enter Ticket\n2. Delete Ticket\n3. Display All Tickets\n4. Quit");
            int task = scan.nextInt();

            if (task > 4 || task < 1) {
                System.out.println("Enter a valid Integer please.");
                scan.hasNextInt();
            }

            if (task == 1) {
                System.out.println("Task 1");
                addTickets(ticketQueue);
            }

            if (task == 2) {
                System.out.println("Task 2");
                deleteTicket(ticketQueue);
            }
            if (task == 3) {
                System.out.println("Task 3");
                if (ticketQueue.isEmpty()) System.out.println("Ticket queue is empty.");
                else printAllTickets(ticketQueue);
            }
            if ( task == 4) {
                System.out.println("Task 4");
                writeTaskFile(ticketQueue);
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            }

        }

        scan.close();

    }

    private static void writeTaskFile(LinkedList<Ticket> ticketQueue) {

        File unresolvedTickets = new File("unresolvedTickets.txt");


        try{
            BufferedWriter bW = new BufferedWriter(new FileWriter("unresolvedTickets.txt"));
            for (Ticket ticket : ticketQueue) {
                bW.write(ticket.toString());
                bW.newLine();
            }
            System.out.println("bW has run its course.");
        }
        catch (IOException ioe) {
            System.out.println("Could not access txt file");

        }



    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {

        if (ticketQueue.isEmpty()) System.out.println("Ticket Queue is empty, no tickets to delete.");

        else {
            printAllTickets(ticketQueue);
            //The user can see a list of tickets, can identify the ticketID of the ticket to be deleted.

            Ticket ticketToRemove = null;
            /* Creates null ticket,
            used as placeholder until the ticket for removal is identified and applied to this value.

             This could be done without, by applying this variable name to the ticket helps for reading the code
             Otherwise, the for loop below could simply be :
             for (Ticket ticket : ticketQueue) if (ticketID == ticket.getTicketID()) ticketQueue.remove(ticket);
             However, we would then not be able to call the the ticket in the following print statement.
             */

            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the TicketID of the Ticket to be deleted.");
            int ticketID = sc.nextInt();
            if (ticketID > 0) System.out.println("Input Accepted. Checking Queue.");
            else {
                System.out.println("Invalid input. Try again");
                sc.hasNextInt();
            }


            for (Ticket ticket : ticketQueue) if (ticketID == ticket.getTicketID()) ticketToRemove = ticket;

                if (ticketToRemove == null) System.out.println("No Matching TicketID was found");
                else System.out.println(ticketToRemove.ticketID + " has been removed.");
                ticketQueue.remove(ticketToRemove);
        }
    }

    //Move the adding ticket code to a method
    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        System.out.println("Reached addTickets.");
        Scanner sc = new Scanner(System.in);

        boolean moreProblems = true;
        String description, reporter;   //Description of problem, name of person who submitted the ticket
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){

            System.out.println("Enter problem");
            description = sc.nextLine();
            while (description.equals("")) {
                System.out.println("Description cannot be empty, try again.");
                sc.hasNext();
            }

            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            while (reporter.equals("")) {
                System.out.println("Reported by cannot be empty, try again.");
                sc.hasNext();
            }

            System.out.println("Enter priority of " + description + "\nEnter 1(Lowest) - 5(Highest)");
            priority = Integer.parseInt(sc.nextLine());
            while ((priority > 5) || (priority < 1)) {
                System.out.println("Valid priority range is 1-5, try again.");
                sc.hasNextInt();
            }

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            addTicketInPriorityOrder(ticketQueue, t);


            System.out.println("More tickets to add? (Y or N)");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) moreProblems = false;

            //To test, let's print out all of the currently stored tickets
            printAllTickets(ticketQueue);
        }

    }
    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) System.out.println(t.toString());

        System.out.println(" ------- End of ticket list ----------");

    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }

}

