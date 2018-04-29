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

let brandToProducts = productGen.buildFromTree(brandCount);
let allProducts = [];

for (let brand in brandToProducts) {
    for (let product of brandToProducts[brand]) {
        allProducts.push(product);
    }
}

const productCount = allProducts.length;

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
let rewardsMembers = [];
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
    if (cust.isRewardsMember) {
        rewardsMembers.push(customer);
    }
}

for (let trans = 1; trans <= onlineTransactionCount; trans++) {
    let pMethod = util.randChoice(paymentMethods);
    transGen.genOnlineTransaction(pMethod, allProducts, allStores, customerAddresses);
}

for (let trans = 1; trans <= physicalTransactionCount; trans++) {
    transGen.genPhysicalTransaction(allStores, allProducts, rewardsMembers);
}