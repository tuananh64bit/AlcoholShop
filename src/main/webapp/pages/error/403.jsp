<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../includes/header.jsp" />

<jsp:include page="../includes/topmenu.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6 text-center">
            <div class="error-page">
                <div class="error-icon mb-4">
                    <i class="fas fa-ban fa-5x text-danger"></i>
                </div>
                
                <h1 class="display-1 fw-bold text-gold mb-3">403</h1>
                <h2 class="h3 mb-4 text-secondary">Access Forbidden</h2>
                
                <div class="alert alert-warning mb-4">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>You don't have permission to access this resource.</strong>
                </div>
                
                <p class="lead text-muted mb-4">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser == null}">
                            You need to be logged in to access this page. Please log in with your account.
                        </c:when>
                        <c:when test="${sessionScope.currentUser.role != 'ADMIN'}">
                            This page is restricted to administrators only. You need admin privileges to access this area.
                        </c:when>
                        <c:otherwise>
                            You don't have the necessary permissions to perform this action.
                        </c:otherwise>
                    </c:choose>
                </p>
                
                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser == null}">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">
                                <i class="fas fa-sign-in-alt me-2"></i>Login
                            </a>
                        </c:when>
                        <c:when test="${sessionScope.currentUser.role != 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                                <i class="fas fa-home me-2"></i>Go Home
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary btn-lg">
                                <i class="fas fa-tachometer-alt me-2"></i>Admin Dashboard
                            </a>
                        </c:otherwise>
                    </c:choose>
                    
                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline-primary btn-lg">
                        <i class="fas fa-home me-2"></i>Home Page
                    </a>
                </div>
                
                <div class="mt-5">
                    <h5 class="text-muted">Need Help?</h5>
                    <p class="text-muted">
                        If you believe this is an error, please contact our support team.
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
    animation: bounce 2s infinite;
}

@keyframes bounce {
    0%, 20%, 50%, 80%, 100% {
        transform: translateY(0);
    }
    40% {
        transform: translateY(-10px);
    }
    60% {
        transform: translateY(-5px);
    }
}
</style>

<jsp:include page="../includes/footer.jsp" />
