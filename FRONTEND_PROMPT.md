# Lenskart ReactJS Frontend Generation Prompt

## Project Context
You are tasked with building a **complete ReactJS frontend** for the **Lenskart e-commerce application** - an eyewear e-commerce platform with role-based access control for customers and administrators.

**Key Requirement**: The application uses **role-based authentication** (field `role: "CUSTOMER" | "ADMIN"` in the User entity). **There is NO JWT token system**. Authentication is session-based with email/password verification against a role field in the database.

---

## Backend API Specification

### Base URL
```
http://localhost:8080
```

### API Endpoints

#### **AUTHENTICATION & USER MANAGEMENT**

**1. Customer Registration**
- **Method**: POST
- **URL**: `/customer`
- **Body**:
```json
{
  "userName": "string (4-20 chars)",
  "password": "string (min 6 chars)",
  "name": "string",
  "email": "string",
  "phoneNumber": 10-digit-number,
  "address": "string",
  "role": "CUSTOMER"
}
```
- **Response**: Success message string
- **Status**: 201 Created

**2. Customer Login** (Validate & Retrieve)
- **Method**: GET
- **URL**: `/customer?email={email}`
- **Response**:
```json
{
  "userid": int,
  "userName": "string",
  "name": "string",
  "email": "string",
  "phoneNumber": number,
  "address": "string",
  "role": "CUSTOMER"
}
```
- **Note**: Use this after password verification to fetch user details
- **Status**: 200 OK

**3. Update Customer Name**
- **Method**: PATCH
- **URL**: `/customer?email={email}&password={password}&name={newName}`
- **Response**: Success message
- **Status**: 200 OK

**4. Update Customer Profile**
- **Method**: PUT
- **URL**: `/customer?email={email}&password={password}`
- **Body**: Updated UserDTO
- **Response**: Success message
- **Status**: 200 OK

**5. Delete Customer**
- **Method**: DELETE
- **URL**: `/customer?id={customerId}`
- **Response**: Success message
- **Status**: 200 OK

**6. Admin Registration** (Admin only)
- **Method**: POST
- **URL**: `/Admin`
- **Body**: UserDTO with role="ADMIN"
- **Response**: Success message
- **Status**: 201 Created

**7. Admin Login** (Validate & Retrieve)
- **Method**: GET
- **URL**: `/Admin?email={email}`
- **Response**: Admin UserDTO
- **Status**: 200 OK

**8. Update Admin Name**
- **Method**: PATCH
- **URL**: `/Admin?email={email}&password={password}&name={newName}`
- **Response**: Success message
- **Status**: 200 OK

**9. Update Admin Profile**
- **Method**: PUT
- **URL**: `/Admin?email={email}&password={password}`
- **Body**: Updated UserDTO
- **Response**: Success message
- **Status**: 200 OK

**10. Delete Admin**
- **Method**: DELETE
- **URL**: `/Admin?adminId={adminId}`
- **Response**: Success message
- **Status**: 200 OK

---

#### **PRODUCT MANAGEMENT** (All GET endpoints are public; POST/PUT/DELETE are Admin only)

**11. Get All Products**
- **Method**: GET
- **URL**: `/products`
- **Response**: Array of ProductDTO
```json
{
  "productId": int,
  "productName": "string",
  "productPrice": double,
  "productImage": "string (URL)",
  "quantity": int,
  "brand": "string",
  "categoryName": "string"
}
```
- **Status**: 200 OK

**12. Get Product by ID**
- **Method**: GET
- **URL**: `/products/{productId}`
- **Response**: Single ProductDTO
- **Status**: 200 OK

**13. Get Products by Brand**
- **Method**: GET
- **URL**: `/products/{brand}`
- **Response**: Array of ProductDTO
- **Status**: 200 OK

**14. Add Product** (Admin Only)
- **Method**: POST
- **URL**: `/products`
- **Body**: ProductDTO (without productId)
- **Response**: Success message
- **Status**: 201 Created

**15. Update Product** (Admin Only)
- **Method**: PUT
- **URL**: `/products`
- **Body**: ProductDTO (with productId)
- **Response**: Success message
- **Status**: 200 OK

**16. Delete Product** (Admin Only)
- **Method**: DELETE
- **URL**: `/products/{productId}`
- **Response**: Success message
- **Status**: 200 OK

