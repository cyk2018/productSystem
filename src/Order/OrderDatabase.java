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
		rowData.clear();//�Ȱ�ǰ���еı�����
		columnName.clear();
		rowcount = 0;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pres = null;
			
			conn.setAutoCommit(false);
			//���ձ����������
			String sql = "select order_no, client_no, order_time, product_no, order_amount from orderitems order by order_no;";
			pres = conn.prepareStatement(sql);
			
			ResultSet rs = pres.executeQuery();
			//��ѯ���sql���ʹ�ô�ָ�����ֵ�ǲ�ѯ���
			ResultSetMetaData data = rs.getMetaData();
			
			//ʹ��������Ϊ��ͷ�����ٰ�ȫ����
			columnName.add("�������");
			columnName.add("�ͻ����");
			columnName.add("�µ�ʱ��");
			columnName.add("������");
			columnName.add("��������");
			
			
			while(rs.next()) {
				Vector line = new Vector();
				for(int j = 1; j <= data.getColumnCount(); j++)
				{
					line.add(rs.getString(data.getColumnName(j)));//��ӵ���Ӧ��
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
	
	public boolean insert(Order order) throws SQLException{//����ֻ��orderdetails���޸ģ���������ӵ����ݶ����������֪�Ķ��������ͬ�������ֻͬ��Ҫ�޸�orderdetails��񼴿�
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into orderitems values(?, ?, ?, ?, ?);";
		
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			//���ʺŴ��滻Ϊ��Ӧ��ֵ
			pres.setString(1, order.get_order_no());
			pres.setString(2, order.get_order_client_no());
			pres.setString(3, order.get_order_time());
			pres.setString(4, order.get_order_product_no());
			pres.setString(5, order.get_order_amount());
			
			//����
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
		//���ݶ�Ӧ��client_noɾ����Ӧ��Ԫ��
		pres = conn.prepareStatement(sql);
		try {
			conn.setAutoCommit(false);
			
			//���ʺŴ��滻Ϊ��Ӧ��ֵ
			pres.setString(1, order.get_order_no());
			
			//����
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
			
			//����
			int n = pres.executeUpdate();
			//д��ʧ�ܵ�ʱ���Զ��Ӹýӿ��˳�������ִ������Ĵ��룬����ֵ���޸ĵ�����
			
			if(n > 0)
			{
				b = true;
			}
			
			if(pres != null || !pres.isClosed())//û�رյĻ��ر����ݿ�
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
			
			//ִ��Query����, ����ֵ���������
			ResultSetMetaData data = rs.getMetaData();
			
			scolumnName.add("�������");
			scolumnName.add("�ͻ����");
			scolumnName.add("�ͻ�����");
			scolumnName.add("�µ�ʱ��");
			scolumnName.add("������");
			scolumnName.add("��������");
			scolumnName.add("��������");

			while(rs.next()) {
				
				Vector line = new Vector();
				
				//getcolumnCount��ʾ�������˴�ͨ��ѭ��ʵ��ÿ�����ݵ���ӣ�rowData�����д洢��ÿһ��Ԫ�ض������飨line)��ÿ��line�д洢��Ԫ�ض���ÿ����Ԫ���ڵ�����
				for(int j = 1; j <= data.getColumnCount(); j++)
				{
					
					line.add(rs.getString(data.getColumnName(j)));//��ӵ���Ӧ��
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
