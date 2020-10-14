package Client;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ClientShowFrame extends JFrame{//需要导入的参数是实例化的行, 列名
	
	public ClientShowFrame(ClientDatabase cd) {		
		//接下来是对一些搜索结果页面的设置
		this.setVisible(true);
		this.setTitle("查询结果");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		DefaultTableModel dtm = new DefaultTableModel(cd.srowData, cd.scolumnName);
		JTable jt = new JTable(dtm)//设置成不可编辑，查询结果不可直接修改
				{
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
		JScrollPane jsp = new JScrollPane(jt);
		jt.setRowHeight(25);
		contentPane.add(jsp);
	}
	
}
