var hospitalSections = [];
var salaries = [];

function loadData() {

    var positionSelect = document.getElementById("position-select");
    var hospitalSectionSelect = document.getElementById("hospital-section-select");
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    var roomsDiv = document.getElementById("rooms-div");

    fetch("/api/salary").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            salaries = data.salaries;
            salaries.map(function (salary) {
                if (salary.position !== "Dyrektor") {
                    var op = document.createElement("option");
                    op.innerText = salary.position;
                    op.value = salary.position;
                    positionSelect.appendChild(op);
                }
            })
        } else {
            alert(data.description);
        }

    });
    fetch("/api/hospitalSections?hospitalId=" + hospitalId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            hospitalSections = data.sections;
            getDoctors();
            hospitalSections.map(function (hospitalSection) {
                var op = document.createElement("option");
                op.innerText = "id-" + hospitalSection.id + " " + hospitalSection.name;
                op.value = hospitalSection.id;
                hospitalSectionSelect.appendChild(op);
            })
        } else {
            alert(data.description);
        }
    });
}

function getDoctors() {
    var doctorsDiv = document.getElementById("doctors-div");
    doctorsDiv.innerHTML = "";
    hospitalSections.map(
        function (hospitalSection) {
            fetch("/api/doctors?hospitalSectionId=" + hospitalSection.id).then(
                function (value) {
                    return value.json();
                }
            ).then(function (data) {
                console.log(data);
                if (data.resp_status === "ok") {
                    var sectionDiv = document.createElement("div");
                    sectionDiv.className = "section-div";

                    var label = document.createElement("label");
                    label.className = "section-name-label";
                    label.innerText = hospitalSection.id + ". " + hospitalSection.name;
                    sectionDiv.appendChild(label);


                    var doctors = data.doctors;
                    if (doctors.length > 0) {

                        var table = document.createElement("table");
                        var thead = document.createElement("thead");
                        var tr = document.createElement("tr");

                        var idTh = document.createElement("th");
                        idTh.innerText = "ID lekarza";
                        tr.appendChild(idTh);

                        var firstNameTh = document.createElement("th");
                        firstNameTh.innerText = "Imię";
                        tr.appendChild(firstNameTh);

                        var lastNameTh = document.createElement("th");
                        lastNameTh.innerText = "Nazwisko";
                        tr.appendChild(lastNameTh);

                        var salaryTh = document.createElement("th");
                        salaryTh.innerText = "Płaca";
                        tr.appendChild(salaryTh);

                        var positionTh = document.createElement("th");
                        positionTh.innerText = "Stanowisko";
                        tr.appendChild(positionTh);

                        var sectionTh = document.createElement("th");
                        sectionTh.innerText = "Oddział";
                        tr.appendChild(sectionTh);

                        var manageTh = document.createElement("th");
                        manageTh.innerText = "Edytuj";
                        tr.appendChild(manageTh);

                        var deleteTh = document.createElement("th");
                        deleteTh.innerText = "Usuń";
                        tr.appendChild(deleteTh);

                        thead.appendChild(tr);

                        table.appendChild(thead);

                        var tbody = document.createElement("tbody");
                        table.appendChild(tbody);


                        sectionDiv.appendChild(table);

                        doctors.map(function (doctor) {
                            var row = document.createElement("tr");

                            var idTd = document.createElement("td");
                            idTd.className = "text-td";
                            idTd.innerText = doctor.id;
                            row.appendChild(idTd);

                            var firstNameTd = document.createElement("td");

                            var firstNameLabel = document.createElement("label");
                            firstNameLabel.className = "table-text-label";
                            firstNameLabel.innerText = doctor.first_name;
                            firstNameTd.appendChild(firstNameLabel);

                            var firstNameInput = document.createElement("input");
                            firstNameInput.className = "text-input";
                            firstNameInput.type = "text";
                            firstNameInput.value = doctor.first_name;
                            firstNameInput.style.display = "none";
                            firstNameTd.appendChild(firstNameInput);

                            row.appendChild(firstNameTd);

                            var lastNameTd = document.createElement("td");

                            var lastNameLabel = document.createElement("label");
                            lastNameLabel.className = "table-text-label";
                            lastNameLabel.innerText = doctor.last_name;
                            lastNameTd.appendChild(lastNameLabel);


                            var lastNameInput = document.createElement("input");
                            lastNameInput.className = "text-input";
                            lastNameInput.type = "text";
                            lastNameInput.value = doctor.last_name;
                            lastNameInput.style.display = "none";
                            lastNameTd.appendChild(lastNameInput);

                            row.appendChild(lastNameTd);

                            var positionTd = document.createElement("td");

                            var positionLabel = document.createElement("label");
                            positionLabel.className = "table-text-label";
                            positionLabel.innerText = doctor.position;
                            positionTd.appendChild(positionLabel);

                            var positionSelect = document.createElement("select");
                            positionSelect.className = "text-input";
                            positionSelect.style.display = "none";
                            salaries.map(function (salary) {
                                var option = document.createElement("option");
                                option.value = salary.position;
                                option.innerText = salary.position;
                                positionSelect.appendChild(option);
                            });
                            for (var a = 0; a < positionSelect.length; a++) {
                                if (positionSelect[a].value === doctor.position) {
                                    positionSelect.selectedIndex = a;
                                    break;
                                }
                            }
                            positionTd.appendChild(positionSelect);

                            row.appendChild(positionTd);

                            var salaryTd = document.createElement("td");

                            var salaryLabel = document.createElement("label");
                            salaryLabel.className = "table-text-label";
                            salaryLabel.innerText = doctor.salary;

                            salaryTd.appendChild(salaryLabel);

                            var salaryInput = document.createElement("input");
                            salaryInput.className = "text-input";
                            salaryInput.type = "number";
                            salaryInput.value = doctor.salary;
                            salaryInput.style.display = "none";
                            salaryTd.appendChild(salaryInput);

                            row.appendChild(salaryTd);

                            var sectionTd = document.createElement("td");

                            var sectionLabel = document.createElement("label");
                            sectionLabel.className = "table-text-label";
                            sectionTd.appendChild(sectionLabel);

                            var sectionSelect = document.createElement("select");
                            sectionSelect.className = "text-input";
                            sectionSelect.style.display = "none";
                            hospitalSections.map(function (hospitalSection1) {
                                var option = document.createElement("option");
                                option.value = hospitalSection1.id;
                                option.innerText = hospitalSection1.id + "-" + hospitalSection1.name;
                                sectionSelect.appendChild(option);
                            });
                            for (a = 0; a < sectionSelect.length; a++) {
                                if (parseInt(sectionSelect[a].value) === parseInt(doctor.hospital_section_id)) {
                                    sectionSelect.selectedIndex = a;
                                    sectionLabel.innerText = sectionSelect[a].innerText;
                                    break;
                                }
                            }
                            sectionTd.appendChild(sectionSelect);

                            row.appendChild(sectionTd);

                            var manageTd = document.createElement("td");


                            var acceptInput = document.createElement("input");
                            acceptInput.className = "accept-input";
                            acceptInput.type = "submit";
                            acceptInput.value = "Akceptuj";
                            acceptInput.style.display = "none";
                            acceptInput.onclick = function () {
                                updateDoctor(doctor.id, firstNameInput.value, lastNameInput.value,
                                    positionSelect[positionSelect.selectedIndex].value,
                                    salaryInput.value,
                                    sectionSelect[sectionSelect.selectedIndex].value);
                            };
                            manageTd.appendChild(acceptInput);

                            var manageInput = document.createElement("input");
                            manageInput.className = "manage-input";
                            manageInput.type = "submit";
                            manageInput.value = "Edytuj";
                            manageInput.onclick = function () {
                                if (manageInput.value === "Edytuj") {
                                    manageInput.value = "Anuluj";
                                    firstNameInput.style.display = "block";
                                    firstNameLabel.style.display = "none";
                                    lastNameInput.style.display = "block";
                                    lastNameLabel.style.display = "none";
                                    positionSelect.style.display = "block";
                                    positionLabel.style.display = "none";
                                    salaryInput.style.display = "block";
                                    salaryLabel.style.display = "none";
                                    sectionSelect.style.display = "block";
                                    sectionLabel.style.display = "none";
                                    acceptInput.style.display = "block";
                                } else {
                                    manageInput.value = "Edytuj";
                                    firstNameInput.style.display = "none";
                                    firstNameInput.value = doctor.first_name;
                                    firstNameLabel.style.display = "block";
                                    lastNameInput.style.display = "none";
                                    lastNameInput.value = doctor.last_name;
                                    lastNameLabel.style.display = "block";
                                    positionSelect.style.display = "none";
                                    for (var a = 0; a < positionSelect.length; a++) {
                                        if (positionSelect[a].value === doctor.position) {
                                            positionSelect.selectedIndex = a;
                                            break;
                                        }
                                    }
                                    positionLabel.style.display = "block";
                                    salaryInput.style.display = "none";
                                    salaryInput.value = doctor.salary;
                                    salaryLabel.style.display = "block";
                                    sectionSelect.style.display = "none";
                                    for (a = 0; a < sectionSelect.length; a++) {
                                        if (parseInt(sectionSelect[a].value) === parseInt(doctor.hospital_section_id)) {
                                            sectionSelect.selectedIndex = a;
                                            break;
                                        }
                                    }
                                    sectionLabel.style.display = "block";
                                    acceptInput.style.display = "none";

                                }
                            };
                            manageTd.appendChild(manageInput);

                            row.appendChild(manageTd);

                            var deleteTd = document.createElement("td");
                            var deleteInput = document.createElement("input");
                            deleteInput.className = "delete-input";
                            deleteInput.type = "submit";
                            deleteInput.value = "Usuń";
                            deleteInput.onclick = function () {
                                deleteDoctor(doctor.id);
                            };
                            deleteTd.appendChild(deleteInput);

                            row.appendChild(deleteTd);

                            tbody.appendChild(row);

                        });
                        sectionDiv.appendChild(table);
                        doctorsDiv.appendChild(sectionDiv);
                    } else {
                        sectionDiv.appendChild(document.createElement("br"));
                        var errorLabel = document.createElement("label");
                        errorLabel.className = "error-description-label";
                        errorLabel.innerText = "Ten oddział nie ma jeszcze lekarzy!";
                        sectionDiv.appendChild(errorLabel);
                        doctorsDiv.appendChild(sectionDiv);
                    }


                } else {
                    alert("Nastąpił błąd podczas ładowania strony. Odśwież ją.");
                }
            })
        }
    )
}


