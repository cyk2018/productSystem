package Client;


/**
 * ����Ĺ����ǽ����ݿ��еı����������ϵ���Ӧ��java���У�ͨ���������ʵ��java�е��������ͺ�mysql�е��������͵����Ӽ�ת��
 */

import java.io.Serializable;

public class Client implements Serializable{
	private String client_no;
	private String client_name;
	private String client_sex;
	private String client_tel;
	
	public void set_no(String client_no)
	{
		this.client_no = client_no;
	}
	
	public String get_no()
	{
		return this.client_no;
	}
	
	
	public void set_name(String client_name)
	{
		this.client_name = client_name;
	}
	
	public String get_name()
	{
		return this.client_name;
	}
	
	public void set_sex(String client_sex)
	{
		this.client_sex = client_sex;
	}
	
	public String get_sex()
	{
		return this.client_sex;
	}
	
	public void set_tel(String client_tel)
	{
		this.client_tel = client_tel;
	}
	
	public String get_tel()
	{
		return this.client_tel;
	}
}
