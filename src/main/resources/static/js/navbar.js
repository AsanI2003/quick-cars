$(document).ready(function () {
    $("#navbarContainer").load("/navbar.html", function () {
        const profileIcon = $("#profileIcon");
        const profileDropdown = $("#profileDropdown");
        const profileMenu = $("#profileMenu");

        // Default state: show default icon, disable dropdown
        profileDropdown.removeAttr("data-bs-toggle");
        profileMenu.empty();

        // Check login status
        fetch("/home/api/user/profile", { credentials: "include" })
            .then(res => res.ok ? res.json() : Promise.reject())
            .then(user => {
                //  Logged in update profile icon, enable dropdown, show email
                profileIcon.attr("src", user.picture);
                profileDropdown.attr("data-bs-toggle", "dropdown");

                profileMenu.html(`
          <li><span class="dropdown-item-text text-muted">${user.email}</span></li>
          <li><a class="dropdown-item" href="#">Profile</a></li>
          <li><a class="dropdown-item" href="#" id="logoutBtn">Logout</a></li>
        `);
            })
            .catch(() => {
                //  Not logged in redirect to Google login on click
                profileDropdown.on("click", function () {
                    window.location.href = "/oauth2/authorization/google";
                });
            });

        // Logout handler (GET request for Spring Security default)
        $(document).on("click", "#logoutBtn", function () {
            window.location.href = "/logout";
        });
    });
});
