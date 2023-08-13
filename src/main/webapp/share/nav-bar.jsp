<%@ page contentType="text/html; charset=UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../css/bootstrap.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
        <script src="../js/bootstrap.min.js"></script>
    </head>

    <body>

        <div class="p-1 rounded-5 container p-2" style="width: max-content">
            <a class="btn bi bi-house-fill" style="font-size: 32px;" href="/forum"></a>
            <input type="text" name="search" id="search" class="rounded-5 p-1 flex-row-reverse bd-highlight">
            <a class="btn bi bi-search" href="/forum/post/edit" style="font-size: 30px;"></a>
            <% com.hoangdp.forum.entity.User
                u=com.hoangdp.forum.service.UserService.getInstant().getCurrentUser(request); if (u !=null ){ %>
                <img src="<%=u.getAvatar()%>" width="30" alt="avatar" style="border-radius: 90%;">
                <a class="bi bi-patch-plus-fill" href="/forum/post/edit" style="font-size: 32px;"></a>
                <% }else{ %>
                    <a href="/forum/auth/login">Login</a>/
                    <a href="/forum/auth/signup">Signup</a>
                    <%}%>

                        <span class="form-switch container m-5" style="padding-top: 100px;">
                            <input class="form-check-input" type="checkbox" role="switch" id="themeModeSwitch" checked
                                style="font-size: 32px;">
                            <label class="form-check-label" for="flexSwitchCheckChecked">Dark mode</label>
                        </span>
        </div>

        <script>
            document.getElementById('themeModeSwitch').onchange = function () {
                if (this.checked) {
                    localStorage.setItem('themeMode', 'dark');
                    document.documentElement.setAttribute('data-bs-theme', 'dark');
                } else {
                    localStorage.setItem('themeMode', 'light');
                    document.documentElement.setAttribute('data-bs-theme', 'light');

                }
            };
        </script>
    </body>

    </html>