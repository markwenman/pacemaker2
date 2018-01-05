package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import models.User;

public class LoginTest {

	PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  
	  private User loggedInUser = null;

	
	  
	  @Before
	  public void setup() {
	    pacemaker.deleteUsers();
	  }

	  @After
	  public void tearDown() {
	  }
	  
@Test	  
public void checkForValidLoginDetails()
{
	User a = pacemaker.createUser("a", "a", "a@a", "a");
	User b = pacemaker.createUser("b", "b", "b@b", "b");

	 loggedInUser = pacemaker.getUserByEmail("a@a");
        
        assertEquals(loggedInUser.getId(), a.id);
        assertEquals(loggedInUser.getEmail(), a.email);
        assertEquals(loggedInUser.getLastname(), a.lastname);
        assertEquals(loggedInUser.getFirstname(), a.firstname);

        assertNotEquals(loggedInUser.getId(), b.id);
        assertNotEquals(loggedInUser.getEmail(), b.email);
        assertNotEquals(loggedInUser.getLastname(), b.lastname);
        assertNotEquals(loggedInUser.getFirstname(), b.firstname);

        
}
	
@Test	  
public void checkInvalidLogin()
{
	 pacemaker.createUser("a", "a", "a@a", "a");

	 loggedInUser = pacemaker.getUserByEmail("a@a");
       
       assertNotEquals(loggedInUser.getEmail(), "b@b");
      
       
}


@Test	  
public void checkDuplicateLogin()
{
	User a = pacemaker.createUser("a", "a", "a@a", "a");

	 loggedInUser = pacemaker.getUserByEmail("a@a");
      

    	User b = pacemaker.createUser("b", "b", "b@b", "b");

        
    	if (loggedInUser.equals(null) || loggedInUser.equals(""))
    	{
    		 loggedInUser = pacemaker.getUserByEmail(b.email);
	}              
        assertEquals(loggedInUser.getId(), a.id);
        assertNotEquals(loggedInUser.getEmail(), "b@b");
       
}





}
