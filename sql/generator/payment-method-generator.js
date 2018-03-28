const util = require('./util');
const db = require('./db')

function genPaymentMethod(customer_id) {
    let isGiftCard = util.randBool(.1);

    let payment_method_id = util.randId();
    let payment_method = {
        payment_method_id,
        payment_method_name: (isGiftCard ? 'GIFT' : 'BANK') + ' CARD ' + util.randAlphaStr(8),
        customer_id
    };

    db.logInsert('payment_method', payment_method);

    if (isGiftCard) {
        genGiftCard(payment_method_id);
    } else {
        genBankCard(payment_method_id);
    }

    return payment_method_id;
}

function genGiftCard(payment_method_id) {
    let card_number = util.randNumStr(15);
    let balance = util.randMoney();

    db.logInsert('gift_card', {payment_method_id, card_number, balance});
}

function genBankCard(payment_method_id) {
    let bank_card = {
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