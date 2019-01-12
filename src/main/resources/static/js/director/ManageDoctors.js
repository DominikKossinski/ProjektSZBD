var hospitalSections = [];
var salaries = [];

function loadData() {
    var positionSelect = document.getElementById("position-select");
    var hospitalSectionSelect = document.getElementById("hospital-section-select");
    var hospitalId = document.getElementById("hospital-id-span").innerText;
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
    var doctorsUl = document.getElementById("doctors-ul");
    doctorsUl.innerHTML = "";
    var i = 0;
    hospitalSections.map(
        function (hospitalSection) {
            fetch("/api/doctors?hospitalSectionId=" + hospitalSection.id).then(
                function (value) {
                    return value.json();
                }
            ).then(function (data) {
                console.log(data);
                if (data.resp_status === "ok") {
                    var sectionLi = document.createElement("li");

                    var label = document.createElement("label");
                    label.innerText = i + ". " + hospitalSection.name;
                    sectionLi.appendChild(label);

                    var dUl = document.createElement("ul");


                    var doctors = data.doctors;
                    var j = 0;
                    doctors.map(function (doctor) {
                        var doctorLi = document.createElement("li");

                        var doctorLabel = document.createElement("label");
                        doctorLabel.innerText = j + ". Id:" + doctor.id + " imie: " + doctor.first_name + " nazwisko " +
                            doctor.last_name + " płaca: " + doctor.salary + " stanowisko: " + doctor.position;
                        doctorLi.appendChild(doctorLabel);

                        var firstNameInput = document.createElement("input");
                        firstNameInput.type = "text";
                        firstNameInput.value = doctor.first_name;
                        firstNameInput.style.display = "none";
                        doctorLi.appendChild(firstNameInput);


                        var lastNameInput = document.createElement("input");
                        lastNameInput.type = "text";
                        lastNameInput.value = doctor.last_name;
                        lastNameInput.style.display = "none";
                        doctorLi.appendChild(lastNameInput);

                        var positionSelect = document.createElement("select");
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
                        doctorLi.appendChild(positionSelect);

                        var salaryInput = document.createElement("input");
                        salaryInput.type = "number";
                        salaryInput.value = doctor.salary;
                        salaryInput.style.display = "none";
                        doctorLi.appendChild(salaryInput);

                        var sectionSelect = document.createElement("select");
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
                                break;
                            }
                        }
                        doctorLi.appendChild(sectionSelect);


                        var acceptInput = document.createElement("input");
                        acceptInput.type = "submit";
                        acceptInput.value = "Akceptuj";
                        acceptInput.style.display = "none";
                        acceptInput.onclick = function () {
                            updateDoctor(doctor.id, firstNameInput.value, lastNameInput.value,
                                positionSelect[positionSelect.selectedIndex].value,
                                salaryInput.value,
                                sectionSelect[sectionSelect.selectedIndex].value);
                        };
                        doctorLi.appendChild(acceptInput);

                        var manageInput = document.createElement("input");
                        manageInput.type = "submit";
                        manageInput.value = "Edytuj";
                        manageInput.onclick = function () {
                            if (manageInput.value === "Edytuj") {
                                manageInput.value = "Anuluj";
                                doctorLabel.innerText = j + ". Id: " + doctor.id;
                                firstNameInput.style.display = "block";
                                lastNameInput.style.display = "block";
                                positionSelect.style.display = "block";
                                salaryInput.style.display = "block";
                                sectionSelect.style.display = "block";
                                acceptInput.style.display = "block";
                            } else {
                                //TODO resetowanie value inputów
                                manageInput.value = "Edytuj";
                                doctorLabel.innerText = j + ". Id:" + doctor.id + " imie: " + doctor.first_name + " nazwisko " +
                                    " płaca: " + doctor.salary + " stanowisko: " + doctor.position;
                                firstNameInput.style.display = "none";
                                firstNameInput.value = doctor.first_name;
                                lastNameInput.style.display = "none";
                                lastNameInput.value = doctor.last_name;
                                positionSelect.style.display = "none";
                                for (var a = 0; a < positionSelect.length; a++) {
                                    if (positionSelect[a].value === doctor.position) {
                                        positionSelect.selectedIndex = a;
                                        break;
                                    }
                                }
                                salaryInput.style.display = "none";
                                salaryInput.value = doctor.salary;
                                sectionSelect.style.display = "none";
                                for (a = 0; a < sectionSelect.length; a++) {
                                    if (parseInt(sectionSelect[a].value) === parseInt(doctor.hospital_section_id)) {
                                        sectionSelect.selectedIndex = a;
                                        break;
                                    }
                                }
                                acceptInput.style.display = "none";

                            }
                        };
                        doctorLi.appendChild(manageInput);

                        var deleteInput = document.createElement("input");
                        deleteInput.type = "submit";
                        deleteInput.value = "Usuń";
                        deleteInput.onclick = function () {
                            deleteDoctor(doctor.id);
                        };
                        doctorLi.appendChild(deleteInput);

                        dUl.appendChild(doctorLi);
                        j++;
                    });

                    sectionLi.appendChild(dUl);
                    doctorsUl.appendChild(sectionLi);
                    i++;

                } else {
                    alert(data.description);
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
                            //TODO ładniejsze info
                            alert("Update Lekarza");
                        } else {
                            //TODO lepsze wyswietlanie errora
                            alert(response.description);
                        }
                        getDoctors();
                    }
                };
            } else {
                alert("bład");
            }
        } else {
            alert(data.description)
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
                //TODO ładniejsze info
                alert("usunięto Lekarza id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
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
                            //TODO ładniejsze info - konieczne bo tylko tu zobaczysz id
                            var doctor = response.doctor;
                            alert("Dodano Lekarza id:" + doctor.id);
                        } else {
                            //TODO lepsze wyswietlanie errora
                            alert(response.description);
                        }
                    }
                };
            } else {
                alert("bład");
            }
        } else {
            alert(data.description)
        }
    })
}