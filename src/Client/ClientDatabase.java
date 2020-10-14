package Client;

/**
 *对用户表进行增删改查的sql语句设置
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class ClientDatabase {
	public Vector rowData = new Vector();
	public Vector columnName = new Vector();
	public Vector srowData = new Vector();
	public Vector scolumnName = new Vector();
	//用于创建查询结果的数组
	
	private String URL = "jdbc:mysql://localhost:3306/DB2018081301003?serverTimezone=UTC";
	private String USER = "root";
	private String PASS = "admin";
	public int rowcount = 0;//统计当前数据库中的数据行数
	
	public void main()//主要用于对数据库中的数据进行展示
	{
		rowData.clear();//先把前端中的表格清空
		columnName.clear();
		rowcount = 0;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pres = null;
			
			conn.setAutoCommit(false);
			
			String sql = "select * from client order by client_no";
			pres = conn.prepareStatement(sql);
			
			ResultSet rs = pres.executeQuery();
			//查询类的sql语句使用此指令，返回值是查询结果
			ResultSetMetaData data = rs.getMetaData();
			
			//使用中文作为表头，减少安全问题
			columnName.add("客户编号");
			columnName.add("客户姓名");
			columnName.add("客户性别");
			columnName.add("客户电话");
			
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
	public boolean insert(Client c) throws SQLException{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into client values(?, ?, ?, ?)";
		//根据对应的client_no添加相应的元组
		pres = conn.prepareStatement(sql);
		
		try {
			conn.setAutoCommit(false);
			
			//将问号处替换为对应的值
			pres.setString(1, c.get_no());
			pres.setString(2, c.get_name());
			pres.setString(3, c.get_sex());
			pres.setString(4, c.get_tel());
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
	public boolean delete(Client c)throws SQLException 
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "delete from client where client_no = ?";
		//根据对应的client_no删除相应的元组
		pres = conn.prepareStatement(sql);
		
		try {
			conn.setAutoCommit(false);
			
			//将问号处替换为对应的值
			pres.setString(1, c.get_no());
			
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
	
	
	public boolean update(Client c) throws SQLException
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "update client set client_no = ?, client_name = ?, client_sex = ?, client_tel = ? where client_no = ?";
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			pres.setString(1, c.get_no());
			pres.setString(2, c.get_name());
			pres.setString(3, c.get_sex());
			pres.setString(4, c.get_tel());
			pres.setString(5, c.get_no());
			
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
		srowData.clear();
		scolumnName = columnName;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		switch (searchoption) {
		case 0:
			sql = "select * from client where client_no like ?";
			break;
		case 1:
			sql = "select * from client where client_name like ?";
			break;
		case 2:
			sql = "select * from client where client_sex like ?";
			break;
		}
		pres = conn.prepareStatement(sql);
		
		conn.setAutoCommit(false);
			pres.setString(1, searchinput+"%");
			
			ResultSet rs = pres.executeQuery();
			
			//执行Query操作, 返回值是搜索结果
			ResultSetMetaData data = rs.getMetaData();

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
