package dao;

import exception.CustomerException;
import model.Customer;

public interface CustomerDao {

	public Customer loginCustomer (String userName, String password)throws CustomerException;
	
	public void transferAmount(int accNo,long ammount,int id,int cAcc) throws CustomerException;
	
	//public void checkBalance(int id) throws CustomerException;
	
	public void transactionHistory(int cAcc) throws CustomerException;
	
	public String registerCustome(String name,String user_name,String password)throws CustomerException;
	
}
