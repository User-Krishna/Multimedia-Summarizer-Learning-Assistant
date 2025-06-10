<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="CSS/form.css" />
    <script src="https://kit.fontawesome.com/64d58efce2.js" crossorigin="anonymous"></script>
    <style>
    span.face-p {
    font-size: 13px;
    text-align: end;
}
    </style>
</head>

<body>
    <div class="container">
        <div class="forms-container">
            <div class="signin-signup">
                <!-- Login Form -->
                <form action="LoginServlet" method="post" class="sign-in-form">
                    <h2 class="title">Sign in</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="email" name="email" placeholder="Email" required />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" name="password" placeholder="Password" required />
                    </div>
                    <p class="error" id="error-message" style="display: none;"></p>
                    <button type="submit" class="btn">Login</button>
                    <p class="social-text">Or Sign in with social platforms</p>
                    <div class="social-media">
                        <button type="button" id="Google-login-button1" class="social-icon">
                          <img src="image/google.png" alt="Google" /> 
                        </button>
                        
                        <button type="button" id="Github-login-button1" class="social-icon">
                            <img src="image/github.png" alt="GitHub" />
                        </button>
                    </div>
                    <p class="social-text">Or</p>
                    <!-- Link for face login -->
                    <a href="http://localhost:5173/user-select" class="btn" style="display: block; text-align: center; margin-top: 10px;"><span class="face-p" style="display: inline-block; width: 100%; text-align: center; margin-top:1rem;">Login with Face</span></a>
                </form>

                <!-- Registration Form -->
                <form action="RegisterServlet" method="post" class="sign-up-form">
                    <h2 class="title">Sign up</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" name="name" placeholder="Full Name" required />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-envelope"></i>
                        <input type="email" name="email" placeholder="Email" required />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" name="password" placeholder="Password" required />
                    </div>
                   
                   
                    <button type="submit" class="btn">Sign up</button>
                </form>
            </div>
        </div>

        <div class="panels-container">
            <div class="panel left-panel">
                <div class="content">
                    <h3>New User?</h3>
                    <p>Create an account to access all features and services. Register today!</p>
                    <button class="btn transparent" id="sign-up-btn">
                        Register
                    </button>
                </div>
                <img src="image/log.svg" class="image" alt="Login Illustration" />
            </div>
            <div class="panel right-panel">
                <div class="content">
                    <h3>Welcome Back!</h3>
                    <p>Already have an account? Sign in with your email and password.</p>
                    <button class="btn transparent" id="sign-in-btn">
                        Sign in
                    </button>
                </div>
                <img src="image/register.svg" class="image" alt="Register Illustration" />
            </div>
        </div>
    </div>

    <script>
        const sign_in_btn = document.querySelector("#sign-in-btn");
        const sign_up_btn = document.querySelector("#sign-up-btn");
        const container = document.querySelector(".container");

        sign_up_btn.addEventListener("click", () => {
            container.classList.add("sign-up-mode");
        });

        sign_in_btn.addEventListener("click", () => {
            container.classList.remove("sign-up-mode");
        });

        // Fetch the error message safely from the server-side
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
            const errorMessage = "<%= errorMessage.replaceAll("\"", "\\\\\"") %>";
            if (errorMessage.trim()) {
                const errorElement = document.getElementById("error-message");
                errorElement.style.display = "block";
                errorElement.textContent = errorMessage;
            }
        <%
            }
        %>

        // OAuth client IDs and redirect URI
        const GOOGLE_CLIENT_ID = "156404900022-dde7kqg8bs8l29npn8fuau9mb549h10v.apps.googleusercontent.com";
        const GITHUB_CLIENT_ID = "Ov23liWHfuzwir4KuJlC";
        const REDIRECT_URI = "http://localhost:8084/Major-Project/landing.html";

        // Add event listener for Google login button
        document.querySelectorAll("#Google-login-button1").forEach(button => {
            button.addEventListener("click", () => {
                const googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + GOOGLE_CLIENT_ID + 
                    "&response_type=token&redirect_uri=" + encodeURIComponent(REDIRECT_URI) + 
                    "&scope=" + encodeURIComponent("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
                window.location.href = googleAuthUrl;
            });
        });

        // Add event listener for GitHub login button
        document.querySelectorAll("#Github-login-button1").forEach(button => {
            button.addEventListener("click", () => {
                const githubAuthUrl = "https://github.com/login/oauth/authorize?client_id=" + GITHUB_CLIENT_ID + 
                    "&redirect_uri=" + encodeURIComponent(REDIRECT_URI) + 
                    "&scope=" + encodeURIComponent("read:user user:email");
                window.location.href = githubAuthUrl;
            });
        });

        // Handle Google Login Redirect
        window.onload = () => {
            const hash = window.location.hash.substring(1);
            const params = new URLSearchParams(hash);
            const googleAccessToken = params.get("access_token");

            if (googleAccessToken) {
                fetch("https://www.googleapis.com/oauth2/v1/userinfo?alt=json", {
                    headers: { Authorization: `Bearer ${googleAccessToken}` },
                })
                    .then(response => response.json())
                    .then(user => {
                        console.log("Google User Info:", user);
                        window.location.href = REDIRECT_URI;
                    })
                    .catch(error => console.error("Error fetching Google user info:", error));
            }
        };

        // Handle GitHub Login Redirect
        window.onload = () => {
            const urlParams = new URLSearchParams(window.location.search);
            const githubCode = urlParams.get("code");

            if (githubCode) {
                fetch("http://localhost:8084/GitHubTokenExchange", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ code: githubCode, client_id: GITHUB_CLIENT_ID }),
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.access_token) {
                            fetch("https://api.github.com/user", {
                                headers: { Authorization: `Bearer ${data.access_token}` },
                            })
                                .then(response => response.json())
                                .then(user => {
                                    console.log("GitHub User Info:", user);
                                    window.location.href = REDIRECT_URI;
                                })
                                .catch(error => console.error("Error fetching GitHub user info:", error));
                        } else {
                            console.error("GitHub token exchange failed");
                        }
                    })
                    .catch(error => console.error("Error during GitHub token exchange:", error));
            }
        };
    </script>
</body>

</html>