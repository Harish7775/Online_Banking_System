package usecases;

import java.util.Scanner;

import dao.CustomerDao;
import dao.CustomerDaoImpl;
import exception.CustomerException;
import model.Customer;

public class CustomerLogin {

	

		public static void main(String[] args) {
			Scanner sc= new Scanner(System.in);
			
			System.out.println("Enter Username:");
			String uname = sc.next();
			
			System.out.println("Enter Password:");
			String pass = sc.next();
			
			CustomerDao dao = new CustomerDaoImpl();
			
			try {
				Customer customer= dao.loginCustomer(uname, pass);
			
		 		System.out.println("Welcome :"+customer.getName());
		 	
		 	
			}catch (CustomerException e) {
				System.out.println(e.getMessage());
			}
		}

}
