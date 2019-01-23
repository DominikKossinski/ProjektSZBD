function addHospital() {
    var hospitalName = document.getElementById("name-input").value;
    var address = document.getElementById("address-input").value;
    var city = document.getElementById("city-input").value;
    var sectionName = document.getElementById("section-name-input").value;
    var firstName = document.getElementById("first-name-input").value;
    var lastName = document.getElementById("last-name-input").value;
    var salary = document.getElementById("salary-input").value;
    var password = document.getElementById("password-input").value;

    var minSalary = 0;
    var maxSalary = -1;
    fetch("/api/salary?position=Dyrektor").then(function (value) {
        return value.json();
    }).then(function (response) {
        console.log(response);
        if (response.resp_status === "ok") {
            minSalary = response.salary.min_salary;
            maxSalary = response.salary.max_salary;
            if (hospitalName !== "" && address !== "" && city !== "" && sectionName !== "" && firstName !== "" &&
                lastName !== "" && salary <= maxSalary && salary >= minSalary && password !== "") {
                var data = JSON.stringify({
                    "hospital": {
                        "id": 0,
                        "name": hospitalName,
                        "address": address,
                        "city": city
                    },
                    "hospital_section": {
                        "id": 0,
                        "name": sectionName,
                        "hospital_id": 0
                    },
                    "doctor": {
                        "id": 0,
                        "first_name": firstName,
                        "last_name": lastName,
                        "salary": salary,
                        "hospital_section_id": 0,
                        "position": "Dyrektor",
                        "password": password
                    }
                });
                var http = new XMLHttpRequest();
                var url = "/api/admin/addHospital";
                http.open("Put", url, true);
                http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
                http.send(data);
                console.log(data);
                http.onreadystatechange = function (e) {
                    if (http.readyState === 4) {
                        var response = JSON.parse(http.responseText);
                        console.log(response);
                        if (response.resp_status === "ok") {
                            alert("Dodano szpital");
                        } else {
                            alert("Nastąpił błąd podczas dodawania szpitala," +
                                " spróbuj pownownie po odświeżeniu strony");
                        }
                    }
                };
            } else {
                alert("Sprawdź poprawność danych")
            }
        } else {
            alert("Nastąpił błąd podczas dodawania szpitala," +
                " spróbuj pownownie po odświeżeniu strony");
        }
    });


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

