create table address (
    address_id number(8),
    line1 varchar(500),
    line2 varchar(500),
    line3 varchar(500),
    city varchar(100),
    state varchar(100),
    zip varchar(9),
    active number(1),
    primary key (address_id)
);

create table location (
    location_id number(8),
    location_name varchar(100),
    address_id number(8),
    primary key (location_id),
    foreign key (address_id) references address
        on delete cascade
);

create table store (
    location_id number(8),
    hour_open number(2),
    minute_open number(2),
    hour_close number(2),
    minute_close number(2),
    primary key (location_id),
    foreign key (location_id) references location
        on delete cascade
);

create table warehouse (
    location_id number(8),
    open number(1),
    primary key (location_id),
    foreign key (location_id) references location
);

create table vendor (
    vendor_id number(8),
    vendor_name varchar(100),
    address_id number(8),
    primary key (vendor_id),
    foreign key (address_id) references address
);

create table brand (
    brand_id number(8),
    brand_name varchar(100),
    primary key (brand_id)
);

create table vendor_brand (
    vendor_id number(8),
    brand_id number(8),
    primary key (vendor_id, brand_id),
    foreign key (vendor_id) references vendor
        on delete cascade,
    foreign key (brand_id) references brand
        on delete cascade
);

create table product (
    product_id number(8),
    product_name varchar(500),
    upc_code varchar(50),
    brand_id number(8),
    primary key (product_id),
    foreign key (brand_id) references brand
);

create table book (
    product_id number(8),
    isbn varchar(50),
    primary key (product_id),
    foreign key (product_id) references product
);

create table expiring_product (
    product_id number(8),
    days_fresh number(3),
    primary key (product_id),
    foreign key (product_id) references product
);

/* Changed how sub-categories work from ER */
create table category (
    category_id number(8),
    category_name varchar(100),
    parent_id number(8),
    primary key (category_id),
    foreign key (parent_id) references category
);

create table product_category (
    product_id number(8),
    category_id number(8),
    primary key (product_id, category_id),
    foreign key (product_id) references product,
    foreign key (category_id) references category
)

create table vendor_supply (
    product_id number(8),
    vendor_id number(8),
    qty number(8),
    unit_price number(6,2),
    primary key (product_id, vendor_id),
    foreign key (product_id) references product
        on delete cascade,
    foreign key (vendor_id) references vendor
        on delete cascade
);

create table stock (
    location_id number(8),
    product_id number(8),
    qty number(8),
    unit_price number(8,2),
    primary key (location_id, product_id),
    foreign key (location_id) references location
        on delete cascade,
    foreign key (product_id) references product
        on delete cascade
);

create table customer (
    customer_id number(8),
    first_name varchar(50),
    middle_name varchar(50),
    last_name varchar(50),
    preferred_name varchar(50),
    company varchar(100),
    email varchar(500),
    primary key (customer_id)
);

create table customer_address (
    customer_id number(8),
    address_id number(8),
    primary key (customer_id, address_id),
    foreign key (customer_id) references customer
        on delete cascade,
    foreign key (address_id) references address
        on delete cascade
);

create table online_customer (
    customer_id number(8),
    username varchar(50),
    password_hash varchar(500),
    date_registered timestamp,
    primary key (customer_id),
    foreign key (customer_id) references customer
        on delete cascade
);

create table rewards_member (
    customer_id number(8),
    rewards_card_number number(8),
    rewards_balance number(8),
    primary key (customer_id),
    foreign key (customer_id) references customer
        on delete cascade
);

create table payment_method (
    payment_method_id number(8),
    payment_method_name varchar(100),
    customer_id number(8),
    primary key (payment_method_id),
    foreign key (customer_id) references customer
        on delete cascade
);

create table gift_card (
    payment_method_id number(8),
    card_number varchar(50),
    balance number(6,2),
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

create table bank_card (
    payment_method_id number(8),
    card_number varchar(50),
    name_on_card varchar(100),
    exp_date varchar(10),
    security_code varchar(10),
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

/* bitcoin payment methods would be created for each purchase, and so
store their own amount */
create table bitcoin_payment (
    payment_method_id number(8),
    from_address varchar(500),
    amount number(10, 10),
    primary key (payment_method_id),
    foreign key (payment_method_id) references customer
        on delete cascade
);

create table transaction (
    transaction_id number(8),
    subtotal number(10, 2),
    tax number(10, 2),
    total number(10, 2),
    timestamp timestamp,
    primary key (transaction_id)
);

create table online_transaction (
    transaction_id number(8),
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
        on delete cascade
);

create table physical_transaction (
    transaction_id number(8),
    location_id number(8),
    primary key (transaction_id),
    foreign key (transaction_id) references transaction
        on delete cascade,
    foreign key (location_id) references store
        on delete cascade
);

create table purchased (
    transaction_id number(8),
    product_id number(8),
    qty number(8),
    unit_price number(8,2),
    primary key (transaction_id, product_id),
    foreign key (transaction_id) references transaction
        on delete cascade,
    foreign key (product_id) references product
        on delete cascade
);

create table used_payment_method (
    transaction_id number(8),
    payment_method_id number(8),
    amount number(8,2),
    primary key (transaction_id, payment_method_id),
    foreign key (transaction_id) references online_transaction,
    foreign key (payment_method_id) references payment_method
);

create table pickup_order (
    transaction_id number(8),
    location_id number(8),
    est_arrival date,
    primary key (transaction_id, location_id),
    foreign key (transaction_id) references online_transaction
        on delete cascade,
    foreign key (location_id) references store
        on delete cascade
);

create table shipped_order (
    transaction_id number(8),
    address_id number(8),
    est_arrival date,
    tracking_number varchar(50),
    primary key (transaction_id),
    foreign key (transaction_id) references online_transaction
        on delete cascade,
    foreign key (address_id) references address
        on delete cascade
);