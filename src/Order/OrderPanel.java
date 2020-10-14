package Order;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import Client.ClientDatabase;
import Product.ProductDatabase;


public class OrderPanel extends JPanel implements ActionListener{
	private JButton insertButton = new JButton("添加");
	private JButton deleteButton = new JButton("删除");
	private JButton saveButton = new JButton("保存");
	private JButton searchButton = new JButton("查询");
	private JButton refreshButton = new JButton("刷新");
	private JComboBox comboBox = new JComboBox();
	private JTextField searchInput = new JTextField(15);
	//实现工具栏中的主要功能：增删改查;
	
	private JTable jt = null;
	private JScrollPane jsp = null;
	//实现表格功能
	
	//创建表的数据模型
	public DefaultTableModel dtm = null;
	public ComboBoxModel ccbm = null;
			
	//创建一个对象用于执行数据库相关的操作
	private ClientDatabase cd = new ClientDatabase();
	public OrderDatabase od = new OrderDatabase();
	public ProductDatabase pd = new ProductDatabase();
	
	Vector checkorder_no = new Vector();
	
	public OrderPanel()
	{
		this.setLayout(new BorderLayout());
		
		comboBox.addItem("订单编号");
		comboBox.addItem("客户编号");
		comboBox.addItem("货物编号");
		
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT & FlowLayout.LEADING, 20, 0));
		
		toolBar.add(insertButton);
		toolBar.add(deleteButton);
		toolBar.add(saveButton);
		toolBar.add(comboBox);
		toolBar.add(searchInput);
		toolBar.add(searchButton);
		toolBar.add(refreshButton);
		
		this.add(toolBar, BorderLayout.NORTH);
		
		od.main();
		cd.main();
		pd.main();
		
		dtm = new DefaultTableModel(od.rowData, od.columnName);
		
		
		jt = new JTable(dtm)
				{
					public boolean isCellEditable(int row, int column) {
						if((column == OrderConstant.ORDER_NO || column == OrderConstant.ORDER_PRODUCT_NO)&& row < od.rowcount)//对数据库中的元组的主键无法修改，因为后面会根据主键对数据进行更新
							return false;
						else
							return true;
						}
					};
		jt.setRowHeight(25);
		
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();//创建对象用于存储表格渲染器
		cr.setHorizontalAlignment(JLabel.CENTER);//对表格渲染器进行配置
		jt.setDefaultRenderer(Object.class, cr);//对表格进行渲染，使用Object.class对所有对象进行配置
		
		jsp = new JScrollPane(jt); 
		
		this.add(jsp, BorderLayout.CENTER);
		
		insertButton.setActionCommand("insert");
		insertButton.addActionListener(this);
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		searchButton.setActionCommand("search");
		searchButton.addActionListener(this);
		searchInput.setActionCommand("searchInput");
		searchInput.addActionListener(this);
		refreshButton.setActionCommand("refresh");
		refreshButton.addActionListener(this);
		
				}

