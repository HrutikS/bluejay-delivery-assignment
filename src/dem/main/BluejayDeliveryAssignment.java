package dem.main;

public class BluejayDeliveryAssignment {

	public static void main(String[] args){
		
		//To get List of Employees who have worked for 7 consecutive days.
		Condition1ConsecutiveSeven seven = new Condition1ConsecutiveSeven();
		seven.getEmployeesWorkingSevenConsecutiveDays();
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		//To get List of Employees who have less than 10 hours of time between shifts but greater than 1 hour .
		Condition2LessThanTenGreaterThanOne ten = new Condition2LessThanTenGreaterThanOne();
		ten.getEmployeesWorkingLessThanTenGreaterThanOneHours();
		System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		//To get List of Employees who have worked for more than 14 hours in a single shift.
		Condition3MoreThanFourteen fourteen = new Condition3MoreThanFourteen();
		fourteen.getEmployeesWorkingMoreThanFourteenHoursSingleShift();
		
	}
}