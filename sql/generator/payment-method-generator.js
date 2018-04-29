const util = require('./util');
const db = require('./db')

function genPaymentMethod(customer_id, allowAccountBalance) {
    let usedAccountBalance = allowAccountBalance && util.randBool(.25);

    let payment_method_id = util.randId();
    let payment_method = {
        payment_method_id,
        payment_method_name: (usedAccountBalance ? 'BALANCE' : 'BANK') + ' CARD ' + util.randAlphaStr(8),
        customer_id
    };

    db.logInsert('payment_method', payment_method);

    if (usedAccountBalance) {
        genAccountBalance(payment_method_id);
    } else {
        genBankCard(payment_method_id);
    }

    return payment_method_id;
}

function genAccountBalance(payment_method_id) {
    db.logInsert('account_balance', {payment_method_id});
}

function genBankCard(payment_method_id) {
    let bank_card = {
        payment_method_id,
        card_number: util.randNumStr(15),
        name_on_card: util.randName() + ' ' + util.randName(),
        exp_date: util.randInt(1,12) + '/' + util.randInt(2000, 2024),
        security_code: util.randNumStr(util.randInt(3, 4))
    };

    db.logInsert('bank_card', bank_card);
}

module.exports = {
    genPaymentMethod
}