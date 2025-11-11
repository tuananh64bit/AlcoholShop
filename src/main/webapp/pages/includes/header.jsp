<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'AlcoholShop'}" /> - Premium Luxury Alcohol Store</title>
    <meta name="description" content="Premium luxury alcohol and wine store featuring the finest spirits, wines, and craft beverages. Exclusive collection for discerning connoisseurs.">
    <meta name="keywords" content="premium alcohol, luxury wine, whiskey, vodka, champagne, spirits, craft beverages">
    
    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/static/images/favicon.ico">
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- AOS Animation Library -->
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet">
    
    <!-- Age verification notice -->
    <script>
        // Check if user has seen age verification
        if (!localStorage.getItem('ageVerified')) {
            document.addEventListener('DOMContentLoaded', function() {
                showAgeVerification();
            });
        }
        
        function showAgeVerification() {
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
        }
        
        function confirmAge() {
            localStorage.setItem('ageVerified', 'true');
            document.querySelector('.age-verification-modal').remove();
        }
        
        function exitSite() {
            window.location.href = 'https://www.google.com';
        }
    </script>
</head>
<body>
    <!-- Age verification modal styles -->
    <style>
        .age-verification-modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.8);
            z-index: 10000;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .age-verification-content {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            max-width: 500px;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
        
        .age-verification-header {
            color: #dc3545;
            margin-bottom: 1rem;
        }
        
        .age-verification-header i {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        
        .age-verification-footer {
            margin-top: 1.5rem;
        }
        
        .age-verification-footer .btn {
            margin: 0 0.5rem;
            padding: 0.75rem 1.5rem;
        }
    </style>
