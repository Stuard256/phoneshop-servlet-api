<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.entity.Cart" scope="request"/>
<a href="/phoneshop-servlet-api/cart"> Cart</a>:  ${cart.totalQuantity} items, total cost: <fmt:formatNumber value='${cart.totalCost}' type="currency" currencySymbol="${item.product.currency.symbol}"/>