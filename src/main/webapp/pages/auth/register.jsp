<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../includes/header.jsp" />

<jsp:include page="../includes/topmenu.jsp" />

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card bg-glass shadow-lg border-0">
                <div class="card-header text-center py-4" style="background: var(--gradient-gold);">
                    <h3 class="mb-0 text-dark">
                        <i class="fas fa-user-plus me-2"></i>Create Account
                    </h3>
                    <p class="mb-0 mt-2 text-dark">Join AlcoholShop today</p>
                </div>
                
                <div class="card-body p-4">
                    <!-- Error Message -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            <c:out value="${error}" />
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    
                    <!-- Registration Form -->
                    <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="username" class="form-label">
                                    <i class="fas fa-user me-1"></i>Username *
                                </label>
                                <input type="text" class="form-control" id="username" name="username" 
                                       value="${username}" required>
                                <c:if test="${not empty errors.username}">
                                    <div class="text-danger small mt-1">
                                        <i class="fas fa-exclamation-triangle me-1"></i>
                                        <c:out value="${errors.username}" />
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="fullname" class="form-label">
                                    <i class="fas fa-id-card me-1"></i>Full Name *
                                </label>
                                <input type="text" class="form-control" id="fullname" name="fullname" 
                                       value="${fullname}" required>
                                <c:if test="${not empty errors.fullname}">
                                    <div class="text-danger small mt-1">
                                        <i class="fas fa-exclamation-triangle me-1"></i>
                                        <c:out value="${errors.fullname}" />
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="fas fa-envelope me-1"></i>Email Address *
                            </label>
                            <input type="email" class="form-control" id="email" name="email" 
                                   value="${email}" required>
                            <c:if test="${not empty errors.email}">
                                <div class="text-danger small mt-1">
                                    <i class="fas fa-exclamation-triangle me-1"></i>
                                    <c:out value="${errors.email}" />
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="password" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Password *
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="password" name="password" required>
                                    <button class="btn btn-outline-secondary" type="button" id="togglePassword">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                                <small class="form-text text-muted">Minimum 8 characters</small>
                                <c:if test="${not empty errors.password}">
                                    <div class="text-danger small mt-1">
                                        <i class="fas fa-exclamation-triangle me-1"></i>
                                        <c:out value="${errors.password}" />
                                    </div>
                                </c:if>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="confirmPassword" class="form-label">
                                    <i class="fas fa-lock me-1"></i>Confirm Password *
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                    <button class="btn btn-outline-secondary" type="button" id="toggleConfirmPassword">
                                        <i class="fas fa-eye"></i>
                                    </button>
                                </div>
                                <c:if test="${not empty errors.confirmPassword}">
                                    <div class="text-danger small mt-1">
                                        <i class="fas fa-exclamation-triangle me-1"></i>
                                        <c:out value="${errors.confirmPassword}" />
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="birthDate" class="form-label">
                                <i class="fas fa-calendar me-1"></i>Birth Date *
                            </label>
                            <input type="date" class="form-control" id="birthDate" name="birthDate" required>
                            <small class="form-text text-muted">You must be 18 or older to register</small>
                            <c:if test="${not empty errors.birthDate}">
                                <div class="text-danger small mt-1">
                                    <i class="fas fa-exclamation-triangle me-1"></i>
                                    <c:out value="${errors.birthDate}" />
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="mb-3">
                            <label for="address" class="form-label">
                                <i class="fas fa-map-marker-alt me-1"></i>Address *
                            </label>
                            <textarea class="form-control" id="address" name="address" rows="3" 
                                      placeholder="Enter your full address" required>${address}</textarea>
                            <c:if test="${not empty errors.address}">
                                <div class="text-danger small mt-1">
                                    <i class="fas fa-exclamation-triangle me-1"></i>
                                    <c:out value="${errors.address}" />
                                </div>
                            </c:if>
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="agreeTerms" required>
                            <label class="form-check-label" for="agreeTerms">
                                I agree to the <a href="#" class="text-primary">Terms of Service</a> and 
                                <a href="#" class="text-primary">Privacy Policy</a>
                            </label>
                        </div>
                        
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="ageConfirm" required>
                            <label class="form-check-label" for="ageConfirm">
                                I confirm that I am 18 years of age or older
                            </label>
                        </div>
                        
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-user-plus me-2"></i>Create Account
                            </button>
                        </div>
                    </form>
                    
                    <hr class="my-4">
                    
                    <!-- Login Link -->
                    <div class="text-center">
                        <p class="mb-0">Already have an account?</p>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary">
                            <i class="fas fa-sign-in-alt me-2"></i>Login Here
                        </a>
                    </div>
                </div>
            </div>
            
            <!-- Age Requirement Notice -->
            <div class="card mt-4 border-warning">
                <div class="card-header bg-warning text-dark">
                    <h6 class="mb-0">
                        <i class="fas fa-exclamation-triangle me-2"></i>Age Requirement
                    </h6>
                </div>
                <div class="card-body">
                    <p class="mb-0">
                        <strong>Important:</strong> You must be at least 18 years old to create an account and make purchases. 
                        We reserve the right to verify your age and may request identification.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Toggle password visibility
