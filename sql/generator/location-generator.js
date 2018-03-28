const util = require('./util');
const db = require('./db');
const addr = require('./address-generator')

function genLocation(location_id, location_name) {
    let address_id = util.randId();
    addr.genAddress(address_id);

    let location = {
        location_id,
        location_name: location_name || util.randAlphaStr(),
        address_id
    };
    db.logInsert('location', location);
}

function genStore(location_id) {
    genLocation(location_id, 'Store #' + util.randNumStr(3));

    let store = {
        location_id,
        hour_open: util.randInt(0, 23),
        minute_open: util.randInt(0, 59),
        hour_close: util.randInt(0, 23),
        minute_close: util.randInt(0, 59)
    };
    db.logInsert('store', store);
}

function genWarehouse(location_id) {
    genLocation(location_id, 'Warehouse #' + util.randNumStr(3));

    let warehouse = {
        location_id,
        open: util.randBool(.9) ? 1 : 0
    };
    db.logInsert('warehouse', warehouse);
}

function genStock(location_id, product_id) {
    let stock = {
        location_id,
        product_id,
        qty: util.randInt(1, 1000),
        unit_price: util.randMoney()
    };
    db.logInsert('stock', stock);
}

module.exports = {
    genStore,
    genWarehouse,
    genStock
}