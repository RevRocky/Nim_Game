package org.weeklybeat;

import java.util.Random;
import java.util.Scanner;
import org.weeklybeat.InvalidInputError;
import java.util.InputMismatchException;

/*
* This package contains utilities for the Nim Game! To be put in this package, a method has to be judged
* to be general purpose enough that this game of Nim is not the only place where these kind of functionalities
* will be important. Eventually I will plan to turn this into a couple of utility packages each being more carefully
* curated.
*/
class nim_utils{
    /*
    * This method returns a random integer between min and max (inclusive)!
    */
    public static int randInt(int min, int max){
        // Initialising our random number generator
        Random randy = new Random();
        // Now we obtain our number and return it
        int rand_num = randy.nextInt((max-min) + 1) + min;
        return rand_num;
    }

    /* This method "flips a coin". If the coin flip matches
	* The selection of the user... it will return true.
	*
	* Selection will either be a string that reads 'h' or 't'
	*/
    public static boolean coin_flip(String selection) throws InvalidInputError {
        //TODO Maybe I'll have to do some preprocessing to make sure selection and result are of the same form. We will see

        // Initialising the value of my result so it wont be limited to the scope of the conditional
        String result           = "null";
        boolean valid_selection = (selection.equals("h") || selection.equals("t")); // Ensuring we have a proper selection

        // Here we're going to check to make sure that we have a proper selection. This is only really a concern
        // if we are dealing with player input!
        if (!valid_selection){
            throw new InvalidInputError("Please input a either Heads or Tails\nNote: it is not case sensitive <3");
        }
        // I'm sure there is a better way to do this but I don't see the point of initialising a new variable
        // when I can just do the function call in my conditional header
        if (randInt(0, 1) == 0) {
            result = "h";
        } else {   // The flip is a one
            result = "t";
        }
        // Now we compare the result to the selection, returning true if there is a match, otherwise returning false.
        return (result.equals(selection));
    }

    //This takes a string from the user and shortens it to the first character
    public static String FirstCharInput(String prompt, Scanner screen){
        String user_input;     // We want to be as general purpose with our variable names here

        System.out.printf("%s \n> ", prompt);       // Printing our prompt
        user_input = screen.nextLine().substring(0,1);   // We are only concerned with the first character of the string
        user_input = user_input.toLowerCase();   // We want that lowercase!

        return user_input;
    }

    public static int getInt(int min_val, int high_val, String prompt, Scanner screen) {
		/*
		* This method obtains an integer from the prompting the user
		* in the Console. It makes an assumption that a scanner has been instantiated in the calling environment!
		*/

        // Now we are going to prepare for a while loop where we attempt to get the
        // proper input from the user
        int user_num = 0; // Remember to instantiate variables we want to modify in a new scope
        String junk_input = null;

        // Let's loop this mother fucker
        while (true){ // We WILL eventually get valid input so we will be able to return this'ere thang!
            System.out.printf(prompt);

            // Now we do our error handling
            try{
                user_num = screen.nextInt();
                junk_input = screen.nextLine();

                // What if their input falls outside the acceptable range?
                // Throw an error at those bastardos!
                if (user_num > high_val || user_num < min_val){
                    throw new InvalidInputError(user_num + " is not between " + min_val + " and "
                            + high_val);
                }
                return user_num;
            }
            catch (InputMismatchException e){
                System.out.println("You did not type an integer. Please Try Again");
            }
            catch (InvalidInputError e){
                System.out.println("Please Try Again");
            } // end try-catch
        } // end while
    } // end getInt

    /* Apparently Java can not do logarithms in what ever base... Seriosuly, you think this would be a part of the
    * bloody default math package.... (This is why python is BAE). Anywhey, this just takes the approach of calculating
    * the log base x of Y by dividing the natural log of Y by the natural log of x.
    * This version of the method only handles integers. Should I ever incorporate it into a maths 'class' it will get
    * more robust versions of it!
    */
    public static double logAnyBase(int input, int base){
        return(Math.log(input)/ Math.log(base));    // Simple application of the base change formula!
    }



}