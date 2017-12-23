package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


import com.google.common.base.Optional;

import asg.cliche.Command;
import asg.cliche.Param;
import models.Activity;
import models.Friend;
import models.Summary;
import models.User;
import parsers.AsciiTableParser;
import parsers.Info;
import parsers.Parser;

//import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
//import com.bethecoder.ascii_table.spec.IASCIITableAware;


public class PacemakerConsoleService {

  private PacemakerAPI paceApi = new PacemakerAPI("http://localhost:7000");
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;
  //private String Friendmsg  = null;
  
  private int ErrCount  = 0  ;
  
  public PacemakerConsoleService() {}

  
  // Starter Commands
  
  
	  
// LOGIN / LOGOUT
  
  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,
      @Param(name = "password") String password) {
    Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
 
  if (user.isPresent()) {
      if (user.get().password.equals(password)) {
        loggedInUser = user.get();
        ErrCount = 0 ;
        Info.info("Logged in as " + loggedInUser.email);
        console.println("ok");
      }
    }
      else {
    	 ErrCount = ErrCount + 1 ;
    	 
    	if ( ErrCount < 4 ) {  
           Info.err("Error on login","This user is not Registered, try again ...");
    	   } 
    	   else 
    	   { 
    	   Info.err("Login Error","Too many attempts, goodbye");
    	  System.exit(1) ;
    	   }
    	}
   }
 

  @Command(description = "Logout: Logout current user")
  public void logout() {
    Info.info("Logging out " + loggedInUser.email);
    console.println("ok");
    loggedInUser = null;
  }
  

  
  // CREATE RECORDS
  
  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName, @Param(name = "email") String email,
      @Param(name = "password") String password) {
	  
	   if ( email.toLowerCase().contains("@".toLowerCase()) ) 
       {  
		   console.renderUser(paceApi.createUser(firstName, lastName, email, password)); 
	   }  else          {       
         Info.err("Register a User ", "Invalid Email, please enter a valid email that includes a @" );
       }
    }

  
  
  @Command(description = "Follow Friend: Follow a specific friend")
  public void follow(@Param(name = "email") String email) {
    console.renderFriend(paceApi.createFriend(email)); 
	  
  }
 

  
  @Command(description = "Message Friend: send a message to a friend")
  public void addMessage(@Param(name = "email") String email, @Param(name = "messages") String messages) {
	
	  User Friendmsg = paceApi.getUserByEmail(email) ;
	  Optional<User> user = Optional.fromNullable(Friendmsg);

	    if (user.isPresent()) {
	    	console.renderMessage(paceApi.createMessage(user.get().id, messages)); 
	      }
	  }
	
  
  @Command(description = "Message All Friends: send a message to all friends")
  public void messageAllFriends(@Param(name = "message") String message) {
	
      Collection<Friend> friend = paceApi.getFriends();
      	  
    		  if (friend.isEmpty()  )
    	      {
    	        System.out.println ( "No User Data");
    	      }
    	      else
    	      {
    	    	  for (Friend a : friend) {  
    	    		  User Friendid = paceApi.getUserByEmail(a.email) ;
    	    		  
    	    		  
	    	paceApi.createMessage(Friendid.id, message); 
	      }
    	    Info.info("Message has been sent to all Friends");
    	      }
	  }
  
  
  
  
  
  @Command(description = "Add activity: create and add an activity for the logged in user")
  public void addActivity(@Param(name = "type") String type,
      @Param(name = "location") String location, @Param(name = "distance") double distance) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivity(paceApi.createActivity(user.get().id, type, location, distance));
    }
  }

  
  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude, @Param(name = "latitude") double latitude) {
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      paceApi.addLocation(loggedInUser.getId(), activity.get().id, latitude, longitude);
      console.println("ok");
    } else {
      console.println("not found");
    }
  }


  // DELETE RECORDS

       @Command(description="Unfollow Friends: Stop following a friend")
       public void unfollow (@Param(name="email") String email)
     {
    
	 User Friendmsg = paceApi.getUserByEmail(email) ;
	  Optional<User> user = Optional.fromNullable(Friendmsg);
    
    if (user.isPresent())
       {
         String decision;
         Scanner kbd = new Scanner (System.in);

         System.out.println("Are you sure you want to delete this Friend ? : yes or no");
         decision = kbd.nextLine();
      
         switch(decision){
         case "yes":
         {
            	 paceApi.deleteFriend(email);
         
        	 Info.info("Friend Deleted");    
         break ;
         }
         case "no":
         {
         Info.info("No problem - record stays!");    
         break; // optional
         }
         default :
           Info.err("Delete a Friend", "Invalid option - yes or no are the options");
         break;
         }
       }
       else
       {      
         Info.warn("Unfollow a Friend", "Invalid Friend email");    
       }
     
 }
  
  
  
  
  
  
  
  
  
  
  // *******    LIST RECORDS *********
  
  
  @Command(description = "List Users: List all users emails, first and last names")
  public void listUsers() {
    console.renderUsers(paceApi.getUsers());
  }



  @Command(description = "List Friends: List all of the friends of the logged in user")
  public void listFriends()  {
 	
	  
	  try {
	 console.renderFriends(paceApi.getFriends());
 	  
	  }
      catch (Exception e) {
      System.out.println("Its a problem " + e.getMessage());
    }
}
  


  @Command(description = "List Messages: List all messages for the logged in user")
  public void listMessages() {
		    Optional<User> user = Optional.fromNullable(loggedInUser);
		    if (user.isPresent()) {
		      console.renderMessages(paceApi.getMessages(user.get().id));
 	  }

	  
  }

  @Command(description = "List Activities: List all activities for logged in user")
  public void listActivities() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivities(paceApi.getActivities(user.get().id));
    }
  }
  
  
  

  @Command(
      description = "Friend Activity Report: List all activities of specific friend, sorted alphabetically by type)")
  public void friendActivityReport(@Param(name = "email") String email) {
	  
	  User Friendmsg = paceApi.getUserByEmail(email) ;
	  Optional<User> user = Optional.fromNullable(Friendmsg);

    if (user.isPresent()) {
        List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
      usersActivities.forEach(a -> {
    	  reportActivities.add(a);
      });
      
      reportActivities.sort((a1, a2) -> a1.type.compareTo(a2.type));
  
           console.renderActivities(reportActivities);
      
      }
  }
    
  
    

  
  
  @Command(
	      description = "ActivityReport: List all activities for logged in user, sorted alphabetically by type")
	  public void activityReport() {
	    Optional<User> user = Optional.fromNullable(loggedInUser);
	    if (user.isPresent()) {
	        List<Activity> reportActivities = new ArrayList<>();
	      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
	      usersActivities.forEach(a -> {
	    	  reportActivities.add(a);
	      });
	      
	      reportActivities.sort((a1, a2) -> a1.type.compareTo(a2.type));
	  
	           console.renderActivities(reportActivities);
	      
	      }
	  }
  
  

   



  @Command(
      description = "Activity Report: List all activities for logged in user by type. Sorted longest to shortest distance")
  public void activityReport(@Param(name = "byType: type") String type) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceApi.getActivities(user.get().id);
      usersActivities.forEach(a -> {
        if (a.type.equals(type))
          reportActivities.add(a);
      });
      reportActivities.sort((a1, a2) -> {
        if (a1.distance >= a2.distance)
          return -1;
        else
          return 1;
      });
      console.renderActivities(reportActivities);
    }
  }

  
  
  
  @Command(description = "List all locations for a specific activity")
  public void listActivityLocations(@Param(name = "activity-id") String id) {
 
    Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      // console.renderLocations(activity.get().route);
    }
  }

  
  
  
  
  
  
 

  // Good Commands
  

  @Command(
      description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard() {
	  
/**
	  
	  List<Summary> list = Arrays.asList(
	            new Summary( "P1", 300),
	            new Summary( "P2", 600),
	            new Summary( "P3", 30),
	            new Summary( "P3", 70),
	            new Summary( "P1", 360),
	            new Summary( "P4", 320),
	            new Summary( "P4", 500));


// Replace above by expression of adding all acticities for specificy Users
	  
	        List<Summary> transform = list.stream()
	            .collect(Collectors.groupingBy(summary -> summary.name))
	            .entrySet().stream()
	            .map(e -> e.getValue().stream()
	                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
	                .map(f -> f.get())
	                .collect(Collectors.toList());
	        
   List<Summary> slist = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());
	System.out.println("---Sorting using Comparator by Distance with reverse order---");
	    	     
 	slist.forEach( e ->  System.out.println( "Name: "+e.getName()+", Distance :"+e.getDistance()));

**/ 			  
                   	  paceApi.deleteSummary() ;
	  
	                   Collection<Friend> friendslist = paceApi.getFriends();
	                		   for(Friend nextu : friendslist){
	                		       User userf = paceApi.getUserByEmail(nextu.email);
		                		       
	                		       Collection<Activity> usersActivities = paceApi.getActivities(userf.id);
	         
	                		      for( Activity nextact: usersActivities){
	                		         	 paceApi.createSummary(userf.id, nextact.distance);
	//	                                 list.add(userf.id, nextact.distance);
	                		      };   
                    		   }	                		   
	           		console.renderSummarys(paceApi.getSummary());
	                		   		 
                    		
	    	    	        //   Collection<Summary> summaryx = new ArrayList<Summary>();
	    	    	       // 		 for(Summary newBo : summaryx){
	    	    	        //		       System.out.println(newBo.distance);
	    	    	       // 		   }
	    	    	           
	                		   
	                		   // ABOVE WRITES TO THE TEMP TABLE
	                	      //  List<Summary> list = new ArrayList<>();

	       	            //   List<Summary> list = new ArrayList<>();

	                		//   list.forEach( e ->  System.out.println( "Name: "+e.getName()+", Age:"+e.getDistance()));
	                		   
	                		   
	                    		  
	                		
    	//   	    List<Summary> list2 = new ArrayList<>();

	              		//  List<Collection<Summary>> list2 = Arrays.asList(paceApi.getSummary());
	                		
	                		  
	                		//  coll.stream().collect(Collectors.toList());
	                		//  List list = new ArrayList(paceApi.getSummary());
	                //		  Collections.sort(list);
	         //  	 List<Summary> list = Arrays.asList(
	     	 //           new Summary( "P1", 300));
/**
	           
	              		  
	            **/  		  
        		
	                List<Summary> reportActivities = new ArrayList<>();
	                Collection<Summary> usersActivities = paceApi.getSummary();
	                usersActivities.forEach(a -> {
	              	  reportActivities.add(a);
	                });
          			
	           			
	           	
	           		
	     	     //   Collection<Summary> a =  new ArrayList<Summary>();
					List<Summary> transform =  reportActivities.stream()
							
	            		            .collect(Collectors.groupingBy(summary -> summary.name))
	            		            .entrySet().stream()
	            		            .map(e -> e.getValue().stream()
	            		                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
	            		                .map(f -> f.get())
	            		                .collect(Collectors.toList());
	            		        
	            		        
    List<Summary> list2 = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());
    System.out.println("---Sorting2 using Comparator by Distance with reverse order---");
	            		    	     
	            list2.forEach( e ->  System.out.println( "Name: "+e.getName()+"  "
	            +paceApi.getUser(e.getName()).lastname + ", Distance :"+e.getDistance()));
	                		   
  }
	
	  
  
  
  
  // Excellent Commands

  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) {
	  
   	  paceApi.deleteSummary() ;
	  
       Collection<Friend> friendslist = paceApi.getFriends();
    		   for(Friend nextu : friendslist){
    		       User userf = paceApi.getUserByEmail(nextu.email);
        		       
    		       Collection<Activity> usersActivities = paceApi.getActivities(userf.id);

    		      for( Activity nextact: usersActivities)
    		      {
    		    	  if ( nextact.type == type ) {
    		         	 paceApi.createSummary(userf.id, nextact.distance);
    		    	  }
//	                                 list.add(userf.id, nextact.distance);
    		      };   
    		   }	                		   
		console.renderSummarys(paceApi.getSummary());

	
		
		
        List<Summary> reportActivities = new ArrayList<>();
        Collection<Summary> usersActivities = paceApi.getSummary();
        usersActivities.forEach(a -> {
      	  reportActivities.add(a);
        });
			
   			
   	
   		
	     //   Collection<Summary> a =  new ArrayList<Summary>();
		List<Summary> transform =  reportActivities.stream()
				
    		            .collect(Collectors.groupingBy(summary -> summary.name))
    		            .entrySet().stream()
    		            .map(e -> e.getValue().stream()
    		                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
    		                .map(f -> f.get())
    		                .collect(Collectors.toList());
    		        
    		        
List<Summary> list2 = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());
System.out.println("---Sorting2 using Comparator by Distance with reverse order---");
    		    	     
    list2.forEach( e ->  System.out.println( "Name: "+e.getName()+"  "
    +paceApi.getUser(e.getName()).lastname + ", Distance :"+e.getDistance()));
	  
	  
	  
	  
  }



  @Command(
      description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String message) {}

  // Outstanding Commands

  // Todo
}
