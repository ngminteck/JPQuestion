package com.io;

import com.obj.Show;
import com.obj.Ticket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class IO
{
    private final Scanner sc;


    public IO()
    {
        sc = new Scanner(System.in).useDelimiter("\n");

    }

    public void ScreenContinue()
    {
        System.out.println("Key any key to continue.");
        sc.next();
    }


    public int UserIntChoice(String msg, int maxValidChoice)
    {
        int userOption;
        do {
            userOption = -1;
            System.out.println(msg);
            if(sc.hasNextInt()) {
                userOption = sc.nextInt();

                if(userOption < 0 && userOption > maxValidChoice)
                    System.out.println("Invalid request!");

            }
            else {
                sc.next();
                System.out.println("Invalid request!");
            }
        }
        while (userOption < 0 && userOption > maxValidChoice);
        return userOption;
    }

    public int UserShowSelection(String msg, Map<Integer, Show> showList)
    {
        int userOption;
        do {
            System.out.println(msg);
            if(sc.hasNextInt()) {
                userOption = sc.nextInt();

                if(userOption == 0 || showList.containsKey(userOption))
                    break;
                else
                    System.out.println("Invalid request!");

            }
            else {
                sc.next();
                System.out.println("Invalid request!");
            }
        }
        while (true);
        return userOption;
    }

    public void SetupInput(String msg, Map<Integer, Show> showList)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;

            if(VerifySetupInput(input, showList))
            {
                System.out.println("Key any key to continue.");
                sc.next();
                break;
            }

        }
        while (true);
    }

    public boolean VerifySetupInput(String input, Map<Integer, Show> showList)
    {
        String[] data = input.split(" ");
        String dataTypeErrorMsg= "";

        if(data.length !=4)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int [] number = new int[4];
        boolean invalidDataType = false;

        /* check correct type data */
        for(int i =0; i < data.length; ++i)
        {
            try
            {
                number[i] = Integer.parseInt(data[i]);
            }
            catch (NumberFormatException ignored)
            {
                invalidDataType = true;
                dataTypeErrorMsg = data[i] + " is not a number.";
                break;
            }
        }
        if(invalidDataType)
        {
            System.out.println(dataTypeErrorMsg);
            return false;
        }

        /* check setup constraint*/
        return VerifySetup(number[0],number[1],number[2],number[3], showList);

    }

    public boolean VerifySetup(int showNumber, int row, int col, int windowMin, Map<Integer, Show> showList)
    {
        /* make show number cannot be less than 1, 0 for exit option.*/
        if(showNumber < 1)
        {
            System.out.println("Show Number cannot be negative or 0.");
            return false;
        }

        if(row<1 || row> 26)
        {
            System.out.println("Row must be between 1 to 26.");
            return false;
        }

        if(col <1 || col > 10)
        {
            System.out.println("Row per seat must be between 1 to 10.");
            return false;
        }

        if(windowMin < 0)
        {
            System.out.println("Cancel window cannot be negative.");
            return false;
        }

        if(showList.containsKey(showNumber))
        {
            System.out.println("Show number " + showNumber + " already exist.");
            return false;
        }

        showList.put(showNumber, new Show(row,col,windowMin));
        System.out.println("Show number:" + showNumber + "\nNumber of Rows:" + row + "\nNumber of seats per row:" + col + "\nCancellation window in minutes:" + windowMin + "\nSetup sucess.");
        return true;
    }


    public void BookInput(String msg, int showNumber, Show show)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;


            boolean status = VerifyUserBookingInput(input, showNumber, show);
            System.out.println("Key any key to continue.");
            sc.next();
            if(status)
             {

                break;
             }

        }
        while (true);
    }

    public boolean VerifyUserBookingInput(String input, int showNumber, Show show)
    {
        String[] data = input.split(" ");
        if(data.length !=2)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int phoneNumber;
        try
        {
            phoneNumber= Integer.parseInt(data[0]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println(data[0] + " is not a number.");
            return false;
        }

        return VerifyBooking(showNumber, phoneNumber, data[1], show);

    }

    public boolean VerifyBooking(int showNumber, int phoneNumber, String seatInput, Show show)
    {
        if(phoneNumber < 0)
        {
            System.out.println("Phone number " + phoneNumber + " cannot be negative.");
            return false;
        }

        if(show.getTicketInfo().get(phoneNumber) != null)
        {
            System.out.println(phoneNumber + " already exist.");
            return false;
        }

        seatInput = seatInput.toUpperCase();
        String[] seat = seatInput.split(",");
        ArrayList <Integer> numberPair= new ArrayList<>();
        /*check valid format*/
        for (String s : seat) {
            if (s.length() == 3 && Character.isAlphabetic(s.charAt(0)) && s.charAt(1) == '1' && s.charAt(2) == '0') {
                if (show.getCol() == 10) {
                    int rowNumber = s.charAt(0) - 64;
                    if (show.getRow() >= rowNumber) {
                        if (show.GetSeatSOccupied(rowNumber - 1, 9)) {
                            System.out.println(s + " seat is unavailable");
                            return false;
                        } else {
                            numberPair.add(rowNumber - 1);
                            numberPair.add(9);
                        }
                    } else {
                        System.out.println("No " + rowNumber + " row number in show number" + showNumber + ". Please review seat " + s + ".");
                        return false;
                    }
                } else {
                    System.out.println("No " + 10 + " column number in show number " + showNumber + ". Please review seat " + s + ".");
                    return false;
                }
            } else if (s.length() == 2 && Character.isAlphabetic(s.charAt(0)) && Character.isDigit(s.charAt(1)) && s.charAt(1) != '0') {
                /* 48 ascii table is 0 */
                int colNumber = s.charAt(1) - 48;

                if (show.getCol() >= colNumber) {
                    int rowNumber = s.charAt(0) - 64;
                    if (show.getRow() >= rowNumber) {

                        if (show.GetSeatSOccupied(rowNumber - 1, colNumber - 1)) {
                            System.out.println(s + " seat is unavailable");
                            return false;
                        } else {
                            numberPair.add(rowNumber - 1);
                            numberPair.add(colNumber - 1);
                        }
                    } else {
                        System.out.println("No " + rowNumber + " row number in show number " + showNumber + ". Please review seat " + s + ".");
                        return false;
                    }
                } else {
                    System.out.println("No " + colNumber + " column number in show number " + showNumber + ". Please review seat " + s + ".");
                    return false;
                }
            } else {
                System.out.println(s + " is invalid format for seat.");
                return false;
            }
        }
        /* mark all available seat occupied */
        for(int i = 0; i < numberPair.size(); i+=2)
        {
            show.setSeatSOccupied(numberPair.get(i),numberPair.get(i + 1), true);
        }

        String ticketText = "Ticker Number:" + showNumber + "-" + show.GenerateNewTicketNumber();
        show.BookShow(phoneNumber, seatInput);
        System.out.println("================================================================================");
        System.out.println("Show Number:" + showNumber);
        System.out.println(ticketText);
        System.out.println("Phone Number:" + phoneNumber);
        System.out.println("Seat:" + seatInput);
        System.out.println("Book Success!");
        System.out.println("================================================================================");
        return true;
    }
    public void CancelInput(String msg, Map<Integer, Show> showList)
    {
        do {
            System.out.println(msg);
            String input = sc.next();

            /*exit*/
            if(input.length() == 1 && input.charAt(0) == '0')
                break;

            if(VerifyUserInputForCancel(input,showList))
            {
                System.out.println("Key any key to continue.");
                sc.next();
                break;
            }

        }
        while (true);
    }

    public boolean VerifyUserInputForCancel(String input, Map<Integer, Show> showList)
    {
        String[] data = input.split(" ");

        if(data.length !=2)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        int phoneNumber;
        try
        {
            phoneNumber= Integer.parseInt(data[1]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println(data[1] + " is not a number.");
            return false;
        }

        String[] no = data[0].split("-");
        if(no.length !=2)
        {
            System.out.println("Invalid number of data input.");
            return false;
        }

        int showNumber;
        try
        {
            showNumber= Integer.parseInt(no[0]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        int ticketNumber;
        try
        {
            ticketNumber= Integer.parseInt(no[1]);
        }
        catch (NumberFormatException ignored)
        {
            System.out.println("Invalid ticket format.");
            return false;
        }

        return VerifyTicket(showNumber,phoneNumber,ticketNumber, showList);


    }
    public boolean VerifyTicket(int showNumber, int phoneNumber, int ticketNumber, Map<Integer, Show> showList)
    {
        /* make show number cannot be less than 1, 0 for exit option.*/
        if(showNumber < 1)
        {
            System.out.println("Phone number " + phoneNumber + " cannot be negative.");
            return false;
        }

        if(phoneNumber < 0)
        {
            System.out.println(phoneNumber + " cannot be negative.");
            return false;
        }

        /* check show exist in the collection or not */
        if(!showList.containsKey(showNumber))
        {
            System.out.println("Show number " + showNumber + " not found.");
            return false;
        }

        Show show = showList.get(showNumber);
        /* check phone number exist in the collection or not */
        if(!show.getTicketInfo().containsKey(phoneNumber))
        {
            System.out.println("Phone number "+ phoneNumber + " not found in show number " + showNumber + ".");
            return false;
        }

        Ticket ticket = show.getTicketInfo().get(phoneNumber);
        if(ticketNumber != ticket.getTicketNo())
        {
            System.out.println("Ticket number not match.");
            return false;
        }

        long diff = ChronoUnit.MINUTES.between(ticket.getDateTime(), LocalDateTime.now());
        if(diff > show.getCancelMinWin())
        {
            System.out.println("Cancel window period exceeded.");
            return false;
        }

        String seatInfo = ticket.getSeatInfo();
        show.CancelTicket(phoneNumber);
        String[] seatData = seatInfo.split(",");
        for (String seat : seatData)
        {
            if(seat.length()==3)
            {
                int rowNumber = seat.charAt(0) - 64;
                show.setSeatSOccupied(rowNumber - 1, 9, false);
            }
            if(seat.length() == 2)
            {
                int rowNumber = seat.charAt(0) - 64;
                int colNumber = seat.charAt(1) - 48;
                show.setSeatSOccupied(rowNumber - 1, colNumber - 1, false);
            }
        }
        System.out.println("Ticket Number:" + showNumber + "-" + ticketNumber + " cancel success.");
        return true;
    }
}

