package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.AccountantException;
import exception.CustomerException;
import model.Accountant;
import utility.DBUtil;

public class AccountantDaoImpl implements AccountantDao{


	@Override
	public Accountant loginAcountant(String userName, String password) throws AccountantException {
		
		Accountant accountant=null;
		
		if(userName.equals("masai@123") && password.equals("123456")) {
			accountant=new Accountant(12, "Harish", "Sonkusare", userName, password);
		}else {
			throw new AccountantException("Username and password not valid");
		}
		
		
		return accountant;
	}

	@Override
	public void addCustomer(int id,int acc_no) throws CustomerException {
		
		try(Connection conn = DBUtil.provideConnection()) {
			
			
			PreparedStatement ps= conn.prepareStatement("insert into account (accno,customerid) values (?,?)");			
			ps.setInt(1, acc_no);
			ps.setInt(2, id);
			
			
			int x=ps.executeUpdate();
			
			
			if(x>0) {
				
				System.out.println("Registration Succesfull");
				PreparedStatement ps1= conn.prepareStatement("Select * from customer inner join account on customer.id=account.customer_id where account.acc_no=? and customer.id=?");			
				
				ps1.setInt(1, acc_no);
				ps1.setInt(2, id);
				
				ResultSet res=ps1.executeQuery();
				res.next();
				String sert=res.getString("name");
				System.out.println("Account no "+acc_no+" Assign to user "+sert);
				
				
			}else {
				throw new CustomerException("Customer not exist");
			
			 }
			
			
		} catch (SQLException e) {
			throw new CustomerException("Customer not Exist");
		}
		
	}

	@Override
	public void changeName(int id,String name) throws CustomerException {


		try(Connection conn = DBUtil.provideConnection()) {
			
			PreparedStatement ps= conn.prepareStatement("UPDATE customer SET name=? WHERE id = ?");	
			
			ps.setString(1, name);
			ps.setInt(2, id);
			
			int  up= ps.executeUpdate();
			
			if(up<=0) {
				throw new CustomerException("invalid user");
			}
			System.out.println("Name changed succesfully");
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
	}

	@Override
	public void changeUserName(int id,String name) throws CustomerException {
		try(Connection conn = DBUtil.provideConnection()) {
			
			PreparedStatement ps= conn.prepareStatement("UPDATE customer SET user_name=? WHERE id = ?");	
			
			ps.setString(1, name);
			ps.setInt(2, id);
			
			int  up= ps.executeUpdate();
			
			if(up<=0) {
				throw new CustomerException("invalid user");
			}
			System.out.println("user name changed succesfully");
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		
	}

	@Override
	public void removeAcc(int accNo) throws CustomerException {
		try(Connection conn = DBUtil.provideConnection()) {
			
			PreparedStatement ps= conn.prepareStatement("DELETE FROM account WHERE acc_no=?");	
			
			ps.setInt(1, accNo);
			
			int  up= ps.executeUpdate();
			
			if(up<=0) {
				throw new CustomerException("invalid Account");
			}
			System.out.println("Account deleted Succesfully");
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		
	}

	@Override
	public void viewDetail(int accNo) throws CustomerException {
		try(Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps1=conn.prepareStatement("select customer.id, customer.name, customer.user_name, account.acc_no, account.balance,account.loan"
					+ "from customer inner join account on customer.id=account.customer_id where account.acc_no=?");
			ps1.setInt(1, accNo);
			
			ResultSet res=ps1.executeQuery();
			
			if(res.next()) {
				System.out.println("======================================================");
				System.out.println("account no.:- "+res.getInt("acc_no")+"\n "+"Customer Id :- "+res.getInt("id")+"\n "+"Customer Name :- "+res.getString("name")
				+"\n "+"Customer user name :- "+res.getString("user_name")+"\n "+"Customer Account balance :- "+res.getString("balance")+" Customer loan :- "+res.getString("loan"));
				System.out.println("======================================================");
				System.out.println("transaction history");
				try {
					CustomerDao dao=new CustomerDaoImpl();
					
					dao.transactionHistory(res.getInt("id"));
					
				
				}catch (CustomerException e) {
					System.out.println(e.getMessage());
				}
			}else {
				throw new CustomerException("invalid account");
			}
			
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		
	}

	@Override
	public void viewAllAccDetail() throws CustomerException {


		try(Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps1=conn.prepareStatement("select customer.id, customer.name, customer.user_name, account.acc_no, account.balance,account.loan "
					+ "from customer inner join account on customer.id=account.customer_id");
			
			
			ResultSet res=ps1.executeQuery();
			
			while(res.next()) {
				System.out.println("======================================================");
				System.out.println("account no.:- "+res.getInt("acc_no")+"\n "+"Customer Id :- "+res.getInt("id")+"\n "+"Customer Name :- "+res.getString("name")
				+"\n "+"Customer user name :- "+res.getString("user_name")+"\n "+"Customer Account balance :- "+res.getString("balance")+" Customer loan :- "+res.getString("loan"));
				//System.out.println("======================================================");
				
				
			}
			
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		
		
	}

	@Override
	public void depositeAmount(int accNo , int amount) throws CustomerException {
		try(Connection conn = DBUtil.provideConnection()) {
			
			PreparedStatement ps= conn.prepareStatement("UPDATE account SET balance=balance+? WHERE acc_no = ?");	
			
			ps.setInt(1, amount);
			ps.setInt(2, accNo);
			
			int  up= ps.executeUpdate();
			
			if(up<=0) {
				throw new CustomerException("invalid user");
			}
			System.out.println("balance deposited succesfully");
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
	}

	@Override
	public void withdrawAmount(int accNo,int amount) throws CustomerException {
		try(Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps1=conn.prepareStatement("select balance from account where acc_no=?");
			ps1.setInt(1, accNo);
			
			ResultSet res=ps1.executeQuery();
			
			if(res.next()) {
				long bal=res.getLong("balance");
				if(bal<amount) {
					throw new CustomerException("insufficient balance ");
				}
			}else {
				throw new CustomerException("invalid account");
			}
			PreparedStatement ps= conn.prepareStatement("UPDATE account SET balance=balance-? WHERE acc_no = ?");	
			
			ps.setInt(1, amount);
			ps.setInt(2, accNo);
			
			int  up= ps.executeUpdate();
			
			if(up<=0) {
				throw new CustomerException("invalid user");
			}
			System.out.println("balance withdraw succesfull");
		} catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
	}

}
