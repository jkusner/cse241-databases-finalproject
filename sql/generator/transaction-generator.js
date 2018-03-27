const util = require('./util');
const db = require('./db')

// TODO all of the money amounts in this file are random and should probably be calculated
// in some alternative way (like decide all price splits per product etc)

function genTransaction(transaction_id, possible_product_ids) {
    let subtotal = util.randMoney();
    let tax = util.randMoney();
    let total = subtotal + tax;
    let timestamp = util.randTimestamp();

    let transaction = {
        transaction_id,
        subtotal,
        tax,
        total
    };

    db.logInsert('transaction', transaction);
}

function genPhysicalTransaction(pickup_locations, possible_product_ids) {
    let transaction_id = util.randId();
    genTransaction(transaction_id, possible_product_ids);
    genPurchased(transaction_id, possible_product_ids);
    
    let physical_transaction = {
        transaction_id,
        location_id: util.randChoice(pickup_locations)
    };

    db.logInsert('physical_transaction', physical_transaction);
}

function genOnlineTransaction(payment_method_id, possible_product_ids, pickup_locations, shipping_locations) {
    let transaction_id = util.randId();
    genTransaction(transaction_id, possible_product_ids);

    // Nothing special in online_transaction table
    db.logInsert('online_transaction', {transaction_id});

    genPurchased(transaction_id, possible_product_ids);

    //TODO possibility to generate with multiple payment methods
    genUsedPaymentMethod(transaction_id, payment_method_id);

    if (util.randBool()) {
        genPickupOrder(transaction_id, util.randChoice(pickup_locations));
    } else {
        genShippedOrder(transaction_id, util.randChoice(shipping_locations));
    }

    return transaction_id;
}

function genPurchased(transaction_id, possible_product_ids) {
    let num_purchased = util.randInt(1, Math.min(100, possible_product_ids.length));
    let purchasedIds = [];
    for (let i = 0; i < num_purchased; i++) {
        let prod = util.randChoice(possible_product_ids);
        if (purchasedIds.indexOf(prod) != -1) {
            i--;
            continue;
        }
        genPurchasedSingleItem(transaction_id, prod);
        purchasedIds.push(prod);
    }
}

function genPurchasedSingleItem(transaction_id, product_id) {
    let purchased = {
        transaction_id,
        product_id,
        qty: util.randInt(1, 10),
        unit_price: util.randMoney()
    };
    db.logInsert('purchased', purchased);
}

function genUsedPaymentMethod(transaction_id, payment_method_id) {
    let used_payment_method = {
        transaction_id,
        payment_method_id,
        amount: util.randMoney()
    };
    db.logInsert('used_payment_method', used_payment_method);
}

function genPickupOrder(transaction_id, location_id) {
    let pickup_order = {
        transaction_id,
        location_id,
        est_arrival: util.randTimestamp()
    };
    db.logInsert('pickup_order', pickup_order);
}

function genShippedOrder(transaction_id, address_id) {
    let shipped_order = {
        transaction_id,
        address_id,
        est_arrival: util.randTimestamp(),
        tracking_number: util.randNumStr(20)
    };
    db.logInsert('shipped_order', shipped_order);
}

module.exports = {
    genOnlineTransaction,
    genPhysicalTransaction
}