//按钮监听
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	String command = e.getActionCommand();
	//System.out.println(command);
	
	//添加数据
	if(command == "insert")
	{
		Vector line = new Vector();
		//添加新的一行
		dtm.addRow(line);
	}
	if(command == "delete")//删除数据库需要做到前后端结合，也即，不只是从前端表格中删除数据，也要从后台数据库中删除数据;
	{
		boolean check = true;
		Order order = new Order();
		String order_no = null;
		int[] j = jt.getSelectedRows();
		if(j.length != 0)
		{
			//添加提示信息，四个参数分别为：父窗口(null表示缺省)，此时窗口将显示在屏幕正中间，窗口信息， 窗口标题， 窗口类型
			int t = JOptionPane.showConfirmDialog(this, "是否删除该数据？", "确认删除", JOptionPane.YES_NO_OPTION);
			if(t == JOptionPane.YES_OPTION)
			{
				for(int i = 0; i < j.length; i++)
				{
					order_no = (String)jt.getValueAt(j[i], OrderConstant.ORDER_NO);
					order.set_order_no(order_no);
					try {
						boolean checkdelete = od.delete(order);
						if(checkdelete && check)
						{
							check = true;
						}
						else {
							check = false;
						}
					} catch (SQLException e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				for(int i = 0; i < j.length; i++)
				{
					dtm.removeRow(jt.getSelectedRow());
				}
				if(check)
				{
					JOptionPane.showConfirmDialog(this, "删除成功", "删除成功", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
				else {
					JOptionPane.showConfirmDialog(this, "删除失败（数据未写入数据库或正在被使用）", "删除失败", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
			}
		}
		
	}
	if(command == "save")//保存功能存在的问题是如果数据表中的用户数据过多之后，每次重复添加之后可能造成速度的下降，因此需要判断哪些行元组是经过用户修改的
	{
		//设置主键不可更改
		//可以通过前端的设置使得对应的表格元素不能编辑
		boolean check1 = true;
		boolean check2 = true;
		int t = JOptionPane.showConfirmDialog(this, "确认保存？（注意检查数据是否出错！）", "保存修改", JOptionPane.YES_NO_OPTION);
		if(t == JOptionPane.YES_OPTION)
		{
			for(int j = od.rowcount; j < jt.getRowCount(); j++)//增加判断
			{
				Order order = new Order();
				
				String order_no = (String) jt.getValueAt(j, OrderConstant.ORDER_NO);
				String order_client_no = (String) jt.getValueAt(j, OrderConstant.ORDER_CLIENT_NO);
				String order_time = (String) jt.getValueAt(j, OrderConstant.ORDER_TIME);
				String order_product_no = (String) jt.getValueAt(j, OrderConstant.ORDER_PRODUCT_NO);
				String order_amount = (String) jt.getValueAt(j, OrderConstant.ORDER_AMOUNT);
				
				order.set_order_no(order_no);
				order.set_order_client_no(order_client_no);
				order.set_order_time(order_time);
				order.set_order_product_no(order_product_no);
				order.set_order_amount(order_amount);
				
				try {
					//此处利用对应的java对象去执行对应的更新操作
					boolean checki = od.insert(order);//执行更新操作， 对数据库中的内容进行更新
//					通过更新的结果判断是否成功
					if(checki && check1)
					{
						check1 = true;
					}
					else {
						check1 = false;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(check1 && od.rowcount != jt.getRowCount())//当有数据添加且添加成功时才判断
			{
				JOptionPane.showConfirmDialog(this, "数据添加成功", "数据添加成功", JOptionPane.DEFAULT_OPTION);
				refresh();
			}
			if(!check1 && od.rowcount != jt.getRowCount())
			{
				JOptionPane.showConfirmDialog(this, "数据添加失败", "数据添加失败", JOptionPane.DEFAULT_OPTION);
			}
			
			for(int i = 1; i <= od.rowcount; i++)//放在添加功能的后面是因为可能存在某些用户在前端添加数据之后又修改数据
			{
					Order order = new Order();
					//c代表着客户表格在java中的实例化对象，内部存储着表格各属性在java中的表示
					//以下四行代码是将现在表格中的数据获取之后存储到四个变量中
					String order_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_NO);
					String order_client_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_CLIENT_NO);
					String order_time = (String) jt.getValueAt(i-1, OrderConstant.ORDER_TIME);
					String order_product_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_PRODUCT_NO);
					String order_amount = (String) jt.getValueAt(i-1, OrderConstant.ORDER_AMOUNT);
					
					//以下四行代码是将现在表格中的数据转换到对应的java对象中
					order.set_order_no(order_no);
					order.set_order_client_no(order_client_no);
					order.set_order_time(order_time);
					order.set_order_product_no(order_product_no);
					order.set_order_amount(order_amount);
					
					try {
						//此处利用对应的java对象去执行对应的更新操作
						boolean checku = od.update(order);//执行更新操作， 对数据库中的内容进行更新
//						通过更新的结果判断是否成功
						if(checku && check2)
						{
							check2 = true;//checku为每一行的数据更新之后返回的结果，一旦有数据更新失败check 就始终为false
						}
						else {
							check2 = false;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				}		
		if(check2)
		{
			JOptionPane.showConfirmDialog(this, "更新成功", "更新成功", JOptionPane.DEFAULT_OPTION);
			if(check1)
			{
				refresh();
			}
		}
		else {
			JOptionPane.showConfirmDialog(this, "更新失败", "更新失败", JOptionPane.DEFAULT_OPTION);
		}
	}
				
	if(command == "search" || command == "searchInput")
	{
		OrderDatabase ods = new OrderDatabase();
		int s = comboBox.getSelectedIndex();
		String input = (String)searchInput.getText();
		try {
			boolean checks = ods.search(s, input);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OrderShowFrame csf = new OrderShowFrame(ods);
		}
	if(command == "refresh")
	{
		int t4 = JOptionPane.showConfirmDialog(this, "确定要刷新吗？(所有未成功保存到数据库的改动将会撤销)", "确定刷新", JOptionPane.OK_CANCEL_OPTION);
		if(t4 == JOptionPane.OK_OPTION)
		{
			refresh();
		}
	}
}

public void refresh() {
	od.main();
	jt.setModel(dtm);
	jt.updateUI();
	jsp.updateUI();
	
}

}
