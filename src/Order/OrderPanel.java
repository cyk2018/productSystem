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
	private JButton insertButton = new JButton("���");
	private JButton deleteButton = new JButton("ɾ��");
	private JButton saveButton = new JButton("����");
	private JButton searchButton = new JButton("��ѯ");
	private JButton refreshButton = new JButton("ˢ��");
	private JComboBox comboBox = new JComboBox();
	private JTextField searchInput = new JTextField(15);
	//ʵ�ֹ������е���Ҫ���ܣ���ɾ�Ĳ�;
	
	private JTable jt = null;
	private JScrollPane jsp = null;
	//ʵ�ֱ����
	
	//�����������ģ��
	public DefaultTableModel dtm = null;
	public ComboBoxModel ccbm = null;
			
	//����һ����������ִ�����ݿ���صĲ���
	private ClientDatabase cd = new ClientDatabase();
	public OrderDatabase od = new OrderDatabase();
	public ProductDatabase pd = new ProductDatabase();
	
	Vector checkorder_no = new Vector();
	
	public OrderPanel()
	{
		this.setLayout(new BorderLayout());
		
		comboBox.addItem("�������");
		comboBox.addItem("�ͻ����");
		comboBox.addItem("������");
		
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
						if((column == OrderConstant.ORDER_NO || column == OrderConstant.ORDER_PRODUCT_NO)&& row < od.rowcount)//�����ݿ��е�Ԫ��������޷��޸ģ���Ϊ�����������������ݽ��и���
							return false;
						else
							return true;
						}
					};
		jt.setRowHeight(25);
		
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();//�����������ڴ洢�����Ⱦ��
		cr.setHorizontalAlignment(JLabel.CENTER);//�Ա����Ⱦ����������
		jt.setDefaultRenderer(Object.class, cr);//�Ա�������Ⱦ��ʹ��Object.class�����ж����������
		
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

//��ť����
@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	String command = e.getActionCommand();
	//System.out.println(command);
	
	//�������
	if(command == "insert")
	{
		Vector line = new Vector();
		//����µ�һ��
		dtm.addRow(line);
	}
	if(command == "delete")//ɾ�����ݿ���Ҫ����ǰ��˽�ϣ�Ҳ������ֻ�Ǵ�ǰ�˱����ɾ�����ݣ�ҲҪ�Ӻ�̨���ݿ���ɾ������;
	{
		boolean check = true;
		Order order = new Order();
		String order_no = null;
		int[] j = jt.getSelectedRows();
		if(j.length != 0)
		{
			//�����ʾ��Ϣ���ĸ������ֱ�Ϊ��������(null��ʾȱʡ)����ʱ���ڽ���ʾ����Ļ���м䣬������Ϣ�� ���ڱ��⣬ ��������
			int t = JOptionPane.showConfirmDialog(this, "�Ƿ�ɾ�������ݣ�", "ȷ��ɾ��", JOptionPane.YES_NO_OPTION);
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
					JOptionPane.showConfirmDialog(this, "ɾ���ɹ�", "ɾ���ɹ�", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
				else {
					JOptionPane.showConfirmDialog(this, "ɾ��ʧ�ܣ�����δд�����ݿ�����ڱ�ʹ�ã�", "ɾ��ʧ��", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
			}
		}
		
	}
	if(command == "save")//���湦�ܴ��ڵ�������������ݱ��е��û����ݹ���֮��ÿ���ظ����֮���������ٶȵ��½��������Ҫ�ж���Щ��Ԫ���Ǿ����û��޸ĵ�
	{
		//�����������ɸ���
		//����ͨ��ǰ�˵�����ʹ�ö�Ӧ�ı��Ԫ�ز��ܱ༭
		boolean check1 = true;
		boolean check2 = true;
		int t = JOptionPane.showConfirmDialog(this, "ȷ�ϱ��棿��ע���������Ƿ������", "�����޸�", JOptionPane.YES_NO_OPTION);
		if(t == JOptionPane.YES_OPTION)
		{
			for(int j = od.rowcount; j < jt.getRowCount(); j++)//�����ж�
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
					//�˴����ö�Ӧ��java����ȥִ�ж�Ӧ�ĸ��²���
					boolean checki = od.insert(order);//ִ�и��²����� �����ݿ��е����ݽ��и���
//					ͨ�����µĽ���ж��Ƿ�ɹ�
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
			if(check1 && od.rowcount != jt.getRowCount())//���������������ӳɹ�ʱ���ж�
			{
				JOptionPane.showConfirmDialog(this, "������ӳɹ�", "������ӳɹ�", JOptionPane.DEFAULT_OPTION);
				refresh();
			}
			if(!check1 && od.rowcount != jt.getRowCount())
			{
				JOptionPane.showConfirmDialog(this, "�������ʧ��", "�������ʧ��", JOptionPane.DEFAULT_OPTION);
			}
			
			for(int i = 1; i <= od.rowcount; i++)//������ӹ��ܵĺ�������Ϊ���ܴ���ĳЩ�û���ǰ���������֮�����޸�����
			{
					Order order = new Order();
					//c�����ſͻ������java�е�ʵ���������ڲ��洢�ű���������java�еı�ʾ
					//�������д����ǽ����ڱ���е����ݻ�ȡ֮��洢���ĸ�������
					String order_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_NO);
					String order_client_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_CLIENT_NO);
					String order_time = (String) jt.getValueAt(i-1, OrderConstant.ORDER_TIME);
					String order_product_no = (String) jt.getValueAt(i-1, OrderConstant.ORDER_PRODUCT_NO);
					String order_amount = (String) jt.getValueAt(i-1, OrderConstant.ORDER_AMOUNT);
					
					//�������д����ǽ����ڱ���е�����ת������Ӧ��java������
					order.set_order_no(order_no);
					order.set_order_client_no(order_client_no);
					order.set_order_time(order_time);
					order.set_order_product_no(order_product_no);
					order.set_order_amount(order_amount);
					
					try {
						//�˴����ö�Ӧ��java����ȥִ�ж�Ӧ�ĸ��²���
						boolean checku = od.update(order);//ִ�и��²����� �����ݿ��е����ݽ��и���
//						ͨ�����µĽ���ж��Ƿ�ɹ�
						if(checku && check2)
						{
							check2 = true;//checkuΪÿһ�е����ݸ���֮�󷵻صĽ����һ�������ݸ���ʧ��check ��ʼ��Ϊfalse
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
			JOptionPane.showConfirmDialog(this, "���³ɹ�", "���³ɹ�", JOptionPane.DEFAULT_OPTION);
			if(check1)
			{
				refresh();
			}
		}
		else {
			JOptionPane.showConfirmDialog(this, "����ʧ��", "����ʧ��", JOptionPane.DEFAULT_OPTION);
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
		int t4 = JOptionPane.showConfirmDialog(this, "ȷ��Ҫˢ����(����δ�ɹ����浽���ݿ�ĸĶ����᳷��)", "ȷ��ˢ��", JOptionPane.OK_CANCEL_OPTION);
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
