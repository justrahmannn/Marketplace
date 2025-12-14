# 🛍️ Marketplace - Modern E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

🌐 Live Demo: [https://my-marketplace-site.onrender.com/](https://my-marketplace-site.onrender.com/)

A modern, full-featured e-commerce marketplace platform built with Spring Boot that connects merchants and customers. Features a stunning Google-inspired UI design with bilingual support (English/Azerbaijani).

## ✨ Features

### For Customers
- 🔐 **Secure Authentication** - Register and login with email
- 🛒 **Shopping Cart** - Add products, manage quantities, and checkout
- ❤️ **Wishlist** - Save favorite products for later
- 🔍 **Product Search & Filters** - Search by name, filter by category and brand
- 💳 **Account Management** - View orders, manage balance, save payment cards
- 📦 **Order Tracking** - Track order status (Pending, Accepted, Rejected, Delivered)
- 🌍 **Bilingual Support** - Switch between English and Azerbaijani

### For Merchants
- 🏪 **Product Management** - Add, edit, delete products with images
- 🏷️ **Brand Management** - Select existing brands or create new ones on-the-fly
- ✅ **Required Field Validation** - Brand field is now mandatory to prevent crashes
- 📊 **Order Management** - Accept, reject, or mark orders as delivered
- 📸 **Image Upload** - Upload multiple product images
- 📈 **Dashboard** - View all products and orders in one place
- 💼 **Company Profile** - Manage company name and merchant details
- 🔍 **Brand Search** - Find brands by name (case-insensitive)

### Design & UI
- 🎨 **Modern Google-Style Design** - Sleek, professional interface
- 🌙 **Dark Theme** - Eye-friendly dark navy gradient background
- ✨ **Animated Effects** - Gradient orbs with parallax mouse tracking
- 💫 **Smooth Transitions** - Hover effects, ripples, and animations
- 📱 **Responsive Design** - Works seamlessly on all devices
- 🎯 **Enhanced Buttons** - 5 variants with gradients and effects

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerized deployment)

### Running Locally

1. **Clone the repository**
```bash
git clone https://github.com/justrahmannn/Marketplace.git
cd Marketplace/Marketplace/JavaProject
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

4. **Access the application**
```
http://localhost:8080
```

### Running with Docker

1. **Build the Docker image**
```bash
cd Marketplace/Marketplace
docker build -t marketplace-app:latest .
```

2. **Run the container**
```bash
docker run -d --name marketplace \
  -p 8080:8080 \
  -v marketplace-data:/app/data \
  marketplace-app:latest
