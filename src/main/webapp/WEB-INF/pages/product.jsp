<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="product" type="com.es.phoneshop.model.product.entity.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    ${product.description}
  </p>
  <form method="post">
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
              <td><input name="quantity"> </td>
          </tr>
      </table>
      <button>Add to cart</button>
  </form>
  <script>
  </script>
</tags:master>