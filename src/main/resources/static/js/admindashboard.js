// Section switching
function switchSection(sectionId) {
    document.querySelectorAll(".section").forEach(sec => sec.classList.add("d-none"));
    document.getElementById(`section-${sectionId}`).classList.remove("d-none");

    document.querySelectorAll("#adminNav .nav-link").forEach(btn => btn.classList.remove("active"));
    document.querySelector(`#adminNav [data-section="${sectionId}"]`).classList.add("active");

    if (sectionId === "inquiries") fetchInquiries();
    if (sectionId === "vehicles") fetchVehicles();
}

document.querySelectorAll("#adminNav .nav-link").forEach(btn => {
    btn.addEventListener("click", () => switchSection(btn.dataset.section));
});

switchSection("inquiries");

// Inquiry logic
function fetchInquiries() {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        alert("Login required.");
        window.location.href = "/adminlogin.html";
        return;
    }

    fetch("/admin/auth/dashboard/inquiries", {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => {
            if (res.status === 401) {
                return fetch("/admin/auth/refresh", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ refreshToken: localStorage.getItem("refreshToken") })
                })
                    .then(res => res.json())
                    .then(data => {
                        localStorage.setItem("accessToken", data.accessToken);
                        localStorage.setItem("refreshToken", data.refreshToken);
                        return fetch("/admin/auth/dashboard/inquiries", {
                            headers: { Authorization: `Bearer ${data.accessToken}` }
                        });
                    });
            }
            return res;
        })
        .then(res => res.json())
        .then(data => renderTable(data))
        .catch(() => showError());
}

