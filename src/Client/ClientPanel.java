package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.sql.SQLException;
import java.util.Vector;

import javax.print.attribute.standard.PDLOverrideSupported;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Order.OrderPanel;


public class ClientPanel extends JPanel implements ActionListener{

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
		
	//����һ����������ִ�����ݿ���صĲ���
	private ClientDatabase cd = new ClientDatabase();
		
	public ClientPanel(){
		
		this.setLayout(new BorderLayout());
		
		comboBox.addItem("�ͻ����");
		comboBox.addItem("�ͻ�����");
		comboBox.addItem("�ͻ��Ա�");
		
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT & FlowLayout.LEADING, 20, 0));
		
		toolBar.add(insertButton);
		toolBar.add(deleteButton);
		toolBar.add(saveButton);
		toolBar.add(comboBox);
		toolBar.add(searchInput);
		toolBar.add(searchButton);
		toolBar.add(refreshButton);
		
		//��clientpanel���ó���border���֣���ʱ���ڲ����������ѡ��λ������Ǹ�λ��
		this.add(toolBar, BorderLayout.NORTH);
		
		cd.main();//����Client��main����֮��Ͷ��û����и�ֵ��
		//�Ա������ģ�ͽ��и�ֵ
		dtm = new DefaultTableModel(cd.rowData, cd.columnName);
		jt = new JTable(dtm)
			{
			public boolean isCellEditable(int row, int column) {
				if(column==ClientConstant.CLIENT_NO && row < cd.rowcount)//�����ݿ��е�Ԫ��������޷��޸ģ���Ϊ�����������������ݽ��и���
					return false;
				else
					return true;
				}
			};
		jt.setRowHeight(25);//����һ���иߣ��������õ�ԭ��һ������Ϊ�����ۣ���һ������Ϊ������������������ʹ��
			
		TableColumn sexColumn = jt.getColumn("�ͻ��Ա�");
		
		//�ѿͻ��Ա�һ�����ó����������ʽ
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("��");
		comboBox.addItem("Ů");
		sexColumn.setCellEditor(new DefaultCellEditor(comboBox));
		
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
			Client client = new Client();
			String client_no  = null;
			int[] j = jt.getSelectedRows();//�����е����ݱ�ʾѡ�е������������㿪ʼ
			if(j.length != 0)
			{
				int t = JOptionPane.showConfirmDialog(this, "�Ƿ�ɾ�������ݣ�", "ȷ��ɾ��", JOptionPane.YES_NO_OPTION);
				if(t== JOptionPane.YES_OPTION)
				{
					for(int i = 0; i < j.length; i++)
					{
						client_no = (String) jt.getValueAt(j[i], ClientConstant.CLIENT_NO);
						client.set_no(client_no);
						try {
							boolean checkdelete = cd.delete(client);
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
				for(int j = cd.rowcount; j < jt.getRowCount(); j++)//�����ж�
				{
					Client client = new Client();
					
					String client_no = (String) jt.getValueAt(j, ClientConstant.CLIENT_NO);
					String client_name = (String) jt.getValueAt(j, ClientConstant.CLIENT_NAME);
					String client_sex = (String) jt.getValueAt(j, ClientConstant.CLIENT_SEX);
					String client_tel = (String) jt.getValueAt(j, ClientConstant.CLIENT_TEL);

					client.set_no(client_no);
					client.set_name(client_name);
					client.set_sex(client_sex);
					client.set_tel(client_tel);
					try {
						//�˴����ö�Ӧ��java����ȥִ�ж�Ӧ�ĸ��²���
						boolean checkinsert = cd.insert(client);//ִ�и��²����� �����ݿ��е����ݽ��и���
//						ͨ�����µĽ���ж��Ƿ�ɹ�
						if(checkinsert && check1)
						{
							check1 = true;//checkuΪÿһ�е����ݸ���֮�󷵻صĽ����һ�������ݸ���ʧ��check ��ʼ��Ϊfalse
						}
						else {
							check1 = false;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(check1 && cd.rowcount != jt.getRowCount())//���������������ӳɹ�ʱ���ж�
				{
					JOptionPane.showConfirmDialog(this, "������ӳɹ�", "������ӳɹ�", JOptionPane.DEFAULT_OPTION);
					refresh();
				}
				if(!check1 && cd.rowcount != jt.getRowCount())
				{
					JOptionPane.showConfirmDialog(this, "�������ʧ��", "�������ʧ��", JOptionPane.DEFAULT_OPTION);
				}
				for(int i = 1; i <= cd.rowcount; i++)//������ӹ��ܵĺ�������Ϊ���ܴ���ĳЩ�û���ǰ���������֮�����޸�����
				{
						Client client = new Client();
						//c�����ſͻ������java�е�ʵ���������ڲ��洢�ű���������java�еı�ʾ
							
						//�������д����ǽ����ڱ���е����ݻ�ȡ֮��洢���ĸ�������
						String client_no = (String) jt.getValueAt(i-1, ClientConstant.CLIENT_NO);
						String client_name = (String) jt.getValueAt(i-1, ClientConstant.CLIENT_NAME);
						String client_sex = (String) jt.getValueAt(i-1, ClientConstant.CLIENT_SEX);
						String client_tel = (String) jt.getValueAt(i-1, ClientConstant.CLIENT_TEL);
							
						//�������д����ǽ����ڱ���е�����ת������Ӧ��java������
						client.set_no(client_no);
						client.set_name(client_name);
						client.set_sex(client_sex);
						client.set_tel(client_tel);
							
						try {
							//�˴����ö�Ӧ��java����ȥִ�ж�Ӧ�ĸ��²���
							boolean checkupdate = cd.update(client);//ִ�и��²����� �����ݿ��е����ݽ��и���
//							ͨ�����µĽ���ж��Ƿ�ɹ�
							if(checkupdate && check2)
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
				JOptionPane.showConfirmDialog(this, "���³ɹ�", "���³ɹ�", JOptionPane.DEFAULT_OPTION);
				refresh();
			}
			else {
				JOptionPane.showConfirmDialog(this, "����ʧ��", "����ʧ��", JOptionPane.DEFAULT_OPTION);
			}
			}
					
		if(command == "search" || command == "searchInput")
		{
			int searchoption = comboBox.getSelectedIndex();
			String searchinput = (String)searchInput.getText();
			try {
				boolean checks = cd.search(searchoption, searchinput);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ClientShowFrame csf = new ClientShowFrame(cd);
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
		cd.main();
		jt.setModel(dtm);
		jt.updateUI();
		jsp.updateUI();
		
	}
}




