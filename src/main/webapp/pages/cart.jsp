<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />

<jsp:include page="includes/topmenu.jsp" />

<!-- Cart Section -->
<section class="py-5">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h1 class="display-4 fw-bold text-gold mb-4" data-aos="fade-up">
                    <i class="fas fa-shopping-cart me-3"></i>Shopping Cart
                </h1>
            </div>
        </div>
        
        <c:choose>
            <c:when test="${not empty cartItems && cartItems.size() > 0}">
                <div class="row">
                    <!-- Cart Items -->
                    <div class="col-lg-8">
                        <div class="card bg-glass mb-4">
                            <div class="card-header">
                                <h5 class="mb-0 text-gold">
                                    <i class="fas fa-box me-2"></i>Your Items (${itemCount})
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:forEach var="item" items="${cartItems}">
                                    <div class="cart-item border-bottom p-4" data-aos="fade-up">
                                        <div class="row align-items-center">
                                            <div class="col-md-2">
                                                <c:set var="imgSrc" value="${pageContext.request.contextPath}/static/images/products/${item.productImage}" />
                                                <img src="${imgSrc}"
                                                     alt="${item.productName}" class="img-fluid rounded"
                                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/static/images/placeholder.jpg'" />
                                            </div>
                                            <div class="col-md-4">
                                                <h6 class="mb-1">${item.productName}</h6>
                                                <p class="text-muted small mb-0">${item.productCode}</p>
                                            </div>
                                            <div class="col-md-2">
                                                <span class="text-gold fw-bold">${item.formattedProductPrice}</span>
                                            </div>
                                            <div class="col-md-2">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <select name="quantity" class="form-select form-select-sm" onchange="this.form.submit()">
                                                        <c:forEach begin="1" end="10" var="i">
                                                            <option value="${i}" ${i == item.quantity ? 'selected' : ''}>${i}</option>
                                                        </c:forEach>
                                                    </select>
                                                </form>
                                            </div>
                                            <div class="col-md-2">
                                                <span class="text-gold fw-bold">${item.formattedSubtotal}</span>
                                            </div>
                                        </div>
                                        <div class="row mt-2">
                                            <div class="col-12 text-end">
                                                <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                                        <i class="fas fa-trash me-1"></i>Remove
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <!-- Continue Shopping -->
                        <div class="text-center">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary">
                                <i class="fas fa-arrow-left me-2"></i>Continue Shopping
                            </a>
                        </div>
                    </div>
                    
                    <!-- Order Summary -->
                    <div class="col-lg-4">
                        <div class="card bg-glass sticky-top" style="top: 2rem;">
                            <div class="card-header">
                                <h5 class="mb-0 text-gold">
                                    <i class="fas fa-receipt me-2"></i>Order Summary
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Subtotal:</span>
                                    <span class="text-gold fw-bold">
                                        <fmt:formatNumber value="${total}" type="currency" currencySymbol="$" />
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tax (10%):</span>
                                    <span class="text-gold fw-bold">
                                        <fmt:formatNumber value="${total * 0.1}" type="currency" currencySymbol="$" />
                                    </span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="fw-bold">Total:</span>
                                    <span class="text-gold fw-bold fs-5">
                                        <fmt:formatNumber value="${total * 1.1}" type="currency" currencySymbol="$" />
                                    </span>
                                </div>
                                
                                <div class="d-grid gap-2">
                                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg">
                                        <i class="fas fa-credit-card me-2"></i>Proceed to Checkout
                                    </a>
                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="clear">
                                        <button type="submit" class="btn btn-outline-danger w-100" 
                                                onclick="return confirm('Are you sure you want to clear your cart?')">
                                            <i class="fas fa-trash me-2"></i>Clear Cart
                                        </button>
                                    </form>
                                </div>
                                
                                <div class="mt-3">
                                    <small class="text-muted">
                                        <i class="fas fa-shield-alt me-1"></i>
                                        Secure checkout with SSL encryption
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Empty Cart -->
                <div class="row">
                    <div class="col-12 text-center py-5">
                        <div class="mb-4" data-aos="fade-up">
                            <i class="fas fa-shopping-cart fa-4x text-muted"></i>
                        </div>
                        <h3 class="text-muted mb-3" data-aos="fade-up" data-aos-delay="200">Your Cart is Empty</h3>
                        <p class="text-muted mb-4" data-aos="fade-up" data-aos-delay="300">
                            Looks like you haven't added any items to your cart yet.
                        </p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg" data-aos="fade-up" data-aos-delay="400">
                            <i class="fas fa-store me-2"></i>Start Shopping
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<style>
.cart-item {
    transition: var(--transition-normal);
}

.cart-item:hover {
    background: rgba(212, 175, 55, 0.05);
}

.sticky-top {
    position: sticky;
    top: 2rem;
}
</style>

<jsp:include page="includes/footer.jsp" />
