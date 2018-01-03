package controllers;


import java.util.ArrayList;

import java.util.Collection;

import java.util.Comparator;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

//Keep me to switch between eclipse and dosc command
import org.fusesource.jansi.AnsiConsole;

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

public class PacemakerConsoleService {

  private PacemakerAPI paceApi = new PacemakerAPI("http://localhost:7000");
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;

  public static final String PURPLE = "\033[1;35m"; // PURPLE
  public static final String RESET = "\u001B[0m";
  
  private int ErrCount  = 0  ;
private int Rank = 0 ;
  
  public PacemakerConsoleService() {}

  
  // Starter Commands
  
  
	  
// LOGIN / LOGOUT
  
  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,
      @Param(name = "password") String password) {

	  try {
	  Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
	  
  if (user.isPresent()) {
	 
	  if (user.get().password.equals(password)) {
    	 
    	  if (loggedInUser == null || loggedInUser.id.equals("")) {
    		
    		  loggedInUser = user.get();
              ErrCount = 0 ;
              Info.info("Logged in as " + loggedInUser.email);
    	  }
    	  else
    	  {
    		  Info.err("Error on login","You are already logged in as" + user.get().lastname);
    	  }
   	  }
    }
      else {
    	 ErrCount = ErrCount + 1 ;
    	 
    	if ( ErrCount < 4 ) {  
           Info.err("Error on login","This user is not Registered, try again ...");
    	   } 
    	   else 
    	   { 
    	   Info.err("Login Error","Too many attempts, contact your Administrator");
    	  System.exit(1) ;
    	   }
    	}
   
	  }
      catch (Exception e) {
      System.out.println("Its a problem " + e.getMessage());
    }
	  
	  }
 

  @Command(description = "Logout: Logout current user")
  public void logout() {
 
	  if (loggedInUser == null || loggedInUser.equals(""))
  	{
		Info.warn("Logout", "You are not currently logged in yet !");  
  	}
	  else
	  {
	  Info.info("Logging out " + loggedInUser.email);
    loggedInUser = null;
      }
  }

  
  // CREATE RECORDS
  
  // Check for duplicate email
  
  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName, @Param(name = "email") String email,
      @Param(name = "password") String password) {
	  
	   if ( email.toLowerCase().contains("@".toLowerCase()) ) 
       {  
			  User Friendmsg = paceApi.getUserByEmail(email) ;
			  Optional<User> user = Optional.fromNullable(Friendmsg);

			    if (user.isPresent()) {
			    	Info.err("Register a User ","Email exists already -  no duplicates allowed");
			    }
			     else
			    {
		          console.renderUser(paceApi.createUser(firstName, lastName, email, password)); 
	            }  }
			    else          
			    {       
                   Info.err("Register a User ", "Invalid Email, please enter a valid email that includes a @" );
                  }
   }

  
  
  // Validate for a correct email as well as duplication
  
  @Command(description = "Follow Friend: Follow a specific friend")
  public void follow(@Param(name = "email") String email) {

	  
	  User user = paceApi.getUserByEmail(email) ;
	  Optional<User> userfound = Optional.fromNullable(user);

	    if (userfound.isPresent()) {

	  	 Friend Friendmsg = paceApi.getFriendByEmail(email) ;
		  Optional<Friend> friendfound = Optional.fromNullable(Friendmsg);
	    	
		  if (friendfound.isPresent()) {
	    	  Info.err("Follow a Friend", "This friend is already selected");
		  }
		  else
		  {
	      console.renderFriend(paceApi.createFriend(email)); 
		  }
	    }
	    else
	    {
	    	Info.err("Follow a Friend","Email is not found");
	    }
  }
 

  // Validate for a correct email
  
  
  @Command(description = "Message Friend: send a message to a friend")
  public void addMessage(@Param(name = "email") String email, @Param(name = "messages") String messages) {
	
	  User Friendmsg = paceApi.getUserByEmail(email) ;
	  Optional<User> user = Optional.fromNullable(Friendmsg);

	    if (user.isPresent()) {
	    	console.renderMessage(paceApi.createMessage(user.get().id, messages)); 
	      }
	    else
	    {
	    	Info.err("Message a Friend","Email is not found");
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
	  else
	  {
		  Info.err("Add Activity","You need to be logged in to add an activity");
	  }
  }

  
  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude, @Param(name = "latitude") double latitude) {
	 
	  Optional<User> user = Optional.fromNullable(loggedInUser);
	    if (user.isPresent()) {
	            Optional<Activity> activity = Optional.fromNullable(paceApi.getActivity(loggedInUser.getId(), id));
                if (activity.isPresent()) {
                   paceApi.addLocation(loggedInUser.getId(), activity.get().id, latitude, longitude);
                Info.info("New record added");;
    } 
                else {
                	Info.warn("Add Location","The activity is not found for this user");
                     }
	    }
    else
	  {
		  Info.err("Add Location","You need to be logged in to add a location");
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
            paceApi.deleteMessages(Friendmsg.id);	 
        	 paceApi.deleteFriend(email);
         
        	 Info.info("Friend and all my messages to them are Deleted");    
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
	            Optional<Collection<Activity>> activity = Optional.fromNullable(paceApi.getActivities(user.get().id)) ;
              if (activity.equals(null))
                  {
		          Info.info("No current Activities for " + user.get().firstname + " " + user.get().lastname ); 
		    	    }
	    else
                 {
    		      System.out.println (PURPLE + "\n---------------------------------------" + RESET);
    		      System.out.println (PURPLE +" Listing ALL activities for "+user.get().firstname +" "+ user.get().lastname            + RESET);
    		      System.out.println (PURPLE + "---------------------------------------" + RESET);
    		     
    			  console.renderActivities(paceApi.getActivities(user.get().id));
    	          }
	    }
		  else
  	  {
  		  Info.err("Add Activity","You need to be logged in to add an activity");
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
  
      
      System.out.println (PURPLE + "\n---------------------------------------" + RESET);
      System.out.println (PURPLE +"Friends Activity Report -  ordered by Type" + RESET);
      System.out.println (PURPLE + "        for friend : "+user.get().firstname +" "+ user.get().lastname            + RESET);
      System.out.println (PURPLE + "---------------------------------------" + RESET);
     

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
	
	      System.out.println (PURPLE + "\n---------------------------------------" + RESET);
	      System.out.println (PURPLE +"Activity Report -  ordered by Type" + RESET);
	      System.out.println (PURPLE + "        for "+user.get().firstname +" "+ user.get().lastname            + RESET);
	      System.out.println (PURPLE + "---------------------------------------" + RESET);
	   
	           console.renderActivities(reportActivities);
	      
	      }
	    else
	
  {
	  Info.err("Activity Report","You need to be logged in to view a Users Activity Report");
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
        if (a.type.equalsIgnoreCase(type))
          reportActivities.add(a);
      });
      reportActivities.sort((a1, a2) -> {
        if (a1.distance >= a2.distance)
          return -1;
        else
          return 1;
      });

      
      System.out.println (PURPLE + "\n---------------------------------------" + RESET);
      System.out.println (PURPLE +"Activity Report for Type : " + type.toUpperCase() + "  (ordered by distance desc)" + RESET);
      System.out.println (PURPLE + "        for "+user.get().firstname +" "+ user.get().lastname            + RESET);
      System.out.println (PURPLE + "---------------------------------------" + RESET);

      
      console.renderActivities(reportActivities);
    }
    else
    	
