const util = require('./util');
const custGen = require('./customer-generator');
const locGen = require('./location-generator');
const vendorGen = require('./vendor-generator');
const productGen = require('./product-generator');
const paymentMethodGen = require('./payment-method-generator');
const transGen = require('./transaction-generator');

// const customerCount = 100;
// const storeCount = 10;
// const warehouseCount = 10;
// const vendorCount = 3;
// const brandCount = 10;
// const productCount = 100;
// const categoryCount = 10;

const customerCount = 10;
const storeCount = 2;
const warehouseCount = 2;
const vendorCount = 2;
const brandCount = 2;
const productCount = 3;
const categoryCount = 3;
const onlineTransactionCount = 3;
const physicalTransactionCount = 3;

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
for (let location = 1; location <= storeCount + warehouseCount; location++) {
    if (location <= storeCount) {
        locGen.genStore(location);
    } else {
        locGen.genWarehouse(location);
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

    for (let i = 0; i < util.randInt(0, 2); i++) {
        let method_id = paymentMethodGen.genPaymentMethod(customer);
        paymentMethods.push(method_id);
    }
}

for (let trans = 1; trans <= onlineTransactionCount; trans++) {
    let pMethod = util.randChoice(paymentMethods);
    transGen.genOnlineTransaction(pMethod, allProducts, allLocations, customerAddresses);
}

for (let trans = 1; trans <= physicalTransactionCount; trans++) {
    transGen.genPhysicalTransaction(allLocations, allProducts);
}