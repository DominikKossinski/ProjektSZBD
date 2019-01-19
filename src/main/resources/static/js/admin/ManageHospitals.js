function getHospitals() {
    var hospitalsUl = document.getElementById("hospitals-ul");
    hospitalsUl.innerHTML = "";
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
                var hospitalLi = document.createElement("li");

                var label = document.createElement("label");
                label.innerText = "Nazwa " + hospital.name + " adres: " + hospital.address + " miasto: " + hospital.city;
                hospitalLi.appendChild(label);

                var nameInput = document.createElement("input");
                nameInput.type = "text";
                nameInput.value = hospital.name;
                nameInput.style.display = "none";
                hospitalLi.appendChild(nameInput);

                var addressInput = document.createElement("input");
                addressInput.type = "text";
                addressInput.value = hospital.address;
                addressInput.style.display = "none";
                hospitalLi.appendChild(addressInput);

                var cityInput = document.createElement("input");
                cityInput.type = "text";
                cityInput.value = hospital.city;
                cityInput.style.display = "none";
                hospitalLi.appendChild(cityInput);

                var acceptInput = document.createElement("input");
                acceptInput.type = "submit";
                acceptInput.value = "Akceptuj";
                acceptInput.style.display = "none";
                acceptInput.onclick = function () {
                    updateHospital(hospital.id, nameInput.value, addressInput.value, cityInput.value);
                };
                hospitalLi.appendChild(acceptInput);

                var manageInput = document.createElement("input");
                manageInput.type = "submit";
                manageInput.value = "Edytuj";
                manageInput.onclick = function () {
                    if (manageInput.value === "Edytuj") {
                        manageInput.value = "Anuluj";
                        label.innerText = "";
                        nameInput.style.display = "block";
                        nameInput.value = hospital.name;
                        addressInput.style.display = "block";
                        addressInput.value = hospital.address;
                        cityInput.style.display = "block";
                        cityInput.value = hospital.city;
                        acceptInput.style.display = "block";
                    } else {
                        manageInput.value = "Edytuj";
                        label.innerText = "Nazwa " + hospital.name + " adres: " + hospital.address + " miasto: " + hospital.city;
                        nameInput.style.display = "none";
                        addressInput.style.display = "none";
                        cityInput.style.display = "none";
                        acceptInput.style.display = "none";
                    }
                };
                hospitalLi.appendChild(manageInput);


                var deleteInput = document.createElement("input");
                deleteInput.type = "submit";
                deleteInput.value = "Usuń";
                deleteInput.onclick = function () {
                    deleteHospital(hospital.id);
                };
                hospitalLi.appendChild(deleteInput);

                hospitalsUl.appendChild(hospitalLi);
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