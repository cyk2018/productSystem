create database db2018081301003;

create table client(
	client_no varchar(32) not null,
    client_name varchar(5) not null,
    client_sex varchar(1) not null,
    client_tel varchar(11) not null,
    primary key(client_no),
    check(client_sex in("男", "女"))
);
create table orderitems
(order_no varchar(32) not null,
client_no varchar(32) not null,
order_time datetime not null,
product_no varchar(32) not null,
order_amount int not null,
primary key(order_no, product_no),
foreign key(product_no)references product(product_no),
foreign key(client_no) references client(client_no)
);

create table product(
product_no varchar(32) not null,
product_name varchar(10) not null,
product_price decimal(18, 2) not null,
product_amount int not null,
primary key(product_no)
);

create table invoice(
invoice_no varchar(32) not null,
invoice_client_no varchar(32) not null,
order_no varchar(32) not null,
invoice_time datetime not null,
primary key(invoice_no),
foreign key (invoice_client_no) references client(client_no),
foreign key (order_no) references orderitems(order_no)
);


