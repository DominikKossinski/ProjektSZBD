function addPesel() {
    var pesel = document.getElementById("pesel-span").innerText;
    if (pesel !== "") {
        document.getElementById("pesel-input").value = pesel;
    }
    reloadRooms();
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
            }
        };
    } else {
        //TODO ładne wyświetlanie info o błędzie
        alert("Błąd");
    }
    console.log(data);
}