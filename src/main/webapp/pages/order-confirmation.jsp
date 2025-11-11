<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />

<jsp:include page="includes/topmenu.jsp" />

<!-- Order Confirmation Section -->
<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="text-center mb-5">
                    <div class="mb-4" data-aos="fade-up">
                        <i class="fas fa-check-circle fa-5x text-success"></i>
                    </div>
                    <h1 class="display-4 fw-bold text-gold mb-3" data-aos="fade-up" data-aos-delay="200">
                        Order Confirmed!
                    </h1>
                    <p class="lead text-secondary" data-aos="fade-up" data-aos-delay="300">
                        Thank you for your purchase. Your order has been successfully placed.
                    </p>
                </div>
                
                <div class="card bg-glass mb-4">
                    <div class="card-header">
                        <h5 class="mb-0 text-gold">
                            <i class="fas fa-receipt me-2"></i>Order Details
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-gold">Order Number</h6>
                                <p class="text-secondary">#${param.orderId}</p>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-gold">Order Date</h6>
                                <p class="text-secondary">
                                    <fmt:formatDate value="<%=new java.util.Date()%>" pattern="MMMM dd, yyyy 'at' HH:mm" />
                                </p>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-gold">Status</h6>
                                <span class="badge bg-warning">Pending</span>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-gold">Estimated Delivery</h6>
                                <p class="text-secondary">3-5 business days</p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card bg-glass mb-4">
                    <div class="card-header">
                        <h5 class="mb-0 text-gold">
                            <i class="fas fa-truck me-2"></i>Shipping Information
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-gold">Shipping Address</h6>
                                <p class="text-secondary">
                                    <c:out value="${sessionScope.currentUser.fullname}" /><br>
                                    <c:out value="${sessionScope.currentUser.address}" />
                                </p>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-gold">Contact Information</h6>
                                <p class="text-secondary">
                                    <c:out value="${sessionScope.currentUser.email}" /><br>
                                    Phone: [Your phone number]
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card bg-glass mb-4">
                    <div class="card-header">
                        <h5 class="mb-0 text-gold">
                            <i class="fas fa-info-circle me-2"></i>Important Information
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-warning">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <strong>Age Verification Required:</strong> 
                            A valid ID showing you are 18+ years old will be required upon delivery.
                        </div>
                        
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            <strong>Delivery Instructions:</strong> 
                            Please ensure someone 18+ is available to receive the package and provide ID verification.
                        </div>
                    </div>
                </div>
                
                <div class="text-center">
                    <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg">
                            <i class="fas fa-home me-2"></i>Continue Shopping
                        </a>
                        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-primary btn-lg">
                            <i class="fas fa-list me-2"></i>View My Orders
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="includes/footer.jsp" />
