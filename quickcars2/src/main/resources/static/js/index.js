$(document).ready(function () {

    const API_KEY = "asan-quickcars-2025";

    fetch("http://localhost:8080/home/gettop8", {
        headers: { "X-API-KEY": API_KEY }
    })
        .then(res => res.json())
        .then(vehicles => {
            const container = $("#vehicleList").empty();
            vehicles.forEach(vehicle => {
                container.append(`
          <div class="col-md-3">
            <div class="card h-100 shadow-sm">
              <img src="${vehicle.imageurl}" class="card-img-top" alt="${vehicle.name}">
              <div class="card-body text-center">
                <h5 class="card-title">${vehicle.name}</h5>
                <p class="card-text">
                  Capacity: ${vehicle.capacity}<br>
                  Fuel Economy: ${vehicle.fuelEconomy}<br>
                  Price/Day: LKR${vehicle.dayPrice} 
                </p>
                 <button class="btn btn-dark rent-btn" data-id="${vehicle.id}">Inquire Now</button>
              </div>
            </div>
          </div>
        `);
            });
            attachInquireHandlers();
        })
        .catch(() => {
            $("#vehicleList").html("<p class='text-danger'>Failed to load vehicles.</p>");
        });


    fetch("http://localhost:8080/home/getallreviews", {
        headers: { "X-API-KEY": API_KEY }
    })
        .then(res => res.json())
        .then(reviews => {
            const container = $("#reviewContainer").empty();
            for (let i = 0; i < reviews.length; i += 3) {
                const group = reviews.slice(i, i + 3);
                container.append(`
        <div class="carousel-item${i === 0 ? ' active' : ''}">
            <div class="row justify-content-center g-3">
                ${group.map(review => {
                    const formattedDate = new Date(review.createdAt).toLocaleDateString("en-GB", {
                        year: "numeric",
                        month: "short",
                        day: "numeric"
                    });

                    return `
                    <div class="col-md-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-2">
                                    <img src="${review.imageurl}" alt="Profile" class="rounded-circle me-2" width="40" height="40"
                                         onerror="this.src='/images/default-avatar.png'" />
                                    <div>
                                        <strong>${review.name}</strong><br>
                                        <small class="text-muted">${formattedDate}</small>
                                    </div>
                                </div>
                                <p class="card-text">“${review.description}”</p>
                                <p class="text-warning">${'⭐'.repeat(review.rating)}</p>
                            </div>
                        </div>
                    </div>
                    `;
                }).join('')}
            </div>
        </div>
        `);
            }
        })
        .catch(() => {
            $("#reviewContainer").html("<p class='text-danger text-center'>Failed to load reviews.</p>");
        });


    function attachInquireHandlers() {
        document.querySelectorAll(".rent-btn").forEach(btn => {
            btn.addEventListener("click", function () {
                const vehicleId = this.dataset.id;
                const $btn = $(this);
                $btn.prop("disabled", true).text("Checking...");

                // Step 1: Check vehicle availability first
                fetch(`/api/vehicle/check/${vehicleId}`, {
                    headers: { "X-API-KEY": API_KEY }
                })
                    .then(res => {
                        if (!res.ok) throw new Error("vehicle_check_failed","error");
                        return res.json();
                    })
                    .then(isAvailable => {
                        if (!isAvailable) throw new Error("vehicle_unavailable","error");

                        // Step 2: Check login only if vehicle is available
                        return fetch("/home/api/auth/check", { credentials: "include" });
                    })
                    .then(res => {
                        if (!res.ok) throw new Error("unauthenticated","error");

                        // Step 3: Redirect to rental page
                        window.location.href = `/rentalpage.html?vehicleId=${vehicleId}`;
                    })
                    .catch(err => {
                        switch (err.message) {
                            case "vehicle_unavailable":
                                showToast("Vehicle not available.","error");
                                break;
                            case "unauthenticated":
                                showToast("Wait for Google login or relogin.","success");
                                setTimeout(() => {
                                    window.location.href = "http://localhost:8080/oauth2/authorization/google";
                                }, 2500);
                                break;
                            default:
                                showToast("Unable to proceed. Please try again.","error");
                        }
                    })
                    .finally(() => {
                        setTimeout(() => {
                            $btn.prop("disabled", false).text("Inquire Now");
                        }, 3500);
                    });
            });
        });
    }






    function showToast(message, type = "error") {
        const bgClass = type === "success" ? "bg-success" : "bg-danger";

        const toast = $(`
        <div class="toast align-items-center text-white ${bgClass} border-0 position-fixed bottom-0 end-0 m-3" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `);

        $("body").append(toast);
        const bsToast = new bootstrap.Toast(toast[0]);
        bsToast.show();

        setTimeout(() => {
            toast.remove();
        }, 3000);
    }






});
