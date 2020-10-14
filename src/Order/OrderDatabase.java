package Order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Vector;

import com.sun.prism.Presentable;


public class OrderDatabase {
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
			String sql = "select order_no, client_no, order_time, product_no, order_amount from orderitems order by order_no;";
			pres = conn.prepareStatement(sql);
			
			ResultSet rs = pres.executeQuery();
			//查询类的sql语句使用此指令，返回值是查询结果
			ResultSetMetaData data = rs.getMetaData();
			
			//使用中文作为表头，减少安全问题
			columnName.add("订单编号");
			columnName.add("客户编号");
			columnName.add("下单时间");
			columnName.add("货物编号");
			columnName.add("订货数量");
			
			
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
	
	public boolean insert(Order order) throws SQLException{//存在只对orderdetails的修改，条件是添加的数据订单编号与已知的订单编号相同，如果相同只需要修改orderdetails表格即可
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into orderitems values(?, ?, ?, ?, ?);";
		
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			//将问号处替换为对应的值
			pres.setString(1, order.get_order_no());
			pres.setString(2, order.get_order_client_no());
			pres.setString(3, order.get_order_time());
			pres.setString(4, order.get_order_product_no());
			pres.setString(5, order.get_order_amount());
			
			//更新
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
			try {
				conn.rollback();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		finally {
			try {
				if(!conn.isClosed()||conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public boolean delete(Order order)throws SQLException 
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "delete from orderitems where order_no = ?;";
		//根据对应的client_no删除相应的元组
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			//将问号处替换为对应的值
			pres.setString(1, order.get_order_no());
			
			//更新
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
			try {
				conn.rollback();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		finally {
			try {
				if(!conn.isClosed()||conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b;
	}
	
	
	public boolean update(Order order) throws SQLException
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "update orderitems set client_no = ?, order_time = ?, order_amount = ? where order_no = ? and product_no = ?"; 
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			pres.setString(1, order.get_order_client_no());
			pres.setString(2, order.get_order_time());
			pres.setString(3, order.get_order_amount());
			pres.setString(4, order.get_order_no());
			pres.setString(5, order.get_order_product_no());
			
			//更新
			int n = pres.executeUpdate();
			//写入失败的时候自动从该接口退出，不再执行下面的代码，返回值是修改的行数
			
			if(n > 0)
			{
				b = true;
			}
			
			if(pres != null || !pres.isClosed())//没关闭的话关闭数据库
			{
				pres.close();
			}
			conn.commit();
			
		} catch (SQLException e) {
			// TODO: handle exception
			try {
				conn.rollback();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		finally {
			try {
				if(!conn.isClosed()||conn!=null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			sql = "select orderitems.order_no, orderitems.client_no, client_name, order_time, orderitems.product_no, product_name, order_amount from orderitems, client, product where orderitems.order_no like ? and client.client_no = orderitems.client_no and product.product_no = orderitems.product_no order by orderitems.order_no, orderitems.client_no;";
			
			break;
		case 1:
			sql = "select orderitems.order_no, orderitems.client_no, client_name, order_time, orderitems.product_no, product_name, order_amount from orderitems, client, product where orderitems.client_no like ? and client.client_no = orderitems.client_no and product.product_no = orderitems.product_no order by orderitems.order_no, orderitems.client_no;";
			break;
		case 2:
			sql = "select orderitems.order_no, orderitems.client_no, client_name, order_time, orderitems.product_no, product_name, order_amount orderitems, client, product where orderitems.product_no like ? and client.client_no = orderitems.client_no and product.product_no = orderitems.product_no order by orderitems.order_no, orderitems.client_no;";
			break;
		}
		pres = conn.prepareStatement(sql);
		
		conn.setAutoCommit(false);
			pres.setString(1, "%" + searchinput + "%");
			
			ResultSet rs = pres.executeQuery();
			
			//执行Query操作, 返回值是搜索结果
			ResultSetMetaData data = rs.getMetaData();
			
			scolumnName.add("订单编号");
			scolumnName.add("客户编号");
			scolumnName.add("客户姓名");
			scolumnName.add("下单时间");
			scolumnName.add("货物编号");
			scolumnName.add("货物名称");
			scolumnName.add("订货数量");

			while(rs.next()) {
				
				Vector line = new Vector();
				
				//getcolumnCount表示列数，此处通过循环实现每行数据的添加（rowData数组中存储的每一个元素都是数组（line)，每个line中存储的元素都是每个单元格内的数据
				for(int j = 1; j <= data.getColumnCount(); j++)
				{
					
					line.add(rs.getString(data.getColumnName(j)));//添加到对应行
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
