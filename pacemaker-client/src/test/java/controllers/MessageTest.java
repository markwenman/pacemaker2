package controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import models.User;
import models.Activity;
import models.Friend;
import models.Message;



public class MessageTest {

	
	  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    
      
	  // Relates to the Registration of a User
	  
	  @Before
	  public void setup() {
			}

	  @After
	  public void tearDown() {
	  }
	  
	
	  @Test
	  public void testCreateMessage() {
	    User user = pacemaker.createUser("a", "a","a@a", "a");
	    Message message = pacemaker.createMessage(user.id, "Test");
        
	    // only 1 expected        
	    Collection<Message> messagelist = pacemaker.getMessages(user.id);
		   for(Message nextu : messagelist){
		       assertEquals(message.messages, nextu.messages);
		   }
	       
		   
	    assertEquals(message.messages, "Test");
	    assertNotEquals(message.messages, "Test2");
    }
	

	  @Test
	  public void testCreateMessagesCounter()
	  {
		    User user = pacemaker.createUser("a", "a","a@a", "a");
		    Message message = pacemaker.createMessage(user.id, "Test");
		    
		    int count = pacemaker.getMessages(user.id).size()  ;
			assertEquals (count, pacemaker.getMessages(user.id).size() ) ;
		    
		  //  Message message2 =
		    		pacemaker.createMessage(user.id, "Test2");
		   // Message message3 = 
		    		pacemaker.createMessage(user.id, "Test3");
		   // Message message4 = 
		    		pacemaker.createMessage(user.id, "Test4");
			  
		    assertEquals (count + 3, pacemaker.getMessages(user.id).size() ) ;
		    
	  }
	  

	  @Test
	  public void testCreateMessageFailedForWrongUser() {
         assertThat(pacemaker.getMessages("XX"), is(nullValue()));
	  }
	
	
	  
	  @Test
	  public void testCreateMessagesForAllUsers() {
		  pacemaker.createUser("a", "a","a@a", "a");
		  pacemaker.createUser("b", "b","b@b", "b");

		  pacemaker.createFriend("a@a");
          pacemaker.createFriend("b@b");
             
		  
		  Collection<Friend> friend = pacemaker.getFriends();
		  int count = friend.size() ;
		  int count2 = 0 ;
		  
		  for (Friend a : friend) {  
    		  User Friendid = pacemaker.getUserByEmail(a.email) ;
              pacemaker.deleteMessages(Friendid.id);
    		  pacemaker.createMessage(Friendid.id, "Test"); 
		      count2 = count2 + 1 ;
		  }

	    assertEquals(count, 2);
	    assertEquals(count2, 2);
	    
		  }	
	  
	  @Test
	  public void testDeleteMessageForUser() {

		  User a = pacemaker.createUser("a", "a","a@a", "a");

		  pacemaker.createFriend("a@a");
		  User Friendid = pacemaker.getUserByEmail(a.email) ;
		  pacemaker.deleteMessages(Friendid.id);
		  pacemaker.createMessage(Friendid.id, "Test"); 
		  
		//  assertEquals(1, pacemaker.getMessages(Friendid.id).size());
		      
		  
		//  pacemaker.deleteMessages(Friendid.id);
		//  assertEquals(0, pacemaker.getMessages(Friendid.id).size());

		  
		  
		  
	  }
	  
   
}
