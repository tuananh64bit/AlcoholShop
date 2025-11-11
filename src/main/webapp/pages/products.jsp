<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />

<jsp:include page="includes/topmenu.jsp" />

<div class="container py-5">
    <!-- Page Header -->
    <div class="row mb-4">
        <div class="col-12">
            <h1 class="display-5 fw-bold">
                <c:choose>
                    <c:when test="${not empty pageTitle}">
                        <c:out value="${pageTitle}" />
                    </c:when>
                    <c:otherwise>
                        All Products
                    </c:otherwise>
                </c:choose>
            </h1>
            <p class="lead text-muted">
                <c:choose>
                    <c:when test="${not empty searchKeyword}">
                        Search results for: <strong>"${searchKeyword}"</strong>
                    </c:when>
                    <c:when test="${not empty selectedCategory}">
                        Browse our collection of ${selectedCategory.name}
                    </c:when>
                    <c:otherwise>
                        Discover our premium collection of alcoholic beverages
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
    </div>
    
    <!-- Filters and Search -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="card">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/products" class="row g-3">
                        <div class="col-md-4">
                            <label for="category" class="form-label">Filter by Category</label>
                            <select class="form-select" id="category" name="category">
                                <option value="">All Categories</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}" 
                                            ${param.category == category.id ? 'selected' : ''}>
                                        ${category.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="search" class="form-label">Search Products</label>
                            <input type="text" class="form-control" id="search" name="search" 
                                   placeholder="Search by name or description..." 
                                   value="${param.search}">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">&nbsp;</label>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Search
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Products Grid -->
    <div class="row">
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach var="product" items="${products}">
                    <div class="col-lg-4 col-md-6 mb-4">
                        <div class="card product-card h-100 shadow-sm">
                            <div class="card-img-top-container">
                                <c:choose>
                                    <c:when test="${not empty product.image}">
                                        <img src="${pageContext.request.contextPath}/static/images/products/${product.image}" 
                                             class="card-img-top" alt="${product.name}" 
                                             onerror="this.src='${pageContext.request.contextPath}/static/images/placeholder.jpg'">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/static/images/placeholder.jpg" 
                                             class="card-img-top" alt="${product.name}">
                                    </c:otherwise>
                                </c:choose>
                                <div class="product-badge">
                                    <span class="badge bg-primary">${product.alcoholPercentage}% ABV</span>
                                </div>
                                <c:if test="${product.stock <= 0}">
                                    <div class="product-overlay">
                                        <span class="badge bg-danger fs-6">Out of Stock</span>
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="card-body d-flex flex-column">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <h5 class="card-title">${product.name}</h5>
                                    <span class="badge bg-secondary">${product.categoryName}</span>
                                </div>
                                
                                <p class="card-text text-muted small">${product.code}</p>
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
                <div class="col-12">
                    <div class="text-center py-5">
                        <i class="fas fa-search fa-4x text-muted mb-4"></i>
                        <h3 class="text-muted">No Products Found</h3>
                        <p class="text-muted">
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    No products found matching "${searchKeyword}". Try a different search term.
                                </c:when>
                                <c:otherwise>
                                    No products are available at the moment.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                            <i class="fas fa-store me-2"></i>View All Products
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Results Summary -->
    <c:if test="${not empty products}">
        <div class="row mt-4">
            <div class="col-12">
                <div class="alert alert-info">
                    <i class="fas fa-info-circle me-2"></i>
                    Showing ${products.size()} product(s)
                    <c:if test="${not empty searchKeyword}">
                        for search term: <strong>"${searchKeyword}"</strong>
                    </c:if>
                    <c:if test="${not empty selectedCategory}">
                        in category: <strong>${selectedCategory.name}</strong>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>
</div>

<style>
.product-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 15px 15px 0 0;
}

.product-overlay .badge {
    font-size: 1.2rem;
    padding: 0.5rem 1rem;
}
</style>

<jsp:include page="includes/footer.jsp" />
