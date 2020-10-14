package Client;

/**
 *���û��������ɾ�Ĳ��sql�������
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
	//���ڴ�����ѯ���������
	
	private String URL = "jdbc:mysql://localhost:3306/DB2018081301003?serverTimezone=UTC";
	private String USER = "root";
	private String PASS = "admin";
	public int rowcount = 0;//ͳ�Ƶ�ǰ���ݿ��е���������
	
	public void main()//��Ҫ���ڶ����ݿ��е����ݽ���չʾ
	{
		rowData.clear();//�Ȱ�ǰ���еı�����
		columnName.clear();
		rowcount = 0;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement pres = null;
			
			conn.setAutoCommit(false);
			
			String sql = "select * from client order by client_no";
			pres = conn.prepareStatement(sql);
			
			ResultSet rs = pres.executeQuery();
			//��ѯ���sql���ʹ�ô�ָ�����ֵ�ǲ�ѯ���
			ResultSetMetaData data = rs.getMetaData();
			
			//ʹ��������Ϊ��ͷ�����ٰ�ȫ����
			columnName.add("�ͻ����");
			columnName.add("�ͻ�����");
			columnName.add("�ͻ��Ա�");
			columnName.add("�ͻ��绰");
			
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
	public boolean insert(Client c) throws SQLException{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "insert into client values(?, ?, ?, ?)";
		//���ݶ�Ӧ��client_no�����Ӧ��Ԫ��
		pres = conn.prepareStatement(sql);
		
		try {
			conn.setAutoCommit(false);
			
			//���ʺŴ��滻Ϊ��Ӧ��ֵ
			pres.setString(1, c.get_no());
			pres.setString(2, c.get_name());
			pres.setString(3, c.get_sex());
			pres.setString(4, c.get_tel());
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
	public boolean delete(Client c)throws SQLException 
	{
		boolean b = false;
		Connection conn=DriverManager.getConnection(URL, USER, PASS);
		
		PreparedStatement pres = null;
		String sql = "delete from client where client_no = ?";
		//���ݶ�Ӧ��client_noɾ����Ӧ��Ԫ��
		pres = conn.prepareStatement(sql);
		
		try {
			conn.setAutoCommit(false);
			
			//���ʺŴ��滻Ϊ��Ӧ��ֵ
			pres.setString(1, c.get_no());
			
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
			
			//ִ��Query����, ����ֵ���������
			ResultSetMetaData data = rs.getMetaData();

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
