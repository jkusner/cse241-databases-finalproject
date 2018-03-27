const util = require('./util');
const db = require('./db');

function genProduct(product_id, brand_id) {
    let product = {
        product_id,
        product_name: util.randAlphaStr(),
        upc_code: util.randNumStr(15),
        brand_id
    };
    db.logInsert('product', product);

    if (util.randBool(.25)) {
        if (util.randBool()) {
            genBook(product_id);
        } else {
            genExpiring(product_id);
        }
    }
}

function genBook(product_id) {
    let book = {
        product_id,
        isbn: util.randNumStr(20)
    };
    db.logInsert('book', book);
}

function genExpiring(product_id) {
    let expiring_product = {
        product_id,
        days_fresh: util.randInt(1, 90)
    };
    db.logInsert('expiring_product', expiring_product)
}

function genCategory(category_id) {
    let category = {
        category_id,
        category_name: util.randAlphaStr(),
        parent_id: null
    }
    db.logInsert('category', category);
}

function genProductCategory(product_id, category_id) {
    let product_category = {
        product_id,
        category_id
    }
    db.logInsert('product_category', product_category);
}

module.exports = {
    genProduct,
    genCategory,
    genProductCategory
}