<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<footer class="bg-dark text-light py-5 mt-5">
    <div class="container">
        <div class="row">
            <!-- Company Info -->
            <div class="col-lg-4 mb-4">
                <h5 class="fw-bold mb-3">
                    <i class="fas fa-wine-bottle me-2"></i>AlcoholShop
                </h5>
                <p class="text-muted">
                    Your premium destination for fine spirits, wines, and craft beverages. 
                    We offer the finest selection of alcoholic beverages for discerning customers.
                </p>
                <div class="social-links">
                    <a href="#" class="text-light me-3"><i class="fab fa-facebook-f"></i></a>
                    <a href="#" class="text-light me-3"><i class="fab fa-twitter"></i></a>
                    <a href="#" class="text-light me-3"><i class="fab fa-instagram"></i></a>
                    <a href="#" class="text-light"><i class="fab fa-linkedin-in"></i></a>
                </div>
            </div>
            
            <!-- Quick Links -->
            <div class="col-lg-2 col-md-6 mb-4">
                <h6 class="fw-bold mb-3">Quick Links</h6>
                <ul class="list-unstyled">
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/" class="text-muted text-decoration-none">
                            <i class="fas fa-home me-1"></i>Home
                        </a>
                    </li>
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/products" class="text-muted text-decoration-none">
                            <i class="fas fa-store me-1"></i>Products
                        </a>
                    </li>
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/contact" class="text-muted text-decoration-none">
                            <i class="fas fa-envelope me-1"></i>Contact
                        </a>
                    </li>
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/about" class="text-muted text-decoration-none">
                            <i class="fas fa-info-circle me-1"></i>About Us
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Categories -->
            <div class="col-lg-2 col-md-6 mb-4">
                <h6 class="fw-bold mb-3">Categories</h6>
                <ul class="list-unstyled">
                    <c:forEach var="category" items="${categories}" begin="0" end="4">
                        <li class="mb-2">
                            <a href="${pageContext.request.contextPath}/products?category=${category.id}" 
                               class="text-muted text-decoration-none">
                                <c:out value="${category.name}" />
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            
            <!-- Contact Info -->
            <div class="col-lg-4 mb-4">
                <h6 class="fw-bold mb-3">Contact Information</h6>
                <div class="contact-info">
                    <p class="text-muted mb-2">
                        <i class="fas fa-map-marker-alt me-2"></i>
                        123 Alcohol Street, Beverage City, BC 12345
                    </p>
                    <p class="text-muted mb-2">
                        <i class="fas fa-phone me-2"></i>
                        +1 (555) 123-4567
                    </p>
                    <p class="text-muted mb-2">
                        <i class="fas fa-envelope me-2"></i>
                        info@alcoholshop.com
                    </p>
                    <p class="text-muted">
                        <i class="fas fa-clock me-2"></i>
                        Mon-Fri: 9AM-6PM, Sat: 10AM-4PM
                    </p>
                </div>
            </div>
        </div>
        
        <hr class="my-4">
        
        <!-- Bottom section -->
        <div class="row align-items-center">
            <div class="col-md-6">
                <p class="text-muted mb-0">
                    &copy; 2025 AlcoholShop. All rights reserved.
                </p>
            </div>
            <div class="col-md-6 text-md-end">
                <p class="text-muted mb-0">
                    <small>
                        <i class="fas fa-exclamation-triangle me-1"></i>
                        Must be 18+ to purchase. Please drink responsibly.
                    </small>
                </p>
            </div>
        </div>
        
        <!-- Development Team Info -->
        <div class="row mt-3">
            <div class="col-12">
                <div class="card bg-secondary">
                    <div class="card-body text-center">
                        <h6 class="card-title mb-2">
                            <i class="fas fa-code me-2"></i>Development Team
                        </h6>
                        <p class="card-text mb-1">
                            <strong>Team Members:</strong> 
                            Phùng Tuấn Anh,
                            Nguyễn Xuân Huy,
                            Nguyễn Xuân Ngọc Long
                        </p>
                        <p class="card-text mb-0">
                            <small class="text-muted">
                                <i class="fas fa-calendar me-1"></i>
                                Project completed on: <fmt:formatDate value="<%=new java.util.Date()%>" pattern="dd/MM/yyyy" />
                            </small>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- AOS Animation Library -->
<script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/static/js/scripts.js"></script>

<!-- Initialize AOS -->
<script>
    AOS.init({
        duration: 1000,
        easing: 'ease-in-out',
        once: true,
        offset: 100
    });
</script>
