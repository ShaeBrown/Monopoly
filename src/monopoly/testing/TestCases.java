package monopoly.testing;


import monopoly.*;
import junit.framework.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestCases {
       
     
    
    @Test
    public void testCase1() 
    {
        System.out.println("Running Testcase 1");
        Assert.assertEquals(100, 100);
    }
    
    @Test
    public void testCase2() 
    {
        System.out.println("Running Testcase 2");
        Assert.assertTrue(1 == 1);
    }
    
    @Test
    public void testCase3() 
    {
        System.out.println("Running Testcase 3");
        Assert.assertNotSame("string1", "string2");
    }
    
    @Test
    public void testCase4() 
    {
        System.out.println("Running Testcase 4");
        String nullstr = null;
        Assert.assertNotNull(nullstr);  //will fail
    }
     

    
   
    
}
