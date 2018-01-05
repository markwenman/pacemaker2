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
import models.Location;
import static models.Fixtures.locations;



public class LocationTest {

	
	  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	     
      
	  // Relates to the Location detail
	  
	  @Before
	  public void setup() {
			}

	  @After
	  public void tearDown() {
	  }
	  
	  
		@Test
		public void testAddActivityWithSingleLocation()
		{
		}
	
	
}
