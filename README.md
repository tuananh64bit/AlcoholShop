# AlcoholShop - Premium Alcohol Store

A comprehensive web application for selling alcoholic beverages built with Java 17, Servlet + JSP, JSTL, JDBC with HikariCP, and MariaDB.

## 🍷 Features

### Core Functionality
- **User Authentication & Authorization**: Login/Register with role-based access (ADMIN/CUSTOMER)
- **Age Verification**: 18+ age requirement with validation
- **Product Catalog**: 30+ products across 5 categories (Whiskey, Vodka, Wine, Beer, Liqueur)
- **Shopping Cart**: Session-based cart with add/update/remove functionality
- **Order Processing**: Transactional checkout with stock management
- **Contact System**: Contact form with message storage
- **Admin Panel**: Complete CRUD operations for products, orders, users, and messages

### Security Features
- **Password Hashing**: BCrypt for secure password storage
- **SQL Injection Prevention**: PreparedStatement for all database queries
- **XSS Prevention**: Input sanitization and output escaping
- **Age Verification**: Multiple layers of age checking
- **Session Management**: Secure session handling with timeouts

### Technical Stack
- **Backend**: Java 17, Servlet API 6.0, JSP 3.1, JSTL 3.0
- **Database**: MariaDB (MySQL-compatible) with UTF8MB4 charset
- **Connection Pool**: HikariCP for optimal database performance
- **Build Tool**: Maven 3.x
- **Server**: Apache Tomcat 9/10
- **Frontend**: Bootstrap 5, Font Awesome, Custom CSS
- **Logging**: SLF4J + Logback

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MariaDB 10.2 or higher
- Apache Tomcat 9/10

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd AlcoholShop
   ```

2. **Database Setup**
   ```bash
   # Create database and import schema using MariaDB client
   mariadb -u root -p < db/alcohol_shop.sql
   ```

3. **Configure Database Connection**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   db.url=jdbc:mariadb://localhost:3306/alcohol_shop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
   db.user=root
   db.password=your_password
   ```

   Or set environment variables (example using MariaDB JDBC URL):
   ```bash
   export DB_URL="jdbc:mariadb://localhost:3306/alcohol_shop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
   export DB_USER="root"
   export DB_PASSWORD="your_password"
   ```

4. **Build the Application**
   ```bash
   mvn clean package
   ```

5. **Deploy to Tomcat**
   ```bash
   # Copy WAR file to Tomcat webapps directory
   cp target/AlcoholShop.war $TOMCAT_HOME/webapps/
   
   # Start Tomcat
   $TOMCAT_HOME/bin/startup.sh  # Linux/Mac
   # or
   $TOMCAT_HOME/bin/startup.bat  # Windows
   ```

6. **Access the Application**
   ```
   http://localhost:8080/AlcoholShop/
   ```

## 👥 Default Credentials

### Admin Account
- **Username**: `admin`
- **Password**: `Admin@123`
- **Role**: ADMIN

### Customer Account
- **Username**: `customer1`
- **Password**: `Admin@123`
- **Role**: CUSTOMER

## 📋 Testing Checklist

### Authentication & Authorization
- [ ] Register new user (18+ years old) - should succeed
- [ ] Register new user (<18 years old) - should fail with age error
- [ ] Login with valid credentials - should succeed
- [ ] Login with invalid credentials - should fail
- [ ] Admin login - should access admin panel
- [ ] Customer login - should NOT access admin panel (403 error)
- [ ] Logout - should clear session

### Product Management
- [ ] View all products - should show 30+ products
- [ ] Search products by name/description - should return results
- [ ] Filter products by category - should show filtered results
- [ ] View product details - should show complete information
- [ ] Add product to cart - should update cart count
- [ ] Update cart quantity - should reflect changes
- [ ] Remove product from cart - should remove item

### Shopping Cart & Checkout
- [ ] Add items to cart (authenticated user) - should work
- [ ] Add items to cart (guest user) - should redirect to login
- [ ] Checkout process (18+ user) - should create order
- [ ] Checkout process (<18 user) - should fail with age error
- [ ] Stock validation - should prevent overselling
- [ ] Order creation - should reduce product stock

### Admin Panel
- [ ] Access admin dashboard - should show statistics
- [ ] Product CRUD operations - should work correctly
- [ ] Order management - should update order status
- [ ] User management - should change user roles
- [ ] Contact message management - should view/delete messages

### Contact System
- [ ] Submit contact form - should save message
- [ ] Form validation - should show appropriate errors
- [ ] Admin view messages - should list all messages

