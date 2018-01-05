package controllers;

import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.User;
import static models.Fixtures.users;

public class UserTest {

  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

  
  // Relates to the Registration of a User
  
  @Before
  public void setup() {
    pacemaker.deleteUsers();
  }

  @After
  public void tearDown() {
  }
  
  @Test
  public void testCreateUserValidatingOnEmails() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    assertEquals(user, homer);
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertEquals(user2, homer);
    
    
 
       
    assertEquals(user2.firstname , homer.firstname);
    assertEquals(user2.lastname , homer.lastname);
    assertEquals(user2.email , homer.email);
 

 //   User user3 = pacemaker.getUser(homer.id);
    
    
  // assertEquals(user3, homer);
    

  }

  
  

  
  
  @Test
  public void testCreateUsers() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    assertEquals(users.size(), returnedUsers.size());
  }

  @Test
  public void testFailedUsers() {
	  User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, "XX" );
	      assertNotEquals(user, homer);
	      
	      
	      assertNull (pacemaker.getUser("XX"));
	    }
 

	@Test
	  public void testForValidEmails()
	  {    User homer  = new User ("homer", "simpson", "homer@simpson.com",  "secret");
	       User homer2 = new User ("homer", "simpson", "homersimpson.com",  "secret"); 
	  
	    assertEquals(true, homer.email.toLowerCase().contains("@".toLowerCase()));
	    assertEquals(false, homer2.email.toLowerCase().contains("@".toLowerCase()));
	  }

	
	@Test
	   public void testUserCountsForDeletions ()
	   {
		int count = pacemaker.getUsers().size() ;
		assertEquals (count, pacemaker.getUsers().size());
		
		pacemaker.deleteUsers();
		assertEquals (0, pacemaker.getUsers().size());
		
		User homer = pacemaker.createUser("homer", "simpson", "homer@simpson.com",  "secret");
		assertEquals (1, pacemaker.getUsers().size());
		pacemaker.deleteUser(homer.id);
		assertEquals(0, pacemaker.getUsers().size());   
	   }

	
	@Test
	   public void testForDuplicateUsers ()
	   {
		pacemaker.deleteUsers();
		int count = pacemaker.getUsers().size() ;

		pacemaker.createUser("homer", "simpson", "homer@simpson.com",  "secret");
		pacemaker.createUser("homer", "simpson", "homer@simpson.com",  "secret");
		 assertNotEquals (count + 2, pacemaker.getFriends().size());
	   }

	  @Test
	  public void testForUserString()
	  {
		  User homer = pacemaker.createUser("homer", "simpson", "homer@simpson.com",  "secret");
			     
		  assertEquals("User{"+ homer.id + ", homer, simpson, homer@simpson.com, secret}", homer.toString());
	  }
	
	
}