function updateDoctor(id, firstName, lastName, position,
                      salary, hospitalSectionId) {

    fetch("/api/salary?position=" + position).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        if (data.resp_status === "ok") {
            var s = data.salary;
            if (firstName !== "" && lastName !== ""
                && salary >= s.min_salary && salary <= s.max_salary) {
                var doctorData = JSON.stringify({
                    "id": id,
                    "first_name": firstName,
                    "last_name": lastName,
                    "salary": salary,
                    "hospital_section_id": hospitalSectionId,
                    "position": position,
                    "password": ""

                });
                var http = new XMLHttpRequest();
                var url = "/api/updateDoctor";
                http.open("Post", url, true);
                http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
                http.send(doctorData);
                http.onreadystatechange = function (e) {
                    if (http.readyState === 4) {
                        var response = JSON.parse(http.responseText);
                        console.log(response);
                        if (response.resp_status === "ok") {
                            alert("Zaktualizowano dane lekarza");
                        } else {
                            alert("Próba aktualizacji danych lekarza zakończyła się niepowodzeniem");
                        }
                        getDoctors();
                    }
                };
            } else {
                alert("Podano błędne dane sprawdź czy imię, nazwisko nie są puste," +
                    " a płaca jest między <" + s.min_salary + "; " + s.max_salary + ">!");
            }
        } else {
            alert("Próba aktualizacji danych lekarza zakończyła się niepowodzeniem");
        }

    });
}

