package Client;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ClientShowFrame extends JFrame{//��Ҫ����Ĳ�����ʵ��������, ����
	
	public ClientShowFrame(ClientDatabase cd) {		
		//�������Ƕ�һЩ�������ҳ�������
		this.setVisible(true);
		this.setTitle("��ѯ���");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		DefaultTableModel dtm = new DefaultTableModel(cd.srowData, cd.scolumnName);
		JTable jt = new JTable(dtm)//���óɲ��ɱ༭����ѯ�������ֱ���޸�
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
