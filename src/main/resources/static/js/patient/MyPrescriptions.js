function getPrescriptions() {
    var ul = document.getElementById("prescriptions-ul");
    var pesel = document.getElementById("pesel-span").innerText;
    fetch("/api/patient/" + pesel + "/prescriptions").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var prescriptions = data.prescriptions;
            prescriptions.map(function (prescription) {

                fetch("/api/illness?id=" + prescription.illness_id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {

                        var text = prescription.dosage;
                        if (text === "null") {
                            text = "";
                        }
                        var row = $('<tr/>')
                            .append($('<td/>', {text: prescription.id || ''}))          // ID recepty
                            .append($('<td/>', {text: prescription.illness_id || ''}))          // ID choroby
                            .append($('<td/>', {text: prescription.stay_id || ''}))          // ID choroby
                            .append($('<td/>', {text: prescription.date || ''}))          // Data wystawienia
                            .append($('<td/>', {text: text || ''}));   // Dawkowanie

                        $('table tbody').append(row);


                    } else {
                        alert("Nastąpił błąd podczas ładowania recept. Odśwież stronę");
                    }
                })

            })
        } else {
            alert("Nastąpił błąd podczas ładowania recept. Odśwież stronę");
        }
    })
}

function logout() {
    var url = "/api/logout";
    fetch(url).then(function (value) {
        return value.json();
    }).then(function (data) {
        if (data.resp_status === "ok") {
            if (data.logout.logout === true) {
                console.log("Logged out");
                window.location.assign("/home");
            }
        }
    });

}

