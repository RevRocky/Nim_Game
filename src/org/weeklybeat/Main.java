package org.weeklybeat;

import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.Scanner;
/*
* The following is a simple command line based programme that allows the user to play a game of nim against their computer.
* Such fun. I know, rite!
*/
public class Main {


    static int marbles_left;                            // Declaring the amount of marbles left
    static boolean smarty_pants;                        // Declaring a boolean to hold whether or not the robot is a smarty-pants!
    static boolean player_first;                         // A bool that tells the programme if the player is going first or not
    static Scanner screen = new Scanner(System.in);     // Initialising our scanner.
    static boolean player_win;                          // This tells if the player has won or not. (I'm doing this messily)
    static boolean one_marble_left;                     // Is there one marble left?
    static String turn_manager_state;                   // Stores the state of the turn manager!


    //TODO Here I want to declare a two element string array which will hold the initiative order for the game!

    // I think it's also wise to have our scanner be a class attribute as it will be used in multiple different methods
    // in this programme!

    public static void main(String[] args){
        String play_again;  // Does the user want to play again?

        while(true){    // We can play ad infinitum.
            new_game();     // Initialises a new game
            game_time();    // Manages the main game
            System.out.println("Would you like to play again?\nType y to play again");
            play_again = screen.nextLine();         // Obtaining that sweet input
            play_again = play_again.substring(0,1); // What if they type 'yes'?
            if (!play_again.equals("y")){           // Anything other than a y is not wanted
                break;
                // How would I safely exit the programme
            }
        }
    }

    /*
    * This method handles the creation of a new game.
    * in order it:
    *
    * 1) Initialises the amount of marbles
    * 2) Flips a coin to see who will go first
    * 3) Flips another coin to see how smart the robot will be
    */
    public static void new_game() {
        // Initialising marble count and reporting it to the user
        marbles_left        = nim_utils.randInt(12, 20);
        one_marble_left     = false;    // Reinitialising our game state boolean!
        boolean valid_input = false;

        System.out.printf("There will be %d marbles in this game!\n", marbles_left);
        // We want this in a try catch block in case the user gives a kind of input we don't like.
        while(!valid_input) {
            try {
                String user_selection = nim_utils.FirstCharInput("Turn order will be established by a coin flip.\n" +
                        "Please select Heads or Tails", screen);
                System.out.println("A coin will now be flipped to determine who will play first");
                player_first = nim_utils.coin_flip(user_selection);     // Determining who goes first

                //There is likely a better way to handle this than a nasty conditional but, here goes...
                if (player_first) {
                    System.out.println("You have won the coin toss and as such will play first!");
                } else {  // The player is not going first
                    System.out.println("The computer has won the coin-toss and thus, you will play second...");
                } // end else
                valid_input = true;     // So we can exit our loop
            } catch (InvalidInputError e) {
                System.out.println("Please Try Again");
            } // end try-catch
        } // end while

        // Now we determine whether or not the computer will play smart or not.
        // TODO determine whether or not this is something worth conveying to the user... or should it be a... mystery?
        try{
            smarty_pants = nim_utils.coin_flip("t");    // The computer WILL ALWAYS play Tails
        }
        catch(InvalidInputError e){    // This will LITERALLY never happen
            ; // Do bloody nothing
        }   // end try-catch
    }   // End new_game

    /*
    * This method simply will flip back between the turn of whomever is going first
    * and the user who is going second playing their turns. Once the pile gets down to
    * nothing it will need to trigger a dialogue asking if the user wants to continue
    * playing. If they want to keep playing, the game will go into the new game method
    */
    public static void game_time(){
        // Two different initiative orders that we can have
        if (player_first){
            turn_manager_state = "player";
            }
        else{
            turn_manager_state = "computer";
        } // end if-else
        while(!one_marble_left){
            turnManager();
        }
        // Now we print out a message giving the results of the game
        if (player_win){
            System.out.println("You're Winner!");
        }
        else {
            System.out.println("You have lost. This is bad and you should feel bad!");
        }

    }

    // This calls the correct turn based on the state of the programme. The turn is completed and the turn state
    // then swaps to the other player.
    public static void turnManager(){
        if (turn_manager_state.equals("player")){
            player_turn();
            turn_manager_state = "computer";     // It will now be the computer's turn
        }
        else{
            computer_turn();
            turn_manager_state = "player";
        }   // end if-else
    }

    // Gets the amount of marbles the player wants to take away and... makes appropriate adjustments to the game space!
    public static void player_turn(){

        int user_choice     = 0;     // Initialising the user choice variable to be zero

        if (marbles_left == 1){
            one_marble_left = true;
            player_win = false;      // Player has one marble on his turn
            return;
        }
        // Arguments are minimum acceptable value, max value and the prompt we give the user!
        user_choice = nim_utils.getInt(1, marbles_left / 2, "How many marbles do you want to pick. " +
                                        "\nPlease pick a number between " + 1 + " and " + marbles_left/2 + "\n> ",
                                        screen);
        marbles_left -= user_choice;    // Updating how many marbles there are left!
        System.out.printf("There are now %d marbles remaining\n", marbles_left);
    }

    // This method handles the computer's turn. It branches a bit based upon whether the computer is playing smart or
    // not!
    public static void computer_turn(){
        // First we will check to ensure that there are still marbles left
        if (marbles_left == 1){
            one_marble_left = true;
            player_win = true;  // Computer has one marble left on her turn
            return;
        } // end if

        // Now we go on to initialising our variables
        int computer_choice =   0;

        System.out.println("It is now the computer's turn");
        if (smarty_pants){
            // If we can't find an optimal move, an error will be thrown by our method that finds the optimal
            // move and the computer will pick randomly!
            try{
                computer_choice = findOptimalMove();
            } catch(NoOptimalMoveError e){
                computer_choice = nim_utils.randInt(1, marbles_left/2);  // Picks a random number in the acceptable range
            } // end try-catch
        } else{ // The computer is scatterbrained!
            computer_choice = nim_utils.randInt(1, marbles_left/2);  // Picks a random number in the acceptable range
        } // end if-else
        marbles_left -= computer_choice;
        System.out.printf("The computer uses it's robot hand to pick %d marbles. There are now %d marbles left.\n",
                        computer_choice, marbles_left);
    }

    /*
    In the game of nim any optimal move will leave the pool with 2^n - 1 marbles. This method tries to find an optimal
    move for the computer by taking the log (base 2) of the amount of marbles left. This tells what the appropriate
    power of two we are dealing with is. Log(2) = 4 means that the optimal move will result in (2 ^ 4) - 1 = 15 marbles
    It will then check to see if there is a move that can be made to get to the appropriate amount of marbles. Or
    flag an error
    */
    private static int findOptimalMove() throws NoOptimalMoveError{
        int power_of_two;       // The appropriate power of two for our game state
        int optimal_result;     // The optimal move. One less than 2^(power of two)
        int difference;         // Difference between the current amount of marbles left and optimal mover

        power_of_two = (int)nim_utils.logAnyBase(marbles_left, 2);  // Look at that sexy cast!
        optimal_result = ((int)Math.pow(2, (power_of_two)) - 1);   // Obtaining our optimal result here
        difference = marbles_left - optimal_result;     // Obtaining our difference

        if (1 <= difference && difference <= (marbles_left / 2)){
            return difference;      // We have found an optimal move!
        }
        else{   // No optimal move exists that our algorithm can find!
            throw new NoOptimalMoveError();
        }   // end if-else
    } // end findOptimalMove
}
