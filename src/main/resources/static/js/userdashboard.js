$(document).ready(function () {


    // Load inquiries
    fetch("/userdashboard/inquiries")
        .then(res => res.json())
        .then(data => {
            const list = $("#inquiryList ul");
            list.empty();
            data.forEach(r => {
                const item = `
    <li class="list-group-item bg-dark text-white border border-light rounded-3 mb-2">
        <div class="d-flex justify-content-between align-items-center">
            <strong>${r.name}</strong>
            <span class="badge bg-light text-dark">${r.status}</span>
        </div>
        <hr class="border-light">
        <p class="mb-1"><strong>Pickup:</strong> ${r.pickupDate} from ${r.pickupLocation}</p>
        <p class="mb-1"><strong>Dropoff:</strong> ${r.dropoffDate} at ${r.dropoffLocation}</p>
        <p class="mb-0"><strong>Total Price:</strong> Rs. ${r.totalPrice.toFixed(2)}</p>
    </li>`;

                list.append(item);
            });
        });


    let currentRating = 0;

    $(".star").on("click", function () {
        const clickedValue = $(this).data("value");
        currentRating = (clickedValue === currentRating) ? 0 : clickedValue;
        $("#ratingValue").val(currentRating);

        $(".star").each(function () {
            const val = $(this).data("value");
            $(this).toggleClass("filled", val <= currentRating);
            $(this).html(val <= currentRating ? "&#9733;" : "&#9734;");
        });
    });

    $("#reviewForm").on("submit", function (e) {
        e.preventDefault();

        const payload = {
            description: $("#reviewText").val(),
            rating: currentRating
        };

        fetch("/userdashboard/review", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (!res.ok) throw new Error("Review submission failed");
                showToast("✅ Review submitted successfully!", "success");
                $("#reviewText").val("");
                $(".star").removeClass("filled").html("&#9734;");
                currentRating = 0;
            })
            .catch(err => {
                console.error(err);
                showToast("Failed to submit review. Please try again.", "error");
            });
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

});
