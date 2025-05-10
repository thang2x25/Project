from flask import Flask, request, jsonify
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

DATABASE = 'Database.db'

def get_db():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn

# ---------------------- CUSTOMER ----------------------
@app.route('/customers', methods=['POST'])
def insert_customer():
    data = request.json
    username = data['username']
    password = data['password']
    conn = get_db()
    cursor = conn.cursor()
    try:
        # Kiểm tra xem tài khoản đã tồn tại chưa
        cursor.execute("SELECT * FROM Customer WHERE CustomerName = ?", (username,))
        existing_customer = cursor.fetchone()
        if existing_customer:
            return jsonify({'error': 'Username already exists'}), 400

        cursor.execute("INSERT INTO Customer (CustomerName, CustomerPassword) VALUES (?, ?)", (username, password))
        conn.commit()
        return jsonify({'message': 'Customer added successfully'}), 201
    except Exception as e:
        print("Customer Insert Error:", str(e))
        return jsonify({'error': 'Failed to add customer', 'message': str(e)}), 500
    finally:
        conn.close()


@app.route('/customers/login', methods=['POST'])
def validate_customer():
    data = request.json
    username = data['username']
    password = data['password']
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM Customer WHERE CustomerName = ? AND CustomerPassword = ?", (username, password))
    result = cursor.fetchone()
    conn.close()
    if result:
        return jsonify({'message': 'Login successful'})
    else:
        return jsonify({'error': 'Invalid credentials'}), 401

# ---------------------- PRODUCT ----------------------
@app.route('/products', methods=['GET'])
def get_all_products():
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM Product")
    products = [dict(row) for row in cursor.fetchall()]
    conn.close()
    return jsonify(products)

@app.route('/products', methods=['POST'])
def insert_product():
    data = request.json
    try:
        name = data['ProductName']
        price = data['ProductPrice']
        description = data['ProductDescription']
        image = data['ProductImage']
    except KeyError:
        return jsonify({'error': 'Missing fields'}), 400

    conn = get_db()
    cursor = conn.cursor()
    try:
        cursor.execute(
            "INSERT INTO Product (ProductName, ProductPrice, ProductDescription, ProductImage) VALUES (?, ?, ?, ?)",
            (name, price, description, image))
        conn.commit()
        return jsonify({'message': 'Product added successfully'}), 201
    except Exception as e:
        print("Product Insert Error:", str(e))
        return jsonify({'error': 'Failed to add product', 'message': str(e)}), 500
    finally:
        conn.close()

# ---------------------- ORDER ----------------------
@app.route('/orders', methods=['POST'])
def insert_order():
    data = request.json
    try:
        order_name = data['name']  # Ví dụ: "Apple - Thao"
        quantity = data['quantity']
        total = data['total']
    except KeyError:
        return jsonify({'error': 'Missing fields'}), 400

    # Tách order_name thành product_name và customer_name
    try:
        product_name, customer_name = order_name.split(" - ")
    except ValueError:
        return jsonify({'error': 'Order name format is invalid. It should be: ProductName - CustomerName'}), 400

    conn = get_db()
    cursor = conn.cursor()
    try:
        # Lấy ProductID
        cursor.execute("SELECT ProductID FROM Product WHERE ProductName = ?", (product_name,))
        product_row = cursor.fetchone()
        if not product_row:
            return jsonify({'error': 'Product not found'}), 404
        product_id = product_row['ProductID']

        # Lấy CustomerID
        cursor.execute("SELECT CustomerID FROM Customer WHERE CustomerName = ?", (customer_name,))
        customer_row = cursor.fetchone()
        if not customer_row:
            return jsonify({'error': 'Customer not found'}), 404
        customer_id = customer_row['CustomerID']

        # Insert đơn hàng với đầy đủ khoá ngoại
        cursor.execute(
            '''
            INSERT INTO OrderProduct (OrderName, OrderQuantity, OrderTotal, CustomerID, ProductID)
            VALUES (?, ?, ?, ?, ?)
            ''',
            (order_name, quantity, total, customer_id, product_id)
        )
        conn.commit()
        return jsonify({'message': 'Order placed successfully'}), 201

    except Exception as e:
        print("Order Insert Error:", str(e))
        return jsonify({'error': 'Failed to place order', 'message': str(e)}), 500
    finally:
        conn.close()

@app.route('/orders', methods=['GET'])
def get_orders():
    conn = get_db()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM OrderProduct")
    orders = [dict(row) for row in cursor.fetchall()]
    conn.close()
    return jsonify(orders)

if __name__ == '__main__':
    app.run(debug=True)
