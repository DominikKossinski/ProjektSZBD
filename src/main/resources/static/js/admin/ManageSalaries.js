function getSalaries() {
    var salariesUl = document.getElementById("salaries-ul");
    fetch("/api/salary").then(
        function (value) {
            return value.json()
        }
    ).then(
        function (data) {
            console.log(data);
            if (data.resp_status === "ok") {
                var salaries = data.salaries;
                salariesUl.innerHTML = "";
                salaries.map(function (salary) {
                    var li = document.createElement("li");

                    var label = document.createElement("label");
                    label.innerText = salary.position + " min: " + salary.min_salary + " max: " + salary.max_salary;
                    li.appendChild(label);

                    var minInput = document.createElement("input");
                    minInput.type = "number";
                    minInput.value = salary.min_salary;
                    minInput.style.display = "none";
                    li.appendChild(minInput);

                    var maxInput = document.createElement("input");
                    maxInput.type = "number";
                    maxInput.value = salary.max_salary;
                    maxInput.style.display = "none";
                    li.appendChild(maxInput);

                    var acceptInput = document.createElement("input");
                    acceptInput.type = "submit";
                    acceptInput.value = "Akceptuj";
                    acceptInput.style.display = "none";
                    acceptInput.onclick = function () {
                        updateSalary(salary.position, parseInt(minInput.value), parseInt(maxInput.value));
                    };
                    li.appendChild(acceptInput);

                    var manageInput = document.createElement("input");
                    manageInput.type = "submit";
                    manageInput.value = "Edytuj";
                    manageInput.onclick = function () {
                        if (manageInput.value === "Edytuj") {
                            manageInput.value = "Anuluj";
                            minInput.style.display = "block";
                            maxInput.style.display = "block";
                            acceptInput.style.display = "block";
                        } else {
                            manageInput.value = "Edytuj";
                            minInput.style.display = "none";
                            maxInput.style.display = "none";
                            acceptInput.style.display = "none";
                        }
                    };
                    li.appendChild(manageInput);

                    var deleteInput = document.createElement("input");
                    deleteInput.type = "submit";
                    deleteInput.value = "Usuń";
                    deleteInput.onclick = function () {
                        deleteSalary(salary.position);
                    };
                    li.appendChild(deleteInput);
                    salariesUl.appendChild(li);
                })
            } else {
                //TODO ładniejsze info o błędzie
                alert("error")
            }
        }
    )
}

function updateSalary(position, minSalary, maxSalary) {
    if (minSalary <= maxSalary) {
        var data = JSON.stringify({
            "position": position,
            "min_salary": minSalary,
            "max_salary": maxSalary
        });
        var http = new XMLHttpRequest();
        var url = "/api/updateSalary";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    alert("Update stanowisko: " + position);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getSalaries();
            }
        };
    } else {
        //todo ładniejsze wyświetlanie błędu
        alert("błąd");
    }
}


function deleteSalary(position) {
    var http = new XMLHttpRequest();
    var url = "/api/deleteSalary?position=" + position;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Usunięto stanowisko: " + position);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getSalaries();
        }
    };
}

function addSalary() {
    var position = document.getElementById("position-input").value;
    var minSalary = parseFloat(document.getElementById("min-salary-input").value);
    var maxSalary = parseFloat(document.getElementById("max-salary-input").value);
    if (minSalary <= maxSalary && position !== "") {
        var data = JSON.stringify({
            "position": position,
            "min_salary": minSalary,
            "max_salary": maxSalary
        });
        var http = new XMLHttpRequest();
        var url = "/api/addSalary";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    var salary = response.salary;
                    alert("Dodano stanowisko: " + salary.position);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getSalaries();
            }
        };
    } else {
        //todo ładniejsze wyświetlanie błędu
        alert("błąd");
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

