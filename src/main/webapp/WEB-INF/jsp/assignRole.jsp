<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 2025/5/24
  Time: 16:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>分配角色</title></head>
<body>
<h2>给用户分配角色</h2>

<form action="${pageContext.request.contextPath}/user/assignRole" method="post">
    <input type="hidden" name="userId" value="${userId}"/>

    <select name="roleId">
        <c:forEach var="role" items="${allRoles}">
            <option value="${role.id}"
                    <c:if test="${fn:contains(userRoles, role)}">selected</c:if>>
                    ${role.roleName}
            </option>
        </c:forEach>
    </select>

    <input type="submit" value="保存角色"/>
</form>

</body>
</html>