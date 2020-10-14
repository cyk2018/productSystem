package Order;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OrderShowFrame extends JFrame{
	
	public OrderShowFrame(OrderDatabase order) {
				this.setVisible(true);
				this.setTitle("��ѯ���");
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				this.setSize(1000, 600);
				
				Container contentPane = getContentPane();
				contentPane.setLayout(new BorderLayout());
				
				DefaultTableModel dtm = new DefaultTableModel(order.srowData, order.scolumnName);
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
