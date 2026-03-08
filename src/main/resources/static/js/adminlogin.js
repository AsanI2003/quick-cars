
    document.getElementById("adminLoginForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    fetch("/admin/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
})
    .then(res => {
    if (!res.ok) throw new Error("Invalid credentials");
    return res.json();
})
    .then(data => {
    localStorage.setItem("accessToken", data.accessToken);
    localStorage.setItem("refreshToken", data.refreshToken);
    window.location.href = "/admindashboard.html";
})
    .catch(err => {
    alert(err.message === "Invalid credentials" ? "Wrong username or password." : "Login failed.");
});
});

