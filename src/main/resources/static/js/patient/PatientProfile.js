function getPatientData() {
    var pesel = document.getElementById("pesel-label").innerText;
    var firstNameLabel = document.getElementById("first-name-label");
    var lastNameLabel = document.getElementById("last-name-label");

    fetch("/api/patient/" + pesel + "/patient").then(function (value) {
        return value.json();
    }).then(function (data) {
        if (data.resp_status === "ok") {
            var patient = data.patient;
            firstNameLabel.innerText = patient.first_name;
            lastNameLabel.innerText = patient.last_name;
        } else {
            alert("Nastąpił błąd podczas ładowania danych pacjenta. Odśwież stronę");
            ;
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

