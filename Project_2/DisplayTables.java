//JDBC for Tables

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import java.lang.Object;

public class DisplayTables {
	public DisplayTables() {}
	
	public void show_employees(Connection conn) {
		try {
			//Prepare to call stored procedure:
			CallableStatement st_info = conn.prepareCall("{call project2.show_employees(?)}");
			st_info.registerOutParameter(1, OracleTypes.CURSOR);
			// execute and retrieve the result set
			st_info.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);
			
	        while (op.next()) {
	            System.out.println(op.getString(1) + "\t" +
	                              op.getString(2) + "\t" + 
	                              op.getString(3));
	        }
	        //close the result set, statement
	        op.close();
	        st_info.close();
		}
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}		
	}
	
	public void show_customers(Connection conn) {
    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_customers(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);

	        // print the results
	        while (op.next()) {
	            System.out.println(op.getString(1) + "\t" +
	                             op.getString(2)  + "\t" + 
	                              op.getString(3)  + "\t" +
                                      op.getInt(4) + "\t" + 
	                              op.getString(5));
	        }

	        //close the result set, statement
	        op.close();
	        op.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	}
	
	public void show_products(Connection conn){

    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_products(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);

	        // print the results
	        while (op.next()) {
	            System.out.println(op.getString(1) + "\t" + 
	                              op.getString(2) + "\t" +
	                              op.getInt(3) + "\t" +
                                      op.getInt(4) + "\t" +
                                      op.getDouble(5) + "\t" +
                                      op.getDouble(6));
	        }

	        //close the result set, statement
	        op.close();
	        st_info.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

	}	
	
	public void show_suppliers(Connection conn){

    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_suppliers(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);

	        // print the results
	        while (op.next()) {
	            System.out.println(op.getInt(1) + "\t" +
	                              op.getString(2) + "\t" +
                                      op.getString(3) + "\t" +
	                              op.getString(4));
	        }

	        //close the result set, statement
	        op.close();
	        st_info.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

	}

	public void show_supply(Connection conn){

    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_supply(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);

	        // print the results
	        while (op.next()) {
	            System.out.println(op.getInt(1) + "\t" +
                                       op.getString(2) + "\t" +
                                       op.getInt(3) + "\t" +
                                       op.getTimestamp(4) + "\t" +
	                               op.getInt(5));
	        }

	        //close the result set, statement
	        op.close();
	        st_info.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

	}
	
	public void show_logs(Connection conn){

    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_logs(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);


	        // print the results
	        while (op.next()) {

	            System.out.println(op.getInt(1) + "\t" +
	                              op.getString(2) + "\t" + 
	                              op.getTimestamp(3) + "\t" + 
	                              op.getString(4) + "\t" + 
	                              op.getString(5) + "\t" + 
	                              op.getString(6));
	        }

	        //close the result set, statement
	        op.close();
	        st_info.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n" + e.toString());}

	}
	public void show_purchases(Connection conn){

    	try{

	        //Prepare to call stored procedure:
	        CallableStatement st_info = conn.prepareCall("{call project2.show_purchases(?)}");
	        st_info.registerOutParameter(1, OracleTypes.CURSOR);
	        
	        // execute and retrieve the result set
	        st_info.execute();
	        ResultSet op = (ResultSet)((OracleCallableStatement)st_info).getCursor(1);


	        // print the results
	        while (op.next()) {

	            System.out.println(op.getInt(1) + "\t" +
                                       op.getString(2) + "\t" +
	                              op.getString(3) + "\t" + 
	                              op.getString(4) + "\t" +
                                      op.getInt(5) + "\t" +
                                      op.getTimestamp(6) + "\t" +
                                      op.getDouble(7));
	        }

	        //close the result set, statement
	        op.close();
	        st_info.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n" + e.toString());}

	}	
	
}
