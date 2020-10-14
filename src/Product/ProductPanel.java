package Product;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
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

public class ProductPanel extends JPanel implements ActionListener{
	
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
	
	//创建一个对象用于执行数据库相关的操作
	private ProductDatabase pd = new ProductDatabase();
	
	public ProductPanel() {
		this.setLayout(new BorderLayout());
		
		comboBox.addItem("货物编号");
		comboBox.addItem("货物名称");
		
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
		
		pd.main();
		dtm = new DefaultTableModel(pd.rowData, pd.columnName);
		
		jt = new JTable(dtm)
				{
					public boolean isCellEditable(int row, int column) {
						if(column == ProductConstant.PRODUCT_NO && row < pd.rowcount)//对数据库中的元组的主键无法修改，因为后面会根据主键对数据进行更新
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command == "insert")
		{
			Vector lineVector = new Vector();
			dtm.addRow(lineVector);
		}
		if(command == "delete")
		{boolean check = true;
		Product product = new Product();
		String product_no  = null;
		int[] j = jt.getSelectedRows();//数组中的内容表示选中的行索引，从零开始
		if(j.length != 0)
		{
			int t = JOptionPane.showConfirmDialog(this, "是否删除该数据？", "确认删除", JOptionPane.YES_NO_OPTION);
			if(t== JOptionPane.YES_OPTION)
			{
				for(int i = 0; i < j.length; i++)
				{
					product_no = (String) jt.getValueAt(j[i], ProductConstant.PRODUCT_NO);
					product.set_product_no(product_no);
					try {
						boolean checkdelete = pd.delete(product);
						if(checkdelete && check)
						{
							check = true;
						}
						else {
							check = false;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				for(int i = 0; i < j.length; i ++)
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
		if(command == "save")
		{
			boolean check1 = true;
			boolean check2 = true;
			int t = JOptionPane.showConfirmDialog(this, "确认保存？（注意检查数据是否出错！）", "保存修改", JOptionPane.YES_NO_OPTION);
			if(t == JOptionPane.YES_OPTION)
			{
				for(int j = pd.rowcount; j <jt.getRowCount(); j++)
				{
					Product product = new Product();
					
					String product_no = (String) jt.getValueAt(j, ProductConstant.PRODUCT_NO);
					String product_name = (String)jt.getValueAt(j, ProductConstant.PRODUCT_NAME);
					String product_price = (String)jt.getValueAt(j, ProductConstant.PRODUCT_PRICE);
					String product_amount = (String)jt.getValueAt(j, ProductConstant.PRODUCT_AMOUNT);
					
					product.set_product_no(product_no);
					product.set_product_name(product_name);
					product.set_product_price(product_price);
					product.set_product_amount(product_amount);
					
					try {
						boolean checkinsert = pd.insert(product);
						if(checkinsert && check1)
						{
							check1 = true;
						}
						else {
							check1 = false;
						}
					} catch (SQLException e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				if(check1 && pd.rowcount != jt.getRowCount())
				{
					JOptionPane.showConfirmDialog(this, "数据添加成功", "数据添加成功", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
				if(!check1 && pd.rowcount != jt.getRowCount())
				{
					JOptionPane.showConfirmDialog(this, "数据添加失败", "数据添加失败", JOptionPane.DEFAULT_OPTION);
				}
				for(int i = 1; i <= pd.rowcount; i++)
				{
					Product product = new Product();
					
					String product_no = (String)jt.getValueAt(i-1, ProductConstant.PRODUCT_NO);
					String product_name = (String)jt.getValueAt(i-1, ProductConstant.PRODUCT_NAME);
					String product_price = (String) jt.getValueAt(i-1, ProductConstant.PRODUCT_PRICE);
					String product_amount = (String)jt.getValueAt(i-1, ProductConstant.PRODUCT_AMOUNT);
					
					product.set_product_no(product_no);
					product.set_product_name(product_name);
					product.set_product_price(product_price);
					product.set_product_amount(product_amount);
					
					try {
						boolean checkupdate = pd.update(product);
						if(check2 && checkupdate)
						{
							check2 = true;
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
				refresh();
			}
			else {
				JOptionPane.showConfirmDialog(this, "更新失败", "更新失败", JOptionPane.DEFAULT_OPTION);
			}
		}
		if(command == "search" || command == "searchInput")
		{
			int s = comboBox.getSelectedIndex();
			String inputString = (String) searchInput.getText();
			try {
				boolean checksearch = pd.search(s, inputString);
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			ProductShowFrame pds = new ProductShowFrame(pd);
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
		pd.main();
		jt.setModel(dtm);
		jt.updateUI();
		jsp.updateUI();
	}
}