function renderTable(data) {
    const tbody = document.querySelector("#inquiryTable tbody");
    tbody.innerHTML = "";

    const formatter = new Intl.DateTimeFormat("en-GB", {
        dateStyle: "medium",
        timeStyle: "short"
    });

    data.forEach(inquiry => {
        const createdAt = formatter.format(new Date(inquiry.createdAt));
        const row = `
            <tr>
                <td>${inquiry.id}</td>
                <td>${createdAt}</td>
                <td>${inquiry.vehicleId}</td>
                <td>${inquiry.contactNumber}</td>
                <td>${inquiry.pickupDate}</td>
                <td>${inquiry.dropoffDate}</td>
                <td>${inquiry.pickupLocation} → ${inquiry.dropoffLocation}</td>
                <td>${inquiry.totalPrice.toFixed(2)}</td>
                <td>
                    <select class="form-select form-select-sm" onchange="updateStatus(${inquiry.id}, this.value, '${inquiry.status}')">
                        <option ${inquiry.status === 'pending' ? 'selected' : ''}>pending</option>
                        <option ${inquiry.status === 'confirmed' ? 'selected' : ''}>confirmed</option>
                        <option ${inquiry.status === 'rental completed' ? 'selected' : ''}>rental completed</option>
                    </select>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}

function updateStatus(id, newStatus, currentStatus) {
    if (newStatus === currentStatus) return;

    const token = localStorage.getItem("accessToken");
    fetch(`/admin/auth/dashboard/inquiries/${id}/status`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ status: newStatus })
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to update");
            return res.text();
        })
        .then(msg => {
            if (msg === "Status updated successfully") {
                showToast(msg, "success");
                fetchInquiries();
            }
        })
        .catch(() => showToast("Failed to update status", "danger"));
}

function showError() {
    document.querySelector("#inquiryTable tbody").innerHTML = `
        <tr><td colspan="9" class="text-danger">Failed to load inquiries.</td></tr>
    `;
}

function logout() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    window.location.href = "/adminlogin.html";
}

// Vehicle logic
function fetchVehicles() {
    const token = localStorage.getItem("accessToken");
    fetch("/admin/auth/dashboard/vehicles", {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("vehicleTableBody");
            tbody.innerHTML = "";
            data.forEach(v => {
                const row = `
                    <tr>
                        <td>${v.id}</td>
                        <td>${v.name}</td>
                        <td>${v.capacity}</td>
                        <td>${v.fuelEconomy}</td>
                        <td>
                            <input type="number" class="form-control form-control-sm" value="${v.dayPrice}" 
                                onchange="updateDayPrice(${v.id}, this.value)" min="1000" max="999999">
                        </td>
                        <td>
                            <div class="form-check form-switch d-flex justify-content-center">
                                <input class="form-check-input" type="checkbox" ${v.availability === 1 ? "checked" : ""} 
                                    onchange="updateAvailability(${v.id}, this.checked ? 1 : 0)">
                            </div>
                        </td>
                        <td>${v.fuelCapacityL}</td>
                        <td>${v.vehicleType}</td>
                        <td>${v.fuelType}</td>
                        <td>${v.driveType}</td>
                    </tr>
                `;
                tbody.insertAdjacentHTML("beforeend", row);
            });
        })
        .catch(() => showToast("Failed to load vehicles", "danger"));
}

function updateDayPrice(id, newPrice) {
    const token = localStorage.getItem("accessToken");
    fetch(`/admin/auth/dashboard/vehicles/${id}/price`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ dayPrice: parseInt(newPrice) })
    })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.text();
        })
        .then(msg => showToast(msg, "success"))
        .catch(() => showToast("Failed to update price", "danger"));
}

function updateAvailability(id, value) {
    const token = localStorage.getItem("accessToken");
    fetch(`/admin/auth/dashboard/vehicles/${id}/availability`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ availability: value })
    })
        .then(res => {
            if (!res.ok) throw new Error();
            return res.text();
        })
        .then(msg => showToast(msg, "success"))
        .catch(() => showToast("Failed to update availability", "danger"));
}

// Real-time validation block
const form = document.getElementById("vehicleForm");
const saveBtn = document.getElementById("saveVehicleBtn");

form.querySelectorAll("input, select").forEach(field => {
    field.addEventListener("input", () => {
        if (field.checkValidity()) {
            field.classList.remove("is-invalid");
            field.classList.add("is-valid");
        } else {
            field.classList.remove("is-valid");
            field.classList.add("is-invalid");
        }

        saveBtn.disabled = !form.checkValidity();
    });
});

// Final form submission
form.addEventListener("submit", function (event) {
    event.preventDefault();
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return;
    }
    saveVehicle();
});

function saveVehicle() {
    const token = localStorage.getItem("accessToken");
    const payload = {
        name: document.getElementById("name").value,
        capacity: document.getElementById("capacity").value,
        fuelEconomy: document.getElementById("fuelEconomy").value,
        dayPrice: parseInt(document.getElementById("dayPrice").value),
        availability: parseInt(document.getElementById("availability").value),
        fuelCapacityL: parseInt(document.getElementById("fuelCapacityL").value),
        vehicleType: document.getElementById("vehicleType").value,
        fuelType: document.getElementById("fuelType").value,
        driveType: document.getElementById("driveType").value,
        imageurl: document.getElementById("imageurl").value
    };

    fetch("/admin/auth/dashboard/vehicles", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to save");
            return res.text();
        })
        .then(msg => {
            showToast(msg, "success");
            form.reset();
            form.classList.remove("was-validated");
            form.querySelectorAll("input, select").forEach(field => {
                field.classList.remove("is-valid", "is-invalid");
            });
            saveBtn.disabled = true;
            fetchVehicles(); // refresh table
        })
        .catch(() => showToast("Failed to save vehicle", "danger"));
}

function generateReport() {
    const month = document.getElementById("reportMonth").value;
    const year = document.getElementById("reportYear").value;
    const token = localStorage.getItem("accessToken");

    if (!month || !year) {
        showToast("Please select both month and year", "danger");
        return;
    }

    fetch(`/admin/auth/dashboard/inquiries/report?month=${month}&year=${year}`, {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) {
                showToast("No completed inquiries found for this month", "info");
                return;
            }
            renderTable(data); // reuse existing table renderer
            showToast("Report generated", "success");
        })
        .catch(() => showToast("Failed to generate report", "danger"));
}

function downloadReport() {
    const month = document.getElementById("reportMonth").value;
    const year = document.getElementById("reportYear").value;
    const token = localStorage.getItem("accessToken");

    if (!month || !year) {
        showToast("Please select both month and year", "danger");
        return;
    }

    fetch(`/admin/auth/dashboard/inquiries/report/download?month=${month}&year=${year}`, {
        headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = `Completed_Inquiries_${year}_${month}.csv`;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        })
        .catch(() => showToast("Failed to download report", "danger"));
}

