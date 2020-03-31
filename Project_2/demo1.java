


//front end JAVA JDBC




//Illustrate call stored procedure
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class demo1 {

   public static void main (String args []) throws SQLException {
	   boolean exit = false;
    try
    {

        //Connection to Oracle server. Need to replace username and
      	//password by your username and your password. For security
      	//consideration, it's better to read them in from keyboard.
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
        Connection conn = ds.getConnection("aanveka2", "Ravalnath#2195");
        DisplayTables dt = new DisplayTables();
	        
	        while(!exit) {
	        	System.out.println();
	        	String input;
	        	BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
	        	
	        	System.out.println("Select anyone from the following:");
	        	System.out.println("1.Display Table");
	        	System.out.println("2.report the monthly sale information");
	        	System.out.println("3.add values into purchase table");
	        	System.out.println("4.add values into products table");
				System.out.println("5.exit");
	        	
	        	input = userInput.readLine();
	        	
	        	boolean exit1 = false;
	        	
	        	if(input.equals("1")) {
	        		while(!exit1) {
	        			System.out.println();
	        			String selectedtable;
	        			
	        			System.out.println("select a table to display:");
	        			System.out.println("1.employees");
	        			System.out.println("2.customers");
	        			System.out.println("3.products");
	        			System.out.println("4.suppliers");
	        			System.out.println("5.supply");
	        			System.out.println("6.purchases");
	        			System.out.println("7.logs");
	        			
	        			selectedtable = userInput.readLine();
	        			//show employees table
	        			if(selectedtable.equals("1")) {
	        				dt.show_employees(conn);
	        				exit1 = true;
	        			}
						//show customers table
	        			else if(selectedtable.equals("2")) {
	        				dt.show_customers(conn);
	        				exit1 = true;
	        			}
						//show products table
	        			else if(selectedtable.equals("3")) {
	        				dt.show_products(conn);
	        				exit1 = true;
	        			}
						// show suppliers table
	        			else if(selectedtable.equals("4")) {
	        				dt.show_suppliers(conn);
	        				exit1 = true;
	        			}
						// show supply table
	        			else if(selectedtable.equals("5")) {
	        				dt.show_supply(conn);
	        				exit1 = true;
	        			}
						// show purchases table
	        			else if(selectedtable.equals("6")) {
	        				dt.show_purchases(conn);
	        				exit1 = true;
	        			}
						// show logs table
	        			else if(selectedtable.equals("7")) {
	        				dt.show_logs(conn);
	        				exit1 = true;
	        			}	        			
	        			else {
	        				System.out.println("Please select one among the given options");
	        			}
	        		}
	        		
	        	}
	        	// call report_monthly_sale method
	        	else if(input.equals("2")) {
	        		System.out.println();
	        		String p_id;
	        		System.out.println("Please enter productid :");
	        		p_id = userInput.readLine();
	        		report_monthly_sale(p_id,conn);	        		
	        	}
	        	// call insert_purchases method
	        	
	        	else if(input.equals("3")) {
	                String e_id;
	                String p_id;
					String c_id;
					String qty_purc;
	                System.out.println("Please enter The employee id: ");
	                e_id = userInput.readLine();
	                System.out.println("Please Enter The product id: ");
	                p_id = userInput.readLine();
		
		System.out.println("Please Enter The customer id: ");
	                c_id = userInput.readLine();
	                System.out.println("Please Enter The quantity: ");
	                qty_purc = userInput.readLine();
	                insert_purchases(e_id, p_id, c_id, qty_purc, conn); 
	        	}
	        	// call insert_products method
	        	else if(input.equals("4")) {
	                String p_id;
                    String p_name;
                    String q_oh;
                    String q_oh_threshold;
                    String orig_price;
                    String discnt_rt;
	                System.out.println("Please enter The product id: ");
	                p_id = userInput.readLine();
	                System.out.println("Please Enter The product name: ");
	                p_name = userInput.readLine();
					 System.out.println("Please Enter The QOH: ");
	                q_oh = userInput.readLine();
					 System.out.println("Please Enter The QOH Threshold: ");
	                q_oh_threshold = userInput.readLine();
					 System.out.println("Please Enter The Original price: ");
	                 orig_price = userInput.readLine();
					  System.out.println("Please Enter The discount rate: ");
	                 discnt_rt = userInput.readLine();
					 insert_products(p_id, p_name, q_oh, q_oh_threshold, orig_price, discnt_rt, conn); 
	        	}
	        	// exit
	        	
	         else if(input.equals("5")) {
	        		  exit = true;
	        		conn.close();
	        	}
	        	else {
	        		System.out.println("Please select one among the given options");
	        	}
	       	
				
	  //close the result set, statement, and the connection
      // conn.close();
   }
    }
   catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
  }
  
   public static void report_monthly_sale(String p_id, Connection conn) {
		try {
			//Prepare to call stored procedure:
			CallableStatement st_info = conn.prepareCall("{call project2.report_monthly_sale(?, ?)}");
			
			//set parameters
			st_info.setString(1, p_id);
			st_info.registerOutParameter(2,OracleTypes.CURSOR);
			// execute and retrieve the result set
			st_info.execute();
			
			ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(2);
			// print the results
			while(op.next()) {
				System.out.println("the monthly sale for product is\n \n");
				System.out.println(op.getString(1) + "\t" +
								  op.getString(2) + "\t" +
								  op.getInt(3) + "\t" +
								  op.getDouble(4) + "\t" +
								  op.getDouble(5));
			}
			//close the result set, statement
			st_info.close();
			op.close();
		}
	    catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
	    catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}		
		
	}
   public static void insert_purchases(String e_id, String p_id, String c_id,String qty_purc, Connection conn ) {
		try{
			//Prepare to call stored procedure:
			CallableStatement cs = conn.prepareCall("{call project2.insert_purchases(?, ?, ?, ?, ?)}");

			//set parameters
			cs.setString(1, e_id);
			cs.setString(2, p_id);
			cs.setString(3, c_id);
			cs.setString(4, qty_purc);
			cs.registerOutParameter(5, Types.VARCHAR);

			// execute and retrieve the result set
			cs.execute();

			//get the out parameter result.
			String status = cs.getString(5);
			System.out.println(status);

			//close the statement
			cs.close();

		}
		catch (SQLException ex) { System.out.println ("\n** SQLException caught **\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n** other Exception caught **\n");}
	}
   public static void insert_products(String p_id, String p_name, String q_oh, String q_oh_threshold, String orig_price, String discnt_rt, Connection conn ) {
		try{
			//Prepare to call stored procedure:
			CallableStatement cs = conn.prepareCall("{call project2.insert_products(?, ?, ?,?, ?, ?, ?)}");

			//set parameters
			cs.setString(1, p_id);
			cs.setString(2, p_name);
			cs.setString(3, q_oh);
			cs.setString(4, q_oh_threshold);
			cs.setString(5, orig_price);
			cs.setString(6, discnt_rt);
			cs.registerOutParameter(7, Types.VARCHAR);

			// execute and retrieve the result set
			cs.execute();

			//get the out parameter result.
			String status = cs.getString(7);
			System.out.println(status);

			//close the statement
			cs.close();

		}
		catch (SQLException ex) { System.out.println ("\n** SQLException caught **\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n** other Exception caught **\n");}
	}
	}
   
	
	
	
	
	
