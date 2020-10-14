package Function;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Order.OrderDatabase;

public class FunctionShowFrame extends JFrame{
	public FunctionShowFrame(FunctionDatabase fd) {
		this.setVisible(true);
		this.setTitle("��ѯ���");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(1000, 600);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		DefaultTableModel dtm = new DefaultTableModel(fd.srowData, fd.scolumnName);
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
