const API_KEY = "asan-quickcars-2025";

let selectedVehicle = null;
const urlParams = new URLSearchParams(window.location.search);
const vehicleId = urlParams.get("vehicleId");
document.querySelector("button[type='submit']").disabled = true;

loadSelectedVehicle(vehicleId);
showAvailableCalendar(vehicleId);


function loadSelectedVehicle(vehicleId) {
    fetch(`/vehicle/getallvehicle`, {
        headers: { "X-API-KEY": API_KEY }
    })

        .then(res => res.ok ? res.json() : Promise.reject())
        .then(vehicles => {
            const vehicle = vehicles.find(v => v.id == vehicleId);
            if (!vehicle) {
                document.getElementById("selectedVehicleCard").innerHTML = `
                    <div class="card-body text-center text-danger">
                        <p>Vehicle not found or unavailable.</p>
                    </div>`;
                return;
            }

            selectedVehicle = vehicle; // used for pricing
            document.getElementById("selectedVehicleCard").innerHTML = `
                <img src="${vehicle.imageurl}" class="card-img-top" alt="${vehicle.name}">
                <div class="card-body text-center">
                    <h5 class="card-title">${vehicle.name}</h5>
                    <p class="card-text">
                        ${vehicle.capacity} Seater<br>
                        Fuel: ${vehicle.fuelType}<br>
                        Drive: ${vehicle.driveType}<br>
                        Price/Day: LKR ${vehicle.dayPrice}
                    </p>
                </div>
            `;
        })
        .catch(() => {
            document.getElementById("selectedVehicleCard").innerHTML = `
                <div class="card-body text-center text-danger">
                    <p>Failed to load vehicle details.</p>
                </div>`;
        });
}

loadSelectedVehicle(vehicleId);

const today = new Date().toISOString().split("T")[0];
document.getElementById("pickupDate").setAttribute("min", today);
document.getElementById("dropoffDate").setAttribute("min", today);

document.getElementById("pickupDate").addEventListener("change", function () {
    const pickupDate = this.value;
    if (pickupDate) {
        document.getElementById("dropoffDate").setAttribute("min", pickupDate);
    }
});

document.getElementById("dropoffDate").addEventListener("change", function () {
    const dropoffDate = this.value;
    if (dropoffDate) {
        document.getElementById("pickupDate").setAttribute("max", dropoffDate);
    }
});


const contactInput = document.getElementById("contactNumber");
const contactError = document.getElementById("contactError");

contactInput.addEventListener("input", () => {
    const value = contactInput.value.trim();
    const valid = /^07[0-9]{8}$/.test(value);
    if (!valid) {
        contactError.textContent = "Invalid Sri Lankan mobile number format";
        contactError.classList.remove("d-none");
    } else {
        contactError.classList.add("d-none");
    }
});

function calculateTotal() {
    const pickupDate = new Date(document.getElementById("pickupDate").value);
    const dropoffDate = new Date(document.getElementById("dropoffDate").value);
    const pickupLocation = document.getElementById("pickupLocation").value;
    const dropoffLocation = document.getElementById("dropoffLocation").value;

    if (isNaN(pickupDate) || isNaN(dropoffDate)) return;

    const days = Math.ceil((dropoffDate - pickupDate) / (1000 * 60 * 60 * 24));
    if (days <= 0) return;

    const baseRate = selectedVehicle?.dayPrice || 0;// set this from backend
    let total = days * baseRate;
    if (pickupLocation !== dropoffLocation) total += 1500;

    document.getElementById("totalAmount").textContent = total.toFixed(2);
}
["pickupDate", "dropoffDate", "pickupLocation", "dropoffLocation"].forEach(id => {
    document.getElementById(id).addEventListener("change", calculateTotal);
});

function checkAvailability(vehicleId, pickupDate, dropoffDate) {
    return fetch(`/rental/check-availability?vehicleId=${vehicleId}&pickupDate=${pickupDate}&dropoffDate=${dropoffDate}`, {
        headers: { "X-API-KEY": API_KEY }
    })
        .then(res => res.ok ? res.json() : Promise.reject());
}




