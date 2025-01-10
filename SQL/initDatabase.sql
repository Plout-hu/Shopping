drop database if exists shopping;
create database if not exists shopping;
use shopping;
drop table if exists goods;
drop table if exists customer;
create table if not exists customer
(
    user_id            int         not null primary key auto_increment comment '用户id',
    username           varchar(16) not null unique,
    user_level         int         not null,
    register_time      datetime    not null,
    consumption_amount float      default 0 comment '总消费金额',
    tel                varchar(11) not null,
    email              varchar(30) not null,
    password           varchar(32) not null,
    power              int        default 0
);
insert into customer
values (null, "admin", 0, now(), 0, "16596527769", "571156946@qq.com", md5("12345678"), 1);
drop table if exists shopping.goods;
create table if not exists goods
(
    goods_id     int         not null primary key auto_increment,
    goods_name   varchar(10) not null unique,
    picture      varchar(200) comment '商品图片',
    producer     varchar(10) comment '生产厂家',
    produce_date date        not null comment '生产日期',
    type         int         not null comment '信号',
    buying_price float       not null comment '进货价',
    price        float       not null comment '售价',
    inventory    int default 0 comment '库存'
);
drop table if exists historical_orders;
create table if not exists historical_orders
(
    order_id      int         not null primary key auto_increment,
    customer_id   int         not null comment '用户id',
    customer_name varchar(16) not null,
    goods_id      int         not null,
    goods_name    varchar(10) not null,
    price         float       not null,
    goods_number  int         not null,
    order_time    datetime    not null
);
create table if not exists LoginHistory
(
    username             varchar(16) not null,
    consecutive_failures int         not null comment '连续失败次数',
    last_fail_time       datetime    not null comment '最后一次登录失败的时间'
);
create table if not exists shopping_cart
(
    id                 int primary key not null auto_increment,
    user_id            int             not null,
    goods_id           int             not null,
    goods_name         varchar(10)     not null,
    goods_price        float           not null,
    goods_buying_price float           not null,
    amount             int default 1
);
drop table if exists verification_code;
create table if not exists verification_code
(
    username     varchar(20)         not null,
    code        varchar(4)  not null,
    email       varchar(30) not null,
    create_time datetime    not null,
    expire_time datetime    not null
);