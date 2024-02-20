package project;


import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.math.*;

import project.entity.Project;
import project.exception.DbException;
import project.service.ProjectService;

public class ProjectApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	
	//Exercise week 9
	
	
	@SuppressWarnings("unused")
	/*private  List<String> ops = List.of(
			"1) Create and populate all tables",
			"2) Add a project");*/
	
	
 //added a new list to create the menu
	
	private List<String> operations = List.of(
			"1) Add project");
	
	
	
	public static void main(String[] args) {
	//	DbConnection.getConnection();
		
		
		//new ProjectApp().displayMenu();
		
		
		
		new ProjectApp().processUserSelection();
		
		
		
	}



	private void processUserSelection() {
		
		boolean done = false;
		while(!done) {
			try {
				int selection = getUserSelection();
				switch (selection) {
				
				case -1: 
					done = exitMenu();
					break;
					
				case 1:
					createProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
					
				}
				
			}catch(Exception e) {
				System.out.println("\nError: " + e );
			}
		}
		
	}



	private void createProject() {
		String projectName = getStringInput("enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project ();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		
		System.out.println("You have successfully created project: " + dbProject);
	
	}



	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
			
		}catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}



	private int getUserSelection() {
		
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 :  input;
	}



	private void printOperations() {
		
		System.out.println();
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		
		operations.forEach(line -> System.out.println("   " +line));
		
	}
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
			
		}catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number. Try again.");
		}
		
	}

	
	private String getStringInput(String prompt) {
		System.out.print(prompt +": ");
		//the scanner nextLine will allow us to have a cleaner  input from the user and refresh the screen
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}



	/*private void displayMenu() {
		boolean done = false;
		while(!done) {
			
			
			try{
			int operation = getOperation();
			switch(operation) {
			case -1:
				done = exitMenu();
				break;
				
			case 1: 
				createTables();
				break;
				
				default:
					System.out.println("\n" + operation +" is not valid. Try again");
					
				break;
				case 2:
					addProject();
				
				}
			}catch(Exception e ) {
			System.out.println("\nError: " + e.toString() + "Try again.");
		    }
	     }
     }
*/




	private void createTables() {
		projectService.createAndPopulateTables();
		System.out.println("\nTables created");
	}




	private boolean exitMenu() {
		System.out.println("\nExiting the menu."); 
		return true;
	}



/*
	private int getOperation() {
		printOperation();
		Integer op  = getInput("enter operation number (press Enter to quit)");
		//return null if not a valid number if not return op -1
		return Objects.isNull(op) ? -1: op;
	
	}*/



/*
	private Integer getInput(String prompt) {
		
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
			
		}
		try {
				return Integer.parseInt(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not valid number.");
		}
		
	}*/

	@SuppressWarnings("unused")
	private Double getDoubleInput(String prompt) {
		
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
			
		}
		try {
				return Double.parseDouble(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not valid number.");
		}
		
	}






/*
	private void printOperation() {
	System.out.println();
	System.out.println("Menu Options");
	
	
	ops.forEach(op -> System.out.println("   " +op));
		
	}*/

}
