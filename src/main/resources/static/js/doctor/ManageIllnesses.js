function getIllnesses() {
    var pattern = document.getElementById("name-search-input").value;
    var illnessesUl = document.getElementById("illnesses-ul");
    illnessesUl.innerHTML = "";
    fetch("/api/illness?pattern=" + pattern).then(
        function (value) {
            return value.json()
        }
    ).then(function (data) {
        if (data.resp_status === "ok") {
            var illnesses = data.illnesses;
            var i = 0;
            illnesses.map(function (illness) {
                var illnessLi = document.createElement("li");

                var label = document.createElement("label");
                if (illness.description !== "null") {
                    label.innerText = i + ". " + illness.name + " - " + illness.description;
                } else {
                    label.innerText = i + ". " + illness.name;
                }
                illnessLi.appendChild(label);

                var nameInput = document.createElement("input");
                nameInput.type = "text";
                nameInput.value = illness.name;
                nameInput.style.display = "none";
                illnessLi.appendChild(nameInput);

                var descriptionTextArea = document.createElement("textarea");
                descriptionTextArea.value = illness.description;
                descriptionTextArea.style.display = "none";
                illnessLi.appendChild(descriptionTextArea);

                var acceptInput = document.createElement("input");
                acceptInput.type = "submit";
                acceptInput.value = "Akceptuj";
                acceptInput.style.display = "none";
                acceptInput.onclick = function (ev) {
                    updateIllness(illness.id, nameInput.value, descriptionTextArea.value);
                };
                illnessLi.appendChild(acceptInput);

                var manageInput = document.createElement("input");
                manageInput.type = "submit";
                manageInput.value = "Edytuj";
                manageInput.onclick = function () {
                    if (manageInput.value === "Edytuj") {
                        label.innerText = i + ". ";
                        manageInput.value = "Anuluj";
                        nameInput.style.display = "block";
                        descriptionTextArea.style.display = "block";
                        acceptInput.style.display = "block";
                    } else {
                        if (illness.description !== "null") {
                            label.innerText = i + ". " + illness.name + " - " + illness.description;
                        } else {
                            label.innerText = i + ". " + illness.name;
                        }
                        manageInput.value = "Edytuj";
                        nameInput.style.display = "none";
                        nameInput.value = illness.name;
                        descriptionTextArea.style.display = "none";
                        descriptionTextArea.value = illness.description;
                        acceptInput.style.display = "none";
                    }
                };
                illnessLi.appendChild(manageInput);

                var deleteInput = document.createElement("input");
                deleteInput.type = "submit";
                deleteInput.value = "Usuń";
                deleteInput.onclick = function () {
                    deleteIllness(illness.id);
                };
                illnessLi.appendChild(deleteInput);

                illnessesUl.appendChild(illnessLi);
                i++;
            })
        } else {
            //TODO ładniejsze wyświetlanie błędów
            alert("error");
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
                    //TODO ładniejsze info
                    alert("Update Chorobę id:" + id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
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
                //TODO ładniejsze info
                alert("Usunięto chorobę id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
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
                    //TODO ładniejsze info
                    var illness = response.illness;
                    alert("Dodano Chorobę id:" + illness.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getIllnesses()
            }
        };
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

