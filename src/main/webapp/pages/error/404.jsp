<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../includes/header.jsp" />

<jsp:include page="../includes/topmenu.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6 text-center">
            <div class="error-page">
                <div class="error-icon mb-4">
                    <i class="fas fa-search fa-5x text-warning"></i>
                </div>
                
                <h1 class="display-1 fw-bold text-gold mb-3">404</h1>
                <h2 class="h3 mb-4 text-secondary">Page Not Found</h2>
                
                <div class="alert alert-info mb-4">
                    <i class="fas fa-info-circle me-2"></i>
                    <strong>The page you're looking for doesn't exist.</strong>
                </div>
                
                <p class="lead text-muted mb-4">
                    The page you requested could not be found. It might have been moved, deleted, 
                    or you might have entered the wrong URL.
                </p>
                
                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg">
                        <i class="fas fa-home me-2"></i>Go Home
                    </a>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary btn-lg">
                        <i class="fas fa-store me-2"></i>Browse Products
                    </a>
                </div>
                
                <div class="mt-5">
                    <h5 class="text-muted">Popular Pages</h5>
                    <div class="row mt-3">
                        <div class="col-md-4 mb-2">
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-sm w-100">
                                <i class="fas fa-store me-1"></i>All Products
                            </a>
                        </div>
                        <div class="col-md-4 mb-2">
                            <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-secondary btn-sm w-100">
                                <i class="fas fa-envelope me-1"></i>Contact Us
                            </a>
                        </div>
                        <div class="col-md-4 mb-2">
                            <c:choose>
                                <c:when test="${sessionScope.currentUser != null}">
                                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary btn-sm w-100">
                                        <i class="fas fa-shopping-cart me-1"></i>My Cart
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary btn-sm w-100">
                                        <i class="fas fa-sign-in-alt me-1"></i>Login
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                
                <div class="mt-4">
                    <h5 class="text-muted">Need Help?</h5>
                    <p class="text-muted">
                        If you continue to have problems, please contact our support team.
                    </p>
                    <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-secondary">
                        <i class="fas fa-envelope me-2"></i>Contact Support
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.error-page {
    padding: 3rem 0;
}

.error-icon {
    animation: pulse 2s infinite;
}

@keyframes pulse {
    0% {
        transform: scale(1);
    }
    50% {
        transform: scale(1.05);
    }
    100% {
        transform: scale(1);
    }
}
</style>

<jsp:include page="../includes/footer.jsp" />

