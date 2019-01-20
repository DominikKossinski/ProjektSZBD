function getMyStays() {
    var ul = document.getElementById("stays-ul");
    var pesel = document.getElementById("pesel-span").innerText;
    console.log(pesel);
    fetch("/api/patient/" + pesel + "/stays").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var stays = data.stays;
            stays.map(function (stay) {
                //ToDO ładniejsze wyświetlanie informacji
                var li = document.createElement("li");
                li.innerText = "Id: " + stay.id + " termin: " + stay.start_date + " doctor id: " + stay.doctor_id;
                ul.appendChild(li);
            })
        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert(data.description);
        }
    })
    getRoomById(1);
}

//TODo DODAj dwa parametry - element przechowujący nazwę oddziału i element przechowujący nazwę szpitaka
function getRoomById(id) {
    fetch("/api/rooms?id=" + id).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var room = data.room;
            getHospitalSectionById(room.hospital_section_id);
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
        }
    })
}

//TODO dodać przekazywanie elementu przechowującego nazwę oddziału i szpitala
function getHospitalSectionById(id) {
    fetch("/api/hospitalSection?id=" + id).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospitalSection = data.hospital_section;
            //ToDO tu musisz obrobić element od oddziału

            //TODO dodaj drugi parametr wywołania
            getHospitalById(hospitalSection.hospital_id);
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
            return null;
        }
    })
}


//TODO dodaj drugi parametr - element przechowujący nazwę szpitala
function getHospitalById(id) {
    fetch("/api/hospital?id=" + id).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospital = data.hospital;
            //TODO obrobić dane szpitala tutaj
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
            return null;
        }
    })
}