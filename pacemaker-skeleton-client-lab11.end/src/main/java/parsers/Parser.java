package parsers;

import java.util.Collection;
import java.util.List;
import models.Activity;
import models.Location;
import models.User;
import models.Friend;
import models.Message;

public class Parser {

  public void println(String s) {
    System.out.println(s);
  }

  public void renderUser(User user) {
    System.out.println(user.toString());
  }

  public void renderUsers(Collection<User> users) {
	    System.out.println(users.toString());
	  }

  
  public void renderFriend(Friend friend) {
    System.out.println(friend.toString());
  }
  
  public void renderFriends(Collection<Friend> friends) {
	    System.out.println(friends.toString());
	  }
  
  
  public void renderMessage(Message message) {
	    System.out.println(message.toString());
	  }
	  
  public void renderMessages(Collection<Message> messages) {
	    System.out.println(messages.toString());
		  }
	  
 
 

  public void renderActivity(Activity activities) {
    System.out.println(activities.toString());
  }

  public void renderActivities(Collection<Activity> activities) {
    System.out.println(activities.toString());
  }

  public void renderLocations(List<Location> locations) {
    System.out.println(locations.toString());
  }
}