<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <script src="../js/bootstrap.min.js"></script>
    <title>Login</title>
</head>

<body>
    <div class="mx-auto p-5 m-5 container bg-body-tertiary rounded-4" style="max-width: 600px;">
        <h2>Login</h2>
        <form action="./login" method="post">
            <div class="mb-3">
                <label for="inputUsername" class="form-label">Username / Email address</label>
                <input type="text" class="form-control" name="username" id="inputUsername" aria-describedby="emailHelp"
                    required>
                <div id="emailHelp" class="form-text">We'll never share your sensitive information with anyone else.
                </div>
            </div>
            <div class="mb-3">
                <label for="inputPassword" class="form-label">Password</label>
                <input type="password" class="form-control" id="inputPassword" name="password" required>
            </div>
            <%=request.getAttribute("msg")==null ? "" : request.getAttribute("msg")%>
                <button type="submit" class="btn btn-primary">Login</button>
        </form>
    </div>
    <script>
        window.addEventListener('load', function () {
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                document.documentElement.setAttribute('data-bs-theme', 'dark');
            } else {
                document.documentElement.setAttribute('data-bs-theme', 'light');
            }
        });
    </script>
</body>

</html>