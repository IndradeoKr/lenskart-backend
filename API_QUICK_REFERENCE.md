# Lenskart API Quick Reference

## Base URL
`http://localhost:8080`

---

## 🔐 Authentication Endpoints

### Customer Registration
```bash
POST /customer
Body: {
  "userName": "john_doe",
  "password": "password123",
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNumber": 1234567890,
  "address": "123 Main St",
  "role": "CUSTOMER"
}
Response: "Customer registered successfully"
Status: 201
```

### Customer Login / Get User
```bash
GET /customer?email=john@example.com
Response: {
  "userid": 1,
  "userName": "john_doe",
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNumber": 1234567890,
  "address": "123 Main St",
  "role": "CUSTOMER"
}
Status: 200
```

### Update Customer Name
```bash
PATCH /customer?email=john@example.com&password=password123&name=John Smith
Response: "Name updated successfully"
Status: 200
```

### Update Customer Profile
```bash
PUT /customer?email=john@example.com&password=password123
Body: { updated UserDTO }
Response: "Customer updated successfully"
Status: 200
```

### Delete Customer
```bash
DELETE /customer?id=1
Response: "Customer deleted successfully"
Status: 200
```

### Admin Registration
```bash
POST /Admin
Body: {
  "userName": "admin_user",
  "password": "admin123",
  "name": "Admin Name",
  "email": "admin@example.com",
  "phoneNumber": 9876543210,
  "address": "Admin Address",
  "role": "ADMIN"
}
Response: "Admin created successfully"
Status: 201
```

### Admin Login / Get Admin
```bash
GET /Admin?email=admin@example.com
Response: { Admin UserDTO with role: "ADMIN" }
Status: 200
```

### Update Admin Name
```bash
PATCH /Admin?email=admin@example.com&password=admin123&name=New Admin Name
Response: "Admin name updated successfully"
Status: 200
```

### Update Admin Profile
```bash
PUT /Admin?email=admin@example.com&password=admin123
Body: { updated UserDTO }
Response: "Admin updated successfully"
Status: 200
```

### Delete Admin
```bash
DELETE /Admin?adminId=1
Response: "Admin deleted successfully"
Status: 200
```

---

## 📦 Product Endpoints

### Get All Products
```bash
GET /products
Response: [
  {
    "productId": 1,
    "productName": "Aviator Sunglasses",
    "productPrice": 3999.99,
    "productImage": "https://...",
    "quantity": 50,
    "brand": "Ray-Ban",
    "categoryName": "Sunglasses"
  },
  ...
]
Status: 200
```

### Get Product by ID
```bash
GET /products/1
Response: { ProductDTO }
Status: 200
```

### Get Products by Brand
```bash
GET /products/Ray-Ban
Response: [ array of ProductDTO with brand Ray-Ban ]
Status: 200
```

### Add Product (Admin Only)
```bash
POST /products
Body: {
  "productName": "Aviator Sunglasses",
  "productPrice": 3999.99,
  "productImage": "https://...",
  "quantity": 50,
  "brand": "Ray-Ban",
  "categoryName": "Sunglasses"
}
Response: "Product added successfully"
Status: 201
```

### Update Product (Admin Only)
```bash
PUT /products
Body: {
  "productId": 1,
  "productName": "Updated Name",
  "productPrice": 4999.99,
  "productImage": "https://...",
  "quantity": 100,
  "brand": "Ray-Ban",
  "categoryName": "Sunglasses"
}
Response: "Product updated successfully"
Status: 200
```

### Delete Product (Admin Only)
```bash
DELETE /products/1
Response: "Product deleted successfully"
Status: 200
```

---

## 🏷️ Category Endpoints

### Get Category by ID
```bash
GET /category/1
Response: {
  "categoryId": 1,
  "categoryName": "Sunglasses"
}
Status: 200
```

### Get Category by Name
```bash
GET /category/Sunglasses
Response: { CategoryDTO }
Status: 200
```

### Add Category (Admin Only)
```bash
POST /category
Body: {
  "categoryName": "Prescription Glasses"
}
Response: "Category added successfully"
Status: 201
```

### Update Category (Admin Only)
```bash
PUT /category/1/Updated Category Name
Response: "Category updated successfully"
Status: 200
```

### Delete Category (Admin Only)
```bash
DELETE /category/1
Response: "Category deleted successfully"
Status: 200
```

---

