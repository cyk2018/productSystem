package Function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class FunctionDatabase {
	
	public Vector srowData = new Vector();
	public Vector scolumnName = new Vector();
	
	private String URL = "jdbc:mysql://localhost:3306/DB2018081301003?serverTimezone=UTC";
	private String USER = "root";
	private String PASS = "admin";
	
	public boolean search(int searchoption, String searchInput)throws SQLException
	{
		boolean b = false;
		scolumnName.clear();
		srowData.clear();
		String sql = null;
		Connection conn = DriverManager.getConnection(URL, USER, PASS);
		PreparedStatement pres = null;
		switch (searchoption) {
		case 0:
			sql = "select orderitems.order_no, orderitems.client_no, product_name, order_amount, product_price * order_amount from orderitems, product where order_no like ? and product.product_no = orderitems.product_no order by order_no";
			break;

		case 1:
			sql = "select orderitems.order_no, orderitems.client_no, product_name, order_amount, product_price * order_amount from orderitems, product where client_no like ? and product.product_no = orderitems.product_no order by order_no";
			break; 
		}
		pres = conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		pres.setString(1, "%" + searchInput + "%");
		ResultSet rs = pres.executeQuery();
		ResultSetMetaData data = rs.getMetaData();
		scolumnName.add("订单编号");
		scolumnName.add("客户编号");
		scolumnName.add("货物名称");
		scolumnName.add("订货数量");
		scolumnName.add("订货金额");
		
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