{
  Info.err("Activity Report","You need to be logged in to view a Users Activity Report");
} 

  }

  
  
  
    
  
 

  // Complicated Reports !!!
  

  @Command(
      description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard() {
	  
                   	  paceApi.deleteSummary() ;
	  
	                   Collection<Friend> friendslist = paceApi.getFriends();
	                   if (friendslist.isEmpty())
	                   {
	                 	  Info.warn("Distance Leader Board", "No Friends selected");	
	                   }
	                   else
	                   {	                   
	                   for(Friend nextu : friendslist){
	                		       User userf = paceApi.getUserByEmail(nextu.email);
	                		       Collection<Activity> usersActivities = paceApi.getActivities(userf.id);
	         
	                		      for( Activity nextact: usersActivities){
	                		         	 paceApi.createSummary(userf.id, nextact.distance);
	                		      };   
                    		   }	                		   
	          // 		console.renderSummarys(paceApi.getSummary());
	            // above used for debugginfg    		   		 
                    		
  	                List<Summary> reportActivities = new ArrayList<>();
	                Collection<Summary> usersActivities = paceApi.getSummary();
	                usersActivities.forEach(a -> {
	              	  reportActivities.add(a);
	                });
    
	                List<Summary> transform =  reportActivities.stream()
						            .collect(Collectors.groupingBy(summary -> summary.name))
	            		            .entrySet().stream()
	            		            .map(e -> e.getValue().stream()
	            		                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
	            		                .map(f -> f.get())
	            		                .collect(Collectors.toList());
	            		        
	            		        
    List<Summary> list2 = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());
    if ( list2.isEmpty()) {
   	 Info.warn("Distance Leader Board", "No Data available");	
   	}
   	else
   	{
   	Rank = 0 ;

   	System.out.println (PURPLE + "\n------------------------------------------------------------------------------" + RESET);
   	System.out.println (PURPLE +"Friends Distance Leader Board "  + "  (Ranked by distance desc)" + RESET);
   	System.out.println (PURPLE + "------------------------------------------------------------------------------" + RESET);

   	System.out.println(" ");
   	System.out.println (PURPLE + "--Friend-------Distance----Rank--" + RESET);

   	 list2.forEach( e ->  
   	   System.out.println(  String.format("%-15s", paceApi.getUser(e.getName()).firstname +
   			   " " + paceApi.getUser(e.getName()).lastname)
   			+String.format("%-12s",e.getDistance()) +  getRank() ));
        	}
	   }		   
  }
	
	
  
  
  
  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) {
	
	  // Clear out staging table
   	  paceApi.deleteSummary() ;
	  
       Collection<Friend> friendslist = paceApi.getFriends();

       if (friendslist.isEmpty())
       {
     	  Info.warn("Distance Leader Board", "No Friends selected");	
       }
       else
       {
       for(Friend nextu : friendslist){
    		       User userf = paceApi.getUserByEmail(nextu.email);
        		       
    		       Collection<Activity> usersActivities = paceApi.getActivities(userf.id);

    		      for( Activity nextact: usersActivities)
    		      {
    		    	   if(nextact.type.equalsIgnoreCase(type) ) {
      		         	 paceApi.createSummary(userf.id, nextact.distance);
    		    	  }
    		      };   
    		   }	                		   
		
        List<Summary> reportActivities = new ArrayList<>();
        Collection<Summary> usersActivities = paceApi.getSummary();
        usersActivities.forEach(a -> {
      	  reportActivities.add(a);
        });
		
		List<Summary> transform =  reportActivities.stream()
			            .collect(Collectors.groupingBy(summary -> summary.name))
    		            .entrySet().stream()
    		            .map(e -> e.getValue().stream()
    		                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
    		                .map(f -> f.get())
    		                .collect(Collectors.toList());
    		        
    		        
List<Summary> list2 = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());

if ( list2.isEmpty()) {
	 Info.warn("Distance Leader Board", "No Data available");	
	}
	else
	{
	Rank = 0 ;

	System.out.println (PURPLE + "\n------------------------------------------------------------------------------" + RESET);
	System.out.println (PURPLE +"Friends Distance Leader Board for type : " + type.toUpperCase() + "  (Ranked by distance desc)" + RESET);
	System.out.println (PURPLE + "------------------------------------------------------------------------------" + RESET);

	System.out.println(" ");
	System.out.println (PURPLE + "--Friend-------Distance----Rank--" + RESET);

	 list2.forEach( e ->  
	   System.out.println(  String.format("%-15s", paceApi.getUser(e.getName()).firstname +
			   " " + paceApi.getUser(e.getName()).lastname)
			   +String.format("%-12s",e.getDistance()) +  getRank() ));
     	}
       }	  
  }




  @Command(
      description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String location) {

	  paceApi.deleteSummary() ;
	  
      Collection<Friend> friendslist = paceApi.getFriends();
   	
      if (friendslist.isEmpty())
      {
    	  Info.warn("Location Leader Board", "No Friends selected");	
      }
      else
      {
      for(Friend nextu : friendslist){
   		       User userf = paceApi.getUserByEmail(nextu.email);
       		       
   		       Collection<Activity> usersActivities = paceApi.getActivities(userf.id);

   		      for( Activity nextact: usersActivities)
   		      {
   		    	   if(nextact.location.equalsIgnoreCase(location) ) {
     		         	 paceApi.createSummary(userf.id, nextact.distance);
   		    	  }
   		      };   
   		   }	                		   
		
       List<Summary> reportActivities = new ArrayList<>();
       Collection<Summary> usersActivities = paceApi.getSummary();
       usersActivities.forEach(a -> {
     	  reportActivities.add(a);
       });
			
		List<Summary> transform =  reportActivities.stream()
   		            .collect(Collectors.groupingBy(summary -> summary.name))
   		            .entrySet().stream()
   		            .map(e -> e.getValue().stream()
   		                .reduce((f1,f2) -> new Summary(f1.name,f1.distance + f2.distance)))
   		                .map(f -> f.get())
   		                .collect(Collectors.toList());
   		        
   		        
List<Summary> list2 = transform.stream().sorted(Comparator.comparing(Summary::getDistance).reversed()).collect(Collectors.toList());

if ( list2.isEmpty()) {
 Info.warn("Location Leader Board", "No Data available");	
	
}
else
{
Rank = 0 ;

System.out.println (PURPLE + "\n------------------------------------------------------------------------------" + RESET);
System.out.println (PURPLE +"Friends Location Leader Board for location : " + location.toUpperCase() + "  (Ranked by distance desc)" + RESET);
System.out.println (PURPLE + "------------------------------------------------------------------------------" + RESET);

System.out.println(" ");
System.out.println (PURPLE + "--Friend-------Distance----Rank--" + RESET);

 list2.forEach( e ->  
   System.out.println(  String.format("%-15s", paceApi.getUser(e.getName()).firstname +
		   " " + paceApi.getUser(e.getName()).lastname)
		    +String.format("%-12s",e.getDistance()) +  getRank() ));
}
      }
  
  
  }	  
 
  
  public  int getRank() {
	  Rank = Rank + 1 ;  
	  return Rank ;
	} 
  
  
