package models;

import static com.google.common.base.MoreObjects.toStringHelper;
import java.io.Serializable;
import com.google.common.base.Objects;

public class Message implements Serializable {

  public String id ;
  public String messages;
  


  public Message() {
  }

  public String getId() {
	    return id;
	  }


  public String getMessages() {
    return messages;
  }



  public Message(  String messages) {
    this.messages = messages;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Message) {
      final Message other = (Message) obj;
         return Objects.equal(messages, other.messages);
    } else {
      return false;
    }
  }

  

  
  
  
  @Override
  public String toString() {
      return toStringHelper(this).addValue(id)
        .addValue(messages)
        .toString();
  }

  
  
  
  
  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.messages);
  }
}