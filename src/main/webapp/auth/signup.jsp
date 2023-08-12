<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <script src="../js/bootstrap.min.js"></script>
    <title>Sign up</title>
</head>

<body>
    <div class="mx-auto p-5 m-5 container bg-body-tertiary rounded-4" style="max-width: 600px;">
        <h2>Sign up</h2>
        <form action="./signup" method="post">
            <div class="mb-3">
                <label for="inputUsername" class="form-label">Username / Email address</label>
                <input type="text" class="form-control" id="inputUsername" name="inputUsername"
                    aria-describedby="emailHelp" required>
                <div id="emailHelp" class="form-text">We'll never share your sensitive information with anyone else.
                </div>
            </div>
            <div class="mb-3">
                <label for="nickname" class="form-label">Nickname</label>
                <input type="text" class="form-control" id="inputNickname" name="inputNickname"
                    aria-describedby="nicknameHelp" required>
                <div id="nicknameHelp" class="form-text">Every one will see this nickname</div>
            </div>
            <div class="mb-3">
                <label for="inputPassword" class="form-label">Password</label>
                <input type="password" class="form-control" id="inputPassword" name="inputPassword">
                <div id="passwordHelp" class="form-text text-danger"></div>
            </div>
            <div class="mb-3">
                <label for="inputConfirmPassword" class="form-label">Comfirm password</label>
                <input type="password" class="form-control" id="inputConfirmPassword" required>
                <div id="confirmPasswordHelp" class="form-text text-danger"></div>
            </div>
            <div class="mb-3">
                <input type="checkbox" id="checkbox">
                <span for="checkbox" class="form-label"> Agree to Forum's Terms of Service and acknowledge that Forum's
                    Privacy Policy applies to you.</span>
            </div>
            <button type="submit" class="btn btn-primary" id="btnSignUp" disabled>Sign up</button>
        </form>
    </div>
    <script>
        window.addEventListener('load', function () {
            if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
                document.documentElement.setAttribute('data-bs-theme', 'dark');
            } else {
                confirmPasswordHelp
                document.documentElement.setAttribute('data-bs-theme', 'light');
            }
        });

        document.getElementById('checkbox').onchange = function () {
            document.getElementById('btnSignUp').disabled = !this.checked;
        };

        const form = document.querySelector('form');
        form.addEventListener('submit', (event) => {
            const password = document.querySelector('#inputPassword').value;
            const confirmPassword = document.querySelector('#inputConfirmPassword').value;
            if (document.getElementById("inputConfirmPassword").value.length < 6) {
                document.getElementById("passwordHelp").textContent = "Password least 6 characters";
                event.preventDefault();
            }
            if (password !== confirmPassword) {
                document.getElementById("confirmPasswordHelp").textContent = "Confirm password is not match";
                event.preventDefault();
            }
        });
    </script>
</body>

</html>