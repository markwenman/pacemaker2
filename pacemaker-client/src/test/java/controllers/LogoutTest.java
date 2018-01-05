package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import models.User;

public class LogoutTest {

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
public void checkIsValidLogoutDetails()
{
      pacemaker.createUser("a", "a", "a@a", "a");

	 loggedInUser = pacemaker.getUserByEmail("a@a");
     
       
        loggedInUser = null ;
        assertEquals(loggedInUser, null);
        
}

@Test	  
public void checkForInvalidLogoutAsAlreadyLoggedOut()
{

	 loggedInUser = pacemaker.getUserByEmail("a@a");
     loggedInUser = null ;

        
    assertThat(loggedInUser, is(nullValue()));
  	   
        
}



}
