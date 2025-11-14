<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../includes/header.jsp" />
<jsp:include page="../includes/topmenu.jsp" />

<div class="card-footer bg-transparent border-0">
    <a href="${pageContext.request.contextPath}/admin/contacts" class="btn btn-outline-light">
        <i class="fas fa-arrow-left"></i> Back to Messages
    </a>
</div>

<div class="container py-4">
    <div class="row">
        <div class="col-md-10 offset-md-1">
            <div class="card bg-glass mb-4 text-light">
                <div class="card-header d-flex justify-content-between align-items-center bg-transparent border-0">
                    <h5 class="mb-0 text-warning">Contact Message Details</h5>
                </div>

                <div class="card-body">
                    <c:if test="${not empty message}">
                        <dl class="row">

                            <dt class="col-sm-3 text-info">ID</dt>
                            <dd class="col-sm-9 text-light">${message.id}</dd>

                            <dt class="col-sm-3 text-info">Name</dt>
                            <dd class="col-sm-9 text-light">${message.name}</dd>

                            <dt class="col-sm-3 text-info">Email</dt>
                            <dd class="col-sm-9">
                                <a href="mailto:${message.email}" class="text-warning">${message.email}</a>
                            </dd>

                            <dt class="col-sm-3 text-info">Phone</dt>
                            <dd class="col-sm-9 text-light">${message.phone}</dd>

                            <dt class="col-sm-3 text-info">Subject</dt>
                            <dd class="col-sm-9 text-light">${message.subject}</dd>

                            <dt class="col-sm-3 text-info">Message</dt>
                            <dd class="col-sm-9 text-light" style="white-space:pre-wrap;">${message.message}</dd>

                            <dt class="col-sm-3 text-info">Received</dt>
                            <dd class="col-sm-9 text-light">
                                <c:choose>
                                    <c:when test="${not empty message.createdAtAsDate}">
                                        <span class="text-light">
                                            <fmt:formatDate value="${message.createdAtAsDate}" pattern="MMM dd, yyyy HH:mm" />
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </dd>

                        </dl>
                    </c:if>

                    <c:if test="${empty message}">
                        <div class="alert alert-warning">Message not found.</div>
                    </c:if>
                </div>

            </div>
        </div>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
