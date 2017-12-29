package controllers;

import static models.Fixtures.users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import models.User;

public class Logins {

	PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  User a = new User("a", "a", "a@a", "a");
	  
	  @Before
	  public void setup() {
	    pacemaker.deleteUsers();
	  }

	  @After
	  public void tearDown() {
	  }
	  
	  
	//  @Test
//	  public void testLogin() {
  //      User success =   pacemaker.getUserByEmail("a@a");
   //     assertEquals(success, true);
	//  
//	  }
	  
	  

	
	
}