function deleteDoctor(id) {
    var http = new XMLHttpRequest();
    var url = "/api/deleteDoctor?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                alert("Pomyślnie usunięto lekarza");
            } else {
                alert("Nie można usunąć lekarza, prawdopodobnie są jeszcze z nim powiązane pobyty");
            }
            getDoctors();
        }
    };
}

function addDoctor() {
    var firstName = document.getElementById("first-name-input").value;
    var lastName = document.getElementById("last-name-input").value;
    var salary = parseInt(document.getElementById("salary-input").value);
    var password = document.getElementById("password-input").value;
    var positionSelect = document.getElementById("position-select");
    var hospitalSectionSelect = document.getElementById("hospital-section-select");
    var position = positionSelect.options[positionSelect.selectedIndex].value;
    var hospitalSectionId = parseInt(hospitalSectionSelect.options[hospitalSectionSelect.selectedIndex].value);
    var doctorData = JSON.stringify({
        "id": 0,
        "first_name": firstName,
        "last_name": lastName,
        "salary": salary,
        "hospital_section_id": hospitalSectionId,
        "position": position,
        "password": password

    });
    console.log(doctorData);
    fetch("/api/salary?position=" + position).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        if (data.resp_status === "ok") {
            var s = data.salary;
            if (firstName !== "" && lastName !== "" && password !== ""
                && salary >= s.min_salary && salary <= s.max_salary) {
                var http = new XMLHttpRequest();
                var url = "/api/addDoctor";
                http.open("Put", url, true);
                http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
                http.send(doctorData);
                http.onreadystatechange = function (e) {
                    if (http.readyState === 4) {
                        var response = JSON.parse(http.responseText);
                        console.log(response);
                        if (response.resp_status === "ok") {
                            var doctor = response.doctor;
                            alert("Dodano Lekarza id:" + doctor.id);
                        } else {
                            alert("Nastąpił błąd przy dodawaniu lekarza. Odśwież stronę i spróbuj jeszcze raz");
                        }
                        getDoctors();
                    }
                };
            } else {
                s = data.salary;
                console.log(s);
                alert("Podano błędne dane sprawdź czy imię, nazwisko nie są puste," +
                    " a płaca jest między <" + s.min_salary + "; " + s.max_salary + ">!");
            }
        } else {
            alert("Nastąpił błąd przy dodawaniu lekarza. Odśwież stronę i spróbuj jeszcze raz");
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