---

#### **CATEGORY MANAGEMENT** (GET is public; POST/PUT/DELETE are Admin only)

**17. Get Category by ID**
- **Method**: GET
- **URL**: `/category/{categoryId}`
- **Response**: CategoryDTO
```json
{
  "categoryId": int,
  "categoryName": "string"
}
```
- **Status**: 200 OK

**18. Get Category by Name**
- **Method**: GET
- **URL**: `/category/{categoryName}`
- **Response**: CategoryDTO
- **Status**: 200 OK

**19. Add Category** (Admin Only)
- **Method**: POST
- **URL**: `/category`
- **Body**: CategoryDTO
- **Response**: Success message
- **Status**: 201 Created

**20. Update Category** (Admin Only)
- **Method**: PUT
- **URL**: `/category/{categoryId}/{newName}`
- **Response**: Success message
- **Status**: 200 OK

**21. Delete Category** (Admin Only)
- **Method**: DELETE
- **URL**: `/category/{categoryId}`
- **Response**: Success message
- **Status**: 200 OK

---

#### **CART MANAGEMENT** (Customer Only)

**22. Add Item to Cart**
- **Method**: POST
- **URL**: `/cart`
- **Body**:
```json
{
  "productId": int,
  "customerId": int,
  "quantity": int (1-999)
}
```
- **Response**: Success message
- **Status**: 201 Created

**23. Increase Cart Quantity**
- **Method**: PUT
- **URL**: `/cart`
- **Body**: CartDTO with increased quantity
- **Response**: Success message
- **Status**: 200 OK

**24. Decrease Cart Quantity**
- **Method**: PATCH
- **URL**: `/cart`
- **Body**: CartDTO with decreased quantity
- **Response**: Success message
- **Status**: 200 OK

**25. Remove Item from Cart**
- **Method**: DELETE
- **URL**: `/cart/{cartId}`
- **Response**: Success message
- **Status**: 200 OK

---

#### **ORDER MANAGEMENT**

**26. Get All Orders** (Admin Only)
- **Method**: GET
- **URL**: `/orders`
- **Response**: Array of OrdersDTO
```json
{
  "orderId": int,
  "date": "ISO DateTime string",
  "status": "IN_PROGRESS" | "DELIVERED",
  "cartId": int
}
```
- **Status**: 200 OK

**27. Get Orders by Customer ID**
- **Method**: GET
- **URL**: `/orders/{customerId}`
- **Response**: Array of OrdersDTO
- **Status**: 200 OK

**28. Place Order** (Customer)
- **Method**: POST
- **URL**: `/orders`
- **Body**:
```json
{
  "date": "ISO DateTime",
  "status": "IN_PROGRESS",
  "cartId": int
}
```
- **Response**: Success message
- **Status**: 201 Created

**29. Update Order** (Admin Only)
- **Method**: PUT
- **URL**: `/orders`
- **Body**: OrdersDTO with updated status
- **Response**: OrdersDTO
- **Status**: 200 OK

**30. Delete Order** (Admin Only)
- **Method**: DELETE
- **URL**: `/orders/{orderId}`
- **Response**: Success message
- **Status**: 200 OK

---

## Frontend Features & Pages

### **CUSTOMER INTERFACE**

#### 1. **Customer Login Page**
- Email and password input fields
- Login button
- Link to customer registration page
- Error message display for invalid credentials
- After login: store user details in localStorage/Context, redirect to product listing

#### 2. **Customer Registration Page**
- Form fields: Username, Password, Full Name, Email, Phone Number, Address
- Submit button
- Validation: Email format, password length, phone 10 digits
- Success message and redirect to login on completion
- Link back to login

#### 3. **Product Listing Page** (Main Dashboard)
- Display all products in a grid/card layout
- Show: Product image, name, brand, price, category
- Filter options:
  - By Brand (dropdown or checkboxes)
  - By Category
  - By Price Range (slider)
- Search bar for product name
- "Add to Cart" button on each product
- Click on product to view details
- Navigation to cart and profile

#### 4. **Product Details Page**
- Full product information
- Large product image
- Name, brand, price, category, stock quantity
- Quantity selector (spinner/input)
- "Add to Cart" button
- Back to listing button

