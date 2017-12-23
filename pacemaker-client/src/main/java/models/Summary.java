package models;

	import static com.google.common.base.MoreObjects.toStringHelper;
	import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Objects;

	public class Summary  implements Serializable {

	 public String id;
	  public String name ;
	  public double distance;

	  public Summary() {
	  }

	 public String getId() {
	    return id;
	  }

	  public String getName() {
	    return name;
	  }

	  public double getDistance() {
	    return distance;
	  }


	  public Summary(String name,  double distance) {
	
	      this.name = name ;
		  this.distance = distance;
	  }

	  @Override
	  public boolean equals(final Object obj) {
	    if (obj instanceof Summary) {
	      final Summary other = (Summary) obj;
	      return Objects.equal(name, other.name)
	          && Objects.equal(distance, other.distance);
	    } else {
	      return false;
	    }
	  }

	  @Override
	  public String toString() {
	    return toStringHelper(this)   
	    		.addValue(id)
	        .addValue(name)
	        .addValue(distance)
	        .toString();
	  }

	  
	  
	  @Override
	  public int hashCode() {
	    return Objects.hashCode(this.id, this.name, this.distance);
	   // return Objects.hashCode(this.name, this.distance);
	  }
  }
	
	
