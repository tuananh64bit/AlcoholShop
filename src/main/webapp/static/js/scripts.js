// AlcoholShop JavaScript Functions

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

/**
 * Initialize application
 */
function initializeApp() {
    setupFormValidation();
    setupCartFunctions();
    setupAgeVerification();
    setupTooltips();
}

/**
 * Setup form validation
 */
function setupFormValidation() {
    // Real-time validation for registration form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirmPassword');
        
        if (password && confirmPassword) {
            confirmPassword.addEventListener('input', function() {
                if (this.value && password.value !== this.value) {
                    this.setCustomValidity('Passwords do not match');
                } else {
                    this.setCustomValidity('');
                }
            });
        }
    }
    
    // Contact form character counter
    const messageField = document.getElementById('message');
    if (messageField) {
        messageField.addEventListener('input', function() {
            const length = this.value.length;
            const maxLength = 1000;
            
            if (length > maxLength) {
                this.value = this.value.substring(0, maxLength);
            }
            
            // Update character count display if exists
            const counter = document.getElementById('charCounter');
            if (counter) {
                counter.textContent = `${length}/${maxLength}`;
            }
        });
    }
}

/**
 * Setup cart functions
 */
function setupCartFunctions() {
    // Add to cart buttons
    const addToCartButtons = document.querySelectorAll('[data-action="add-to-cart"]');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            addToCart(this);
        });
    });
    
    // Update cart quantity
    const quantityInputs = document.querySelectorAll('.cart-quantity');
    quantityInputs.forEach(input => {
        input.addEventListener('change', function() {
            updateCartQuantity(this);
        });
    });
    
    // Remove from cart
    const removeButtons = document.querySelectorAll('[data-action="remove-from-cart"]');
    removeButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            removeFromCart(this);
        });
    });
}

/**
 * Add product to cart
 */
function addToCart(button) {
    const productId = button.dataset.productId;
    const quantity = button.dataset.quantity || 1;
    
    if (!productId) {
        showAlert('Product ID not found', 'error');
        return;
    }
    
    // Show loading state
    const originalText = button.innerHTML;
    button.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Adding...';
    button.disabled = true;
    
    // Create form data
    const formData = new FormData();
    formData.append('action', 'add');
    formData.append('productId', productId);
    formData.append('quantity', quantity);
    
    // Submit request
    fetch('/AlcoholShop/cart', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            showAlert('Product added to cart!', 'success');
            updateCartBadge();
        } else {
            showAlert('Failed to add product to cart', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('An error occurred', 'error');
    })
    .finally(() => {
        // Restore button state
        button.innerHTML = originalText;
        button.disabled = false;
    });
}

/**
 * Update cart quantity
 */
function updateCartQuantity(input) {
    const productId = input.dataset.productId;
    const quantity = input.value;
    
    if (!productId || quantity < 0) {
        return;
    }
    
    // Create form data
    const formData = new FormData();
    formData.append('action', 'update');
    formData.append('productId', productId);
    formData.append('quantity', quantity);
    
    // Submit request
    fetch('/AlcoholShop/cart', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            location.reload(); // Reload to show updated totals
        } else {
            showAlert('Failed to update cart', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('An error occurred', 'error');
    });
}

/**
 * Remove product from cart
 */
function removeFromCart(button) {
    const productId = button.dataset.productId;
    
    if (!productId) {
        showAlert('Product ID not found', 'error');
        return;
    }
    
    if (!confirm('Are you sure you want to remove this item from your cart?')) {
        return;
    }
    
    // Create form data
    const formData = new FormData();
    formData.append('action', 'remove');
    formData.append('productId', productId);
    
    // Submit request
    fetch('/AlcoholShop/cart', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            location.reload(); // Reload to show updated cart
        } else {
            showAlert('Failed to remove item from cart', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('An error occurred', 'error');
    });
}

/**
 * Update cart badge
 */
function updateCartBadge() {
    // This would typically be updated via AJAX
    // For now, we'll just show a visual feedback
    const badge = document.querySelector('.cart-badge');
    if (badge) {
        badge.classList.add('animate__animated', 'animate__pulse');
        setTimeout(() => {
            badge.classList.remove('animate__animated', 'animate__pulse');
        }, 1000);
    }
}

/**
 * Setup age verification
 */
function setupAgeVerification() {
    // Check if age verification is needed
    if (!localStorage.getItem('ageVerified')) {
        showAgeVerificationModal();
    }
}

/**
 * Show age verification modal
 */
function showAgeVerificationModal() {
    const modal = document.createElement('div');
    modal.className = 'age-verification-modal';
    modal.innerHTML = `
        <div class="age-verification-content">
            <div class="age-verification-header">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Age Verification Required</h3>
            </div>
            <div class="age-verification-body">
                <p>You must be at least 18 years old to access this website.</p>
                <p>By continuing, you confirm that you are of legal drinking age in your jurisdiction.</p>
                <p><strong>Please drink responsibly and in accordance with local laws.</strong></p>
            </div>
            <div class="age-verification-footer">
                <button type="button" class="btn btn-danger" onclick="exitSite()">I am under 18</button>
                <button type="button" class="btn btn-success" onclick="confirmAge()">I am 18 or older</button>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    document.body.style.overflow = 'hidden';
}

/**
 * Confirm age verification
 */
function confirmAge() {
    localStorage.setItem('ageVerified', 'true');
    const modal = document.querySelector('.age-verification-modal');
    if (modal) {
        modal.remove();
    }
    document.body.style.overflow = '';
}

/**
 * Exit site (for underage users)
 */
function exitSite() {
    window.location.href = 'https://www.google.com';
}

/**
 * Setup tooltips
 */
function setupTooltips() {
    // Initialize Bootstrap tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Show alert message
 */
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alertContainer') || createAlertContainer();
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        <i class="fas fa-${getAlertIcon(type)} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alert);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (alert.parentNode) {
            alert.remove();
        }
    }, 5000);
}

/**
 * Create alert container
 */
function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alertContainer';
    container.className = 'position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}

/**
 * Get alert icon based on type
 */
function getAlertIcon(type) {
    const icons = {
        'success': 'check-circle',
        'error': 'exclamation-circle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate age from date
 */
function calculateAge(birthDate) {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    return age;
}

/**
 * Check if user is 18 or older
 */
function isAdult(birthDate) {
    return calculateAge(birthDate) >= 18;
}

/**
 * Toggle password visibility
 */
function togglePasswordVisibility(inputId, buttonId) {
    const input = document.getElementById(inputId);
    const button = document.getElementById(buttonId);
    const icon = button.querySelector('i');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

/**
 * Debounce function for search
 */
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

/**
 * Setup search functionality
 */
function setupSearch() {
    const searchInput = document.getElementById('search');
    if (searchInput) {
        const debouncedSearch = debounce(function() {
            const query = this.value.trim();
            if (query.length >= 2) {
                performSearch(query);
            }
        }, 300);
        
        searchInput.addEventListener('input', debouncedSearch);
    }
}

/**
 * Perform search
 */
function performSearch(query) {
    // This would typically make an AJAX request
    // For now, we'll just show a loading state
    console.log('Searching for:', query);
}

// Export functions for global access
window.AlcoholShop = {
    addToCart,
    updateCartQuantity,
    removeFromCart,
    showAlert,
    formatCurrency,
    isValidEmail,
    calculateAge,
    isAdult,
    togglePasswordVisibility,
    confirmAge,
    exitSite
};

