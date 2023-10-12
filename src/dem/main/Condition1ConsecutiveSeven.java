package dem.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Condition1ConsecutiveSeven {
	
	//Variables for JDBC connection
	protected String jdbcURL = "jdbc:mysql://localhost:3306/bluejay_delivery";
	protected String username = "root";
	protected String password = "password";

	//Variables 
	protected int currDay = 0;
	protected int prevDay = 0;
	private int consecutiveDays = 0;
	protected int index = 0;
	protected List<String> consecutiveWorkers = new ArrayList<String>();
	protected List<String> listOfUniqueEmployees = new ArrayList<String>();
	protected Map<String, String> listOfUniqueEmployeesWithPoisitionStatus = new HashMap<String, String>();
	BluejayDeliveryAssignment obj = new BluejayDeliveryAssignment();
	
	//Method to get Employees who are worked seven consecutive days. 
	public void getEmployeesWorkingSevenConsecutiveDays(){
		
		try {
            		Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            		if (connection != null) {		
                		System.out.println("Connected to the database.");

                		// Query to retrieve all data
                		String query = "SELECT * FROM employee";
                		PreparedStatement statementForAll = connection.prepareStatement(query);
                		ResultSet resultSetForAll = statementForAll.executeQuery();
		                
                		// Query to retrieve distinct data 
		                String queryForUniqueEmployee = "SELECT DISTINCT `Employee Name`, `Position Status` FROM employee;";
		                PreparedStatement statementForUniqueEmployee = connection.prepareStatement(queryForUniqueEmployee);
		                ResultSet resultSetForUniqueEmployee = statementForUniqueEmployee.executeQuery();
                
		                //Saving resultSetForUniqueEmployee data into list.
		                while(resultSetForUniqueEmployee.next()) {
                			listOfUniqueEmployees.add(resultSetForUniqueEmployee.getString("Employee Name"));
                			listOfUniqueEmployeesWithPoisitionStatus.put(resultSetForUniqueEmployee.getString("Employee Name"),
                												resultSetForUniqueEmployee.getString("Position Status"));
                		}
                	
                		//Traversing resultSet
                		while (resultSetForAll.next()) {

                			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                	
                			String currEmployeeName = resultSetForAll.getString("Employee Name");
                			String prevEmployeeName = listOfUniqueEmployees.get(index);
	                	
        		        	if(!currEmployeeName.equals(prevEmployeeName)) {
                				index++;
                				prevEmployeeName = listOfUniqueEmployees.get(index);
                				prevDay = 0;
                			}
                	
		                	//Traversing for same employee
                			while(prevEmployeeName.equals(currEmployeeName)) {

                				String currTime = resultSetForAll.getString("Time");
	                		
        		        		if(currTime.isEmpty()) {
                					index++;
		                			break;
                				}
                		
                				LocalDateTime currDateTime = LocalDateTime.parse(currTime, formatter);
                		
                				if(!currTime.isEmpty()) {
                					currDay = currDateTime.getDayOfMonth();
                					if(prevDay == 0) {
                						prevDay = currDay;
                					}
                					if(currDay == prevDay) {
                						break;	
                					}
                					if(currDay == prevDay+1) {
                						consecutiveDays++;
                						prevDay = currDay;
                					}else if(currDay != prevDay+1 && currDay > prevDay){
                						consecutiveDays = 0;
                						prevDay = currDay;
                					}
                				}
                		
                				//Adding employee names to consecutiveWorkers list.
                				if(consecutiveDays == 7) {
                    					consecutiveWorkers.add(currEmployeeName);
                    				}
                			}
                		}       
            		}

            		//Print employees who worked for 7 consecutive days
            		System.out.println("Employees who worked for 7 consecutive days:");
            		for(String worker : consecutiveWorkers) {
            			System.out.println(worker+" - "+listOfUniqueEmployeesWithPoisitionStatus.get(worker));
            		}

            		// Close the database connection
            		connection.close();
        	}
        	catch (Exception e) {
            		e.printStackTrace();
        	}
		
	}
	
}
