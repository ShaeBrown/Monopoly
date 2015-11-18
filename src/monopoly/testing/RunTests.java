package monopoly.testing;

import monopoly.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTests {
       
    
    /*
        This file will run all test cases defined in TestCase.java under the package monopoly.testing.
    */
    
    public static void main(String[] args) 
    {      
        Result test_result = JUnitCore.runClasses(TestCases.class);
        
        for (Failure test_failures : test_result.getFailures()) 
        {
            System.out.println("A failure was detected. Problem: ");
            System.out.println(test_failures.toString() +"\n");
        }
        System.out.println("Did ALL test cases pass? " +test_result.wasSuccessful());

    }
    
}
