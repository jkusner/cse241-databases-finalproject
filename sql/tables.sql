create table address (
    address_id number(8)
        generated always as identity,
    line1 varchar(500) not null,
    line2 varchar(500),
    line3 varchar(500),
    city varchar(100) not null,
    state varchar(2) not null,
    zip varchar(9),
    active number(1) default 1,
    primary key (address_id)
);

create table location (
    location_id number(8)
        generated always as identity,
    location_name varchar(100) not null,
    address_id number(8) not null,
    primary key (location_id),
    foreign key (address_id) references address
        on delete cascade
);

create table store (
    location_id number(8) not null,
    hour_open number(2) not null,
        check (hour_open between 0 and 23),
    minute_open number(2) not null,
        check (minute_open between 0 and 59),
    hour_close number(2) not null,
        check (hour_close between 0 and 23),
    minute_close number(2) not null,
        check (minute_close between 0 and 59),
    primary key (location_id),
    foreign key (location_id) references location
        on delete cascade
);

create table warehouse (
    location_id number(8) not null,
    open number(1) default 1,
    primary key (location_id),
    foreign key (location_id) references location
);

create table vendor (
    vendor_id number(8)
        generated always as identity,
    vendor_name varchar(100) not null,
    address_id number(8) not null,
    primary key (vendor_id),
    foreign key (address_id) references address
);

create table brand (
    brand_id number(8) not null,
    brand_name varchar(100) not null,
    primary key (brand_id)
);

create table vendor_brand (
    vendor_id number(8) not null,
    brand_id number(8) not null,
    primary key (vendor_id, brand_id),
    foreign key (vendor_id) references vendor
        on delete cascade,
    foreign key (brand_id) references brand
        on delete cascade
);

create table product (
    product_id number(8) not null,
    product_name varchar(500) not null,
    upc_code varchar(50),
    brand_id number(8) not null,
    primary key (product_id),
    foreign key (brand_id) references brand
);

create table book (
    product_id number(8) not null,
    isbn varchar(50) not null,
    primary key (product_id),
    foreign key (product_id) references product
);

create table expiring_product (
    product_id number(8) not null,
    days_fresh number(3) not null,
        check (days_fresh > 0),
    primary key (product_id),
    foreign key (product_id) references product
);

/* Changed how sub-categories work from ER */
create table category (
    category_id number(8) not null,
    category_name varchar(100) not null,
    parent_id number(8),
    primary key (category_id),
    foreign key (parent_id) references category
);

create table product_category (
    product_id number(8) not null,
    category_id number(8) not null,
    primary key (product_id, category_id),
    foreign key (product_id) references product,
    foreign key (category_id) references category
);

create table vendor_supply (
    product_id number(8) not null,
    vendor_id number(8) not null,
    qty number(8) not null,
        check (qty >= 0),
    unit_price number(6,2) not null,
        check (unit_price >= 0),
    primary key (product_id, vendor_id),
    foreign key (product_id) references product
        on delete cascade,
    foreign key (vendor_id) references vendor
        on delete cascade
);

create table stock (
    location_id number(8) not null,
    product_id number(8) not null,
    qty number(8) not null,
        check (qty >= 0),
    unit_price number(8,2) not null,
        check (unit_price >= 0),
    primary key (location_id, product_id),
    foreign key (location_id) references location
        on delete cascade,
    foreign key (product_id) references product
        on delete cascade
);

create table customer (
    customer_id number(8) not null,
    first_name varchar(50) not null,
    middle_name varchar(50),
    last_name varchar(50) not null,
    preferred_name varchar(50),
    company varchar(100),
    email varchar(500) not null,
    primary key (customer_id)
);

create table customer_phone (
    customer_id number(8) not null,
    phone_number varchar(20) not null,
    active number(1) default 1,
    primary key (customer_id, phone_number),
    foreign key (customer_id) references customer
);

create table customer_address (
    customer_id number(8) not null,
    address_id number(8) not null,
    primary key (customer_id, address_id),
    foreign key (customer_id) references customer
        on delete cascade,
    foreign key (address_id) references address
        on delete cascade
);

/* requires testing */
create table online_customer (
    customer_id number(8) not null,
    username varchar(50) not null,
    password_hash varchar(500) not null,
    date_registered timestamp default current_timestamp,
    primary key (customer_id),
    foreign key (customer_id) references customer
        on delete cascade,
    constraint unique_username UNIQUE (username)
);

create table rewards_member (
    customer_id number(8) not null,
    rewards_card_number number(8) not null,
    rewards_balance number(8),
    primary key (customer_id),
    foreign key (customer_id) references customer
        on delete cascade,
    constraint unique_number UNIQUE (rewards_card_number)
);

create table payment_method (
    payment_method_id number(8)
        generated always as identity,
    payment_method_name varchar(100),
    customer_id number(8) not null,
    primary key (payment_method_id),
    foreign key (customer_id) references customer
        on delete cascade
);

create table gift_card (
    payment_method_id number(8) not null,
    card_number varchar(50) not null,
    balance number(6,2) not null
        check (balance >= 0),
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

create table bank_card (
    payment_method_id number(8) not null,
    card_number varchar(50) not null,
    name_on_card varchar(100) not null,
    exp_date varchar(10) not null,
    security_code varchar(10) not null,
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

/* bitcoin payment methods would be created for each purchase, and so
store their own amount */
create table bitcoin_payment (
    payment_method_id number(8) not null,
    from_address varchar(500) not null,
    amount number(10, 10) not null,
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

/* should returns be a thing? */
create table transaction (
    transaction_id number(8) not null,
    subtotal number(10, 2) not null,
        check (subtotal > 0),
    tax number(10, 2) not null,
        check (tax >= 0),
    total number(10, 2),
        check (total > 0),
    timestamp timestamp,
    primary key (transaction_id)
);

create table online_transaction (
    transaction_id number(8) not null,
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
        on delete cascade
);

create table physical_transaction (
    transaction_id number(8) not null,
    location_id number(8) not null,
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
        on delete cascade,
    foreign key (location_id) references store
        on delete cascade
);

create table purchased (
    transaction_id number(8) not null,
    product_id number(8) not null,
    qty number(8) not null,
        check (qty > 0),
    unit_price number(8,2),
        check (unit_price >= 0),
    primary key (transaction_id, product_id),
    foreign key (transaction_id) references transaction
        on delete cascade,
    foreign key (product_id) references product
        on delete cascade
);

create table used_payment_method (
    transaction_id number(8) not null,
    payment_method_id number(8) not null,
    amount number(8,2) not null,
        check (amount > 0),
    primary key (transaction_id, payment_method_id),
    foreign key (transaction_id) references online_transaction,
    foreign key (payment_method_id) references payment_method
);

create table pickup_order (
    transaction_id number(8) not null,
    location_id number(8) not null,
    est_arrival date not null,
    primary key (transaction_id, location_id),
    foreign key (transaction_id) references online_transaction
        on delete cascade,
    foreign key (location_id) references store
        on delete cascade
);

create table shipped_order (
    transaction_id number(8) not null,
    address_id number(8) not null,
    est_arrival date,
    tracking_number varchar(50) not null,
    primary key (transaction_id),
    foreign key (transaction_id) references online_transaction
        on delete cascade,
    foreign key (address_id) references address
        on delete cascade
);