function getRooms() {
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    var sectionsSelect = document.getElementById("hospital-section-select");
    var roomsDiv = document.getElementById("rooms-div");
    fetch("/api/hospitalSections?hospitalId=" + hospitalId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var sections = data.sections;
            roomsDiv.innerHTML = "";
            sections.map(function (hospitalSection) {
                var op = document.createElement("option");
                op.value = hospitalSection.id;
                op.innerText = hospitalSection.id + "-" + hospitalSection.name;
                sectionsSelect.appendChild(op);

                var sectionDiv = document.createElement("div");
                sectionDiv.className = "section-div";
                roomsDiv.appendChild(sectionDiv);

                var nameLabel = document.createElement("label");
                nameLabel.className = "section-name-label";
                nameLabel.innerText = "Id" + hospitalSection.id + ". " + hospitalSection.name;
                sectionDiv.appendChild(nameLabel);

                fetch("/api/rooms?hospitalSectionId=" + hospitalSection.id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var rooms = data1.rooms;
                        if (rooms.length > 0) {
                            var table = document.createElement("table");
                            var thead = document.createElement("thead");
                            var tr = document.createElement("tr");

                            var idTh = document.createElement("th");
                            idTh.innerText = "ID pokoju";
                            tr.appendChild(idTh);

                            var floorTh = document.createElement("th");
                            floorTh.innerText = "Piętro";
                            tr.appendChild(floorTh);

                            var placesCountTh = document.createElement("th");
                            placesCountTh.innerText = "Liczba miejsc";
                            tr.appendChild(placesCountTh);

                            var actPlacedTh = document.createElement("th");
                            actPlacedTh.innerText = "Zajęte";
                            tr.appendChild(actPlacedTh);

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

                            rooms.map(function (room) {
                                var row = document.createElement("tr");

                                var idTd = document.createElement("td");
                                idTd.className = "text-td";
                                idTd.innerText = room.id;
                                row.appendChild(idTd);

                                var floorTd = document.createElement("td");
                                floorTd.className = "text-td";
                                floorTd.innerText = room.floor;
                                row.appendChild(floorTd);

                                var placesCountTd = document.createElement("td");

                                var countLabel = document.createElement("label");
                                countLabel.className = "table-text-label";
                                countLabel.innerText = room.number_of_places;
                                placesCountTd.appendChild(countLabel);

                                var countInput = document.createElement("input");
                                countInput.className = "text-input";
                                countInput.type = "number";
                                countInput.min = "1";
                                countInput.step = "1";
                                countInput.value = room.number_of_places;
                                countInput.style.display = "none";
                                placesCountTd.appendChild(countInput);

                                row.appendChild(placesCountTd);

                                var placedTd = document.createElement("td");
                                placedTd.className = "text-td";
                                placedTd.innerText = room.act_placed_count;
                                row.appendChild(placedTd);

                                var manageTd = document.createElement("td");

                                var acceptInput = document.createElement("input");
                                acceptInput.className = "accept-input";
                                acceptInput.type = "submit";
                                acceptInput.value = "Akceptuj";
                                acceptInput.style.display = "none";
                                acceptInput.onclick = function () {
                                    updateRoom(room.id, countInput.value, room);
                                };
                                manageTd.appendChild(acceptInput);


                                var manageInput = document.createElement("input");
                                manageInput.className = "manage-input";
                                manageInput.type = "submit";
                                manageInput.value = "Edytuj";
                                manageInput.onclick = function () {
                                    if (manageInput.value === "Edytuj") {
                                        manageInput.value = "Anuluj";
                                        countInput.style.display = "block";
                                        countInput.value = room.number_of_places;
                                        countLabel.style.display = "none";
                                        acceptInput.style.display = "block";
                                    } else {
                                        manageInput.value = "Edytuj";
                                        countInput.style.display = "none";
                                        countLabel.style.display = "block";
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
                                    deleteRoom(room.id);
                                };
                                deleteTd.appendChild(deleteInput);

                                row.appendChild(deleteTd);

                                table.appendChild(row);
                            })
                        } else {
                            sectionDiv.appendChild(document.createElement("br"));
                            var errorLabel = document.createElement("label");
                            errorLabel.className = "error-description-label";
                            errorLabel.innerText = "Ten oddział nie ma jeszcze pokoi!";
                            sectionDiv.appendChild(errorLabel);
                        }
                    }
                })
            })
        } else {
            var errorLabel = document.createElement("label");
            errorLabel.className = "error-description-label";
            errorLabel.innerText = "Nastąpił błąd podczas ładownia strony. Odśwież ją.";
            roomsDiv.appendChild(errorLabel);
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
                    alert("Zaktualizowano pokój");
                } else {
                    alert("Nastąpił błąd podczas aktualizowania pokoju, " +
                        " sprawdź czy liczba aktualnie zajętych miejsc nie jest większa od podanej liczby miejsc");
                }
                getRooms();
            }
        };
    } else {
        alert("błąd");
    }
}

function deleteRoom(id) {
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
                alert("Usunięto pokój");
            } else {
                alert("Nastąpił błąd podczas usuwania pokoju, " +
                    "sprawdź czy nie ma do niego przypisanych pobytów");
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
                    var room = response.room;
                    alert("Dodano pokój jego id to " + room.id);
                } else {
                    alert("Nastąpił błąd podczas dodawania pokoju, odśwież " +
                        "stronę i spróbuj ponownie");
                }
                getRooms();
            }
        };
    } else {
        alert("Sprawdź czy podano oddział, piętro oraz ilość miejsc," +
            " dwa ostatnie powinny wynosić najmiej 0.");
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

