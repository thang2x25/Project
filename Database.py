import sqlite3

# Kết nối (nếu chưa có file thì sẽ tự tạo)
conn = sqlite3.connect('Database.db')
cursor = conn.cursor()

# Tạo bảng Customer
cursor.execute('''
CREATE TABLE IF NOT EXISTS Customer (
    CustomerID INTEGER PRIMARY KEY AUTOINCREMENT,
    CustomerName TEXT,
    CustomerPassword TEXT
)
''')

# Tạo bảng Product
cursor.execute('''
CREATE TABLE IF NOT EXISTS Product (
    ProductID INTEGER PRIMARY KEY AUTOINCREMENT,
    ProductPrice INTEGER,
    ProductDescription TEXT,
    ProductImage INTEGER,
    ProductName TEXT
)
''')

# Tạo bảng OrderProduct (có khóa ngoại)
cursor.execute('''
CREATE TABLE IF NOT EXISTS OrderProduct (
    OrderID INTEGER PRIMARY KEY AUTOINCREMENT,
    OrderQuantity INTEGER,
    OrderName TEXT,
    CustomerID INTEGER,
    ProductID INTEGER,
    OrderTotal INTEGER,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
)
''')

conn.commit()
conn.close()

print("✅ Đã tạo xong file Database.db với 3 bảng đầy đủ.")
