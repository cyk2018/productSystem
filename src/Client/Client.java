package Client;


/**
 * 该类的功能是将数据库中的表及其属性整合到对应的java类中，通过该类可以实现java中的数据类型和mysql中的数据类型的连接及转换
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
