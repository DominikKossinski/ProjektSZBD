function getSalaries() {
    var salariesDiv = document.getElementById("salaries-div");
    fetch("/api/salary").then(
        function (value) {
            return value.json()
        }
    ).then(
        function (data) {
            console.log(data);
            if (data.resp_status === "ok") {
                var salaries = data.salaries;
                salariesDiv.innerHTML = "";
                if (salaries.length > 0) {
                    var table = document.createElement("table");
                    var thead = document.createElement("thead");
                    var tr = document.createElement("tr");

                    var nameTh = document.createElement("th");
                    nameTh.innerText = "Stanowisko";
                    tr.appendChild(nameTh);

                    var minTh = document.createElement("th");
                    minTh.innerText = "Płaca min";
                    tr.appendChild(minTh);

                    var maxTh = document.createElement("th");
                    maxTh.innerText = "Płaca max";
                    tr.appendChild(maxTh);

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


                    salariesDiv.appendChild(table);
                    salaries.map(function (salary) {
                        var row = document.createElement("tr");

                        var positionTd = document.createElement("td");
                        positionTd.className = "text-td";
                        positionTd.innerText = salary.position;
                        row.appendChild(positionTd);

                        var minTd = document.createElement("td");

                        var minLabel = document.createElement("label");

                        minLabel.className = "table-text-label";
                        minLabel.innerText = salary.min_salary;
                        minTd.appendChild(minLabel);

                        var minInput = document.createElement("input");
                        minInput.className = "text-input";
                        minInput.type = "number";
                        minInput.value = salary.min_salary;
                        minInput.style.display = "none";
                        minTd.appendChild(minInput);

                        row.appendChild(minTd);

                        var maxTd = document.createElement("td");

                        var maxLabel = document.createElement("label");
                        maxLabel.className = "table-text-label";
                        maxLabel.innerText = salary.max_salary;
                        maxTd.appendChild(maxLabel);

                        var maxInput = document.createElement("input");
                        maxInput.className = "text-input";
                        maxInput.type = "number";
                        maxInput.value = salary.max_salary;
                        maxInput.style.display = "none";
                        maxTd.appendChild(maxInput);

                        row.appendChild(maxTd);


                        var manageTd = document.createElement("td");

                        var acceptInput = document.createElement("input");
                        acceptInput.className = "accept-input";
                        acceptInput.type = "submit";
                        acceptInput.value = "Akceptuj";
                        acceptInput.style.display = "none";
                        acceptInput.onclick = function () {
                            updateSalary(salary.position, parseInt(minInput.value), parseInt(maxInput.value));
                        };
                        manageTd.appendChild(acceptInput);
                        console.log("99");
                        var manageInput = document.createElement("input");
                        manageInput.className = "manage-input";
                        manageInput.type = "submit";
                        manageInput.value = "Edytuj";
                        manageInput.onclick = function () {
                            if (manageInput.value === "Edytuj") {
                                manageInput.value = "Anuluj";
                                minInput.style.display = "block";
                                minInput.value = salary.min_salary;
                                maxInput.style.display = "block";
                                maxInput.value = salary.max_salary;
                                maxLabel.style.display = "none";
                                minLabel.style.display = "none";
                                acceptInput.style.display = "block";
                            } else {
                                manageInput.value = "Edytuj";
                                minInput.style.display = "none";
                                maxInput.style.display = "none";
                                acceptInput.style.display = "none";
                                maxLabel.style.display = "block";
                                minLabel.style.display = "block";
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
                            deleteSalary(salary.position);
                        };
                        deleteTd.appendChild(deleteInput);
                        row.appendChild(deleteTd);

                        tbody.appendChild(row);
                    })
                } else {
                    var label = document.createElement("label");
                    label.className = "error-description-label";
                    label.innerText = "Brak stanowisk. Dodaj stanowisko używając panelu po lewej stronie";
                    salariesDiv.appendChild(label);
                }
            } else {
                var label1 = document.createElement("label");
                label1.className = "error-description-label";
                label1.innerText = "Nastąpił błąd podczas ładowania stanowisk.";
                salariesDiv.appendChild(label1);
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
                    alert("Zaktualizowano stanowisko: " + position);
                } else {
                    alert("Nastąpił błąd podczas aktualizowania stanowiska," +
                        " spróbuj pownownie po odświeżeniu strony");
                }
                getSalaries();
            }
        };
    } else {
        alert("Błąd. Sprawdź czy płaca maksymalne jest większa od minimalnej!");
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
                alert("Usunięto stanowisko: " + position);
            } else {
                alert("Nastąpił błąd podczas usuwania stanowiska," +
                    " prawdopodobnie ktoś jest zatrudniony na takim stanowisku");
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
                    var salary = response.salary;
                    alert("Dodano stanowisko: " + salary.position);
                } else {
                    alert("Nastąpił błąd podczas dodawania stanowiska," +
                        " spróbuj pownownie po odświeżeniu strony");
                }
                getSalaries();
            }
        };
    } else {
        alert("Błąd. Sprawdź czy płaca maksymalne jest większa od minimalnej oraz" +
            " czy została podana nazwa stanowiska");
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

