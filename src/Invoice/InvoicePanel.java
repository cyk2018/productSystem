package Invoice;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
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
import javax.swing.table.TableColumn;

import Client.ClientDatabase;
import Order.OrderDatabase;

public class InvoicePanel extends JPanel implements ActionListener{
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
	
	public ClientDatabase cd = new ClientDatabase();
	public OrderDatabase od = new OrderDatabase();
	public InvoiceDatabase id = new InvoiceDatabase();
	public InvoicePanel()
	{
		this.setLayout(new BorderLayout());
		comboBox.addItem("��Ʊ���");
		comboBox.addItem("��Ʊ�˱��");
		comboBox.addItem("�������");
		
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
		
		cd.main();
		od.main();
		id.main();
		dtm = new DefaultTableModel(id.rowData, id.columnName);
		
		jt = new JTable(dtm)
				{
					public boolean isCellEditable(int row, int column) {
						if(column == InvoiceConstant.INVOICE_NO && row < id.rowcount)
						{
							return false;
						}
						else {
							return true;
						}
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if(command == "insert")
		{
			Vector line = new Vector();
			dtm.addRow(line);
		}
		if (command == "delete") {
			Invoice invoice = new Invoice();
			boolean check = true;
			String invoice_no = null;
			int[] j = jt.getSelectedRows();
			if(j.length != 0)
			{
				int t = JOptionPane.showConfirmDialog(this, "�Ƿ�ɾ�������ݣ�", "ȷ��ɾ��", JOptionPane.YES_NO_OPTION);
				if(t == JOptionPane.YES_OPTION)
				{
					for(int i = 0; i < j.length; i++)
					{
						invoice_no = (String)jt.getValueAt(j[i], InvoiceConstant.INVOICE_NO);
						invoice.set_invoice_no(invoice_no);
						try {
							boolean checkdelete = id.delete(invoice);
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
		if(command == "save")
		{
			boolean check1 = true;
			boolean check2 = true;
			int t = JOptionPane.showConfirmDialog(this, "ȷ�ϱ��棿��ע���������Ƿ������", "�����޸�", JOptionPane.YES_NO_OPTION);
			if(t == JOptionPane.YES_OPTION)
			{
				for(int j = id.rowcount; j < jt.getRowCount(); j++)
				{
					Invoice invoice = new Invoice();
					String invoice_no = (String)jt.getValueAt(j, InvoiceConstant.INVOICE_NO);
					String invoice_client_no = (String)jt.getValueAt(j, InvoiceConstant.INVOICE_CLIENT_NO);
					String order_no = (String)jt.getValueAt(j, InvoiceConstant.ORDER_NO);
					String invoice_time = (String)jt.getValueAt(j, InvoiceConstant.INVOICE_TIME);
					
					invoice.set_invoice_no(invoice_no);
					invoice.set_invoice_client_no(invoice_client_no);
					invoice.set_order_no(order_no);
					invoice.set_invoice_time(invoice_time);
					
					try {
						boolean checkinsert = id.insert(invoice);
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
				if(check1 && id.rowcount != jt.getRowCount())
				{
					JOptionPane.showConfirmDialog(this, "������ӳɹ�", "������ӳɹ�", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
				if(!check1 && id.rowcount != jt.getRowCount())
				{
					JOptionPane.showConfirmDialog(this, "�������ʧ��", "�������ʧ��", JOptionPane.DEFAULT_OPTION);
				}
				
				for(int i = 1; i <= id.rowcount; i++)
				{
					Invoice invoice = new Invoice();
					String invoice_no = (String)jt.getValueAt(i - 1, InvoiceConstant.INVOICE_NO);
					String invoice_client_no = (String)jt.getValueAt(i - 1, InvoiceConstant.INVOICE_CLIENT_NO);
					String order_no = (String)jt.getValueAt(i - 1, InvoiceConstant.ORDER_NO);
					String invoice_time = (String)jt.getValueAt(i - 1, InvoiceConstant.INVOICE_TIME);
					
					
					invoice.set_invoice_no(invoice_no);
					invoice.set_invoice_client_no(invoice_client_no);
					invoice.set_order_no(order_no);
					invoice.set_invoice_time(invoice_time);
					
					try {
						boolean checkupdate = id.update(invoice);
						if(checkupdate && check2)
						{
							check2 = true;
						}
						else {
							check2 = false;
						}
					} catch (SQLException e2) {
						// TODO: handle exception
					}
				}
				if(check1)
				{
					if(check2)
					{
						JOptionPane.showConfirmDialog(this, "���³ɹ�", "���³ɹ�", JOptionPane.DEFAULT_OPTION);
						refresh();
					}
					else {
						JOptionPane.showConfirmDialog(this, "����ʧ��", "����ʧ��", JOptionPane.DEFAULT_OPTION);
					}
				}
				
			}
			
		}
		if(command == "search" || command == "searchInput")
		{
			int s = comboBox.getSelectedIndex();
			String input = (String)searchInput.getText();
			try {
				boolean checksearch = id.search(s, input);
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			InvoiceShowFrame isf = new InvoiceShowFrame(id);
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
	public void refresh()
	{
		id.main();
		od.main();
		cd.main();
		jt.setModel(dtm);
		jt.updateUI();
		jsp.updateUI();
	}
}
