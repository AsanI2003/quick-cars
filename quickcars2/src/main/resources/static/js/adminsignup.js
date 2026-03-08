
    document.getElementById("adminSignupForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("signupUsername").value.trim();
    const password = document.getElementById("signupPassword").value.trim();

    fetch("/admin/auth/signup", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
})
    .then(res => {
    if (res.ok) {
    alert("Signup successful! You can now log in.");
    window.location.href = "/adminlogin.html";
} else if (res.status === 409) {
    alert("Username already exists.");
} else {
    alert("Signup failed.");
}
})
    .catch(() => alert("Error during signup."));
});

