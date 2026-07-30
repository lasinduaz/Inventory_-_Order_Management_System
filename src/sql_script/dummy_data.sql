
CREATE DATABASE IF NOT EXISTS stockflow_db;
USE stockflow_db;

INSERT INTO
    categories (name, description)
VALUES
    (
        'Electronics',
        'Electronic devices and accessories'
    ),
    (
        'Office Supplies',
        'Office stationery and supplies'
    ),
    ('Furniture', 'Office and home furniture'),
    ('Networking', 'Networking equipment'),
    (
        'Computer Components',
        'Internal computer hardware'
    );

INSERT INTO
    products (
        name,
        description,
        price,
        stock_quantity,
        sku,
        category_id
    )
VALUES
    (
        'Dell Latitude 5440',
        'Business Laptop',
        950.00,
        15,
        'LAP-001',
        1
    ),
    (
        'Logitech MX Master 3S',
        'Wireless Mouse',
        99.99,
        40,
        'MOU-001',
        1
    ),
    (
        'Mechanical Keyboard',
        'RGB Mechanical Keyboard',
        79.50,
        25,
        'KEY-001',
        1
    ),
    (
        'Office Chair',
        'Ergonomic Office Chair',
        180.00,
        10,
        'FUR-001',
        3
    ),
    (
        'Standing Desk',
        'Height Adjustable Desk',
        350.00,
        8,
        'FUR-002',
        3
    ),
    (
        'HP LaserJet Printer',
        'Black & White Printer',
        220.00,
        7,
        'PRN-001',
        2
    ),
    (
        'A4 Paper Pack',
        '500 Sheets',
        6.50,
        150,
        'OFF-001',
        2
    ),
    (
        'Cisco Switch 24-Port',
        'Managed Network Switch',
        520.00,
        5,
        'NET-001',
        4
    ),
    (
        'Kingston SSD 1TB',
        'NVMe SSD',
        125.00,
        30,
        'SSD-001',
        5
    ),
    (
        'Corsair 16GB RAM',
        'DDR4 Memory',
        68.99,
        45,
        'RAM-001',
        5
    );

INSERT INTO
    customers (first_name, last_name, email, phone)
VALUES
    (
        'John',
        'Smith',
        'john.smith@email.com',
        '+1-555-1001'
    ),
    (
        'Emily',
        'Johnson',
        'emily.johnson@email.com',
        '+1-555-1002'
    ),
    (
        'Michael',
        'Brown',
        'michael.brown@email.com',
        '+1-555-1003'
    ),
    (
        'Sophia',
        'Wilson',
        'sophia.wilson@email.com',
        '+1-555-1004'
    ),
    (
        'David',
        'Lee',
        'david.lee@email.com',
        '+1-555-1005'
    );

INSERT INTO
    orders (customer_id, order_date, status, total_amount)
VALUES
    (1, NOW (), 'CONFIRMED', 1049.99),
    (2, NOW (), 'PENDING', 186.50),
    (3, NOW (), 'SHIPPED', 350.00);

INSERT INTO
    order_items (
        order_id,
        product_id,
        quantity,
        unit_price,
        subtotal
    )
VALUES
    (1, 1, 1, 950.00, 950.00),
    (1, 2, 1, 99.99, 99.99),
    (2, 7, 1, 6.50, 6.50),
    (2, 4, 1, 180.00, 180.00),
    (3, 5, 1, 350.00, 350.00);