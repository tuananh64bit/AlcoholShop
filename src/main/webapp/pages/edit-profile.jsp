<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/topmenu.jsp" />

<section class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h4 class="mb-3">Edit Profile</h4>

                        <form action="${pageContext.request.contextPath}/edit-profile" method="post" class="row g-3">
                            <input type="hidden" name="id" value="${sessionScope.currentUser.id}" />

                            <div class="col-md-6">
                                <label for="fullname" class="form-label">Full name</label>
                                <input id="fullname" type="text" name="fullname" class="form-control" value="${sessionScope.currentUser.fullname}" required />
                            </div>

                            <div class="col-md-6">
                                <label for="username" class="form-label">Username</label>
                                <input id="username" type="text" name="username" class="form-control" value="${sessionScope.currentUser.username}" required />
                            </div>

                            <div class="col-md-12">
                                <label for="email" class="form-label">Email</label>
                                <input id="email" type="email" name="email" class="form-control" value="${sessionScope.currentUser.email}" required />
                            </div>

                            <div class="col-md-6">
                                <label for="birthDate" class="form-label">Birth date</label>
                                <input id="birthDate" type="date" name="birthDate" class="form-control" value="${sessionScope.currentUser.birthDate}" />
                            </div>

                            <div class="col-md-6">
                                <label for="role" class="form-label">Role</label>
                                <input id="role" type="text" name="role" class="form-control" value="${sessionScope.currentUser.role}" readonly />
                            </div>

                            <div class="col-12">
                                <label for="address" class="form-label">Address</label>
                                <textarea id="address" name="address" class="form-control" rows="3">${sessionScope.currentUser.address}</textarea>
                            </div>

                            <div class="col-12 d-flex gap-2">
                                <button type="submit" class="btn btn-primary">Save changes</button>
                                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                        </form>

                    </div>
                </div>

                <c:if test="${not empty param.success}">
                    <div class="alert alert-success mt-3">Profile updated successfully.</div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger mt-3">An error occurred while updating profile: <c:out value="${param.error}"/></div>
                </c:if>

                <!-- Display validation errors next to fields (if any) -->
                <c:if test="${not empty requestScope.errors}">
                    <div class="alert alert-danger mt-3">
                        <strong>Please fix the following errors:</strong>
                        <ul class="mb-0">
                            <c:forEach var="entry" items="${requestScope.errors}">
                                <li><c:out value="${entry.value}"/></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <!-- Success modal shown after saving changes -->
                <div class="modal fade" id="profileSavedModal" tabindex="-1" aria-labelledby="profileSavedLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="profileSavedLabel">Changes saved</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                Your profile changes have been saved successfully.
                            </div>
                            <div class="modal-footer">
                                <a href="${pageContext.request.contextPath}/pages/profile.jsp" class="btn btn-primary">Go to Profile</a>
                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Stay on this page</button>
                            </div>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty requestScope.saved}">
                    <script>
                        document.addEventListener('DOMContentLoaded', function() {
                            var myModal = new bootstrap.Modal(document.getElementById('profileSavedModal'));
                            myModal.show();
                        });
                    </script>
                </c:if>
             </div>
         </div>
     </div>
 </section>

 <jsp:include page="includes/footer.jsp" />
