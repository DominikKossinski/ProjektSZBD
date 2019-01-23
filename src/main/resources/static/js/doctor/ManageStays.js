function addPesel() {
    var pesel = document.getElementById("pesel-span").innerText;
    if (pesel !== "") {
        document.getElementById("pesel-input").value = pesel;
    } else {
        var peselDataList = document.getElementById("pesel-datalist");
        peselDataList.innerHTML = "";
        fetch("/api/patient").then(
            function (value) {
                return value.json();
            }
        ).then(function (data) {
            console.log(data);
            if (data.resp_status === "ok") {
                var patients = data.patients;
                patients.map(function (patient) {
                    var op = document.createElement("option");
                    op.value = patient.pesel;
                    op.innerText = patient.pesel;
                    peselDataList.appendChild(op);
                })
            } else {
                alert("Nastąpił błąd podczas ładowania peseli. Odśwież stronę.");
            }
        })
    }
    reloadRooms();
    getStays();
}

function reloadRooms() {
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    fetch("/api/rooms?hospitalSectionId=" + hospitalSectionId + "&free=true").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var rooms = data.rooms;
            var roomSeclect = document.getElementById("rooms-id-select");
            roomSeclect.innerHTML = "";
            rooms.map(function (room) {
                var op = document.createElement("option");
                op.value = room.id;
                op.innerText = "room" + room.id;
                roomSeclect.appendChild(op);
            })
        } else {
            alert(data.description);
        }
    });
}

function addStay() {
    var pesel = document.getElementById("pesel-input").value;
    var id = document.getElementById("id-span").innerText;
    var date = document.getElementById("date-input").value;
    var select = document.getElementById("rooms-id-select");
    var roomId = select.options[select.selectedIndex].value;
    var data = JSON.stringify({
        "id": 0,
        "start_date": date,
        "end_date": null,
        "room_id": parseInt(roomId),
        "doctor_id": parseInt(id),
        "pesel": parseInt(pesel)
    });
    if (parseInt(pesel) >= 10000000000 && parseInt(pesel) < 100000000000 && id !== "" && date !== "" && roomId !== "") {
        var http = new XMLHttpRequest();
        var url = "/api/addStay";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    alert("Dodano Pobyt");
                } else {
                    alert("Nastąpił błąd podczas dodawania pobytu. Odśwież stronę.");
                }
                reloadRooms();
                getStays();
            }
        };
    } else {
        alert("Podano złe dane. Sprawdź poprawność danych dodawanego pobytu.");
    }
    console.log(data);
}

function getStays() {
    var id = document.getElementById("id-span").innerText;
    var pesel = document.getElementById("pesel-span").innerText;
    if (pesel !== "") {
        fetch("/api/stays?pesel=" + pesel).then(
            function (value) {
                return value.json();
            }
        ).then(function (data) {
            console.log(data);
            if (data.resp_status === "ok") {
                var stays = data.stays;
                mapStays(stays)
            } else {
                alert("Nastąpił błąd podczas ładowania pobytów. Odśwież stronę.");
            }
        })
    } else {
        fetch("/api/stays?doctorId=" + id).then(
            function (value) {
                return value.json();
            }
        ).then(function (data) {
            console.log(data);
            if (data.resp_status === "ok") {
                var stays = data.stays;
                mapStays(stays)
            } else {
                alert("Nastąpił błąd podczas ładowania pobytów. Odśwież stronę.");
            }
        })
    }

}

function mapStays(stays) {

    var patientsTbody = document.getElementById("patients-tbody");
    patientsTbody.innerHTML = "";

    stays.map(function (stay) {

        var row = document.createElement("tr");

        var numTd = document.createElement("td");
        numTd.innerText = stay.id;
        numTd.className = "text-td";
        row.append(numTd);

        var peselTd = document.createElement("td");
        peselTd.className = "text-td";
        peselTd.innerText = stay.pesel;
        row.append(peselTd);

        var firstNameTd = document.createElement("td");
        var firstNameLabel = document.createElement("label");
        firstNameLabel.className = "table-text-label";
        firstNameLabel.innerText = stay.start_date;
        firstNameTd.appendChild(firstNameLabel);

        row.append(firstNameTd);

        var lastNameTd = document.createElement("td");
        var lastNameLabel = document.createElement("label");
        lastNameLabel.className = "table-text-label";
        var end_d = stay.end_date;
        if (end_d === "null") {
            end_d = "-";
        }
        lastNameLabel.innerText = end_d;
        lastNameTd.appendChild(lastNameLabel);

        var lastNameInput = document.createElement("input");
        lastNameInput.type = "date";

        lastNameInput.value = end_d;
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
            updateStay(stay, lastNameInput.value);
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
                lastNameInput.style.display = "inline-block";
                lastNameInput.value = stay.end_date;
                lastNameLabel.style.display = "none";
                acceptInput.style.display = "inline-block";
            } else {
                manageInput.value = "Edytuj";
                manageInput.style.height = "100%";
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
            deleteStay(stay.id);
        };
        deleteTd.appendChild(deleteInput);

        row.append(deleteTd);
        patientsTbody.append(row);
    })
}


function updateStay(stay, endDate) {
    var data = JSON.stringify({
        "id": stay.id,
        "start_date": stay.start_date,
        "end_date": endDate,
        "room_id": stay.room_id,
        "doctor_id": stay.doctor_id,
        "pesel": stay.pesel
    });

    var http = new XMLHttpRequest();
    var url = "/api/updateStay";
    http.open("Post", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send(data);
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                alert("Zaktualizowano pobyt");
            } else {
                alert("Nastąpił błąd podczas aktualizowania pobytu. Sprawdź czy podano datę późniejszą niż data startu");
            }
            reloadRooms();
            getStays()
        }
    };

}

function deleteStay(id) {
    var http = new XMLHttpRequest();
    var url = "/api/deleteStay?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                alert("Usunięto pobyt");
            } else {
                alert("Nastąpił błąd podczas usuwania pobytu. Prawdopodobnie są z nim powiązane recepty");
            }
            reloadRooms();
            getStays()
        }
    };
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

