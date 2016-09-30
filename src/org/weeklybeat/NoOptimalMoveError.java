package org.weeklybeat;

// This is raised when our algorithm to find an optimal move that results in (2 ^ n) - 1 marbles left is
// unsuccessful.
class NoOptimalMoveError extends Exception{
    public NoOptimalMoveError(){
    }
}



