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
            var salaries = data.salaries;
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
            var hospitalSections = data.sections;
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
                            alert("Dodano Pobyt id:" + doctor.id);
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