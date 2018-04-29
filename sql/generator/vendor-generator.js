const util = require('./util');
const db = require('./db');
const addr = require('./address-generator')

function genVendor(vendor_id) {
    let address_id = util.randId();
    addr.genAddress(address_id);

    let vendor = {
        vendor_id,
        vendor_name: util.randName() + ' Inc.',
        address_id
    };
    db.logInsert('vendor', vendor);
}

function genBrand(brand_id) {
    let brand = {
        brand_id,
        brand_name: util.randName() + ' Brand'
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
        shipment_qty: util.randInt(1, 100) * 100,
        shipment_price: util.randMoney(true)
    };
    db.logInsert('vendor_supply', vendor_supply);
}

module.exports = {
    genVendor,
    genBrand,
    genVendorBrand,
    genVendorSupply
}