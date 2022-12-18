package usecases;

import java.util.Scanner;

import dao.CustomerDao;
import dao.CustomerDaoImpl;
import exception.CustomerException;

public class CustomerTranseferAmount {

	

		public static void main(String[] args) {
			try (Scanner sc = new Scanner(System.in)) {
				System.out.println("Enter reciever Account number:");
				int acc_no = sc.nextInt();
				
				System.out.println("Enter amount:");
				long amount = sc.nextLong();
				
				CustomerDao dao = new CustomerDaoImpl();
				
				try {
					dao.transferAmount(acc_no, amount,1,25);
					
				
				}catch (CustomerException e) {
					System.out.println(e.getMessage());
				}
			}

		}

}
