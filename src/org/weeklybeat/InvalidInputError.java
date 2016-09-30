package org.weeklybeat;

// This is raised whenever we have an invalid input.
class InvalidInputError extends Exception{
    public InvalidInputError(String message){
        super(message);
    }
}