document.querySelector("form").addEventListener("submit", function (e) {
    e.preventDefault();

    const contact = contactInput.value.trim();
    const pickupDate = document.getElementById("pickupDate").value;
    const dropoffDate = document.getElementById("dropoffDate").value;
    const pickupLocation = document.getElementById("pickupLocation").value;
    const dropoffLocation = document.getElementById("dropoffLocation").value;
    const submitBtn = document.querySelector("button[type='submit']");

    if (!/^07[0-9]{8}$/.test(contact) || !pickupDate || !dropoffDate || !pickupLocation || !dropoffLocation) {
        showToast("Please fill all fields correctly.","error");
        return;
    }

    submitBtn.disabled = true;

    checkAvailability(vehicleId, pickupDate, dropoffDate).then(isAvailable => {
        if (!isAvailable) {
            showToast("Already inquired for this date.<br>Check available dates please","error");


            submitBtn.disabled = true;
            return;
        }

        const rental = {
            contactNumber: contact,
            vehicleId,
            pickupDate,
            dropoffDate,
            pickupLocation,
            dropoffLocation,
            totalPrice: parseFloat(document.getElementById("totalAmount").textContent)
        };

        fetch("/rental/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(rental)
        })
            .then(res => {
                if (res.ok) {
                    if (res.ok) {
                        window.location.href = "/thankyoupage.html";
                    }

                } else {
                    showToast("Submission failed.","error");
                    submitBtn.disabled = false;
                }
            })
            .catch(() => {
                showToast("Error submitting inquiry.","error");
                submitBtn.disabled = false;
            });
    });
});


function validateForm() {
    const contact = contactInput.value.trim();
    const pickupDate = document.getElementById("pickupDate").value;
    const dropoffDate = document.getElementById("dropoffDate").value;
    const pickupLocation = document.getElementById("pickupLocation").value;
    const dropoffLocation = document.getElementById("dropoffLocation").value;
    const validContact = /^07[0-9]{8}$/.test(contact);

    const allFilled = validContact && pickupDate && dropoffDate && pickupLocation && dropoffLocation;
    document.querySelector("button[type='submit']").disabled = !allFilled;
}

["contactNumber", "pickupDate", "dropoffDate", "pickupLocation", "dropoffLocation"].forEach(id => {
    document.getElementById(id).addEventListener("input", validateForm);
    document.getElementById(id).addEventListener("change", validateForm);
});

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

function showAvailableCalendar(vehicleId) {
    fetch(`/rental/available-dates?vehicleId=${vehicleId}`, {
        headers: { "X-API-KEY": API_KEY }
    })

        .then(res => res.ok ? res.json() : Promise.reject())
        .then(availableDates => {
            if (!Array.isArray(availableDates) || availableDates.length === 0) {
                document.getElementById("availableDatesDisplay").innerHTML = "<p class='text-danger'>No available dates.</p>";
                return;
            }

            flatpickr("#availableDatesDisplay", {
                inline: true,
                dateFormat: "Y-m-d",
                enable: availableDates,
                disableMobile: true,
                clickOpens: false, // prevents input behavior
                minDate: "today",
                maxDate: new Date(Date.now() + 90 * 24 * 60 * 60 * 1000),
                onDayCreate: function (dObj, dStr, fp, dayElem) {
                    const calendarDate = fp.formatDate(dayElem.dateObj, "Y-m-d");
                    const todayStr = fp.formatDate(new Date(), "Y-m-d");

                    if (availableDates.includes(calendarDate)) {
                        dayElem.classList.add("available-ball");

                        if (calendarDate === todayStr) {
                            dayElem.classList.add("today-ball");
                        }
                    }
                }
            });
        })
        .catch(() => {
            document.getElementById("availableDatesDisplay").innerHTML = "<p class='text-danger'>Failed to load dates.</p>";
        });
}







