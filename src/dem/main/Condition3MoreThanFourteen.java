package dem.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Condition3MoreThanFourteen extends Condition1ConsecutiveSeven{
	
	//Method to get Employees who has worked for more than 14 hours in a single shift
	public void getEmployeesWorkingMoreThanFourteenHoursSingleShift() {
		try {
            		Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            		if (connection != null) {		
                		System.out.println("Connected to the database.");

                		// Query to retrieve data
                		String query = "SELECT * FROM employee";
                		PreparedStatement statementForAll = connection.prepareStatement(query);
                		ResultSet resultSetForAll = statementForAll.executeQuery();
                
                		//Traversing resultSet
                		while(resultSetForAll.next()) {
                	
	                		String timeCardHours = resultSetForAll.getString("Timecard Hours (as Time)");
        	        	
                			if(!timeCardHours.isEmpty()) {
                				String[] hours = timeCardHours.split(":");
                    				int hoursInInt = Integer.parseInt(hours[0]);
                    				if(hoursInInt > 14) {
	                    				System.out.println("Employees who worked for more than 14hrs in single shift:");
        	            				System.out.println(resultSetForAll.getString("Employee Name")+" - "+resultSetForAll.getString("Position Status"));
                    				}
                			}
                		}
            		}
            		// Close the database connection
            		connection.close();
            
		}catch (Exception e) {
			e.printStackTrace();
		}            
	}
}
