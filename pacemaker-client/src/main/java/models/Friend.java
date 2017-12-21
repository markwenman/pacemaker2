package models;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;

import com.google.common.base.Objects;


public class Friend implements Serializable {
  
	public String id;
    public String email;


  public Friend() {
  }

  public String getId() {
	    return id;
	  }
  public String getEmail() {
	    return email;
	  }


  public Friend( String email) {
    this.email = email;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Friend) {
      final Friend other = (Friend) obj;
      return Objects.equal(email, other.email);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return toStringHelper(this).addValue(id)
    	.addValue(email)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id, this.email);
  }


}