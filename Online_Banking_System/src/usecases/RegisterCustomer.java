package usecases;

import java.util.Scanner;

import dao.CustomerDao;
import dao.CustomerDaoImpl;
import exception.CustomerException;

public class RegisterCustomer {

	

		public static void main(String[] args) {
			
			try (Scanner sc = new Scanner(System.in)) {
				System.out.println("Enter name:");
				String name = sc.next();
				System.out.println("Enter user name:");
				String uname = sc.next();
				
				System.out.println("Enter password:");
				String pass = sc.next();
				
				CustomerDao dao= new CustomerDaoImpl();
				
				String str = null;
				try {
					str = dao.registerCustome(name,uname,pass);
				} catch (CustomerException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}

				System.out.println(str);
				}
				
				
			
		}

}
