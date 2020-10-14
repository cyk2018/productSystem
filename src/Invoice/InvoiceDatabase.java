package Invoice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.Select;


public class InvoiceDatabase {
	public Vector rowData = new Vector();
	public Vector columnName = new Vector();
	public Vector srowData = new Vector();
	public Vector scolumnName = new Vector();
	
	private String URL = "jdbc:mysql://localhost:3306/DB2018081301003?serverTimezone=UTC";
	private String USER = "root";
	private String PASS = "admin";
	
	public int rowcount = 0;
	
	public void main()
	{
		rowData.clear();
		columnName.clear();
		rowcount = 0;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pres = null;
			conn.setAutoCommit(false);
			String sql = "select * from Invoice order by invoice_no";
			pres = conn.prepareStatement(sql);
			ResultSet rs = pres.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			
			columnName.add("发票编号");
			columnName.add("开票人编号");
			columnName.add("订单编号");
			columnName.add("开票时间");
			
			while(rs.next()) {
				Vector line = new Vector();
				for(int j = 1; j <= data.getColumnCount(); j++)
				{
					line.add(rs.getString(data.getColumnName(j)));//添加到对应行
				}
				rowData.add(line);
				rowcount++;
			}
			
			if(pres != null || !pres.isClosed())
			{
				pres.close();
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getClass().getName()+":->"+e.getMessage());
			System.exit(0);
		}
	}
	
	public boolean insert(Invoice invoice)throws SQLException
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into invoice values(?, ?, ?, ?);";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			pres.setString(1, invoice.get_invoice_no());
			pres.setString(2, invoice.get_invoice_client_no());
			pres.setString(3, invoice.get_order_no());
			pres.setString(4, invoice.get_invoice_time());
			
			int n = pres.executeUpdate();
			if(n > 0)
			{
				b = true;
			}			
			if(pres != null || !pres.isClosed())
			{
				pres.close();
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO: handle exception
			conn.rollback();
		}
		finally {
			if(!conn.isClosed() || conn != null)
			{
				conn.close();
			}
		}
		return b;
	}
	
	public boolean delete(Invoice invoice)throws SQLException
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "delete from invoice where invoice_no = ?;";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			pres.setString(1, invoice.get_invoice_no());
			int n = pres.executeUpdate();
			if(n > 0)
			{
				b = true;
			}
			if(pres!= null || !pres.isClosed())
			{
				pres.close();
			}
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			conn.rollback();
		}
		finally {
			if(!conn.isClosed() || conn != null)
			{
				conn.close();
			}
		}
		
		return b;
	}
	
	public boolean update(Invoice invoice)throws SQLException
	{
		boolean b = false;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		String sql = "update invoice set invoice_client_no = ?, order_no = ?, invoice_time = ? where invoice_no = ?";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			pres.setString(1, invoice.get_invoice_client_no());
			pres.setString(2, invoice.get_order_no());
			pres.setString(3, invoice.get_invoice_time());
			pres.setString(4, invoice.get_invoice_no());
			
			int n = pres.executeUpdate();
			
			if(n > 0)
			{
				b = true;
			}
			if(pres != null || !pres.isClosed())
			{
				pres.close();
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO: handle exception
			conn.rollback();
		}
		finally {
			if(!conn.isClosed() || conn != null)
			{
				conn.isClosed();
			}
		}
		return b;
	}
	
	public boolean search(int searchoption, String searchinput)throws SQLException
	{
		boolean b = false;
		String sql = null;
		scolumnName.clear();
		srowData.clear();
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		switch (searchoption) {
		case 0:
			sql = "select invoice_no, invoice_client_no, invoice.order_no, invoice_time from invoice where invoice_no like ?";
			break;
		case 1:
			sql = "select invoice_no, invoice_client_no, order_no, invoice_time from invoice where invoice_client_no like ?";
			break;
		case 2:
			sql = "select invoice_no, invoice_client_no, order_no, invoice_time from invoice where order_no like ?";
			break;
		}
		
		conn.setAutoCommit(false);
		pres = conn.prepareStatement(sql);
		pres.setString(1, "%" + searchinput + "%");
		ResultSet rs = pres.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		scolumnName = columnName;
		while(rs.next())
		{
			Vector line = new Vector();
			for(int j = 1; j <= data.getColumnCount(); j++)
			{
				line.add(rs.getString(data.getColumnName(j)));
			}
			srowData.add(line);
		}
		if(pres != null || !pres.isClosed())
		{
			pres.close();
		}
		conn.commit();
		conn.close();
		return b;
	}
}
