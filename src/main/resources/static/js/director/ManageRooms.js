function getRooms() {
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    var sectionsSelect = document.getElementById("hospital-section-select");
    var roomsUl = document.getElementById("rooms-ul");
    fetch("/api/hospitalSections?hospitalId=" + hospitalId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var sections = data.sections;
            var i = 0;
            roomsUl.innerHTML = "";
            sections.map(function (hospitalSection) {
                var op = document.createElement("option");
                op.value = hospitalSection.id;
                op.innerText = hospitalSection.id + "-" + hospitalSection.name;
                sectionsSelect.appendChild(op);

                var sectionLi = document.createElement("li");
                roomsUl.appendChild(sectionLi);
                var nameLabel = document.createElement("label");
                nameLabel.innerText = i + ". id: " + hospitalSection.id + " " + hospitalSection.name;
                sectionLi.appendChild(nameLabel);


                var ul = document.createElement("ul");
                sectionLi.appendChild(ul);
                i++;
                fetch("/api/rooms?hospitalSectionId=" + hospitalSection.id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var rooms = data1.rooms;
                        var j = 0;
                        rooms.map(function (room) {
                            var li = document.createElement("li");

                            var roomLabel = document.createElement("label");
                            roomLabel.innerText = j + ". " + room.id + " l miejsc : " + room.number_of_places +
                                " l zajętyhc: " + room.act_placed_count + " piętro: " + room.floor;
                            li.appendChild(roomLabel);

                            var countInput = document.createElement("input");
                            countInput.type = "number";
                            countInput.value = room.number_of_places;
                            countInput.style.display = "none";
                            li.appendChild(countInput);

                            var acceptInput = document.createElement("input");
                            acceptInput.type = "submit";
                            acceptInput.value = "Akceptuj";
                            acceptInput.style.display = "none";
                            acceptInput.onclick = function () {
                                updateRoom(room.id, countInput.value, room);
                            };
                            li.appendChild(acceptInput);


                            var manageInput = document.createElement("input");
                            manageInput.type = "submit";
                            manageInput.value = "Edytuj";
                            manageInput.onclick = function () {
                                if (manageInput.value === "Edytuj") {
                                    manageInput.value = "Anuluj";
                                    roomLabel.innerText = j + ". " + room.id +
                                        " l zajętyhc: " + room.act_placed_count + " piętro: " + room.floor;
                                    countInput.style.display = "block";
                                    acceptInput.style.display = "block";
                                } else {
                                    manageInput.value = "Edytuj";
                                    roomLabel.innerText = j + ". " + room.id + " l miejsc : " + room.number_of_places +
                                        " l zajętyhc: " + room.act_placed_count + " piętro: " + room.floor;
                                    countInput.style.display = "none";
                                    acceptInput.style.display = "none";
                                }
                            };
                            li.appendChild(manageInput);

                            var deleteInput = document.createElement("input");
                            deleteInput.type = "submit";
                            deleteInput.value = "Usuń";
                            deleteInput.onclick = function () {
                                deleteRoom(room.id);
                            };
                            li.appendChild(deleteInput);


                            ul.appendChild(li);
                            j++;
                        })
                    }
                })
            })
        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert("error");
        }
    })
}


function updateRoom(id, count, room) {
    if (count >= 0) {
        var http = new XMLHttpRequest();
        var url = "/api/updateRoom";
        var data = JSON.stringify({
            "id": parseInt(id),
            "floor": parseInt(room.floor),
            "number_of_places": parseInt(count),
            "hospital_section_id": parseInt(room.hospital_section_id),
            "act_placed_count": parseInt(room.act_placed_count)
        });
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info

                    alert("Upate pokój");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getRooms();
            }
        };
    } else {
        alert("błąd");
    }
}

function deleteRoom(id) {
    //TODO potwierdzenie zamiaru
    var http = new XMLHttpRequest();
    var url = "/api/deleteRoom?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Usunięto pokój");
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getRooms();
        }
    };
}

function addRoom() {
    var floor = parseInt(document.getElementById("floor-input").value);
    var hospitalSectionSelect = document.getElementById("hospital-section-select");
    var hospitalSectionId = parseInt(hospitalSectionSelect.options[hospitalSectionSelect.selectedIndex].value);
    var placesCount = parseInt(document.getElementById("places-count-input").value);
    if (floor >= 0 && hospitalSectionId >= 0 && placesCount >= 0) {
        var http = new XMLHttpRequest();
        var url = "/api/addRoom";
        var data = JSON.stringify({
            "id": 0,
            "floor": floor,
            "number_of_places": placesCount,
            "hospital_section_id": hospitalSectionId,
            "act_placed_count": 0
        });
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    var room = response.room;
                    alert("Dodano pokój:" + room.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getRooms();
            }
        };
    } else {
        alert("Błąd");
    }
}