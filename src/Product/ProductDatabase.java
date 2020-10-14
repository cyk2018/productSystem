package Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class ProductDatabase {
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
		rowData.clear();//先把前端中的表格清空
		columnName.clear();
		rowcount = 0;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pres = null;
			
			conn.setAutoCommit(false);
			//按照编号增序排列
			String sql = "select * from product order by product_no";
			pres = conn.prepareStatement(sql);
			
			ResultSet rs = pres.executeQuery();
			//查询类的sql语句使用此指令，返回值是查询结果
			ResultSetMetaData data = rs.getMetaData();
			
			//使用中文作为表头，减少安全问题
			columnName.add("货物编号");
			columnName.add("货物名称");
			columnName.add("货物单价");
			columnName.add("库存容量");
			
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
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println(e.getClass().getName()+":->"+e.getMessage());
			System.exit(0);
		}
	}
	
	public boolean delete(Product product)throws SQLException
	{
		boolean b = false;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "delete from product where product_no = ?";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			pres.setString(1, product.get_product_no());
			int n = pres.executeUpdate();
			if(n > 0)
			{
				b = true;
			}
			if(pres != null || !pres.isClosed()) {
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
	
	public boolean insert(Product product)throws SQLException
	{
		boolean b = false;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into product values(?, ?, ?, ?)";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			pres.setString(1, product.get_product_no());
			pres.setString(2, product.get_product_name());
			pres.setString(3, product.get_product_price());
			pres.setString(4, product.get_product_amount());
			
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
	
	public boolean update(Product product)throws SQLException {
		boolean b = false;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		String sql = "update product set product_name = ?, product_price = ?, product_amount = ? where product_no = ? ";
		pres = conn.prepareStatement(sql);
		
		try {
			conn.setAutoCommit(false);
			
			pres.setString(1, product.get_product_name());
			pres.setString(2, product.get_product_price());
			pres.setString(3, product.get_product_amount());
			pres.setString(4, product.get_product_no());
			
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
			if(!conn.isClosed() || conn!= null)
			{
				conn.close();
			}
		}
		return b;
	}
	
	public boolean search(int searchoption, String searchinput) throws SQLException{
		boolean b = false;
		String sql = null;
		srowData.clear();
		scolumnName = columnName;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		switch (searchoption) {
		case 0:
			sql = "select * from product where product_no like ?";
			break;
		case 1:
			sql = "select * from product where product_name like ?";
			break;
		}
		pres = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		pres.setString(1, "%" + searchinput + "%");
		ResultSet rs = pres.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		
		while (rs.next()) {
			Vector lineVector = new Vector();
			for(int j = 1; j <= data.getColumnCount(); j++)
			{
				lineVector.add(rs.getString(data.getColumnName(j)));
			}
			srowData.add(lineVector);
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
