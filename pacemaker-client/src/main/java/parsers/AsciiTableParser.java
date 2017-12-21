package parsers;

import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
import com.bethecoder.ascii_table.spec.IASCIITableAware;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import models.Activity;
import models.Location;
import models.User;
import models.Friend;
import models.Message;
import models.Summary;

public class AsciiTableParser extends Parser {

  public void renderUser(User user) {
    if (user != null) {
      renderUsers(Arrays.asList(user));
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  
  public void renderFriend(Friend friend) {
	    if (friend != null) {
	      renderFriends(Arrays.asList(friend));
	      System.out.println("ok");
	    } else {
	      System.out.println("not found");
	    }
	  }

  public void renderMessage(Message message) {
	    if (message != null) {
	      renderMessages(Arrays.asList(message));
	      System.out.println("ok");
	    } else {
	      System.out.println("not found");
	    }
	  }

  public void renderSummary(Summary summary) {
	    if (summary != null) {
	      renderSummarys(Arrays.asList(summary));
	      System.out.println("ok");
	    } else {
	      System.out.println("summary not found");
	    }
	  }
  
  
  
  
  
  
  public void renderActivity(Activity activity) {
	    if (activity != null) {
	      renderActivities(Arrays.asList(activity));
	      System.out.println("ok");
	    } else {
	      System.out.println("not found");
	    }
	  }


  
  public void renderUsers(Collection<User> users) {
    if (users != null) {
      if (!users.isEmpty()) {
        List<User> userList = new ArrayList<User>(users);
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<User>(userList, "id",  "firstname",
            "lastname", "email");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  
  public void renderFriends(Collection<Friend> friends) {
	    if (friends != null) {
	      if (!friends.isEmpty()) {
	        List<Friend> friendList = new ArrayList<Friend>(friends);
	        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Friend>(friendList,"id", "email");
	        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
	      }
	      System.out.println("ok");
	    } else {
	      System.out.println("ascii not found");
	    }
	  }
  
  public void renderMessages(Collection<Message> messages) {
	    if (messages != null) {
	      if (!messages.isEmpty()) {
	        List<Message> messageList = new ArrayList<Message>(messages);
	        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Message>(messageList,
	        		 "id", "messages");
	        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
	      }
	      System.out.println("ok");
	    } else {
	      System.out.println("ascii not found");
	    }
  }
	    public void renderSummarys(Collection<Summary> summarys) {
		    if (summarys != null) {
		      if (!summarys.isEmpty()) {
		        List<Summary> summaryList = new ArrayList<Summary>(summarys);
		        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Summary>(summaryList,
		        		"id", "name", "distance");
		        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
		      }
		      System.out.println("ok");
		    } else {
		      System.out.println("summary ascii not found");
		    }

  
  
  
  }

  
  
  
 
  public void renderActivities(Collection<Activity> activities) {
    if (activities != null) {
      if (!activities.isEmpty()) {
        List<Activity> activityList = new ArrayList<Activity>(activities);
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Activity>(activityList,
        		  "id",
            "type", "location", "distance");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }

  public void renderLocations(List<Location> locations) {
    if (locations != null) {
      if (!locations.isEmpty()) {
        IASCIITableAware asciiTableAware = new CollectionASCIITableAware<Location>(locations,
            "id",
            "latitude", "longitude");
        System.out.println(ASCIITable.getInstance().getTable(asciiTableAware));
      }
      System.out.println("ok");
    } else {
      System.out.println("not found");
    }
  }
}