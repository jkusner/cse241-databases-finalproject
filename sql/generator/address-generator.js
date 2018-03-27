const util = require('./util');
const db = require('./db');

function genAddress(address_id) {
    let address = {
        address_id,
        line1: util.randStreetAddress(),
        line2: util.randBool() ? util.randAlphaStr() : '',
        line3: util.randBool(.1) ? util.randAlphaStr() : '',
        city: util.randAlphaStr(),
        state: util.randState(2),
        zip: util.randNumStr(5),
        active: 1
    };
    db.logInsert('address', address);
}

module.exports = {
    genAddress
}