function togglePasswordVisibility(inputId, buttonId) {
    const passwordField = document.getElementById(inputId);
    const toggleIcon = document.getElementById(buttonId).querySelector('i');
    
    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        toggleIcon.classList.remove('fa-eye');
        toggleIcon.classList.add('fa-eye-slash');
    } else {
        passwordField.type = 'password';
        toggleIcon.classList.remove('fa-eye-slash');
        toggleIcon.classList.add('fa-eye');
    }
}

document.getElementById('togglePassword').addEventListener('click', function() {
    togglePasswordVisibility('password', 'togglePassword');
});

document.getElementById('toggleConfirmPassword').addEventListener('click', function() {
    togglePasswordVisibility('confirmPassword', 'toggleConfirmPassword');
});

// Form validation
document.getElementById('registerForm').addEventListener('submit', function(e) {
    const username = document.getElementById('username').value.trim();
    const fullname = document.getElementById('fullname').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const birthDate = document.getElementById('birthDate').value;
    const address = document.getElementById('address').value.trim();
    const agreeTerms = document.getElementById('agreeTerms').checked;
    const ageConfirm = document.getElementById('ageConfirm').checked;
    
    // Basic validation
    if (!username || username.length < 3) {
        e.preventDefault();
        alert('Username must be at least 3 characters long');
        return;
    }
    
    if (!fullname || fullname.length < 2) {
        e.preventDefault();
        alert('Full name must be at least 2 characters long');
        return;
    }
    
    if (!email || !email.includes('@')) {
        e.preventDefault();
        alert('Please enter a valid email address');
        return;
    }
    
    if (!password || password.length < 8) {
        e.preventDefault();
        alert('Password must be at least 8 characters long');
        return;
    }
    
    if (password !== confirmPassword) {
        e.preventDefault();
        alert('Passwords do not match');
        return;
    }
    
    if (!birthDate) {
        e.preventDefault();
        alert('Please enter your birth date');
        return;
    }
    
    // Check age
    const today = new Date();
    const birth = new Date(birthDate);
    const age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    if (age < 18) {
        e.preventDefault();
        alert('You must be at least 18 years old to register');
        return;
    }
    
    if (!address || address.length < 10) {
        e.preventDefault();
        alert('Please enter a complete address');
        return;
    }
    
    if (!agreeTerms) {
        e.preventDefault();
        alert('You must agree to the Terms of Service and Privacy Policy');
        return;
    }
    
    if (!ageConfirm) {
        e.preventDefault();
        alert('You must confirm that you are 18 years of age or older');
        return;
    }
});

// Real-time password confirmation
document.getElementById('confirmPassword').addEventListener('input', function() {
    const password = document.getElementById('password').value;
    const confirmPassword = this.value;
    
    if (confirmPassword && password !== confirmPassword) {
        this.setCustomValidity('Passwords do not match');
    } else {
        this.setCustomValidity('');
    }
});
</script>

<jsp:include page="../includes/footer.jsp" />
