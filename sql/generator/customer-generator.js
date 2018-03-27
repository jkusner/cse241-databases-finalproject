const util = require('./util');
const db = require('./db')
const addr = require('./address-generator');

function genCustomer(customer_id) {
    let customer = {
        customer_id,
        first_name: util.randName(),
        middle_name: util.randBool(.9) ? util.randName() : '',
        last_name: util.randName(),
        preferred_name: util.randBool(.9) ? '' : util.randName(),
        company: util.randBool(.05) ? util.randName() : '',
        email: util.randEmail()
    };

    db.logInsert('customer', customer);

    let rand = Math.random();
    let isRewardsMember = rand < .2;
    let isOnlineMember = !isRewardsMember || rand < .1;

    if (isRewardsMember) {
        genRewardsMember(customer_id);
    }
    if (isOnlineMember) {
        genOnlineCustomer(customer_id);
    }

    let addressCount = Math.floor(Math.sqrt(util.randInt(1, 9)));
    let addresses = [];
    for (let i = 0; i < addressCount; i++) {
        addresses.push(genCustomerAddress(customer_id));
    }

    let phoneNumbers = Math.floor(Math.sqrt(util.randInt(1, 9)));
    for (let i = 0; i < phoneNumbers; i++) {
        genPhoneNumber(customer_id);
    }

    return {addresses};
}

function genRewardsMember(customer_id) {
    let rewards_card_number = util.randStr(10, '0123456789');
    db.logInsert('rewards_member', {customer_id, rewards_card_number});
}

function genOnlineCustomer(customer_id) {
    let username = util.randAlphaStr();
    let password_hash = util.randAlphaStr();
    let date_registered = `TIMESTAMP '${util.randTimestamp()}'`;
    db.logInsert('online_customer', {customer_id, username, password_hash, date_registered});
}

function genCustomerAddress(customer_id) {
    let address_id = util.randId();
    addr.genAddress(address_id);

    let customer_address = {
        customer_id,
        address_id
    };
    db.logInsert('customer_address', customer_address);
    return address_id;
}

function genPhoneNumber(customer_id) {
    let phone_number = util.randPhone();

    let customer_phone = {
        customer_id,
        phone_number,
        active: util.randBool(.95) ? 1 : 0
    };
    db.logInsert('customer_phone', customer_phone);
}

module.exports = {
    genCustomer
}