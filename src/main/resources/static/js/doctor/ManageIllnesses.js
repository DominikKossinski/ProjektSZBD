function getIllnesses() {
    var pattern = document.getElementById("name-search-input").value;
    var illnessesDiv = document.getElementById("illnesses-div");
    illnessesDiv.innerHTML = "";
    fetch("/api/illness?pattern=" + pattern).then(
        function (value) {
            return value.json()
        }
    ).then(function (data) {
        if (data.resp_status === "ok") {
            var illnesses = data.illnesses;
            if (illnesses.length === 0) {
                var label = document.createElement("label");
                label.className = "error-description-label";
                label.innerText = "Nie znaleziono chorób. Dodaj chorobę, aby ją wyświetlić";
                illnessesDiv.appendChild(label);
            }
            illnesses.map(function (illness) {
                var illnessDiv = document.createElement("div");
                illnessDiv.className = "illness-div";

                var nameLabel = document.createElement("label");
                nameLabel.className = "illness-name-label";
                nameLabel.innerText = illness.name;
                illnessDiv.appendChild(nameLabel);

                var nameInput = document.createElement("input");
                nameInput.className = "illness-name-input";
                nameInput.type = "text";
                nameInput.value = illness.name;
                nameInput.style.display = "none";
                illnessDiv.appendChild(nameInput);

                var descriptionP = document.createElement("p");
                descriptionP.className = "illness-description-p";
                if (illness.description !== "null") {
                    descriptionP.innerText = illness.description;
                } else {
                    descriptionP.innerText = "Przypadek nie opisany";
                }
                illnessDiv.appendChild(descriptionP);

                var descriptionTextArea = document.createElement("textarea");
                descriptionTextArea.className = "illness-description-textarea";
                if (illness.description !== "null") {
                    descriptionTextArea.value = illness.description;
                } else {
                    descriptionTextArea.value = "";
                }
                descriptionTextArea.style.display = "none";
                illnessDiv.appendChild(descriptionTextArea);

                var acceptInput = document.createElement("input");
                acceptInput.className = "accept-input";
                acceptInput.type = "submit";
                acceptInput.value = "Akceptuj";
                acceptInput.style.display = "none";
                acceptInput.onclick = function () {
                    updateIllness(illness.id, nameInput.value, descriptionTextArea.value);
                };
                illnessDiv.appendChild(acceptInput);

                var manageInput = document.createElement("input");
                manageInput.className = "manage-input";
                manageInput.type = "submit";
                manageInput.value = "Edytuj";
                manageInput.onclick = function () {
                    if (manageInput.value === "Edytuj") {
                        manageInput.value = "Anuluj";
                        nameInput.style.display = "block";
                        nameInput.value = illness.name;
                        nameLabel.style.display = "none";
                        descriptionTextArea.style.display = "block";
                        if (illness.description !== "null") {
                            descriptionTextArea.value = illness.description;
                        } else {
                            descriptionTextArea.value = "";
                        }
                        descriptionP.style.display = "none";
                        acceptInput.style.display = "inline-block";
                    } else {
                        manageInput.value = "Edytuj";
                        nameInput.style.display = "none";
                        nameLabel.style.display = "block";
                        descriptionTextArea.style.display = "none";
                        descriptionP.style.display = "block";
                        acceptInput.style.display = "none";
                    }
                };
                illnessDiv.appendChild(manageInput);

                var deleteInput = document.createElement("input");
                deleteInput.className = "delete-input";
                deleteInput.type = "submit";
                deleteInput.value = "Usuń";
                deleteInput.onclick = function () {
                    deleteIllness(illness.id);
                };
                illnessDiv.appendChild(deleteInput);

                illnessesDiv.appendChild(illnessDiv);
            })
        } else {
            var label1 = document.createElement("label");
            label1.className = "error-description-label";
            label1.innerText = "Natąpił błąd podczas ładowania chorób";
            illnessesDiv.appendChild(label1);
        }
    })
}

function updateIllness(id, name, description) {
    var data = JSON.stringify({
        "id": id,
        "name": name,
        "description": description
    });
    console.log(data);
    if (name !== "") {
        var http = new XMLHttpRequest();
        var url = "/api/updateIllness";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    alert("Zaktualizowano pomyślnie chorobę " + name);
                } else {
                    alert("Nastąpił błąd podczas aktualizowania choroby. " +
                        "Odśwież stronę i spróbuj ponownie.");
                }
                getIllnesses();
            }
        };
    }
}

function deleteIllness(id) {
    var http = new XMLHttpRequest();
    var url = "/api/deleteIllness?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                alert("Usunięto chorobę");
            } else {
                alert("Nastąpił błąd podczas usuwania choroby. " +
                    "Sprawdź czy nie ma przypisanych do niej recept.");
            }
            getIllnesses()
        }
    };
}

function addIllness() {
    var name = document.getElementById("name-input").value;
    var description = document.getElementById("description-textarea").value;
    var data = JSON.stringify({
        "id": 0,
        "name": name,
        "description": description
    });
    console.log(data);
    if (name !== "") {
        var http = new XMLHttpRequest();
        var url = "/api/addIllness";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    alert("Dodano Chorobę");
                } else {
                    alert("Nastąpił błąd podczas dodawania choroby. " +
                        "Odśwież stronę i spróbuj ponownie.");
                }
                getIllnesses()
            }
        };
    } else {
        alert("Nazwa choroby nie może być pusta!");
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