### Security & Validation
- [ ] SQL injection attempts - should be prevented
- [ ] XSS attempts - should be sanitized
- [ ] Age verification - should work at multiple points
- [ ] Session management - should timeout appropriately

## 🏗️ Project Structure

```
AlcoholShop/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── db/
│   └── alcohol_shop.sql             # Database schema and seed data
├── src/main/
│   ├── java/com/example/alcoholshop/
│   │   ├── model/                   # POJO classes
│   │   │   ├── UserAccount.java
│   │   │   ├── Product.java
│   │   │   ├── Category.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java
│   │   │   ├── CartItem.java
│   │   │   └── ContactMessage.java
│   │   ├── dao/                     # Data Access Objects
│   │   │   ├── ProductDAO.java
│   │   │   ├── CategoryDAO.java
│   │   │   ├── UserDAO.java
│   │   │   ├── OrderDAO.java
│   │   │   ├── ContactDAO.java
│   │   │   └── impl/               # DAO implementations
│   │   ├── servlet/                # Controllers
│   │   │   ├── HomeServlet.java
│   │   │   ├── ProductServlet.java
│   │   │   ├── CartServlet.java
│   │   │   ├── CheckoutServlet.java
│   │   │   ├── ContactServlet.java
│   │   │   ├── auth/               # Authentication servlets
│   │   │   └── admin/              # Admin servlets
│   │   ├── filter/                 # Filters
│   │   │   ├── EncodingFilter.java
│   │   │   ├── AuthFilter.java
│   │   │   └── AdminFilter.java
│   │   └── util/                   # Utilities
│   │       ├── DBUtil.java
│   │       └── ValidationUtil.java
│   ├── resources/
│   │   └── application.properties  # Configuration
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml            # Web application configuration
│       ├── pages/                 # JSP pages
│       │   ├── includes/          # Common includes
│       │   ├── auth/              # Authentication pages
│       │   ├── admin/             # Admin pages
│       │   └── error/             # Error pages
│       └── static/                # Static resources
│           ├── css/
│           ├── js/
│           └── images/
└── target/                        # Maven build output
    └── AlcoholShop.war            # Deployable WAR file
```

## 🔧 Configuration

### Database Configuration
The application reads database configuration from:
1. Environment variables (highest priority)
2. `application.properties` file
3. Default values

### Environment Variables
```bash
export DB_URL="jdbc:mariadb://localhost:3306/alcohol_shop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
export DB_USER="root"
export DB_PASSWORD="your_password"
```

### Application Properties
```properties
# Database Configuration
db.url=jdbc:mariadb://localhost:3306/alcohol_shop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=password

# HikariCP Configuration
db.pool.maximumPoolSize=20
db.pool.minimumIdle=5
db.pool.connectionTimeout=30000
db.pool.idleTimeout=600000
db.pool.maxLifetime=1800000

# Security Configuration
security.minAge=18
security.password.minLength=8
```

## 🛡️ Security Considerations

### Production Deployment
- Change default admin password
- Use HTTPS in production
- Configure proper database credentials
- Set up proper logging and monitoring
- Regular security updates

### Legal Compliance
- Age verification is implemented but may need additional verification
- Local laws regarding alcohol sales must be followed
- Consider implementing additional compliance features

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check MariaDB service is running
   - Verify database credentials
   - Ensure database exists and schema is imported

2. **Build Failures**
   - Ensure Java 17 is installed
   - Check Maven version (3.6+)
   - Clear Maven cache: `mvn clean`

3. **Deployment Issues**
   - Check Tomcat version compatibility
   - Verify WAR file is in correct location
   - Check Tomcat logs for errors

4. **Age Verification Not Working**
   - Check browser localStorage
   - Verify JavaScript is enabled
   - Check server-side age validation

## 📝 Development Team

**Project Members:**
- Nguyễn Văn A (DOB: 15/03/1995)
- Trần Thị B (DOB: 22/07/1998)  
- Lê Văn C (DOB: 10/12/1993)

**Project Completion Date:** ${new java.util.Date()}

## 📄 License

This project is developed for educational purposes. Please ensure compliance with local laws regarding alcohol sales and age verification.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For technical support or questions:
- Email: info@alcoholshop.com
- Phone: +1 (555) 123-4567
- Contact Form: Available in the application

---

**⚠️ Important Notice:** This application is for educational purposes. Ensure compliance with local laws and regulations regarding alcohol sales and age verification in your jurisdiction.
