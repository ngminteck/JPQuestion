package com;


import com.io.IO;
import com.obj.Show;
import com.screen.AdminScreen;
import com.screen.BuyerScreen;
import com.screen.Screen;

import java.util.*;

public class Main {
    public static void main(String[] args)
    {


        Map<Integer, Show> showList = new TreeMap<>();


        IO io = new IO();

        Screen mainmenu = new Screen();
        AdminScreen adminScreen = new AdminScreen();
        BuyerScreen buyerScreen = new BuyerScreen();

        while(true)
        {
            mainmenu.DisplayOption();
            int mainOption = io.UserIntChoice(mainmenu.DisplayOption(),2);
            if(mainOption == 1)
            {
                while(true)
                {
                    int adminOption = io.UserIntChoice(adminScreen.DisplayOption(),2);
                    if(adminOption == 1)
                    {
                        io.SetupInput(adminScreen.DisplaySetupInstruction(), showList);
                    }
                    else if (adminOption == 2)
                    {
                        int adminViewOption = io.UserShowSelection(adminScreen.DisplayShowList(showList), showList);

                        if(adminViewOption == 0)
                            continue;

                        Show selectedShow = showList.get(adminViewOption);
                        System.out.println(adminScreen.PrintShowNumberHeader(adminViewOption));
                        selectedShow.PrintBuyerInfo();
                        io.ScreenContinue();
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else if (mainOption == 2)
            {
                while(true)
                {
                    int buyerOption = io.UserIntChoice(buyerScreen.DisplayOption(),2);
                    if(buyerOption == 1)
                    {
                        int buyerViewOption = io.UserShowSelection(buyerScreen.DisplayShowList(showList), showList);

                        if(buyerViewOption == 0)
                            continue;

                        Show selectedShow = showList.get(buyerViewOption);
                        String msg = buyerScreen.PrintShowNumberHeader(buyerViewOption) + selectedShow.PrintAllAvailableSeat() + buyerScreen.DisplayBookInstruction();
                        io.BookInput(msg, buyerViewOption,selectedShow);

                    }
                    else if (buyerOption == 2)
                    {
                        io.CancelInput(buyerScreen.DisplayCancelBookingScreenInstruction(), showList);

                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                break;
            }

        }



    }
}