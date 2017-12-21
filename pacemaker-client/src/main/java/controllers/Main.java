package controllers;

import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import parsers.Info;


public class Main {

	  public static void main(String[] args) throws Exception {
	    PacemakerConsoleService main = new PacemakerConsoleService();
	    Shell shell = ShellFactory
	        .createConsoleShell("pm", "Welcome to Marks pacemaker-console v2", main);
	  Info.info("Use the ?list for the full set of menus");
	    shell.commandLoop();
	  }
	}