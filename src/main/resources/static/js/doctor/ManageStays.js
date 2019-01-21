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
                //todo łeniejsza informacja o błędzie
                alert("Error");
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
    //TODO sprawdznie poprawności danych i fech do api
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
                    //TODO ładniejsze info
                    var stay = response.stay;
                    alert("Dodano Pobyt id:" + stay.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                reloadRooms();
                getStays();
            }
        };
    } else {
        //TODO ładne wyświetlanie info o błędzie
        alert("Błąd");
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
                //todo łaniejsze wyświetlanie błędu
                alert(data.description);
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
                //todo łaniejsze wyświetlanie błędu
                alert(data.description);
            }
        })
    }

}

function mapStays(stays) {
    var staysUl = document.getElementById("stays-ul");
    staysUl.innerHTML = "";
    stays.map(function (stay) {
        var stayLi = document.createElement("li");

        var label = document.createElement("label");
        label.innerText = "id" + stay.id + " pesel" + stay.pesel + " data startu" + stay.start_date +
            "data konca" + stay.end_date;
        stayLi.appendChild(label);

        var endDateInput = document.createElement("input");
        endDateInput.type = "date";
        endDateInput.value = stay.end_date;
        endDateInput.style.display = "none";
        stayLi.appendChild(endDateInput);

        var acceptButton = document.createElement("input");
        acceptButton.type = "submit";
        acceptButton.value = "Akceptuj";
        acceptButton.style.display = "none";
        acceptButton.onclick = function () {
            updateStay(stay, endDateInput.value);
        };
        stayLi.appendChild(acceptButton);

        var manageInput = document.createElement("input");
        manageInput.type = "submit";
        manageInput.value = "Edytuj";
        manageInput.onclick = function () {
            if (manageInput.value === "Edytuj") {
                manageInput.value = "Anuluj";
                label.innerText = "id" + stay.id + " pesel" + stay.pesel + " data startu" + stay.start_date;
                endDateInput.value = stay.end_date;
                endDateInput.style.display = "block";
                acceptButton.style.display = "block";
            } else {
                manageInput.value = "Edytuj";
                label.innerText = "id" + stay.id + " pesel" + stay.pesel + " data startu" + stay.start_date +
                    "data konca" + stay.end_date;
                endDateInput.style.display = "none";
                acceptButton.style.display = "none";
            }
        };
        stayLi.appendChild(manageInput);

        var deleteInput = document.createElement("input");
        deleteInput.type = "submit";
        deleteInput.value = "Usuń";
        deleteInput.onclick = function () {
            deleteStay(stay.id);
        };
        stayLi.appendChild(deleteInput);

        staysUl.appendChild(stayLi);
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
                //TODO ładniejsze info
                alert("update Pobyt id:" + stay.id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
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
                //TODO ładniejsze info
                alert("delete Pobyt id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
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

