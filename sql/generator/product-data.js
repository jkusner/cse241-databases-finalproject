const PRODUCT_TREE = {
    Food: {
        Snacks: {
            Chips: {
                _stuff: [
                    {product_name: "Plain Potato Chips"},
                    {product_name: "BBQ Potato Chips"},
                    {product_name: "Sour Cream & Onion Potato Chips"}
                ]
            },
            Candy: {
                _stuff: [
                    {product_name: "Chocolate Bar"},
                    {product_name: "Skittles"},
                    {product_name: "M&M''s"},
                ]
            }
        },
        Fruit: {
            _stuff: [
                {product_name: "Bag of Apples", expiring: {days_fresh: 10}},
                {product_name: "Bunch of Bananas", expiring: {days_fresh: 10}},
                {product_name: "Baby Carrots", expiring: {days_fresh: 10}},
            ]
        },
        Dairy: {
            _stuff: [
                {product_name: "Milk", expiring: {days_fresh: 14}},
                {product_name: "Cheese", expiring: {days_fresh: 30}},
            ]
        }
    },
    Electronics: {
        Computers: {
            _stuff: [
                {product_name: "Terrible Laptop"},
                {product_name: "Decent Desktop"},
            ]
        },
        Televisions: {
            _stuff: [
                {product_name: "Tiny TV"},
                {product_name: "Big TV"},
                {product_name: "Fancy New OLED TV"},
            ]
        }
    },
    Books: {
        Fantasy: {
            _stuff: [
                {product_name: "A Game of Thrones", book: {isbn: "978-0553386790"}},
                {product_name: "Harry Potter and the Sorceror''s Stone", book: {isbn: "978-0439708180"}},
            ]
        },
        Textbooks: {
            _stuff: [
                {product_name: "Cracking the Coding Interview", book: {isbn: "978-0553386790"}},
                {product_name: "Database System Concepts", book: {isbn: "978-0073523323"}},
            ]
        }
    }
}

module.exports = PRODUCT_TREE