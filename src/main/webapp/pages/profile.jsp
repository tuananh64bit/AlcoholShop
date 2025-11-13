<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/topmenu.jsp" />

<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-9">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <div class="d-flex align-items-center gap-4 mb-4">
                            <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="Avatar" class="rounded-circle" width="120" height="120">
                            <div>
                                <h3 class="mb-1"> <c:out value="${sessionScope.currentUser.fullname}" default="Guest"/> </h3>
                                <p class="mb-0 text-muted"><i class="fas fa-user me-2"></i><c:out value="${sessionScope.currentUser.username}"/></p>
                                <p class="mb-0 text-muted"><i class="fas fa-user-tag me-2"></i><c:out value="${sessionScope.currentUser.role}"/></p>
                            </div>
                        </div>

                        <hr />

                        <div class="row">
                            <div class="col-md-6">
                                <h6 class="text-uppercase text-muted small">Account</h6>
                                <p class="mb-1"><strong>Full name:</strong> <c:out value="${sessionScope.currentUser.fullname}"/></p>
                                <p class="mb-1"><strong>Username:</strong> <c:out value="${sessionScope.currentUser.username}"/></p>
                                <p class="mb-1"><strong>Email:</strong> <c:out value="${sessionScope.currentUser.email}"/></p>
                                <p class="mb-1"><strong>Role:</strong> <c:out value="${sessionScope.currentUser.role}"/></p>
                            </div>
                            <div class="col-md-6">
                                <h6 class="text-uppercase text-muted small">Details</h6>
                                <p class="mb-1"><strong>Birth date:</strong> <c:out value="${sessionScope.currentUser.birthDate}" default="-"/></p>
                                <p class="mb-1"><strong>Address:</strong> <c:out value="${sessionScope.currentUser.address}" default="-"/></p>
                                <p class="mb-1"><strong>Member since:</strong> <c:out value="${sessionScope.currentUser.createdAt}" default="-"/></p>
                            </div>
                        </div>

                        <div class="mt-4 d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/edit-profile" class="btn btn-primary">
                                <i class="fas fa-edit me-1"></i> Edit Profile
                            </a>

                            <!-- Button trigger modal -->
                            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                                <i class="fas fa-key me-1"></i> Change Password
                            </button>

                            <form action="${pageContext.request.contextPath}/logout" method="post" class="ms-auto">
                                <button type="submit" class="btn btn-danger">
                                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Empty state / notes: keep page structure even if some fields are missing -->
                <c:if test="${empty sessionScope.currentUser}">
                    <div class="alert alert-info mt-4">
                        <i class="fas fa-info-circle me-2"></i>No user is currently signed in.
                    </div>
                </c:if>

                <!-- Alerts for profile & password change -->
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success mt-3">Profile updated successfully.</div>
                </c:if>
                <c:if test="${not empty param.pwSuccess}">
                    <div class="alert alert-success mt-3">Password changed successfully.</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger mt-3">Error: <c:out value="${param.error}"/></div>
                </c:if>

                <!-- Change Password Modal -->
                <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content shadow-sm">
                            <form action="${pageContext.request.contextPath}/change-password" method="post">
                                <div class="modal-header bg-gold text-dark align-items-center">
                                    <div class="d-flex align-items-center">
                                        <i class="fas fa-key fa-lg me-3"></i>
                                        <h5 class="modal-title mb-0" id="changePasswordModalLabel">Change Password</h5>
                                    </div>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <p class="text-muted small">Enter your current password and choose a new secure password (min 6 characters).</p>

                                    <div class="mb-3">
                                        <label for="oldPassword" class="form-label">Current password</label>
                                        <input id="oldPassword" name="oldPassword" type="password" class="form-control" required />
                                    </div>

                                    <div class="mb-3 row">
                                        <div class="col-md-6 mb-2 mb-md-0">
                                            <label for="newPassword" class="form-label">New password</label>
                                            <input id="newPassword" name="newPassword" type="password" class="form-control" required />
                                        </div>
                                        <div class="col-md-6">
                                            <label for="confirmPassword" class="form-label">Confirm new password</label>
                                            <input id="confirmPassword" name="confirmPassword" type="password" class="form-control" required />
                                        </div>
                                    </div>

                                    <div class="small text-muted">Tip: Use at least 8 characters with letters and numbers for better security.</div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                                    <button type="submit" class="btn btn-primary">Save</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Password changed success modal -->
                <div class="modal fade" id="pwSavedModal" tabindex="-1" aria-labelledby="pwSavedLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content shadow-sm">
                            <div class="modal-header bg-gold text-dark">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-check-circle fa-lg me-3"></i>
                                    <h5 class="modal-title mb-0" id="pwSavedLabel">Password updated</h5>
                                </div>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Your password has been updated successfully.</p>
                            </div>
                            <div class="modal-footer">
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary">OK</a>
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Back</button>
                            </div>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty param.pwSuccess}">
                    <script>
                        document.addEventListener('DOMContentLoaded', function() {
                            var m = new bootstrap.Modal(document.getElementById('pwSavedModal'));
                            m.show();
                        });
                    </script>
                </c:if>
            </div>
        </div>
    </div>
</section>

<jsp:include page="includes/footer.jsp" />
