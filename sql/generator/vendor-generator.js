const util = require('./util');
const db = require('./db');
const addr = require('./address-generator')

function genVendor(vendor_id) {
    let address_id = util.randId();
    addr.genAddress(address_id);

    let vendor = {
        vendor_id,
        vendor_name: util.randAlphaStr(),
        address_id
    };
    db.logInsert('vendor', vendor);
}

function genBrand(brand_id) {
    let brand = {
        brand_id,
        brand_name: util.randName() + ' Foods'
    }
    db.logInsert('brand', brand);
}

function genVendorBrand(vendor_id, brand_id) {
    let vendor_brand = {
        vendor_id,
        brand_id
    };
    db.logInsert('vendor_brand', vendor_brand);
}

function genVendorSupply(vendor_id, product_id) {
    let vendor_supply = {
        product_id,
        vendor_id,
        qty: util.randInt(1, 1000),
        unit_price: util.randMoney()
    };
    db.logInsert('vendor_supply', vendor_supply);
}

module.exports = {
    genVendor,
    genBrand,
    genVendorBrand,
    genVendorSupply
}