package controllers;


import java.util.Collection;

import java.util.List;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Activity;
import models.Location;
import models.User;
import models.Friend;
import models.Message;
import models.Summary;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


// USERS

interface PacemakerInterface {
  @GET("/users")
  Call<List<User>> getUsers();

  @DELETE("/users")
  Call<String> deleteUsers();

  @DELETE("/users/{id}")
  Call<User> deleteUser(@Path("id") String id);

  @GET("/users/{id}")
  Call<User> getUser(@Path("id") String id);

  @POST("/users")
  Call<User> registerUser(@Body User User);

  
  
  
  // FRIENDS

  @GET("/friends")
  Call<List<Friend>> getFriend();

  @DELETE("/friends/{email}")
  Call<Friend> deleteFriend(@Path("email") String email);;

  @POST("/friends")
  Call<Friend> followFriend(@Body Friend friend);
 
  @DELETE("/friends")
  Call<String> deleteFriends();
  

  
  
  //MESSAGES - attached to Users....

  @GET("/users/{id}/messages")
  Call<List<Message>> getMessages(@Path("id") String id);  
  
  @POST("/users/{id}/messages")
  Call<Message> addMessage(@Path("id") String id,@Body Message message);

  @DELETE("/users/{id}/messages")
  Call<String> deleteMessages(@Path("id") String id);  


 //SUMMARY - FOR Reporting 
  
  @DELETE("/summary")
  Call<String> deleteSummary();
   
  @POST("/summary")
  Call<Summary> createSummary(@Body Summary summary);

  @GET("/summary")
  Call<List<Summary>> getSummary();

  
 //ACTIVITIES 
  
  @GET("/users/{id}/activities")
  Call<List<Activity>> getActivities(@Path("id") String id);

  @GET("/users/{id}/activities")
  Call<List<Activity>> listActivities(@Path("id") String id, @Path("sortBy") String sortBy);
  
  
  @POST("/users/{id}/activities")
  Call<Activity> addActivity(@Path("id") String id, @Body Activity activity);

  @DELETE("/users/{id}/activities")
  Call<String> deleteActivities(@Path("id") String id);
  
  @GET("/users/{id}/activities/{activityId}")
  Call<Activity> getActivity(@Path("id") String id, @Path("activityId") String activityId);


  
 // LOCATIONS within Activities 
  

  @POST("/users/{id}/activities/{activityId}/locations")
  Call<Location> addLocation(@Path("id") String id, @Body Location location);
  
 
  @GET("/users/{id}/activities/{activityId}/locations")
  Call<List<Location>> getLocations(@Path("id") String id, @Path("activityId") String activityId);
  
}


public class PacemakerAPI {

