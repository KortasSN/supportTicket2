package com.jacobboline;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketManager {

    public static void main(String[] args) {


        LinkedList<Ticket> ticketQueue = new LinkedList<>();
        ArrayList<Ticket> resolvedTicketList = new ArrayList<>();

        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.println(
                    "1. Enter Ticket\n" +
                            "2. Delete Ticket by ID\n" +
                            "3. Delete Ticket by Issue\n" +
                            "4. Search Tickets by Name\n" +
                            "5. Display All Tickets\n" +
                            "6. Quit");

            int task = scan.nextInt();

            if (task > 6 || task < 1) {
                System.out.println("Enter a valid Integer please.");
                scan.hasNextInt();
            }

            if (task == 1) {
                System.out.println("Task 1");
                addTickets(ticketQueue);
            }

            if (task == 2 || task == 3 || task == 4) {
                System.out.println("Task 2, 3, or 4");
                //deleteTicket(ticketQueue);
                findTicket(task, ticketQueue, resolvedTicketList);
            }

            if (task == 5) {
                System.out.println("Task 5");
                if (ticketQueue.isEmpty()) System.out.println("Ticket queue is empty.");
                else printAllTickets(ticketQueue);
            }
            if (task == 6) {
                System.out.println("Task 6");
                writeTaskFiles(ticketQueue, resolvedTicketList);
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            }

        }

        scan.close();

    }

    private static void findTicket(int task, LinkedList<Ticket> ticketLinkedList, ArrayList<Ticket> resolvedTicketList) {

        switch (task) {

            case (2): {

                Scanner idScanner = new Scanner(System.in);
                System.out.println("Enter the ticketID of the ticket to be deleted.");
                int ticketID = idScanner.nextInt();

                while (ticketID < 1) {
                    System.out.println("Please enter a positive integer as a ticketID");
                    idScanner.hasNextInt();
                }

                for (Ticket ticket : ticketLinkedList) {
                    if (ticket.ticketID == ticketID) {
                        ticket.addToResolutionList(resolvedTicketList);
                        ticketLinkedList.remove(ticket);
                        System.out.println(
                                "The ticket with a ticketID of " +
                                        ticketID + " has been removed from the ticket queue");
                    } else System.out.println("No matching ticketID found in ticket queue.");

                }

                break;
            }

            case (3): {


                ArrayList<Integer> matchingTicketIDs = new ArrayList<>();
                Scanner descriptionScanner = new Scanner(System.in);


                System.out.println("Enter a term to search the ticket queue by the description of the issue.");
                String descriptionPhrase = descriptionScanner.next();
                while (descriptionPhrase.isEmpty()) {
                    System.out.println("The search phrase/keyword cannot be empty.");
                    descriptionScanner.hasNext();
                }

                Pattern pattern = Pattern.compile(descriptionPhrase);

                for (Ticket ticket : ticketLinkedList) {
                    /*Matcher matcher = pattern.matcher(ticket.getDescription());
                    boolean matches = matcher.matches();*/

                    //if (matches)
                    if (ticket.getDescription().contains(descriptionPhrase)) {
                        matchingTicketIDs.add(ticket.ticketID);
                        System.out.println(
                                "Ticket Description Match:\n" +
                                        "Description: " + ticket.getDescription() + "\n" +
                                        "TicketID: " + ticket.ticketID);
                    }
                }

                if (matchingTicketIDs.size() > 0) {

                    int validSelection = deleteTicketDecision(matchingTicketIDs, ticketLinkedList, resolvedTicketList);

                    while (validSelection == 1)
                        deleteTicketDecision(matchingTicketIDs, ticketLinkedList, resolvedTicketList);

                } else System.out.println("No matches were found.");

                break;
            }

            case (4): {

                Scanner nameScanner = new Scanner(System.in);
                System.out.println("Enter the reporter's name you wish to find any tickets with a matching reporter.");
                String namePhrase = nameScanner.next();
                //Pattern pattern = Pattern.compile(namePhrase);
                //ArrayList<String> matchingTicketNames = new ArrayList<>();
                ArrayList<Integer> matchingNameTicketIDs = new ArrayList<>();

                while (namePhrase.isEmpty()) System.out.println("You know a name contains letters, right?");

                //Matcher matcher = pattern.matcher(ticket.getReporter());
                //boolean matches = matcher.matches();
                //matchingTicketNames.add(ticket.getReporter());

                ticketLinkedList.stream().filter(
                        ticket -> ticket.getReporter().contains(namePhrase)).forEach
                        (ticket ->
                        {
                            //matchingTicketNames.add(ticket.getReporter());
                            matchingNameTicketIDs.add(ticket.ticketID);
                            System.out.println(
                                    "Ticket Reporter Match:\n" +
                                            "Description: " + ticket.getDescription() + "\n" +
                                            "Name: " + ticket.getReporter() + "\n" +
                                            "TicketID: " + ticket.ticketID);
                        });

                if (matchingNameTicketIDs.size() >= 1) {
                    int validSelection = deleteTicketDecision(matchingNameTicketIDs, ticketLinkedList, resolvedTicketList);
                    while (validSelection == 1)
                        deleteTicketDecision(matchingNameTicketIDs, ticketLinkedList, resolvedTicketList);
                } else System.out.println("No matches were found.");

                break;
            }

        }

    }


    private static void writeTaskFiles(LinkedList<Ticket> ticketQueue, ArrayList<Ticket> resolvedTicketList) {

        //File unresolvedTickets = new File("unresolvedTickets.txt");

        try {
            for (Ticket ticket : ticketQueue) {
                serialize(ticket, "unresolvedTickets.txt");
            }
        } catch (IOException iOE) {
            System.out.println("Problem finding unresolved tickets text file: " + iOE.getMessage());
        }

        try {
            for (Ticket resolvedTicket : resolvedTicketList) {
                serialize(resolvedTicketList, "resolvedTickets.txt");
            }
        } catch (IOException iOE) {
            System.out.println("Problem finding resolved tickets text file: " + iOE.getMessage());
        }


    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue, ArrayList<Ticket> resolvedTicketList) {

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
            ticketToRemove.addToResolutionList(resolvedTicketList);
            ticketQueue.remove(ticketToRemove);
        }
    }


    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        System.out.println("Reached addTickets.");
        Scanner sc = new Scanner(System.in);

        boolean moreProblems = true;
        String description, reporter;   //Description of problem, name of person who submitted the ticket
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems) {

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

        for (Ticket t : tickets) System.out.println(t.toString());

        System.out.println(" ------- End of ticket list ----------");

    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket) {

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size(); x++) {    //use a regular for loop so we know which element we are looking at

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

    protected static void serialize(Object obj, String fileName) //get rid of throw
            throws IOException {


        FileOutputStream fos = new FileOutputStream(fileName, true);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();


    }

    public static Object deserialize(String fileName) throws IOException, //get rid of throw
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;

    }

    public static int deleteTicketDecision(

            ArrayList<Integer> matchingTicketIDs,
            LinkedList<Ticket> ticketLinkedList,
            ArrayList<Ticket> resolvedTicketList)
    {
        Scanner delTicket = new Scanner(System.in);
        System.out.println("Would you like to delete one of the matching tickets? (Y or N");
        String userChoice = delTicket.next();


        if (userChoice.equalsIgnoreCase("Y")) {
            System.out.println("Enter the ticketID of the ticket to delete.");
            int ticketIDtoDelete = delTicket.nextInt();

            if (matchingTicketIDs.contains(ticketIDtoDelete)) {

                ticketLinkedList.stream().filter
                        (ticket3 -> matchingTicketIDs.contains(ticketIDtoDelete)).forEach
                        (ticket3 -> ticket3.addToResolutionList(resolvedTicketList));

                System.out.println("Ticket removed from ticket queue.");
                ticketLinkedList.stream().filter
                        ((Ticket ticket2) -> ticket2.ticketID == ticketIDtoDelete).forEach(ticketLinkedList::remove);


            }
            return 0;
        }

        if (userChoice.equalsIgnoreCase("N")) {
            System.out.println("Returning to main menu.");
            return 0;
        } else return 1;

    }


    public static void compareOpenAndClosedTickets() {
       /* try {*/

            /*BufferedReader bRopen = new BufferedReader(new FileReader("unresolvedTickets.txt"));
            BufferedReader bRclosed = new BufferedReader(new FileReader("resolvedTickets.txt"));
            bRopen.*/

            //TODO use the deserialize function to create lists from both text files +
            //TODO if any ticket from the resolved list is found in the unresolved list,
            //TODO remove that ticket from the unresolved text file



       /* } catch (IOException iOE) {
            System.out.println(iOE.getMessage());
        } */
    }



}

