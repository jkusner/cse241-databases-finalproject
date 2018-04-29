const util = require('./util');
const db = require('./db');
const PRODUCT_TREE = require('./product-data')

let next_category_id = 1;
let next_product_id = 1;
let max_brand_id = 1;

let brandToProducts = {};

function buildFromTree(num_brands) {
    let product_id = 1;

    brandToProducts = {};
    for (let i = 1; i <= num_brands; i++) {
        brandToProducts[i] = [];
    }

    max_brand_id = num_brands || 1;
    _buildFromTree(PRODUCT_TREE);

    return brandToProducts;
}

function _buildFromTree(parent, parent_category_id) {
    if (!parent) return;

    for (let key in parent) {
        if (key !== '_stuff') {
            let category_id = next_category_id;
            next_category_id++;
            makeCategory(key, category_id, parent_category_id);
            _buildFromTree(parent[key], category_id);
        } else {
            for (let product of parent._stuff) {
                makeProduct(product, next_product_id++, parent_category_id);
            }
        }
    }
}

function makeCategory(category_name, category_id, parent_id) {
    db.logInsert('category', {
        category_id,
        category_name,
        parent_id: parent_id || null
    });
}

function makeProduct(product_data, product_id, category_id) {
    let brand = util.randInt(1, max_brand_id);
    db.logInsert('product', {
        product_id,
        product_name: product_data.product_name,
        upc_code: util.randNumStr(15),
        brand_id: brand
    });
    brandToProducts[brand].push(product_id);

    db.logInsert('product_category', {
        product_id,
        category_id
    });

    if (product_data.book) {
        db.logInsert('book', {
            product_id,
            isbn: product_data.book.isbn
        });
    }
    if (product_data.expiring) {
        db.logInsert('expiring_product', {
            product_id,
            days_fresh: product_data.expiring.days_fresh
        });
    }
}

module.exports = {
    buildFromTree
}
