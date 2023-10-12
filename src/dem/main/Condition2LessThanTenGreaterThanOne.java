package dem.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Condition2LessThanTenGreaterThanOne extends Condition1ConsecutiveSeven{

	private int currHour = 0;
	private int prevHour = 0;
	private int hourDiff = 0;
	protected List<String> timeTakersBetweenShifts = new ArrayList<String>();
	
	//Method to get Employees who have less than 10 hours of time between shifts but greater than 1 hour.
	public void getEmployeesWorkingLessThanTenGreaterThanOneHours(){
		
		try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            if (connection != null) {		
                System.out.println("Connected to the database.");
                
                // Query to retrieve data
                String query = "SELECT * FROM employee";
                PreparedStatement statementForAll = connection.prepareStatement(query);
                ResultSet resultSetForAll = statementForAll.executeQuery();
                
                //Query to retrieve distinct data 
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
                while(resultSetForAll.next()){
                	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
                	
                	String currEmployeeName = resultSetForAll.getString("Employee Name");
                	String prevEmployeeName = listOfUniqueEmployees.get(index);
                	
                	if(!currEmployeeName.equals(prevEmployeeName)) {
                		index++;
                		prevEmployeeName = listOfUniqueEmployees.get(index);
                		prevDay = 0;
                		prevHour = 0;
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
                				currHour = currDateTime.getHour();
                    			
                    			if(prevHour == 0) {
                    				prevHour = currHour;
                    			}
                    			if(currHour == prevHour) {
                    				break;
                    			}
                    			
                    			hourDiff = currHour - prevHour;
                    			
                    			if(1 < hourDiff && hourDiff < 10) {
                    				if(!timeTakersBetweenShifts.contains(currEmployeeName)){
                    					timeTakersBetweenShifts.add(currEmployeeName);
                    				}
                    			}
                				break;
                			}
                			if(currDay > prevDay) {
                				prevDay = currDay;
                				break;
                			}
                		}
                	}
                }
            }
            
            //Print employees who worked for 7 consecutive days
            System.out.println("Employees who have less than 10 hours of time between shifts but greater than 1 hour:");
            for(String worker : timeTakersBetweenShifts) {
            	System.out.println(worker+" - "+listOfUniqueEmployeesWithPoisitionStatus.get(worker));
            }
            
            //Close the database connection
            connection.close();
            
		}catch (Exception e) {
        	e.printStackTrace();
		}
	}	
}