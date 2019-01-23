var globalPesel = "";

function searchPatients() {
    var pattern = document.getElementById("search-input").value;
    var patientsTbody = document.getElementById("patients-tbody");
    patientsTbody.innerHTML = "";
    fetch("/api/searchPatients?pattern=" + pattern).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var patients = data.patients;
            var i = 1;
            patients.map(function (patient) {

                var row = document.createElement("tr");

                var numTd = document.createElement("td");
                numTd.innerText = i + ".";
                numTd.className = "text-td";
                row.append(numTd);

                var peselTd = document.createElement("td");
                peselTd.className = "text-td";
                peselTd.innerText = patient.pesel;
                row.append(peselTd);

                var firstNameTd = document.createElement("td");
                var firstNameLabel = document.createElement("label");
                firstNameLabel.className = "table-text-label";
                firstNameLabel.innerText = patient.first_name;
                firstNameTd.appendChild(firstNameLabel);

                var firstNameInput = document.createElement("input");
                firstNameInput.type = "text";
                firstNameInput.value = patient.last_name;
                firstNameInput.style.display = "none";
                firstNameInput.className = "text-input";
                firstNameTd.appendChild(firstNameInput);

                row.append(firstNameTd);

                var lastNameTd = document.createElement("td");
                var lastNameLabel = document.createElement("label");
                lastNameLabel.className = "table-text-label";
                lastNameLabel.innerText = patient.last_name;
                lastNameTd.appendChild(lastNameLabel);

                var lastNameInput = document.createElement("input");
                lastNameInput.type = "text";
                lastNameInput.value = patient.last_name;
                lastNameInput.style.display = "none";
                lastNameInput.className = "text-input";
                lastNameTd.appendChild(lastNameInput);

                row.append(lastNameTd);

                var editTd = document.createElement("td");

                var acceptInput = document.createElement("input");
                acceptInput.className = "accept-input";
                acceptInput.type = "submit";
                acceptInput.value = "Akceptuj";
                acceptInput.onclick = function () {
                    updatePatient(patient.pesel, firstNameInput.value, lastNameInput.value, patient.password);
                };
                acceptInput.style.display = "none";
                editTd.appendChild(acceptInput);

                var manageInput = document.createElement("input");
                manageInput.className = "manage-input";
                manageInput.type = "submit";
                manageInput.value = "Edytuj";
                manageInput.onclick = function () {
                    if (manageInput.value === "Edytuj") {
                        manageInput.value = "Anuluj";
                        manageInput.style.height = "50%";
                        firstNameInput.style.display = "inline-block";
                        firstNameLabel.style.display = "none";
                        firstNameInput.value = patient.first_name;
                        lastNameInput.style.display = "inline-block";
                        lastNameInput.value = patient.last_name;
                        lastNameLabel.style.display = "none";
                        acceptInput.style.display = "inline-block";
                    } else {
                        manageInput.value = "Edytuj";
                        manageInput.style.height = "100%";
                        firstNameInput.style.display = "none";
                        firstNameLabel.style.display = "inline-block";
                        lastNameInput.style.display = "none";
                        lastNameLabel.style.display = "inline-block";
                        acceptInput.style.display = "none";
                    }
                    deleteInput.style.height = "100%";
                };
                editTd.appendChild(manageInput);

                row.append(editTd);

                var deleteTd = document.createElement("td");

                var deleteInput = document.createElement("input");
                deleteInput.className = "delete-input";
                deleteInput.type = "submit";
                deleteInput.value = "Usuń";
                deleteInput.onclick = function () {
                    deletePatient(patient.pesel);
                };
                deleteTd.appendChild(deleteInput);

                row.append(deleteTd);
                patientsTbody.append(row);
                i++;

            })
        } else {
            alert("Nastąpił błąd przy ładowaniu strony. Odśwież ją");
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
                    globalPesel = pesel;
                    alert("Dodano Pacjenta");
                } else {
                    alert("Nastąpił błąd przy dodawaniu pacjenta. Odśwież stronę i spróbuj ponownie.");
                }
                searchPatients();
            }

        };
    } else {
        alert("Sprawdź poprawność danych pacjenta pesel to 11 cyfrowa liczba.");
    }
}

function deletePatient(pesel) {
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
                alert("Usunięto pacjenta");
            } else {
                alert("Nastąpił błąd podczas usuwania pacjenta. Prawdopodobnie są z nim powiązane pobyty");
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
                    alert("Zaktualizowano dane pacjenta");
                } else {
                    alert("Nastąpił błąd podczas aktualizowania danych pacjenta. Odśwież stronę i spróbuj ponownie");
                }
                searchPatients();
            }
        };
    } else {
        alert("Sprawdź poprawność danych pacjenta pesel to 11 cyfrowa liczba.");
    }
}

function addStay() {
    var id = document.getElementById("id-span").innerText;
    if (globalPesel === "") {
        window.location.assign("/" + id + "/manageStays");
    } else {
        window.location.assign("/" + id + "/manageStays?pesel=" + globalPesel)
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