#### 5. **Shopping Cart Page**
- List all items in cart with:
  - Product image, name, price per unit
  - Quantity (with increase/decrease buttons)
  - Item total price
  - Remove item button
- Cart summary: Total items, Total price
- "Checkout" button
- "Continue Shopping" button
- Empty cart message if cart is empty

#### 6. **Order Checkout Page**
- Display order summary (items, quantities, prices)
- Delivery address from customer profile
- Order date/time
- "Place Order" button
- Success confirmation with order ID

#### 7. **Order History Page**
- Display all customer orders in a table/list
- Columns: Order ID, Date, Items Count, Total Price, Status (IN_PROGRESS/DELIVERED)
- Filter by status
- Click order to view details
- No order placed message if empty

#### 8. **Customer Profile Page**
- Display current user info: Name, Email, Phone, Address, Username
- Edit buttons for each field
- "Update Profile" button
- "Change Password" option
- "Logout" button
- Delete account option (with confirmation)

#### 9. **Navigation Bar** (Logged-in Customer)
- Logo/Brand name
- Links: Products, Cart, Orders, Profile
- Logout button

---

### **ADMIN INTERFACE**

#### 1. **Admin Login Page**
- Similar to customer login but for admin role verification
- After login: store admin details, redirect to admin dashboard

#### 2. **Admin Dashboard** (Main Page)
- Welcome message with admin name
- Quick stats:
  - Total Products
  - Total Orders
  - Total Customers
  - Total Admins
- Links to management sections:
  - Product Management
  - Category Management
  - Order Management
  - Customer Management
  - Admin Management

#### 3. **Product Management Page**
- Table of all products with columns:
  - Product ID, Name, Brand, Category, Price, Quantity, Image URL
- Action buttons: Edit, Delete
- "Add New Product" button at top
- Search and filter options

#### 4. **Add/Edit Product Modal/Page**
- Form fields: Product Name, Brand, Category (dropdown), Price, Quantity, Image URL
- Validation: Price >= 0, Quantity >= 0
- Submit button (Add/Update)
- Cancel button

#### 5. **Delete Product Confirmation**
- Confirmation dialog with product details
- "Confirm Delete" and "Cancel" buttons

#### 6. **Category Management Page**
- Table of all categories with columns: Category ID, Name
- Action buttons: Edit, Delete
- "Add New Category" button

#### 7. **Add/Edit Category Modal**
- Form field: Category Name
- Submit button
- Cancel button

#### 8. **Order Management Page**
- Table of all orders with columns:
  - Order ID, Date, Customer ID, Cart ID, Status, Actions
- Filter by status (IN_PROGRESS, DELIVERED)
- Action buttons: View Details, Update Status
- "Download Report" button (optional)

#### 9. **Update Order Status Modal**
- Current status display
- Dropdown to select new status (IN_PROGRESS → DELIVERED)
- Submit button
- Cancel button

#### 10. **Customer Management Page**
- Table of all customers with columns:
  - Customer ID, Username, Email, Phone, Address, Actions
- Search by email or username
- Action buttons: View Profile, Delete
- "Delete Customer" with confirmation

#### 11. **Admin Management Page**
- Table of all admins with columns:
  - Admin ID, Username, Email, Name, Actions
- "Add New Admin" button
- Action buttons: Edit, Delete
- Admin form for add/edit

#### 12. **Admin Profile Page**
- Similar to customer profile
- Edit profile options
- Logout button

#### 13. **Navigation Bar** (Logged-in Admin)
- Logo/Brand name
- Links: Dashboard, Products, Categories, Orders, Customers, Admins, Profile
- Logout button

---

## Technical Requirements

### **Tech Stack**
- **Frontend**: React 18+
- **Routing**: React Router v6
- **State Management**: Context API or Redux (optional)
- **HTTP Client**: Axios or Fetch API
- **Styling**: Tailwind CSS, Material-UI, or Bootstrap
- **Form Validation**: React Hook Form + Yup/Zod
- **Storage**: localStorage for session/user data

### **Authentication Flow (Role-Based)**
1. User enters email and password on login page
2. Frontend calls GET `/customer?email={email}` or GET `/Admin?email={email}` based on login route
3. **IMPORTANT**: Validate password manually on frontend (or send as query param) since backend doesn't provide a dedicated login endpoint
4. Store returned user object with role in localStorage and Context
5. Check role field to determine admin vs. customer access
6. Implement route guards: redirect to login if not authenticated, redirect to appropriate dashboard based on role

