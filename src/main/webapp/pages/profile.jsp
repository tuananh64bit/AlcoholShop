<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .profile-card {
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            padding: 30px;
            margin-top: 40px;
        }
        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
        }
        .profile-info h4 {
            font-weight: 600;
        }
        .edit-btn {
            background-color: #6f42c1;
            color: white;
            border-radius: 25px;
        }
        .edit-btn:hover {
            background-color: #5a32a1;
        }
    </style>
</head>
<body>

<!-- Include header -->
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="profile-card text-center">
                <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png"
                     alt="Avatar" class="profile-avatar mb-3">
                <div class="profile-info">
                    <h4><c:out value="${sessionScope.currentUser.fullname}" /></h4>
                    <p class="text-muted mb-1"><i class="fas fa-envelope me-2"></i><c:out value="${sessionScope.currentUser.email}" /></p>
                    <p class="text-muted mb-3"><i class="fas fa-user-tag me-2"></i><c:out value="${sessionScope.currentUser.role}" /></p>
                </div>

                <hr>

                <div class="text-start mt-4">
                    <h5 class="fw-bold mb-3">Account Information</h5>
                    <p><strong>Full name:</strong> <c:out value="${sessionScope.currentUser.fullname}" /></p>
                    <p><strong>Email:</strong> <c:out value="${sessionScope.currentUser.email}" /></p>
                    <p><strong>Phone:</strong> <c:out value="${sessionScope.currentUser.phone}" /></p>
                    <p><strong>Address:</strong> <c:out value="${sessionScope.currentUser.address}" /></p>
                </div>

                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/edit-profile" class="btn edit-btn px-4">
                        <i class="fas fa-edit me-2"></i>Edit Profile
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger ms-2 px-4">
                        <i class="fas fa-sign-out-alt me-2"></i>Logout
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
