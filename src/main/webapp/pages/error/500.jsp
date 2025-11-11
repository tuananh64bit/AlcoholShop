<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../includes/header.jsp" />

<jsp:include page="../includes/topmenu.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6 text-center">
            <div class="error-page">
                <div class="error-icon mb-4">
                    <i class="fas fa-exclamation-triangle fa-5x text-danger"></i>
                </div>
                
                <h1 class="display-1 fw-bold text-gold mb-3">500</h1>
                <h2 class="h3 mb-4 text-secondary">Internal Server Error</h2>
                
                <div class="alert alert-danger mb-4">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>Something went wrong on our end.</strong>
                </div>
                
                <p class="lead text-muted mb-4">
                    We're experiencing some technical difficulties. Our team has been notified 
                    and is working to fix the issue. Please try again later.
                </p>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-warning mb-4">
                        <strong>Error Details:</strong><br>
                        <code><c:out value="${error}" /></code>
                    </div>
                </c:if>
                
                <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                        <i class="fas fa-home me-2"></i>Go Home
                    </a>
                    <button onclick="window.location.reload()" class="btn btn-outline-primary btn-lg">
                        <i class="fas fa-redo me-2"></i>Try Again
                    </button>
                </div>
                
                <div class="mt-5">
                    <h5 class="text-muted">What can you do?</h5>
                    <div class="row mt-3">
                        <div class="col-md-4 mb-2">
                            <button onclick="window.location.reload()" class="btn btn-outline-secondary btn-sm w-100">
                                <i class="fas fa-redo me-1"></i>Refresh Page
                            </button>
                        </div>
                        <div class="col-md-4 mb-2">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary btn-sm w-100">
                                <i class="fas fa-home me-1"></i>Go Home
                            </a>
                        </div>
                        <div class="col-md-4 mb-2">
                            <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline-secondary btn-sm w-100">
                                <i class="fas fa-envelope me-1"></i>Report Issue
                            </a>
                        </div>
                    </div>
                </div>
                
                <div class="mt-4">
                    <h5 class="text-muted">Need Immediate Help?</h5>
                    <p class="text-muted">
                        If this problem persists, please contact our support team with details about what you were trying to do.
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
    animation: shake 0.5s ease-in-out infinite alternate;
}

@keyframes shake {
    0% {
        transform: translateX(0);
    }
    100% {
        transform: translateX(5px);
    }
}
</style>

<jsp:include page="../includes/footer.jsp" />