// If time permitted wanted to get this to print 'Distinct' Locations rather than all
  
  @Command(description = "List all locations for a specific activity")
  public void listActivityLocations(@Param(name = "byType: type") String type) {
 
	  if ( loggedInUser.equals(null))
	  { 
		  Info.err("List All Locations","You must be logged in to run this function") ;  
	  }
	  else
	  {
		
		  System.out.println (PURPLE + "\n---------------------------------------" + RESET);
	      System.out.println (PURPLE +"Location Report for Type : " + type.toUpperCase() + "  (ordered by distance desc)" + RESET);
	      System.out.println (PURPLE + "        for "+loggedInUser.firstname +" "+ loggedInUser.lastname            + RESET);
	      System.out.println (PURPLE + "---------------------------------------" + RESET);
	      
	      
      Collection<Activity> usersActivities = paceApi.getActivities(loggedInUser.id);

	   		       
	   		    System.out.println( " Location "  );
		    		
	   		      for( Activity nextact: usersActivities)
	   		      {
	   		    	//  System.out.println(nextact.type);
	     		          
	   		    	   if(nextact.type.equalsIgnoreCase(type) ) {
	   		    	 
	   		    		System.out.println(  nextact.location );
	   		    		}
	   		      }
	  }
	  
  }
	

	  
  
}
	 
   