## 🛒 Cart Endpoints

### Add Item to Cart
```bash
POST /cart
Body: {
  "productId": 1,
  "customerId": 1,
  "quantity": 2
}
Response: "Product added to cart successfully"
Status: 201
```

### Increase Cart Quantity
```bash
PUT /cart
Body: {
  "productId": 1,
  "customerId": 1,
  "quantity": 3
}
Response: "Cart quantity increased successfully"
Status: 200
```

### Decrease Cart Quantity
```bash
PATCH /cart
Body: {
  "productId": 1,
  "customerId": 1,
  "quantity": 1
}
Response: "Cart quantity decreased successfully"
Status: 200
```

### Remove Item from Cart
```bash
DELETE /cart/1
Response: "Cart item deleted successfully"
Status: 200
```

---

## 📋 Order Endpoints

### Get All Orders (Admin Only)
```bash
GET /orders
Response: [
  {
    "orderId": 1,
    "date": "2025-05-12T10:30:00",
    "status": "IN_PROGRESS",
    "cartId": 1
  },
  ...
]
Status: 200
```

### Get Orders by Customer ID
```bash
GET /orders/1
Response: [ array of OrdersDTO for customer ]
Status: 200
```

### Place Order
```bash
POST /orders
Body: {
  "date": "2025-05-12T10:30:00",
  "status": "IN_PROGRESS",
  "cartId": 1
}
Response: "Order placed successfully"
Status: 201
```

### Update Order Status (Admin Only)
```bash
PUT /orders
Body: {
  "orderId": 1,
  "date": "2025-05-12T10:30:00",
  "status": "DELIVERED",
  "cartId": 1
}
Response: { updated OrdersDTO }
Status: 200
```

### Delete Order (Admin Only)
```bash
DELETE /orders/1
Response: "Order deleted successfully"
Status: 200
```

---

## ⚠️ Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid input",
  "message": "Email is required"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found",
  "message": "Product with ID 999 not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Server error",
  "message": "An unexpected error occurred"
}
```

---

## 🔑 Important Notes

### Authentication Flow (No JWT)
1. Frontend calls GET `/customer?email=...` or GET `/Admin?email=...`
2. Backend returns user object if found
3. **Frontend must validate password manually** (backend doesn't provide dedicated login endpoint)
4. Store returned user object in localStorage/Context with role field
5. Use `role` field to determine access level: "CUSTOMER" or "ADMIN"

### Role-Based Access Control
- **Customer**: Can access product listings, cart, orders (own), and profile
- **Admin**: Can access products (CRUD), categories (CRUD), orders (all), customers (view/delete), admins (CRUD)

### Validation Rules
- **Username**: 4-20 characters
- **Password**: Minimum 6 characters
- **Phone Number**: Must be exactly 10 digits
- **Email**: Must be valid email format
- **Product Price**: Must be >= 0
- **Quantity**: Cart quantity must be 1-999
- **Cart Quantity**: Minimum 1

### Status Codes
- **201**: Resource created
- **200**: Success
- **400**: Bad request (validation error)
- **404**: Not found
- **500**: Server error

---

## 📱 Sample Frontend Integration

### Login
```javascript
// Customer login
const email = "john@example.com";
const password = "password123";
const response = await fetch(`http://localhost:8080/customer?email=${email}`);
const user = await response.json();

// Verify password on frontend (or setup backend login endpoint)
if (user.password === password && user.role === "CUSTOMER") {
  localStorage.setItem('user', JSON.stringify(user));
  // Redirect to customer dashboard
}
```

### Add to Cart
```javascript
const cartItem = {
  productId: 1,
  customerId: user.userid,
  quantity: 2
};
const response = await fetch('http://localhost:8080/cart', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(cartItem)
});
```

### Place Order
```javascript
const order = {
  date: new Date().toISOString(),
  status: "IN_PROGRESS",
  cartId: cartId
};
const response = await fetch('http://localhost:8080/orders', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(order)
});
```

---

## 🚨 Critical Backend Issues (Should Be Fixed)

1. **No dedicated login endpoint** - Consider adding `POST /login` for secure authentication
2. **Passwords not hashed** - Should use bcrypt to hash passwords
3. **CORS not configured** - Frontend will get CORS errors from browser
4. **No pagination** - Large datasets will be slow
5. **Status field missing from Orders entity** - DTO has it but entity doesn't
