function getHospitals() {
    var hospitalsUl = document.getElementById("hospitals-ul");
    hospitalsUl.innerHTML = "";

    var patientsTbody = document.getElementById("patients-tbody");
    patientsTbody.innerHTML = "";


    fetch("/api/allHospitals").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospitals = data.hospitals;
            hospitals.map(function (hospital) {
                console.log(hospital);

                var row = document.createElement("tr");

                var nameTd = document.createElement("td");
                var nameTdLabel = document.createElement("label");
                nameTdLabel.className = "table-text-label";
                nameTdLabel.innerText = hospital.name;
                nameTdLabel.style.color = "white";
                nameTd.appendChild(nameTdLabel);

                var nameTdInput = document.createElement("textarea");
                nameTdInput.style.display = "none";
                nameTdInput.innerText = hospital.name;
                nameTdInput.className = "text-td";
                nameTdInput.style.color = "black";
                nameTd.appendChild(nameTdInput);

                row.append(nameTd);

                var adresTd = document.createElement("td");
                var adresTdLabel = document.createElement("label");
                adresTdLabel.className = "table-text-label";
                adresTdLabel.innerText = hospital.address;
                adresTdLabel.style.color = "white";
                adresTd.appendChild(adresTdLabel);

                var adresTdInput = document.createElement("textarea");
                adresTdInput.style.display = "none";
                adresTdInput.innerText = hospital.address;
                adresTdInput.className = "text-td";
                adresTdInput.style.color = "black";
                adresTd.appendChild(adresTdInput);

                row.append(adresTd);

                var miastoTd = document.createElement("td");
                var miastoTdLabel = document.createElement("label");
                miastoTdLabel.className = "table-text-label";
                miastoTdLabel.innerText = hospital.city;
                miastoTdLabel.style.color = "white";
                miastoTd.appendChild(miastoTdLabel);

                var miastoTdInput = document.createElement("textarea");
                miastoTdInput.style.display = "none";
                miastoTdInput.innerText = hospital.city;
                miastoTdInput.className = "text-td";
                miastoTdInput.style.color = "black";
                miastoTd.appendChild(miastoTdInput);

                row.append(miastoTd);

                var editTd = document.createElement("td");

                var acceptInput = document.createElement("input");
                acceptInput.className = "accept-input";
                acceptInput.type = "submit";
                acceptInput.value = "Akceptuj";
                acceptInput.onclick = function () {
                    updateHospital(hospital.id, nameInput.value, addressInput.value, cityInput.value);
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
                        miastoTdInput.style.display = "inline-block";
                        nameTdInput.style.display = "inline-block";
                        adresTdInput.style.display = "inline-block";
                        nameTdLabel.style.display = "none";
                        miastoTdLabel.style.display = "none";
                        adresTdLabel.style.display = "none";
                        acceptInput.style.display = "inline-block";

                    } else {
                        manageInput.value = "Edytuj";
                        manageInput.style.height = "100%";
                        miastoTdInput.style.display = "none";
                        nameTdInput.style.display = "none";
                        adresTdInput.style.display = "none";
                        nameTdLabel.style.display = "inline-block";
                        miastoTdLabel.style.display = "inline-block";
                        adresTdLabel.style.display = "inline-block";
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
                    deleteHospital(hospital.id);
                };
                deleteTd.appendChild(deleteInput);

                row.append(deleteTd);
                patientsTbody.append(row);
            })
        } else {
            //TOdo ładniejsze wyświetlanie błędu
            alert(data.description);
        }
    })
}

function updateHospital(id, name, address, city) {
    //TODO potwierdzenie chęci
    var data = JSON.stringify({
        "id": id,
        "name": name,
        "address": address,
        "city": city
    });
    if (name !== "" && address !== "" && city !== "") {
        var http = new XMLHttpRequest();
        var url = "/api/admin/updateHospital";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info - konieczne bo tylko tu zobaczysz id
                    alert("Update hospital id:" + id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getHospitals();
            }
        };
    } else {
        //TODO ładniejsze wyświetlanie info o błedzie
        alert("błąd")
    }
}

function deleteHospital(id) {
    //TODO potwierdznie woli
    var http = new XMLHttpRequest();
    var url = "/api/admin/deleteHospital?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info - konieczne bo tylko tu zobaczysz id
                alert("Delete hospital id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getHospitals();
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

