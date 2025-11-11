<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Order Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
</head>

<body class="container mt-4">

<h2>Order Management</h2>

<!-- Flash message -->
<c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>

<!-- Filter by Status -->
<form class="form-inline mb-3" method="get" action="${pageContext.request.contextPath}/admin/orders">
    <label class="mr-2">Filter by Status:</label>
    <select name="status" class="form-control mr-2">
        <option value="">All</option>
        <option value="PENDING"     ${selectedStatus == 'PENDING' ? 'selected' : ''}>Pending</option>
        <option value="CONFIRMED"   ${selectedStatus == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
        <option value="SHIPPED"     ${selectedStatus == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
        <option value="DELIVERED"   ${selectedStatus == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
        <option value="CANCELLED"   ${selectedStatus == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
    </select>
    <button class="btn btn-primary">Filter</button>
</form>

<!-- Statistics -->
<div class="mb-3">
    <b>Total:</b> ${totalOrders} |
    <b>Pending:</b> ${pendingOrders} |
    <b>Confirmed:</b> ${confirmedOrders} |
    <b>Shipped:</b> ${shippedOrders} |
    <b>Delivered:</b> ${deliveredOrders} |
    <b>Cancelled:</b> ${cancelledOrders}
</div>

<!-- Orders Table -->
<table class="table table-bordered table-hover">
    <thead>
    <tr>
        <th>ID</th>
        <th>User</th>
        <th>Total Price</th>
        <th>Status</th>
        <th>Update</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.id}</td>
            <td>${order.user.username}</td>
            <td>${order.total}</td>
            <td>${order.status}</td>

            <td>
                <form method="get" action="${pageContext.request.contextPath}/admin/orders" class="form-inline">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${order.id}">

                    <select name="status" class="form-control mr-2">
                        <option value="PENDING">Pending</option>
                        <option value="CONFIRMED">Confirmed</option>
                        <option value="SHIPPED">Shipped</option>
                        <option value="DELIVERED">Delivered</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>

                    <button class="btn btn-success btn-sm">Update</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
