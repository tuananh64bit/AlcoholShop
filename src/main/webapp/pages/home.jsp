<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />

<jsp:include page="includes/topmenu.jsp" />

<!-- Hero Section -->
<section class="hero-section">
    <div class="container">
        <div class="row align-items-center min-vh-100">
            <div class="col-lg-6">
                <div class="hero-content">
                    <h1 class="hero-title" data-aos="fade-up">
                        Premium Alcohol Collection
                    </h1>
                    <p class="hero-subtitle" data-aos="fade-up" data-aos-delay="200">
                        Discover our curated selection of fine spirits, wines, and craft beverages. 
                        From rare whiskeys to premium wines, we have something for every connoisseur.
                    </p>
                    <div class="hero-buttons" data-aos="fade-up" data-aos-delay="400">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg me-3">
                            <i class="fas fa-store me-2"></i>Shop Now
                        </a>
                        <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-primary btn-lg">
                            <i class="fas fa-envelope me-2"></i>Contact Us
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="hero-image text-center" data-aos="fade-left" data-aos-delay="300">
                    <div class="luxury-bottle-display">
                        <img src="https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" 
                             alt="Premium Wine Collection" class="img-fluid rounded-xl shadow-gold">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Featured Products -->
<section class="py-5" style="background: var(--bg-secondary);">
    <div class="container">
        <div class="row mb-5">
            <div class="col-12 text-center">
                <h2 class="display-5 fw-bold mb-3 text-gold" data-aos="fade-up">Featured Products</h2>
                <p class="lead text-secondary" data-aos="fade-up" data-aos-delay="200">Discover our most popular and newest additions</p>
            </div>
        </div>
        
        <div class="row">
            <c:choose>
                <c:when test="${not empty featuredProducts}">
                    <c:forEach var="product" items="${featuredProducts}" varStatus="status">
                        <div class="col-lg-4 col-md-6 mb-4" data-aos="fade-up" data-aos-delay="${status.index * 100}">
                            <div class="card product-card h-100">
                                <div class="product-image">
                                    <c:choose>
                                        <c:when test="${not empty product.image}">
                                            <img src="${pageContext.request.contextPath}/static/images/products/${product.image}" 
                                                 alt="${product.name}" 
                                                 onerror="this.src='https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80'">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" 
                                                 alt="${product.name}">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="product-badge">
                                        <span class="badge bg-gold text-dark">${product.alcoholPercentage}% ABV</span>
                                    </div>
                                    <div class="product-overlay">
                                        <a href="${pageContext.request.contextPath}/product?id=${product.id}" class="btn btn-primary">
                                            <i class="fas fa-eye me-2"></i>View Details
                                        </a>
                                    </div>
                                </div>
                                
                                <div class="card-body d-flex flex-column">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text text-muted small">${product.categoryName}</p>
                                    <p class="card-text">${product.description}</p>
                                    
                                    <div class="mt-auto">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <span class="h5 text-primary mb-0">
                                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$" />
                                            </span>
                                            <span class="badge ${product.stock > 0 ? 'bg-success' : 'bg-danger'}">
                                                ${product.stock > 0 ? 'In Stock' : 'Out of Stock'}
                                            </span>
                                        </div>
                                        
                                        <div class="d-grid gap-2">
                                            <a href="${pageContext.request.contextPath}/product?id=${product.id}" 
                                               class="btn btn-outline-primary">
                                                <i class="fas fa-eye me-1"></i>View Details
                                            </a>
                                            <c:if test="${product.stock > 0}">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="productId" value="${product.id}">
                                                    <input type="hidden" name="quantity" value="1">
                                                    <button type="submit" class="btn btn-primary w-100">
                                                        <i class="fas fa-cart-plus me-1"></i>Add to Cart
                                                    </button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center">
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            No featured products available at the moment.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="row mt-4">
            <div class="col-12 text-center">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-lg">
                    <i class="fas fa-store me-2"></i>View All Products
                </a>
            </div>
        </div>
    </div>
</section>

<!-- Categories Section -->
<section class="py-5 bg-light">
    <div class="container">
        <div class="row mb-5">
            <div class="col-12 text-center">
                <h2 class="display-5 fw-bold mb-3">Shop by Category</h2>
                <p class="lead text-muted">Browse our extensive collection by category</p>
            </div>
        </div>
        
        <div class="row">
            <c:forEach var="category" items="${categories}">
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="card category-card h-100 text-center">
                        <div class="card-body">
                            <div class="category-icon mb-3">
                                <i class="fas fa-wine-bottle fa-3x text-primary"></i>
                            </div>
                            <h5 class="card-title">${category.name}</h5>
                            <p class="card-text text-muted">
                                Explore our collection of premium ${category.name.toLowerCase()}
                            </p>
                            <a href="${pageContext.request.contextPath}/products?category=${category.id}" 
                               class="btn btn-outline-primary">
                                Shop ${category.name}
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>

<!-- Statistics Section -->
<section class="py-5">
    <div class="container">
        <div class="row text-center">
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="stat-item">
                    <i class="fas fa-box fa-3x text-primary mb-3"></i>
                    <h3 class="fw-bold">${totalProducts}</h3>
                    <p class="text-muted">Products Available</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="stat-item">
                    <i class="fas fa-users fa-3x text-success mb-3"></i>
                    <h3 class="fw-bold">1000+</h3>
                    <p class="text-muted">Happy Customers</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="stat-item">
                    <i class="fas fa-shipping-fast fa-3x text-warning mb-3"></i>
                    <h3 class="fw-bold">24/7</h3>
                    <p class="text-muted">Fast Delivery</p>
                </div>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="stat-item">
                    <i class="fas fa-award fa-3x text-danger mb-3"></i>
                    <h3 class="fw-bold">Premium</h3>
                    <p class="text-muted">Quality Guarantee</p>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="includes/footer.jsp" />
