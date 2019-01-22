function getHospitalSections() {
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    var sectionsDiv = document.getElementById("hospital-sections-div");
    sectionsDiv.innerHTML = "";
    fetch("/api/hospitalSections?hospitalId=" + hospitalId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospitalSections = data.sections;
            var j = 0;
            hospitalSections.map(function (hospitalSection) {
                var sectionDiv = document.createElement("div");
                sectionDiv.className = "hospital-section-div";

                var nameLabel = document.createElement("label");
                nameLabel.className = "section-name-label";
                nameLabel.innerText = hospitalSection.name;
                sectionDiv.appendChild(nameLabel);

                var nameInput = document.createElement("input");
                nameInput.className = "section-name-input";
                nameInput.value = hospitalSection.name;
                nameInput.type = "text";
                nameInput.style.display = "none";
                sectionDiv.appendChild(nameInput);

                var acceptButton = document.createElement("input");
                acceptButton.className = "accept-input";
                acceptButton.value = "Akceptuj";
                acceptButton.type = "submit";
                acceptButton.style.display = "none";
                acceptButton.onclick = function () {
                    changeHospitalSection(hospitalSection.id, nameInput.value);
                };
                sectionDiv.appendChild(acceptButton);

                var manageButton = document.createElement("input");
                manageButton.className = "manage-input";
                manageButton.type = "submit";
                manageButton.value = "Edytuj";
                manageButton.onclick = function () {
                    if (manageButton.value === "Edytuj") {
                        manageButton.value = "Anuluj";
                        nameInput.value = hospitalSection.name;
                        nameInput.style.display = "block";
                        acceptButton.style.display = "block";
                        nameLabel.style.display = "none";
                    } else {
                        manageButton.value = "Edytuj";
                        nameInput.style.display = "none";
                        acceptButton.style.display = "none";
                        nameLabel.style.display = "block";
                    }

                };
                sectionDiv.appendChild(manageButton);

                var deleteButton = document.createElement("input");
                deleteButton.className = "delete-input";
                deleteButton.type = "submit";
                deleteButton.value = "Usuń oddział";
                deleteButton.onclick = function () {
                    deleteHospitalSection(hospitalSection.id);
                };
                sectionDiv.appendChild(deleteButton);

                sectionsDiv.appendChild(sectionDiv);
                j++;
            })
        } else {
            //ToDO ładniejsze info obłędzie
            alert(data1.description);
        }
    })
}

function addHospitalSection() {
    var name = document.getElementById("name-input").value;
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    if (name !== "") {
        var data = JSON.stringify({
            "id": 0,
            "name": name,
            "hospital_id": parseInt(hospitalId)
        });
        console.log(data);
        var http = new XMLHttpRequest();
        var url = "/api/addHospitalSection";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    var hospitalSection = response.hospital_section;
                    alert("Dodano oddział id:" + hospitalSection.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getHospitalSections();
            }
        };
    }
}

function deleteHospitalSection(id) {
    //TODO dodać jakieś potwierdzenie zamiaru
    alert("delete " + id);
    var http = new XMLHttpRequest();
    var url = "/api/deleteHospitalSection?id=" + id;
    http.open("delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Usunięto oddział");
            } else {
                //TODO WAŻNE!!! lepsze wyswietlanie errora - ważne bo może powodować naruszenie integralności
                alert(response.description);
            }
            getHospitalSections();
        }
    };
}

function changeHospitalSection(id, newName) {
    //TODO dodać jakieś potwierdzenie zamiaru
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    alert("change " + id + " new name: '" + newName + "'");
    if (newName !== "") {
        var data = JSON.stringify({
            "id": parseInt(id),
            "name": newName,
            "hospital_id": parseInt(hospitalId)
        });
        var http = new XMLHttpRequest();
        var url = "/api/updateHospitalSection";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    alert("Zmieniono nazwę");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getHospitalSections();
            }
        };
    } else {
        //TODO lepsze wyświetlanie informacji o błędzie
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

