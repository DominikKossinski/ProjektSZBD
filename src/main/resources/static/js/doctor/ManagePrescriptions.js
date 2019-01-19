function getData() {
    //TODO wczytaj pobyty,
    //TODO wczytaj chorony

    getPrescriptions();
    getIllnesses();
    getStays();
}

function getIllnesses() {
    var illnessDataList = document.getElementById("illnesses-datalist");
    illnessDataList.innerHTML = "";
    var pattern = document.getElementById("illness-input").value;
    fetch("/api/illness").then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var illnesses = data.illnesses;
            illnesses.map(function (illness) {
                var op = document.createElement("option");
                op.value = illness.id + "-" + illness.name;
                op.innerText = illness.id + "-" + illness.name;
                illnessDataList.appendChild(op);
            })


        } else {
            //todo łaniejsze wyświtlanie informacji
            alert("error")
        }
    })
}

function getStays() {
    var doctorId = document.getElementById("doctor-id-span").innerText;
    var stayDataList = document.getElementById("stay-datalist");
    stayDataList.innerHTML = "";
    fetch("/api/stays?doctorId=" + doctorId).then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var stays = data.stays;
            stays.map(function (stay) {
                var op = document.createElement("option");
                op.value = stay.id + "-" + stay.pesel + " " + stay.start_date;
                op.innerText = stay.id + "-" + stay.pesel + " " + stay.start_date;
                stayDataList.appendChild(op);
            })
        } else {
            //todo ładniejsze wyświetlanie błędu
            alert("error");
        }
    })
}

function getPrescriptions() {
    var doctorId = document.getElementById("doctor-id-span").innerText;
    var pesel = document.getElementById("pesel-span").innerText;
    if (pesel !== "") {
        pesel = "&pesel=" + pesel;
    }
    var prescriptionsUl = document.getElementById("prescriptions-ul");
    prescriptionsUl.innerHTML = "";
    fetch("/api/prescriptions?doctorId=" + doctorId + pesel).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var prescriptions = data.prescriptions;
            var i = 0;
            prescriptions.map(function (prescription) {
                fetch("/api/illness?id=" + prescription.illness_id).then(
                    function (value) {
                        return value.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var prescriptionLi = document.createElement("li");

                        var label = document.createElement("label");
                        var illness = data1.illness;
                        label.innerText = i + ". " + prescription.date + " " + prescription.dosage +
                            " choroba: " + illness.name;
                        prescriptionLi.appendChild(label);

                        var dosageTextarea = document.createElement("textarea");
                        dosageTextarea.value = prescription.dosage;
                        dosageTextarea.style.display = "none";
                        prescriptionLi.appendChild(dosageTextarea);

                        var acceptInput = document.createElement("input");
                        acceptInput.type = "submit";
                        acceptInput.value = "Akceptuj";
                        acceptInput.style.display = "none";
                        acceptInput.onclick = function () {
                            updatePrescription(prescription.id, prescription, dosageTextarea.value);
                        };
                        prescriptionLi.appendChild(acceptInput);

                        var manageInput = document.createElement("input");
                        manageInput.type = "submit";
                        manageInput.value = "Edytuj";
                        manageInput.onclick = function () {
                            if (manageInput.value === "Edytuj") {
                                manageInput.value = "Anuluj";
                                label.innerText = i + ". " + prescription.date +
                                    " choroba: " + illness.name;
                                dosageTextarea.style.display = "block";
                                dosageTextarea.value = prescription.dosage;
                                acceptInput.style.display = "block";
                            } else {
                                manageInput.value = "Edytuj";
                                label.innerText = i + ". " + prescription.date + " " + prescription.dosage +
                                    " choroba: " + illness.name;
                                dosageTextarea.style.display = "none";
                                acceptInput.style.display = "none";
                            }
                        };
                        prescriptionLi.appendChild(manageInput);

                        var deleteInput = document.createElement("input");
                        deleteInput.type = "submit";
                        deleteInput.value = "Usuń";
                        deleteInput.onclick = function () {
                            deletePrescription(prescription.id);
                        };
                        prescriptionLi.appendChild(deleteInput);

                        prescriptionsUl.appendChild(prescriptionLi);
                        i++;
                    } else {
                        //Todo ładniejsze wyświetlanie błaędu
                        alert("error")
                    }
                })


            })
        } else {
            //Todo łaniejsze wyświetlanie erroró
            alert("error");
        }
    })
}

function updatePrescription(id, prescription, dosage) {
    //TODO potwierdzenie chęci
    var data = JSON.stringify({
        "id": id,
        "date": prescription.date,
        "dosage": dosage,
        "illness_id": prescription.illness_id,
        "stay_id": prescription.stay_id
    });
    var http = new XMLHttpRequest();
    var url = "/api/updatePrescription";
    http.open("Post", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send(data);
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Update Receptę id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getPrescriptions();
        }
    };
}

function deletePrescription(id) {
    //TODO potwierdzenie chęci
    var http = new XMLHttpRequest();
    var url = "/api/deletePrescription?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                //TODO ładniejsze info
                alert("Delete Receptę id:" + id);
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getPrescriptions();
        }
    };
}

function addPrescription() {
    var date = document.getElementById("date-input").value;
    var dosage = document.getElementById("dosage-textarea").value;
    var illnessId = document.getElementById("illness-input").value;
    illnessId = illnessId.substring(0, illnessId.indexOf("-"));
    var stayId = document.getElementById("stay-input").value;
    stayId = stayId.substring(0, stayId.indexOf("-"));
    if (date !== "" && illnessId !== "" && stayId !== "") {
        var data = JSON.stringify({
            "id": 0,
            "date": date,
            "dosage": dosage,
            "illness_id": illnessId,
            "stay_id": stayId
        });
        console.log(data);
        var http = new XMLHttpRequest();
        var url = "/api/addPrescription";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    var prescription = response.prescription;
                    alert("Add Receptę id:" + prescription.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getPrescriptions();
            }
        };
    } else {
        //TODO łądniejsza informacja o błędzie
        alert("błąd");

    }
}