```

3. **Access the application**
```
http://localhost:8080
```

## 🏗️ Technology Stack

### Backend
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Database operations
- **Spring Web** - RESTful web services
- **Thymeleaf** - Server-side template engine
- **PostgreSQL** - Production database (Render deployment)
- **H2 Database** - Embedded database for development
- **Lombok** - Reduce boilerplate code
- **Global Exception Handling** - Centralized error management

### Frontend
- **HTML5 & CSS3** - Modern markup and styling
- **JavaScript** - Dynamic interactions
- **Thymeleaf Templates** - Server-side rendering
- **Google Fonts** - Product Sans, Google Sans typography

### DevOps
- **Maven** - Dependency management and build
- **Docker** - Containerization
- **GitHub Actions** - CI/CD pipeline
- **Git** - Version control

## 📁 Project Structure

```
Marketplace/
├── Dockerfile                          # Docker configuration
├── .dockerignore                       # Docker ignore rules
├── .github/
│   └── workflows/
│       ├── ci.yml                     # Continuous Integration
│       └── cd.yml                     # Continuous Deployment
├── JavaProject/
│   ├── pom.xml                        # Maven dependencies
│   ├── data/                          # H2 database files
│   ├── uploads/                       # Product images
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/marketplace/
│   │   │   │   ├── config/            # Configuration classes
│   │   │   │   ├── controller/        # Web controllers
│   │   │   │   ├── entity/            # JPA entities
│   │   │   │   ├── exception/         # Custom exceptions & handlers
│   │   │   │   ├── repository/        # Data repositories
│   │   │   │   ├── service/           # Business logic
│   │   │   │   └── MarketplaceApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── static/
│   │   │       │   └── css/
│   │   │       │       └── style.css  # Modern Google-style CSS
│   │   │       └── templates/         # Thymeleaf HTML templates
│   │   └── test/                      # Unit tests
│   └── target/                        # Compiled files
└── README.md
```

## 🎨 UI Design Highlights

### Color Palette
- **Background**: Dark gradient (#0f0f1e → #1a1a2e)
- **Primary**: Google Blue (#4285f4)
- **Success**: Google Green (#34a853)
- **Warning**: Google Yellow (#fbbc04)
- **Danger**: Google Red (#ea4335)

### Components
- **Modern Header** - Fixed navigation with glassmorphism
- **Animated Hero** - Gradient orbs with parallax effect
- **Role Cards** - Interactive customer/merchant selection
- **Feature Grid** - Showcase platform capabilities
- **Enhanced Buttons** - 5 variants with hover effects and ripples
- **Product Cards** - Clean design with image, price, and actions
- **Modern Forms** - Styled inputs with focus states

## 📊 Database Schema

### Main Entities
- **User** - Base user entity (Customer, Merchant)
- **Customer** - Customer-specific data (balance, card info)
- **Merchant** - Merchant-specific data (company name)
- **Product** - Product details with images
- **ProductPhoto** - Multiple images per product
- **Category** - Product categorization
- **Brand** - Product brands
- **Cart** - Shopping cart for customers
- **CartItem** - Items in cart
- **Order** - Order records with status
- **Wishlist** - Customer wishlists

## 🔒 Security Features

- Password hashing (BCrypt recommended for production)
- Session-based authentication
- SQL injection prevention via JPA
- CSRF protection (configure for production)
- Input validation and form field requirements
- Global exception handling for security errors

## 🛠️ Recent Improvements

### Exception Handling
- **ResourceNotFoundException** - Custom 404 handling for missing resources
- **BadRequestException** - Validation and bad request error handling
- **InsufficientBalanceException** - Custom exception for payment issues
- **GlobalExceptionHandler** - Centralized error handling with detailed responses
- **ErrorResponse** - Structured error response with timestamp and details

### Form Validation
- **Required Brand Field** - Brand selection is now mandatory when adding products
- **JavaScript Validation** - Dynamic form validation with proper error handling
- **Toggle Validation** - Smart validation switching between existing and new brand options

### Brand Management Enhancements
- **Case-Insensitive Search** - Find brands regardless of letter casing
- **Duplicate Prevention** - Automatic detection and reuse of existing brands
- **On-the-Fly Creation** - Create new brands directly from product form
- **Brand Cleanup Utilities** - Tools for maintaining brand data integrity

## 🌐 Supported Languages

- **English** (EN)
- **Azerbaijani** (AZ)

Language switching is available on the landing page with real-time content updates.

## 🧪 Testing

Run unit tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

## 📝 API Endpoints

### Public
- `GET /` - Landing page
- `GET /auth/customer/login` - Customer login
- `GET /auth/merchant/login` - Merchant login
- `POST /auth/customer/register` - Customer registration
- `POST /auth/merchant/register` - Merchant registration

### Customer
- `GET /customer/products` - Browse products
- `GET /customer/products/{id}` - Product details
- `POST /customer/cart/add` - Add to cart
- `GET /customer/cart` - View cart
- `POST /customer/checkout` - Checkout
- `GET /customer/wishlist` - View wishlist
- `GET /customer/account` - Account dashboard

### Merchant
- `GET /merchant/dashboard` - Merchant dashboard
- `GET /merchant/products` - Manage products
- `GET /merchant/products/add` - Show add product form
- `POST /merchant/products/add` - Add new product (with brand validation)
- `GET /merchant/products/edit/{id}` - Show edit product form
- `POST /merchant/products/edit/{id}` - Update product
- `POST /merchant/products/delete/{id}` - Delete product
- `GET /merchant/orders` - View orders
- `POST /merchant/orders/{id}/accept` - Accept order
- `POST /merchant/orders/{id}/reject` - Reject order
- `POST /merchant/orders/{id}/deliver` - Mark delivered

## 🚀 Deployment

### Docker Deployment
```bash
# Build image
docker build -t marketplace-app .

# Run container
docker run -d -p 8080:8080 marketplace-app
```

### Production Considerations
- Configure PostgreSQL/MySQL for production database
- Set up HTTPS/SSL certificates
- Configure environment variables for sensitive data
- Set up monitoring and logging
- Configure backup strategy
- Enable CORS if needed for external APIs

## 🐛 Bug Fixes & Known Issues

### Fixed Issues
- ✅ **Brand Field Crash** - Fixed application crash when adding products without selecting a brand
- ✅ **Brand Validation** - Added required field validation for brand selection
- ✅ **Form Submission** - Improved form validation to prevent incomplete data submission
- ✅ **Brand Duplicate Check** - Implemented case-insensitive brand name checking

### Important Notes
- 📝 Always edit files in `src/main/resources/` not `target/classes/` (build output)
- 🔄 Run `mvn clean install` after major changes to rebuild the project
- 🌐 The application is deployed on Render with PostgreSQL database

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Contribution Guidelines
- Write clear commit messages
- Add comments to complex code
- Update README for new features
- Test thoroughly before submitting PR
- Follow existing code style and patterns

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Author
- **Rahman** - [justrahmannn](https://github.com/justrahmannn)
- **Farid** - [afandiyevfarid](https://github.com/afandiyevfarid)
- **Emin** - [guyFromTV](https://github.com/guyFromTV)
- **Anar** - [Anar765](https://github.com/Anar765)
- 
## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Google for design inspiration
- Thymeleaf team for the template engine
- The open-source community

---

⭐ If you find this project useful, please consider giving it a star!