  PacemakerInterface pacemakerInterface;

 
  public PacemakerAPI(String url) {
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson)).build();

    pacemakerInterface = retrofit.create(PacemakerInterface.class);
  }

  public Collection<User> getUsers() {
    Collection<User> users = null;
    try {
      Call<List<User>> call = pacemakerInterface.getUsers();
      Response<List<User>> response = call.execute();
      users = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public Collection<Friend> getFriends() {
	    Collection<Friend> friends = null;
	    try {
	      Call<List<Friend>> call = pacemakerInterface.getFriend();
	      Response<List<Friend>> response = call.execute();
	      friends = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return friends;
	  }
 
  
  public Collection<Message> getMessages(String id) {
	  Collection<Message> messages = null;
	    try {
	      Call<List<Message>> call = pacemakerInterface.getMessages(id);
	      Response<List<Message>> response = call.execute();
	      messages = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return messages;
	  }


  
  
  public List<Location> getLocations(String id, String activityId) {
	    List<Location> locations = null;
	    try {
	      Call<List<Location>> call = pacemakerInterface.getLocations(id, activityId);
	      Response<List<Location>> response = call.execute();
	      locations = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return locations;
	  }
  
  
  
  public Collection<Summary> getSummary() {
	    Collection<Summary> summary = null;
	    try {
	      Call<List<Summary>> call = pacemakerInterface.getSummary();
	      Response<List<Summary>> response = call.execute();
	      summary = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return summary;
	  }
  
  
  
  public User createUser(String firstName, String lastName, String email, String password) {
    User returnedUser = null;
    try {
    	Call<User> call =
          pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
      Response<User> response = call.execute();
      returnedUser = response.body();
    	} catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedUser;
  }

  
  public Friend createFriend(String email) {
	   Friend returnedFriend = null;
	    try {
	      Call<Friend> call =
	          pacemakerInterface.followFriend(new Friend(email));
	      Response<Friend> response = call.execute();
	     returnedFriend = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return returnedFriend;
	  }
  
  

  public Message createMessage(String id, String messages) {
	   Message returnedMessage = null;
	    try {
	      Call<Message> call =
	        pacemakerInterface.addMessage(id, new Message(messages));
	      Response<Message> response = call.execute();
	     returnedMessage = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return returnedMessage;
	  }

  
  
  public Summary createSummary(String name, double distance) {
	   Summary returnedSummary = null;
	    try {
	      Call<Summary> call =
	        pacemakerInterface.createSummary(new Summary(name, distance));
	      Response<Summary> response = call.execute();
	     returnedSummary = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return returnedSummary;
	  }
  
  
  
  public Activity createActivity(String id, String type, String location, double distance) {
	    Activity returnedActivity = null;
	    try {
	      Call<Activity> call =
	          pacemakerInterface.addActivity(id, new Activity(type, location, distance));
	      Response<Activity> response = call.execute();
	      returnedActivity = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return returnedActivity;
	  }

  
  
  public Location addLocation(String id, double latitude, double longitude) {
	  Location returnedLocation = null ;  
	  try {
	      Call<Location> call =
	          pacemakerInterface.addLocation(id, new Location(latitude, longitude));
	      call.execute();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	  return returnedLocation ;
	  }
  
  
  
  public Friend deleteFriend(String email) {
	    Friend friends = null;
	    try {
	      Call<Friend> call = pacemakerInterface.deleteFriend(email);
	      Response<Friend> response = call.execute();
	      friends = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return friends;
	  }
 

  public Collection<Activity> getActivities(String id) {
	    Collection<Activity> activities = null;
	    try {
	      Call<List<Activity>> call = pacemakerInterface.getActivities(id);
	      Response<List<Activity>> response = call.execute();
	      activities = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return activities;
	  }

  
  public Collection<Activity> listActivities(String id, String sortBy) {
	    Collection<Activity> activities = null;
	    try {
	      Call<List<Activity>> call = pacemakerInterface.listActivities(id, sortBy);
	      Response<List<Activity>> response = call.execute();
	      activities = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return activities;
	  }
  
  
  
  
  public Activity getActivity(String userId, String activityId) {
    Activity activity = null;
    try {
      Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
      Response<Activity> response = call.execute();
      activity = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activity;
  }

 
  
  
  public void deleteActivities(String id) {
    try {
      Call<String> call = pacemakerInterface.deleteActivities(id);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteMessages(String id) {
	    try {
	    	Call<String> call = pacemakerInterface.deleteMessages(id);
        call.execute();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	  }
  
  
  public User getUserByEmail(String email) {
    Collection<User> users = getUsers();
    User foundUser = null;
    for (User user : users) {
      if (user.email.equals(email)) {
        foundUser = user;
      }
    }
    return foundUser;
  }


  public Friend getFriendByEmail(String email) {
	    Collection<Friend> friends = getFriends();
	    Friend foundFriend = null;
	    for (Friend friend : friends) {
	      if (friend.email.equals(email)) {
	        foundFriend = friend;
	      }
	    }
	    return foundFriend;
	  }
  
  
  public User getUser(String id) {
	    Collection<User> users = getUsers();
	    User foundUser = null;
	    for (User user : users) {
	      if (user.id.equals(id)) {
	        foundUser = user;
	      }
	    }
	    return foundUser;
	  }
  
  
  
  public void deleteUsers() {
    try {
      Call<String> call = pacemakerInterface.deleteUsers();
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  
  public void deleteFriends() {
	    try {
	      Call<String> call = pacemakerInterface.deleteFriends();
	      call.execute();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	  }
  
  
  public User deleteUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.deleteUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }



  
  
  public void deleteSummary() {
	    try {
	        Call<String> call = pacemakerInterface.deleteSummary();
	        call.execute();
	      } catch (Exception e) {
	        System.out.println(e.getMessage());
	      }
	    }



}
