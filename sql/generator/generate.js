const util = require('./util');
const custGen = require('./customer-generator');
const locGen = require('./location-generator');
const vendorGen = require('./vendor-generator');
const productGen = require('./product-generator');
const paymentMethodGen = require('./payment-method-generator');
const transGen = require('./transaction-generator');
const db = require('./db');

const customerCount = 10;
const storeCount = 5;
const warehouseCount = 5;
const vendorCount = 3;
const brandCount = 5;
const productCount = 10;
const categoryCount = 3;
const onlineTransactionCount = 10;
const physicalTransactionCount = 10;

db.header();

for (let vendor = 1; vendor <= vendorCount; vendor++) {
    vendorGen.genVendor(vendor);
}

for (let brand = 1; brand <= brandCount; brand++) {
    vendorGen.genBrand(brand);
}

let vendorToBrands = {};
for (let brand = 1; brand <= brandCount; brand++) {
    let vendor = util.randInt(1, vendorCount);
    vendorGen.genVendorBrand(vendor, brand);
    
    if (!vendorToBrands[vendor]) {
        vendorToBrands[vendor] = [];
    }
    vendorToBrands[vendor].push(brand);
}

let brandToProducts = {};
let allProducts = [];
for (let product = 1; product <= productCount; product++) {
    let brand = util.randInt(1, brandCount);
    productGen.genProduct(product, brand);
    allProducts.push(product);

    if (!brandToProducts[brand]) {
        brandToProducts[brand] = [];
    }
    brandToProducts[brand].push(product)
}

for (let category = 1; category <= categoryCount; category++) {
    // TODO? subcategories
    productGen.genCategory(category);    
}

for (let product = 1; product <= productCount; product++) {
    productGen.genProductCategory(product, util.randInt(1, categoryCount));
}

for (let vendor = 1; vendor <= vendorCount; vendor++) {
    let brands = vendorToBrands[vendor];
    if (brands) {
        for (let brand of brands) {
            let products = brandToProducts[brand];
            if (products) {
                for (let product of products) {
                    vendorGen.genVendorSupply(vendor, product);
                }
            }
        }
    }
}

let allLocations = [];
let allStores = [];
let allWarehouses = [];
for (let location = 1; location <= storeCount + warehouseCount; location++) {
    if (location <= storeCount) {
        locGen.genStore(location);
        allStores.push(location);
    } else {
        locGen.genWarehouse(location);
        allWarehouses.push(location);
    }

    allLocations.push(location);

    for (let prod_id = 1; prod_id <= productCount; prod_id++) {
        if (util.randBool(.95)) {
            locGen.genStock(location, prod_id);
        }
    }
}

let customerAddresses = [];
let paymentMethods = [];
for (let customer = 1; customer <= customerCount; customer++) {
    let cust = custGen.genCustomer(customer);
    for (let address of cust.addresses) {
        customerAddresses.push(address);
    }

    if (cust.isOnlineMember) {
        for (let i = 0; i < Math.sqrt(util.randInt(1, 10)); i++) {
            let method_id = paymentMethodGen.genPaymentMethod(customer, i == 0);
            paymentMethods.push(method_id);
        }        
    }
}

for (let trans = 1; trans <= onlineTransactionCount; trans++) {
    let pMethod = util.randChoice(paymentMethods);
    transGen.genOnlineTransaction(pMethod, allProducts, allStores, customerAddresses);
}

for (let trans = 1; trans <= physicalTransactionCount; trans++) {
    transGen.genPhysicalTransaction(allStores, allProducts);
}