<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<%
    String token = request.getParameter("token");
    if (token == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing token parameter");
        return;
    }
%>
<div class="container">
    <form action="/resetPassword" method="post" class="mt-5">
        <input type="hidden" name="token" value="<%= token %>">
        <div class="form-group">
            <label for="password"><spring:message code="label.nouveauMdp"/></label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>
        <div class="form-group">
            <label for="confirmPassword"><spring:message code="label.confirmerMdp"/></label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary"><spring:message code="button.reinitialiser"/></button>
    </form>
</div>
</body>
</html>
