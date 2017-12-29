package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.User;
import models.Friend;
import models.Message;


public class MessageTest {

	
	  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  User user = new User("homer", "simpson", "homer@simpson.com", "secret");
      Friend friend = new Friend ("homer@simpson.com");
	  
	  // Relates to the Registration of a User
	  
	  @Before
	  public void setup() {
	    pacemaker.deleteUsers();
	  }

	  @After
	  public void tearDown() {
	  }
	  
	
	  @Test
	  public void testCreateFriend() {
	    User user2 = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password);
	    Friend friend2 = pacemaker.createFriend(friend.email);
	    assertEquals(friend2.email, user2.email);

	   // Duplicate Fails
	    assertNotNull(pacemaker.createFriend("homer@simpson.com"));
	  }
	  
	
	  @Test
	  public void testCreateMessage() {
	      Friend friend = new Friend ("homer@simpson.com");
		  Message message = pacemaker.createMessage(friend.id, "Test");
	//    assertEquals(message.id, friend.id);

	//    assertEquals(message.messages, "Test");
	//    assertNotEquals(message.messages, "NoTest");

        
	  }
	  
	
	
	
	
	
}
