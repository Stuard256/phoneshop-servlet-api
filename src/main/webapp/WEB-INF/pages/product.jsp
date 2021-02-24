<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="product" type="com.es.phoneshop.model.product.entity.Product" scope="request"/>
<tags:master pageTitle="Product Details">

  <form method="post">
       <p>
            ${cart}
      </p>
        <c:if test="${not empty param.message}">
            <div class="message">
                Product was added successfully to cart
            </div>
        </c:if>
      <c:if test="${not empty error}">
          <div class="error">
              An trouble has occurred while adding to cart
          </div>
      </c:if>
        <p>
          ${product.description}
        </p>
      <table>
          <tr>
            <td> Image: </td>
            <td>
                <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${product.imageUrl}">
            </td>
          </tr>
          <tr>
            <td> Code: </td>
            <td>${product.code}</td>
          </tr>
          <tr>
            <td> Price: </td>
            <td class="price">
                <tags:priceHistory product="${product}"/>
            </td>
          </tr>
          <tr>
            <td> Stock: </td>
            <td>${product.stock}</td>
          </tr>
          <tr>
              <td> Quantity: </td>
              <td><input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity">
              <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
              </c:if>
              </td>
          </tr>
      </table>
      <button>Add to cart</button>
  </form>
  <table>
    <tr>
        <c:forEach var="item" items="${lastSeen}">
            <td> <a href="./${item.getId()}"> ${item.getDescription()} </a> </td>
        </c:forEach>
    </tr>
  </table>
</tags:master>