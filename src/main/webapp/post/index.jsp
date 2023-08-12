<%@ page contentType="text/html; charset=UTF-8" %>
    <!DOCTYPE html>
    <html lang="en" data-bs-theme="dark">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
        <script src="js/bootstrap.min.js"></script>
        <title>Forum</title>
    </head>

    <body>

        <div class="p-1 rounded-5 container p-2" style="width: max-content">
            <button class="btn bi bi-house-fill" style="font-size: 32px;"></button>
            <input type="text" name="search" id="search" class="rounded-5 p-1 flex-row-reverse bd-highlight">
            <button class="btn bi bi-search" style="font-size: 30px;"></button>
            <button class="btn bi bi-person-circle" style="font-size: 32px;"></button>
            <span class="form-switch container m-5" style="padding-top: 100px;">
                <input class="form-check-input" type="checkbox" role="switch" id="themeModeSwitch" checked
                    style="font-size: 32px;">
                <label class="form-check-label" for="flexSwitchCheckChecked">Dark mode</label>
            </span>
        </div>

        <% java.util.List list=(java.util.List) request.getAttribute("list"); int length=list==null ? 0 : list.size();
            for(int i = 0; i < length; i++) { 
                com.hoangdp.forum.entity.Post p = (com.hoangdp.forum.entity.Post) list.get(i);
                %>

            <div class="container p-2"></div>

            <div class="container text-center bg-body-tertiary p-4 rounded-4">
                <div class="row">
                    <div class="col-10 container bg-primary-subtle rounded-4 p-3" style="text-align: right;">
                        <p class="fw-bold text-primary-emphasis"><%=p.getTitle() %></p>
                        <p class="text-primary-emphasis"><%=p.getContent()%></p>
                        <p><a type="button" class="btn btn-link" href="post?id=<%=p.getId()%>">Xem thêm</a></p>
                        <button class="btn bi bi-chat-dots" style="font-size: 20px;"> <%=p.getNumberOfComments()%></button>
                        <button class="btn bi bi-suit-heart" style="font-size: 20px;"> <%=p.getNumberOfHearts()%></button>
                    </div>

                    <div class="col-2">
                        <img src="https://yt3.googleusercontent.com/-CFTJHU7fEWb7BYEb6Jh9gm1EpetvVGQqtof0Rbh-VQRIznYYKJxCaqv_9HeBcmJmIsp2vOO9JU=s900-c-k-c0x00ffffff-no-rj"
                            width="100" alt="avatar" style="border-radius: 90%;">
                        <p class="text-success-emphasis"><%=p.getUser().getNickname()%></p>
                        <p class="text-success-emphasis"><%=p.getLastModifiedOn()%></p>

                    </div>
                </div>
            </div>
            <% } %>

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

                    window.addEventListener('load', function () {

                        // Get the theme mode from local storage
                        var themeMode = localStorage.getItem('themeMode');

                        if (themeMode != null) {
                            document.documentElement.setAttribute('data-bs-theme', themeMode);
                            document.getElementById('themeModeSwitch').checked = themeMode == 'dark';
                            return;
                        }

                        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                            document.documentElement.setAttribute('data-bs-theme', 'dark');
                            document.getElementById('themeModeSwitch').checked = true;
                        } else {
                            document.documentElement.setAttribute('data-bs-theme', 'light');
                            document.getElementById('themeModeSwitch').checked = false;
                        }
                    });
                </script>
    </body>

    </html>