### **Key Implementation Notes**
1. **NO JWT TOKENS**: Use session-based auth with localStorage storing user data
2. **Role Verification**: Always check `user.role === "ADMIN"` or `user.role === "CUSTOMER"` before rendering pages and making requests
3. **Error Handling**: Catch and display errors from API responses
4. **Loading States**: Show loaders/spinners during API calls
5. **Responsive Design**: Mobile-friendly UI
6. **Form Validation**: Client-side validation before submission
7. **CORS**: Backend should have CORS enabled (add this if not present)
8. **Protected Routes**: Implement middleware/guards to protect admin and customer routes

### **File Structure**
```
lenskart-frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── Navbar.jsx
│   │   ├── LoginForm.jsx
│   │   ├── ProductCard.jsx
│   │   ├── CartItem.jsx
│   │   └── ...
│   ├── pages/
│   │   ├── customer/
│   │   │   ├── ProductListing.jsx
│   │   │   ├── ProductDetails.jsx
│   │   │   ├── Cart.jsx
│   │   │   ├── Checkout.jsx
│   │   │   ├── OrderHistory.jsx
│   │   │   └── Profile.jsx
│   │   ├── admin/
│   │   │   ├── Dashboard.jsx
│   │   │   ├── ProductManagement.jsx
│   │   │   ├── CategoryManagement.jsx
│   │   │   ├── OrderManagement.jsx
│   │   │   ├── CustomerManagement.jsx
│   │   │   └── AdminManagement.jsx
│   │   ├── Login.jsx
│   │   └── Register.jsx
│   ├── services/
│   │   └── api.js (Axios configuration)
│   ├── context/
│   │   └── AuthContext.jsx
│   ├── App.jsx
│   └── index.css
├── package.json
└── vite.config.js (if using Vite)
```

---

## API Error Handling
- Handle common HTTP status codes: 400, 404, 500
- Display user-friendly error messages
- Show validation errors from backend

---

## Testing Scenarios

### **Customer Flow**
1. Register as customer
2. Login with customer credentials
3. View all products and filter by brand/category
4. View product details
5. Add products to cart
6. Modify cart (increase/decrease/remove items)
7. Place order from cart
8. View order history with statuses
9. Update profile
10. Logout

### **Admin Flow**
1. Login as admin with admin credentials
2. View dashboard with stats
3. Add a new product
4. Edit an existing product
5. Delete a product
6. Add a new category
7. View all orders and update order status
8. View and delete customers
9. Create and manage admins
10. Logout

---

## IMPORTANT MISSING BACKEND FEATURES TO ADD

### **Critical Issues:**
1. **No Login Endpoint** - Backend doesn't have a dedicated login endpoint. You must:
   - Either add a POST `/login` endpoint to backend that validates credentials
   - Or validate credentials on frontend using GET endpoint (frontend sends password in query)
   - **Recommended**: Add secure login endpoint with password verification

2. **Plain Text Passwords** - Passwords should be hashed using bcrypt on backend

3. **No CORS Configuration** - Add CORS headers to allow frontend requests

4. **Missing Order Status in Orders Entity** - OrdersDTO has `status` field, but Orders entity doesn't seem to have it

5. **Session Management** - No session tracking. Consider adding:
   - Session tokens/cookies
   - Session expiry

### **Recommended Additions to Backend:**
- Pagination for product and order endpoints
- Search endpoint for products
- Standardized error response format
- Input sanitization
- Rate limiting for security

---

## Deliverables
1. Complete React application with all pages mentioned above
2. Role-based routing and authentication guards
3. Full CRUD operations for admin features
4. Customer shopping flow from products to order placement
5. Proper error handling and loading states
6. Responsive UI design
7. Session management with localStorage
8. Integration with all backend API endpoints

---

## Additional Notes
- Assume backend is running on `http://localhost:8080`
- Make sure to handle CORS errors gracefully
- Implement logout functionality that clears localStorage and redirects to login
- Consider adding a "Remember Me" feature for login
- Implement debouncing for search and filter operations
- Add loading spinners for long-running operations
- Consider adding toast notifications for success/error messages
