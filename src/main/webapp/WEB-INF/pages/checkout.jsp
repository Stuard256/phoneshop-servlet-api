<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.entity.Order" scope="request"/>
<tags:master pageTitle="Checkout">
<form method="post" action="${pageContext.servletContext.contextPath}/checkout">
    <div>
        Cart: ${cart} , total Quantity = ${cart.totalQuantity}
    </div>
    <c:if test="${not empty param.message}">
       <div class="success">
         ${param.message}
       </div>
    </c:if>
     <c:if test="${not empty errors}">
        <div class="error">
          An error occurred in order details
        </div>
      </c:if>
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
    <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"> </tags:orderFormRow>
    <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"> </tags:orderFormRow>
    <tags:orderFormRow name="phone" label="Phone for contact" order="${order}" errors="${errors}"> </tags:orderFormRow>
    <tr>
        <td> Delivery date: <span style="color:red">*</span></td>
        <td> <input type="date" name="deliveryDate" value="2021-04-27"/> </td>
         <c:set var="error" value="${errors['deliveryDate']}"/>
         <td>
            <c:if test="${not empty error}">
               <div class="error">
                ${error}
               </div>
            </c:if>
         </td>
    </tr>
    <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"> </tags:orderFormRow>
    <tr>
            <td> Payment method: <span style="color:red">*</span></td>
            <c:set var="error" value="${errors['paymentMethod']}"/>
            <td> <input type="radio" name="paymentMethod" value="0">Cash
                   <input type="radio" name="paymentMethod" value="1">Card
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
            </td>

            </td>
    </tr>
  </table>
  <p>
      <button>
       Pay
      </button>
  </p>
</form>

</tags:master>