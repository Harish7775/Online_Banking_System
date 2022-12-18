package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import exception.CustomerException;
import model.Customer;
import utility.DBUtil;

public class CustomerDaoImpl implements CustomerDao{

	

		@Override
		public Customer loginCustomer(String userName, String password) throws CustomerException {
			Customer customer=null;
			
			try(Connection conn = DBUtil.provideConnection()) {
						
						
						PreparedStatement ps= conn.prepareStatement("select * from customer where user_name = ? AND password = ?");			
						
						ps.setString(1, userName);
						ps.setString(2, password);
						
						ResultSet rs= ps.executeQuery();
						
						
							if(rs.next()) {
							
							int a= rs.getInt("id");
							String n= rs.getString("name");
							String u= rs.getString("user_name");
							String p= rs.getString("password");
							
							customer=new Customer(a, n, u, p);	
							
							
						}else
							throw new CustomerException("Invalid Username or password.. ");
						
					} catch (SQLException e) {
						throw new CustomerException(e.getMessage());
					}
			
			
			
			
			return customer;
		}

		@Override
		public void transferAmount(int accNo,long ammount,int id,int cAcc) throws CustomerException {
			if(accNo==cAcc) {
				System.out.println("Can't send to same account");
				return;
			}
			
			try(Connection conn = DBUtil.provideConnection()) {
				
				LocalDate date=java.time.LocalDate.now() ;
				//System.out.println(date);
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");
			        //System.out.println();
				
				PreparedStatement ps= conn.prepareStatement("select balance from account where acc_no=? AND Customer_id=?");	
				
				ps.setInt(1, cAcc);
				ps.setInt(2, id);
				ResultSet  rs= ps.executeQuery();
				rs.next();
				long bal=rs.getLong("balance");
				//System.out.println(bal);
				if(bal<ammount) {
					//System.out.println("Insufficient Balance");
					throw new CustomerException("Insufficient Balance");
				}
				PreparedStatement ps1= conn.prepareStatement("UPDATE account SET balance=balance+? WHERE acc_no = ?");			
				
				ps1.setLong(1, ammount);
				ps1.setInt(2, accNo);
				int  up= ps1.executeUpdate();
				
				if(up<=0) {
					throw new CustomerException("invalid account number.");
				}
				
				PreparedStatement ps2= conn.prepareStatement("UPDATE account SET balance=balance-? WHERE acc_no = ? AND Customer_id=?;");			
				
				ps2.setLong(1, ammount);
				ps2.setInt(2, cAcc);
				ps2.setInt(3, id);
				ps2.executeUpdate();
			
				PreparedStatement ps3= conn.prepareStatement("insert into transactions(acc_no,receiver,date,amount) values(?,?,?,?)");			
				
				ps3.setInt(1, cAcc);
				
				ps3.setInt(2, accNo);

				ps3.setString(3,formatter.format(date));
				ps3.setLong(4,ammount);
				ps3.executeUpdate();
				
				
				 //System.out.println("Transaction Succesful ");
						
				
			} catch (SQLException e) {
				throw new CustomerException(e.getMessage());
			}

			System.out.println("Transaction Succesful");
		}

		
		@Override
		public String registerCustome(String name, String user_name, String password)throws CustomerException {
			String massage="";
			
			try(Connection conn = DBUtil.provideConnection()) {
				
				
				PreparedStatement ps= conn.prepareStatement("insert into customer (name,user_name,password) values (?,?,?)");			
				
				ps.setString(1, name);
				ps.setString(2, user_name);
				ps.setString(3, password);
				
				int x=ps.executeUpdate();
				
				
				if(x>0) {
					
					massage="Registration Succesfull";
					
					
				}else
					throw new CustomerException("User name Allready Exist");
				
				
				
				
			} catch (SQLException e) {
				throw new CustomerException(e.getMessage());
			}
			
			
			
			
			
			return massage;
		}

		
		
		@Override
		public void transactionHistory( int id) throws CustomerException {
			
			try(Connection conn=DBUtil.provideConnection()){
				PreparedStatement p= conn.prepareStatement("select acc_no from Account where customer_id=?");	
				p.setInt(1, id);
				
				ResultSet res=p.executeQuery();
				while(res.next()) {
					PreparedStatement ps= conn.prepareStatement("select * from transactions where acc_no=? or receiver=?");	
					int cAcc=res.getInt("acc_no");
					ps.setInt(1, cAcc);
					ps.setInt(2, cAcc);
					
					ResultSet rs= ps.executeQuery();
					
					while(rs.next()) {
						System.out.println("================================================================================");
						System.out.println("amount : "+rs.getLong("amount")+" Sender Ac No. : "+rs.getInt("acc_no")+ "  receiver Ac No. : "+rs.getInt("receiver")+"  Date : "+ rs.getDate("date"));
					}
				}
			
		}catch (SQLException e) {
			throw new CustomerException(e.getMessage());
		}
		}

		
		
	
}
