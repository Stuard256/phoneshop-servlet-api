<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.entity.Order" scope="request"/>
<tags:master pageTitle="Order overview">
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description </td>
        <td>
            Price
        </td>
        <td>
            Stock
        </td>
        <td>
             Quantity
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${order.items}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${item.product.imageUrl}">
        </td>
        <td>${item.product.description}</td>
        <td class="price">
            <fmt:formatNumber value='${item.product.price}' type="currency" currencySymbol="${item.product.currency.symbol}"/>
        </td>
        <td>
            ${item.product.stock}
        </td>
        <td>
           ${item.quantity}
        </td>
      </tr>
    </c:forEach>
    <tr>
        <td>Subtotal cost:
            <fmt:formatNumber value='${order.subtotal}' type="currency" currencySymbol="${item.product.currency.symbol}"/>
        </td>
        <td>Delivery cost:</td>
        <td>
            <fmt:formatNumber value='${order.deliveryCost}' type="currency" currencySymbol="${item.product.currency.symbol}"/>
        </td>
        <td>Final cost:</td>
        <td>
            <fmt:formatNumber value='${order.totalCost}' type="currency" currencySymbol="${item.product.currency.symbol}"/>
        </td>
    </tr>
  </table>
</br>
<h2> Order details: </h2>
  <table>
    <tr>
        <td> First name: </td>
        <td> ${order.firstName} </td>
    </tr>
    <tr>
        <td> Last name: </td>
        <td> ${order.lastName} </td>
    </tr>
    <tr>
        <td> Contact number: </td>
        <td> ${order.phone} </td>
    </tr>
    <tr>
        <td> Delivery date: </td>
        <td> ${order.deliveryDate} </td>
    </tr>
    <tr>
        <td> Delivery address: </td>
        <td> ${order.deliveryAddress} </td>
    </tr>
            <td> Payment method:</td>
            <td>
            ${order.getPaymentMethod().toString().toLowerCase()}
            </td>
    </tr>
  </table>
</tags:master>