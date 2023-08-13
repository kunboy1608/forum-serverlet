<%@ page contentType="text/html; charset=UTF-8" %>
    <!DOCTYPE html>
    <html lang="en" data-bs-theme="dark">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../css/bootstrap.min.css">
        <script src="../js/bootstrap.min.js"></script>
        <title>Post Editor</title>
    </head>

    <jsp:include page="../share/nav-bar.jsp" />

    <body>
        <div class="mx-auto p-5 m-5 container bg-body-tertiary rounded-4" style="max-width: 600px;">
            <h2>Post editor</h2>
            <form action="" method="post">
                <div class="mb-3">
                    <label for="inputTitle" class="form-label">Title</label>
                    <input type="text" class="form-control" name="inputTitle" id="inputTitle"
                        aria-describedby="emailHelp" required>
                </div>
                <div class="mb-3">
                    <label for="inputContent" class="form-label">Content</label>
                    <textarea class="form-control" id="inputContent" name="inputContent" required rows="4"></textarea>
                </div>
                <input type="hidden" name="id" value=<%=request.getParameter("id")%>>
                <button type="submit" class="btn btn-primary">Save</button>
            </form>
        </div>
        <script>
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