package main;

import flakiness.CheckFlakiness;
import utils.ResetAppState;

public class CheckSuiteFlakiness {

    public static void main(String[] args){
        ResetAppState.reset();

        CheckFlakiness checkFlakiness = new CheckFlakiness();
        checkFlakiness.check();
    }
}
