package controllers;

import static models.Fixtures.users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.User;
import models.Friend;



public class FriendsTest {

	
	  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  User user = new User("homer", "simpson", "homer@simpson.com", "secret");
      Friend friend = new Friend ("homer@simpson.com");
      Friend friend2 = new Friend ("homer@simpson");
 	 
      
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
	    User user3 = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password);
	    Friend friend3 = pacemaker.createFriend(friend.email);
	    assertEquals(friend3.email, user3.email);

	    assertNotEquals(friend2.email, user3.email);
	    
	    
	  }
	
	  @Test
	  public void testCreateFriends() {
		  
		  pacemaker.deleteFriends();
		  
		  users.forEach(
	        user -> pacemaker.createFriend( user.email));
	    Collection<Friend> returnedFriends = pacemaker.getFriends();
	    assertEquals(users.size(), returnedFriends.size() );
	  
	  
	   // users.forEach(
	   //         user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	   //     Collection<User> returnedUsers = pacemaker.getUsers();
	   //     assertEquals(users.size(), returnedUsers.size());
	  
	  }


		@Test
		   public void testCountsForFriendDeletes ()
		   {
			  User user6 = pacemaker.createUser("c", "c", "mark@smith.com", "c");
			  
			int count = pacemaker.getFriends().size() ;
			
			assertEquals(count , pacemaker.getFriends().size());   
			
			pacemaker.createFriend( "mark@smith.com");
			assertEquals (count + 1, pacemaker.getFriends().size());
			pacemaker.deleteFriend("mark@smith.com");
     	  	assertEquals(count ,  pacemaker.getFriends().size());   

     	  	pacemaker.deleteFriend("mark@xx.com");
     	  	assertEquals(count ,  pacemaker.getFriends().size());   

     	   pacemaker.deleteFriends();
     		assertEquals(0 ,  pacemaker.getFriends().size());   
		   }
	
	  
		@Test
		   public void testForDuplicateFriends ()
		   {
			int count = pacemaker.getFriends().size() ;

			pacemaker.createFriend( "homer@simpson.com");
			pacemaker.createFriend( "homer@simpson.com");
			assertNotEquals (count + 2, pacemaker.getFriends().size());
		   }	
}
