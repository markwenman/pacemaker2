package controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.Activity;
import models.Summary;
import models.User;

public class ActivityTest {

  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
 
  @Before
  public void setup() {
    pacemaker.deleteUsers();
    homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);

  }

  @After
  public void tearDown() {
  }
  
  @Test
  public void testCreateActivity() {
    Activity activity = new Activity("walk", "shop", 2.5);
    
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
   
    
    assertEquals(activity.type, returnedActivity.type);
    assertEquals(activity.type, "walk");
    
    assertThat(activity.type, IsEqualIgnoringCase.equalToIgnoringCase("WALK")); 
    
    assertEquals(activity.location, returnedActivity.location);
    assertEquals(activity.distance, returnedActivity.distance, 0.001);
    assertNotNull(returnedActivity.id);
  }
  
  @Test
  public void testGetActivity() {
    Activity activity = new Activity("run", "carlow", 1);
    Activity returnedActivity1 = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
    Activity returnedActivity2 = pacemaker.getActivity(homer.id, returnedActivity1.id);
    assertEquals(returnedActivity1, returnedActivity2);
    
    assertEquals("run", activity.type);
    assertNotEquals("walk", activity.type);

    assertEquals("carlow", activity.location);
    assertEquals(1,0.001, activity.distance);
    
    
    assertEquals("CARLOW", activity.location.toUpperCase());
    
  }
  
  
  @Test
  public void testForString()
  {
	  Activity test = new Activity("jog", "carlow", 1);
	     
	  assertEquals("Activity{"+ test.id + ", jog, carlow, 1.0}", test.toString());
  }
  
  
  
  @Test
  public void testDeleteActivity() {
    Activity activity = new Activity("sprint", "pub", 4.5);
    Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
   
    // assertNotNull (returnedActivity);
    pacemaker.deleteActivities(homer.id);
    returnedActivity = pacemaker.getActivity(homer.id, returnedActivity.id);
    assertNull (returnedActivity);
  }

	@Test
	   public void testActivityCountsForDeletions ()
	   {
		// Activity activity = new Activity("sprint", "pub", 4.5);
		// Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
		 	
		int count = pacemaker.getActivities(homer.id).size() ;
		assertEquals (count, pacemaker.getActivities(homer.id).size());
		
	    pacemaker.deleteActivities(homer.id);
		assertEquals (0, pacemaker.getActivities(homer.id).size());
		
	   }


	@Test
	   public void testActivityforInvalidUser ()
	   {
	    assertThat(pacemaker.getActivities("XXX"), is(nullValue()));   
	   }

	
	
	@Test
	   public void testActivityForReportByType ()
	   {
		
		  
		  pacemaker.createUser("mark", "wenman", "mark@w.com", "secret");

		  
		  
	    User mark = pacemaker.getUserByEmail("mark@w.com");
	    assertEquals (0, pacemaker.getActivities(mark.id).size());

		
     	  pacemaker.createActivity(mark.id, "walk", "Wexford", 1.1);
		  pacemaker.createActivity(mark.id, "run", "Dublin", 5);
		  pacemaker.createActivity(mark.id, "bike", "Kilkenny", 0.4);
	 	  pacemaker.createActivity(mark.id, "walk", "Wiclow", 1.2);
		  pacemaker.createActivity(mark.id, "run", "Carlow", 6);
		  pacemaker.createActivity(mark.id, "run", "Kilkenny", 4.5); 
		  
		  
		  assertEquals (6, pacemaker.getActivities(mark.id).size());

		   ArrayList<Activity> reportActivities = new ArrayList<>();
		      Collection<Activity> usersActivities = pacemaker.getActivities(mark.id);
		      usersActivities.forEach(a -> {
		        if (a.type.equalsIgnoreCase("walk"))
		          reportActivities.add(a);
		      });
		      reportActivities.sort((a1, a2) -> {
		        if (a1.distance >= a2.distance)
		          return -1;
		        else
		          return 1;
		      });

		      assertEquals (reportActivities.get(0).type,  "walk" ) ;
		      assertEquals (reportActivities.get(1).type,  "walk" ) ;
		      assertEquals (reportActivities.get(0).location,  "Wiclow" ) ;
		      assertEquals (reportActivities.get(1).location,  "Wexford" ) ;
		      assertEquals (reportActivities.get(0).distance,  1.2, 0.01 ) ;
		      assertEquals (reportActivities.get(1).distance,  1.1, 0.01 ) ;
		//      assertNull (reportActivities.get(2).type)	;		
		      assertEquals (2, reportActivities.size());
		   

	
	   ArrayList<Activity> reportActivities2 = new ArrayList<>();
	      usersActivities.forEach(a -> {
	        if (a.type.equalsIgnoreCase("run"))
	          reportActivities2.add(a);
	      });
	      reportActivities2.sort((a1, a2) -> {
	        if (a1.distance >= a2.distance)
	          return -1;
	        else
	          return 1;
	      });

	      assertEquals (reportActivities2.get(0).type,  "run" ) ;
	      assertEquals (reportActivities2.get(1).type,  "run" ) ;
	      assertEquals (reportActivities2.get(2).type,  "run" ) ;

	      assertEquals (reportActivities2.get(0).location,  "Carlow" ) ;
	      assertEquals (reportActivities2.get(1).location,  "Dublin" ) ;
	      assertEquals (reportActivities2.get(1).location,  "Dublin" ) ;

	      assertEquals (reportActivities2.get(0).distance,  6.0, 0.01 ) ;
	      assertEquals (reportActivities2.get(1).distance,  5.0, 0.01 ) ;
	      assertEquals (reportActivities2.get(2).distance,  4.5, 0.01 ) ;
	      assertEquals (3, reportActivities2.size());
		   

		   ArrayList<Activity> reportActivities3 = new ArrayList<>();
		      usersActivities.forEach(a -> {
		        if (a.type.equalsIgnoreCase("bike"))
		          reportActivities3.add(a);
		      });
		      reportActivities3.sort((a1, a2) -> {
		        if (a1.distance >= a2.distance)
		          return -1;
		        else
		          return 1;
		      });

		      
		      
		      
		      
		      
		      
		      
		      
		      assertEquals (reportActivities3.get(0).type,  "bike" ) ;

		      assertEquals (reportActivities3.get(0).location,  "Kilkenny" ) ;

		      assertEquals (reportActivities3.get(0).distance,  0.4, 0.01 ) ;
		      assertEquals (1, reportActivities3.size());
		      assertNotEquals (0, reportActivities3.size());
		      assertNotEquals (2, reportActivities3.size());

	      
	      
	      
	      
	      
	   } 


	@Test
	   public void testActivityForReport ()
	   {
		
		  
		  pacemaker.createUser("jackie", "wenman", "jackie@w.com", "secret");

		  
		  
	    User jackie = pacemaker.getUserByEmail("jackie@w.com");
	    assertEquals (0, pacemaker.getActivities(jackie.id).size());

		
  	  pacemaker.createActivity(jackie.id, "walk", "Wexford", 1.1);
		  pacemaker.createActivity(jackie.id, "run", "Dublin", 5);
		  pacemaker.createActivity(jackie.id, "bike", "Kilkenny", 0.4);
	 	  pacemaker.createActivity(jackie.id, "walk", "Wiclow", 1.2);
		  pacemaker.createActivity(jackie.id, "run", "Carlow", 6);
		  
		  
		  assertEquals (5, pacemaker.getActivities(jackie.id).size());

		   ArrayList<Activity> reportActivities = new ArrayList<>();
		      Collection<Activity> usersActivities = pacemaker.getActivities(jackie.id);
		      usersActivities.forEach(a -> {
		          reportActivities.add(a);
		      });
		      reportActivities.sort((a1, a2) -> {
		        if (a1.distance >= a2.distance)
		          return -1;
		        else
		          return 1;
		      });

		      assertEquals (reportActivities.get(0).type,  "run" ) ;
		      assertEquals (reportActivities.get(1).type,  "run" ) ;
		      assertEquals (reportActivities.get(2).type,  "walk" ) ;
		      assertEquals (reportActivities.get(3).type,  "walk" ) ;
		      assertEquals (reportActivities.get(4).type,  "bike" ) ;
		
		      assertEquals (reportActivities.get(0).distance,  6, 0.01  ) ;
		      assertEquals (reportActivities.get(4).distance,  0.4, 0.01  ) ;
			     
		      assertEquals (reportActivities.get(2).location,"Wiclow"  ) ;
				
		      
	   }
	
	
	
	
	
	
	   }



