package usecases;

import dao.CustomerDao;
import dao.CustomerDaoImpl;
import exception.CustomerException;
import model.Customer;

public class CTransHistory {


	Customer c=new Customer();
	
	
	
	
	public static void main(String[] args) {
		CTransHistory obj=new CTransHistory();
			CustomerDao dao=new CustomerDaoImpl();
			try {
				dao.transactionHistory(obj.c.getId());
				
			
			}catch (CustomerException e) {
				System.out.println(e.getMessage());
			}
		}


}
