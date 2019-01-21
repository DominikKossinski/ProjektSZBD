var globalPesel = "";

function searchPatients() {
    var pattern = document.getElementById("search-input").value;
    var patientsUl = document.getElementById("patients-ul");
    patientsUl.innerHTML = "";
    fetch("/api/searchPatients?pattern=" + pattern).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var patients = data.patients;
            patients.map(function (patient) {
                for (var a = 0; a < 10; a++) {
                    var patientLi = document.createElement("li");

                    var label = document.createElement("label");
                    label.innerText = patient.pesel + " Imie " + patient.first_name + " nazwisko " + patient.last_name;
                    patientLi.appendChild(label);

                    var firstNameInput = document.createElement("input");
                    firstNameInput.type = "text";
                    firstNameInput.value = patient.first_name;
                    firstNameInput.style.display = "none";
                    patientLi.appendChild(firstNameInput);

                    var lastNameInput = document.createElement("input");
                    lastNameInput.type = "text";
                    lastNameInput.value = patient.last_name;
                    lastNameInput.style.display = "none";
                    patientLi.appendChild(lastNameInput);

                    var acceptInput = document.createElement("input");
                    acceptInput.type = "submit";
                    acceptInput.value = "Akceptuj";
                    acceptInput.onclick = function () {
                        updatePatient(patient.pesel, firstNameInput.value, lastNameInput.value, patient.password);
                    };
                    acceptInput.style.display = "none";
                    patientLi.appendChild(acceptInput);

                    var manageInput = document.createElement("input");
                    manageInput.type = "submit";
                    manageInput.value = "Edytuj";
                    manageInput.onclick = function () {
                        if (manageInput.value === "Edytuj") {
                            manageInput.value = "Anuluj";
                            label.innerText = patient.pesel;
                            firstNameInput.style.display = "block";
                            firstNameInput.value = patient.first_name;
                            lastNameInput.style.display = "block";
                            lastNameInput.value = patient.last_name;
                            acceptInput.style.display = "block";
                        } else {
                            manageInput.value = "Edytuj";
                            label.innerText = patient.pesel + " Imie " + patient.first_name + " nazwisko " + patient.last_name;
                            firstNameInput.style.display = "none";
                            lastNameInput.style.display = "none";
                            acceptInput.style.display = "none";
                        }
                    };
                    patientLi.appendChild(manageInput);

                    var deleteInput = document.createElement("input");
                    deleteInput.type = "submit";
                    deleteInput.value = "Usuń";
                    deleteInput.onclick = function () {
                        deletePatient(patient.pesel);
                    };
                    patientLi.appendChild(deleteInput);

                    patientsUl.appendChild(patientLi);
                }
            })
        } else {
            // TODO łdniejsze wyświetlanie błędu
            alert("błąd")
        }
    })
}

function addPatient() {
    var pesel = document.getElementById("pesel-input").value;
    var firstName = document.getElementById("first-name-input").value;
    var lastName = document.getElementById("last-name-input").value;
    var password = document.getElementById("password-input").value;

    if (parseInt(pesel) >= 10000000000 && parseInt(pesel) < 100000000000 && password !== ""
        && firstName !== "" && lastName !== "") {
        var data = JSON.stringify({
            "pesel": parseInt(pesel),
            "first_name": firstName,
            "last_name": lastName,
            "password": password
        });
        var http = new XMLHttpRequest();
        var url = "/api/addPatient";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        console.log(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    globalPesel = pesel;
                    alert("Dodano Pacjenta");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                searchPatients();
            }

        };
    } else {
        //TODO ladniej wyswietlac informacje
        alert("error")
    }
}

function deletePatient(pesel) {
    //TODO potwierdzenie chęci
    var http = new XMLHttpRequest();
    var url = "/api/deletePatient?pesel=" + pesel;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Patient deleted pesel:" + pesel)
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            searchPatients();
        }
    };
}

function updatePatient(pesel, firstName, lastName, password) {
    if (parseInt(pesel) >= 10000000000 && parseInt(pesel) < 100000000000 && password !== ""
        && firstName !== "" && lastName !== "") {
        var data = JSON.stringify({
            "pesel": parseInt(pesel),
            "first_name": firstName,
            "last_name": lastName,
            "password": password
        });
        var http = new XMLHttpRequest();
        var url = "/api/updatePatient";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        console.log(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    globalPesel = pesel;
                    alert("update Pacjenta:" + pesel);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                searchPatients();
            }
        };
    } else {
        //TODO ladniej wyswietlac informacje
        alert("error")
    }
}

function addStay() {
    var id = document.getElementById("id-span").innerText;
    if (globalPesel === "") {
        window.location.assign("/" + id + "/addStay");
    } else {
        window.location.assign("/" + id + "/addStay?pesel=" + globalPesel)
    }
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

