<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/topmenu.jsp" />

<!-- Checkout Section -->
<section class="py-5">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h1 class="display-4 fw-bold text-gold mb-4" data-aos="fade-up">
                    <i class="fas fa-credit-card me-3"></i>Checkout
                </h1>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty cartItems && cartItems.size() > 0}">
                <div class="row">
                    <!-- Checkout Form -->
                    <div class="col-lg-8">
                        <div class="card bg-glass mb-4">
                            <div class="card-header">
                                <h5 class="mb-0 text-gold">
                                    <i class="fas fa-user me-2"></i>Shipping Information
                                </h5>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/checkout" method="post" id="checkoutForm">
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="fullName" class="form-label text-gold">Full Name *</label>
                                            <input type="text" class="form-control" id="fullName" name="fullName"
                                                   value="${user.fullname}" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="email" class="form-label text-gold">Email Address *</label>
                                            <input type="email" class="form-control" id="email" name="email"
                                                   value="${user.email}" required>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="address" class="form-label text-gold">Shipping Address *</label>
                                        <textarea class="form-control" id="address" name="address" rows="3" required>${user.address}</textarea>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="phone" class="form-label text-gold">Phone Number *</label>
                                            <input type="tel" class="form-control" id="phone" name="phone" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="city" class="form-label text-gold">City *</label>
                                            <input type="text" class="form-control" id="city" name="city" required>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="state" class="form-label text-gold">State/Province *</label>
                                            <input type="text" class="form-control" id="state" name="state" required>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="zipCode" class="form-label text-gold">ZIP/Postal Code *</label>
                                            <input type="text" class="form-control" id="zipCode" name="zipCode" required>
                                        </div>
                                    </div>

                                    <hr class="my-4">

                                    <h5 class="text-gold mb-3">
                                        <i class="fas fa-credit-card me-2"></i>Payment Method
                                    </h5>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="paymentMethod"
                                                       id="creditCard" value="creditCard" checked>
                                                <label class="form-check-label text-gold" for="creditCard">
                                                    <i class="fas fa-credit-card me-2"></i>Credit Card
                                                </label>
                                            </div>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="paymentMethod"
                                                       id="cod" value="cod">
                                                <label class="form-check-label text-gold" for="cod">
                                                    <i class="fas fa-money-bill me-2"></i>Cash on Delivery
                                                </label>
                                            </div>
                                        </div>
                                    </div>

                                    <div id="creditCardFields">
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="cardNumber" class="form-label text-gold">Card Number *</label>
                                                <input type="text" class="form-control" id="cardNumber" name="cardNumber"
                                                       placeholder="1234 5678 9012 3456">
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <label for="expiryDate" class="form-label text-gold">Expiry Date *</label>
                                                <input type="text" class="form-control" id="expiryDate" name="expiryDate"
                                                       placeholder="MM/YY">
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <label for="cvv" class="form-label text-gold">CVV *</label>
                                                <input type="text" class="form-control" id="cvv" name="cvv"
                                                       placeholder="123">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-check mb-4">
                                        <input class="form-check-input" type="checkbox" id="ageConfirm" required>
                                        <label class="form-check-label text-gold" for="ageConfirm">
                                            I confirm that I am 18 years of age or older and eligible to purchase alcohol
                                        </label>
                                    </div>

                                    <div class="form-check mb-4">
                                        <input class="form-check-input" type="checkbox" id="termsAgree" required>
                                        <label class="form-check-label text-gold" for="termsAgree">
                                            I agree to the <a href="#" class="text-gold">Terms and Conditions</a>
                                        </label>
                                    </div>

                                    <div class="d-grid">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-lock me-2"></i>Complete Order
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Order Summary -->
                    <div class="col-lg-4">
                        <div class="card bg-glass" style="top: 2rem;">
                            <div class="card-header">
                                <h5 class="mb-0 text-gold">
                                    <i class="fas fa-receipt me-2"></i>Order Summary
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <c:forEach var="item" items="${cartItems}">
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <div>
                                                <small class="text-secondary">${item.productName}</small><br>
                                                <small class="text-muted">Qty: ${item.quantity}</small>
                                            </div>
                                            <span class="text-gold fw-bold">${item.formattedSubtotal}</span>
                                        </div>
                                    </c:forEach>
                                </div>

                                <hr>
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
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Shipping:</span>
                                    <span class="text-success fw-bold">FREE</span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between mb-3">
                                    <span class="fw-bold">Total:</span>
                                    <span class="text-gold fw-bold fs-5">
                                        <fmt:formatNumber value="${total * 1.1}" type="currency" currencySymbol="$" />
                                    </span>
                                </div>

                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle me-2"></i>
                                    <small><strong>Age Verification Required:</strong> You must be 18+ to complete this purchase.</small>
                                </div>

                                <div class="mt-3">
                                    <small class="text-muted">
                                        <i class="fas fa-shield-alt me-1"></i> Secure checkout with SSL encryption
                                    </small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row">
                    <div class="col-12 text-center py-5">
                        <div class="mb-4" data-aos="fade-up">
                            <i class="fas fa-shopping-cart fa-4x text-muted"></i>
                        </div>
                        <h3 class="text-muted mb-3" data-aos="fade-up">Your Cart is Empty</h3>
                        <p class="text-muted mb-4">You need to add items to your cart before checkout.</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">
                            <i class="fas fa-store me-2"></i>Start Shopping
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const paymentRadios = document.querySelectorAll('input[name="paymentMethod"]');
        const creditCardFields = document.getElementById('creditCardFields');
        const checkoutForm = document.getElementById('checkoutForm');

        if (paymentRadios.length > 0 && creditCardFields) {
            paymentRadios.forEach(radio => {
                radio.addEventListener('change', function() {
                    creditCardFields.style.display = (this.value === 'creditCard') ? 'block' : 'none';
                });
            });
        }

        if (checkoutForm) {
            checkoutForm.addEventListener('submit', function(e) {
                const ageConfirm = document.getElementById('ageConfirm');
                const termsAgree = document.getElementById('termsAgree');

                if (!ageConfirm?.checked) {
                    e.preventDefault();
                    alert('You must confirm that you are 18 or older.');
                    return;
                }

                if (!termsAgree?.checked) {
                    e.preventDefault();
                    alert('You must agree to the Terms and Conditions.');
                    return;
                }

                const submitBtn = this.querySelector('button[type="submit"]');
                if (submitBtn) {
                    const originalText = submitBtn.innerHTML;
                    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';
                    submitBtn.disabled = true;

                    setTimeout(() => {
                        submitBtn.innerHTML = originalText;
                        submitBtn.disabled = false;
                    }, 3000);
                }
            });
        }
    });
</script>

<style>
    #creditCardFields {
        display: block;
    }
    .bg-glass {
        backdrop-filter: blur(10px);
    }
</style>

<jsp:include page="includes/footer.jsp" />
