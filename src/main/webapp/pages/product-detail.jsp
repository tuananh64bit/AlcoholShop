<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="./includes/header.jsp" />
<jsp:include page="./includes/topmenu.jsp" />

<!-- Product Detail Section -->
<section class="py-5">
    <div class="container">
        <c:choose>
            <c:when test="${not empty product}">
                <div class="row">
                    <!-- Product Image -->
                    <div class="col-lg-6 mb-4">
                        <div class="product-detail-image" data-aos="fade-right">
                            <div class="card bg-glass">
                                <div class="card-body p-0">
                                    <c:choose>
                                        <c:when test="${not empty product.image}">
                                            <img src="${pageContext.request.contextPath}/static/images/products/${product.image}"
                                                 class="img-fluid rounded-lg" alt="${product.name}"
                                                 onerror="this.src='https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?auto=format&fit=crop&w=600&q=80'">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?auto=format&fit=crop&w=600&q=80"
                                                 class="img-fluid rounded-lg" alt="${product.name}">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Product Info -->
                    <div class="col-lg-6" data-aos="fade-left">
                        <!-- Breadcrumb -->
                        <nav aria-label="breadcrumb" class="mb-3">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-gold">Home</a></li>
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-gold">Products</a></li>
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products?category=${product.categoryId}" class="text-gold">${product.categoryName}</a></li>
                                <li class="breadcrumb-item active text-secondary">${product.name}</li>
                            </ol>
                        </nav>

                        <h1 class="display-5 fw-bold text-primary mb-3">${product.name}</h1>
                        <p class="text-muted mb-3">
                            <i class="fas fa-barcode me-2"></i>Product Code: <strong>${product.code}</strong>
                        </p>

                        <!-- Price and Stock -->
                        <div class="d-flex align-items-center mb-4">
                            <span class="display-4 fw-bold text-gold me-4">
                                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
                            </span>
                            <c:choose>
                                <c:when test="${product.stock > 0}">
                                    <span class="badge bg-success fs-6">In Stock</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger fs-6">Out of Stock</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Product Details -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="detail-item">
                                    <i class="fas fa-wine-bottle text-gold me-2"></i>
                                    <strong>Category:</strong> ${product.categoryName}
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-item">
                                    <i class="fas fa-percentage text-gold me-2"></i>
                                    <strong>Alcohol Content:</strong> ${product.alcoholPercentage}% ABV
                                </div>
                            </div>
                        </div>

                        <!-- Description -->
                        <div class="mb-4">
                            <h5 class="text-gold mb-3">Description</h5>
                            <p class="text-secondary">${product.description}</p>
                        </div>

                        <!-- Add to Cart -->
                        <c:if test="${product.stock > 0}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="mb-4">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.id}">

                                <div class="row align-items-center">
                                    <div class="col-md-4">
                                        <label for="quantity" class="form-label text-gold">Quantity:</label>
                                        <select class="form-select" id="quantity" name="quantity">
                                            <c:forEach begin="1" end="${product.stock > 10 ? 10 : product.stock}" var="i">
                                                <option value="${i}">${i}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-8">
                                        <button type="submit" class="btn btn-primary btn-lg w-100">
                                            <i class="fas fa-cart-plus me-2"></i>Add to Cart
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </c:if>

                        <!-- Stock Warning -->
                        <c:if test="${product.stock <= 0}">
                            <div class="alert alert-danger">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                This product is currently out of stock. Please check back later.
                            </div>
                        </c:if>

                        <!-- Product Features -->
                        <div class="product-features">
                            <h5 class="text-gold mb-3">Product Features</h5>
                            <ul class="list-unstyled">
                                <li><i class="fas fa-check text-success me-2"></i>Premium Quality</li>
                                <li><i class="fas fa-check text-success me-2"></i>Authentic Origin</li>
                                <li><i class="fas fa-check text-success me-2"></i>Secure Packaging</li>
                                <li><i class="fas fa-check text-success me-2"></i>Fast Delivery</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Related Products -->
                <c:if test="${not empty relatedProducts}">
                    <div class="row mt-5">
                        <div class="col-12">
                            <h3 class="text-center text-gold mb-4" data-aos="fade-up">Related Products</h3>
                            <div class="row">
                                <c:forEach var="relatedProduct" items="${relatedProducts}" varStatus="status">
                                    <div class="col-lg-3 col-md-6 mb-4" data-aos="fade-up" data-aos-delay="${status.index * 100}">
                                        <div class="card product-card h-100">
                                            <div class="product-image">
                                                <c:choose>
                                                    <c:when test="${not empty relatedProduct.image}">
                                                        <img src="${pageContext.request.contextPath}/static/images/products/${relatedProduct.image}"
                                                             alt="${relatedProduct.name}"
                                                             onerror="this.src='https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?auto=format&fit=crop&w=300&q=80'">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?auto=format&fit=crop&w=300&q=80"
                                                             alt="${relatedProduct.name}">
                                                    </c:otherwise>
                                                </c:choose>
                                                <div class="product-badge">
                                                    <span class="badge bg-gold text-dark">${relatedProduct.alcoholPercentage}% ABV</span>
                                                </div>
                                            </div>

                                            <div class="card-body d-flex flex-column">
                                                <h6 class="card-title">${relatedProduct.name}</h6>
                                                <p class="card-text text-muted small">${relatedProduct.categoryName}</p>

                                                <div class="mt-auto">
                                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                                        <span class="h6 text-gold mb-0">
                                                            <fmt:formatNumber value="${relatedProduct.price}" type="currency" currencySymbol="$"/>
                                                        </span>
                                                        <c:choose>
                                                            <c:when test="${relatedProduct.stock > 0}">
                                                                <span class="badge bg-success">In Stock</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">Out of Stock</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>

                                                    <a href="${pageContext.request.contextPath}/product?id=${relatedProduct.id}"
                                                       class="btn btn-outline-primary btn-sm w-100">
                                                        <i class="fas fa-eye me-1"></i>View Details
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <!-- Product Not Found -->
                <div class="row">
                    <div class="col-12 text-center py-5">
                        <div class="mb-4" data-aos="fade-up">
                            <i class="fas fa-exclamation-triangle fa-4x text-muted"></i>
                        </div>
                        <h3 class="text-muted mb-3" data-aos="fade-up" data-aos-delay="200">Product Not Found</h3>
                        <p class="text-muted mb-4" data-aos="fade-up" data-aos-delay="300">
                            The product you're looking for doesn't exist or has been removed.
                        </p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" data-aos="fade-up" data-aos-delay="400">
                            <i class="fas fa-store me-2"></i>Browse Products
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<style>
    .product-detail-image img {
        width: 100%;
        height: 500px;
        object-fit: cover;
        border-radius: var(--radius-lg);
    }

    .detail-item {
        padding: 0.5rem 0;
        border-bottom: 1px solid rgba(255,255,255,0.1);
    }

    .product-features ul li {
        padding: 0.25rem 0;
    }

    .breadcrumb {
        background: transparent;
        padding: 0;
    }

    .breadcrumb-item + .breadcrumb-item::before {
        content: ">";
        color: var(--text-muted);
    }
</style>

<jsp:include page="./includes/footer.jsp" />
