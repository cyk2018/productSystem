package Invoice;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class InvoiceShowFrame extends JFrame{
	
	public InvoiceShowFrame(InvoiceDatabase invoice) {
		this.setVisible(true);
		this.setTitle("��ѯ���");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		DefaultTableModel dtm = new DefaultTableModel(invoice.srowData, invoice.scolumnName);
		//System.out.println(cd.srowData);
		JTable jt = new JTable(dtm)//���óɲ��ɱ༭����ѯ�������ֱ���޸�
				{
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
				jt.setRowHeight(25);
				JScrollPane jsp = new JScrollPane(jt);
		
		contentPane.add(jsp);
}
}
