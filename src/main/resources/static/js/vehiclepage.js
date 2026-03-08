const API_KEY = "asan-quickcars-2025";

document.addEventListener("DOMContentLoaded", function () {
    const grid = document.getElementById("vehicleGrid");

    //  Render vehicle cards
    function renderVehicles(vehicles) {
        grid.innerHTML = "";
        vehicles.forEach(vehicle => {
            const card = `
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card glass-card h-100">
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
            `;
            grid.insertAdjacentHTML("beforeend", card);
        });
        attachInquireHandlers();
    }

    //  Apply filters and sort
    function applyFilters() {
        const selectedType = document.querySelector(".vehicle-type:checked")?.value;
        const selectedFuel = document.querySelector(".fuel-type:checked")?.value;
        const selectedDrive = document.querySelector(".drive-type:checked")?.value;
        const selectedSort = document.getElementById("sortDropdown").dataset.value;

        let query = [];
        if (selectedType) query.push(`type=${selectedType}`);
        if (selectedFuel) query.push(`fuelType=${selectedFuel}`);
        if (selectedDrive) query.push(`driveType=${selectedDrive}`);

        let endpoint = "/filtervehicles";
        if (query.length) endpoint += "?" + query.join("&");

        fetch(`http://localhost:8080/vehicle${endpoint}`, {
            headers: { "X-API-KEY": API_KEY }
        })

            .then(res => res.ok ? res.json() : Promise.reject())
            .then(vehicles => {
                if (selectedSort) {
                    vehicles.sort((a, b) => {
                        switch (selectedSort) {
                            case "capacityAsc": return parseInt(a.capacity) - parseInt(b.capacity);
                            case "capacityDesc": return parseInt(b.capacity) - parseInt(a.capacity);
                            case "fuelAsc": return parseFloat(a.fuelEconomy) - parseFloat(b.fuelEconomy);
                            case "fuelDesc": return parseFloat(b.fuelEconomy) - parseFloat(a.fuelEconomy);
                            case "priceAsc": return a.dayPrice - b.dayPrice;
                            case "priceDesc": return b.dayPrice - a.dayPrice;
                            default: return 0;
                        }
                    });
                }
                renderVehicles(vehicles);
            })
            .catch(() => {
                grid.innerHTML = "<p class='text-danger text-center'>Failed to load filtered vehicles.</p>";
            });
    }


    //  Initial load
    fetch("http://localhost:8080/vehicle/getallvehicle", {
        headers: { "X-API-KEY": API_KEY }
    })

        .then(res => res.ok ? res.json() : Promise.reject())
        .then(renderVehicles)
        .catch(() => {
            grid.innerHTML = "<p class='text-danger text-center'>Failed to load vehicles.</p>";
        });

    //  Sort dropdown logic
    document.querySelectorAll("#sortOptions .dropdown-item").forEach(item => {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const selectedText = this.textContent;
            const selectedValue = this.dataset.value;
            const sortDropdown = document.getElementById("sortDropdown");
            sortDropdown.textContent = selectedText;
            sortDropdown.dataset.value = selectedValue;
            applyFilters();
        });
    });

    //  Single-select checkbox logic
    document.querySelectorAll(".vehicle-type").forEach(box => {
        box.addEventListener("change", function () {
            document.querySelectorAll(".vehicle-type").forEach(other => {
                if (other !== this) other.checked = false;
            });
            applyFilters();
        });
    });

    document.querySelectorAll(".fuel-type").forEach(box => {
        box.addEventListener("change", function () {
            document.querySelectorAll(".fuel-type").forEach(other => {
                if (other !== this) other.checked = false;
            });
            applyFilters();
        });
    });

    document.querySelectorAll(".drive-type").forEach(box => {
        box.addEventListener("change", function () {
            document.querySelectorAll(".drive-type").forEach(other => {
                if (other !== this) other.checked = false;
            });
            applyFilters();
        });
    });

    function attachInquireHandlers() {
        document.querySelectorAll(".rent-btn").forEach(btn => {
            btn.addEventListener("click", function () {
                const vehicleId = this.dataset.id;
                const $btn = $(this);
                $btn.prop("disabled", true).text("Checking...");

                // Step 1: Check vehicle availability first
                fetch(`/vehicle/check/${vehicleId}`, {
                    headers: { "X-API-KEY": API_KEY }
                })

                    .then(res => {
                        if (!res.ok) throw new Error("vehicle_check_failed");
                        return res.json();
                    })
                    .then(isAvailable => {
                        if (!isAvailable) throw new Error("vehicle_unavailable");

                        // Step 2: Check login only if vehicle is available
                        return fetch("/home/api/auth/check", { credentials: "include" });
                    })
                    .then(res => {
                        if (!res.ok) throw new Error("unauthenticated");

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
