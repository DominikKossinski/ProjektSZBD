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

                var end_date = stay.end_date;
                if (end_date === "null") {
                    end_date = "-";
                }

                var hosp_id = document.createElement("td");
                var oddzial = document.createElement("td");
                var lek_nazw = document.createElement("td");

                getRoomById(stay.id, hosp_id, oddzial);
                getDoctorById(stay.doctor_id, lek_nazw);

                var row = $('<tr/>')
                    .append($('<td/>', {text: stay.id || ''}))      //ID pobytu
                    .append(hosp_id)    //ID szpitala
                    .append(oddzial)        //Oddzial
                    .append($('<td/>', {text: stay.room_id || ''}))             // nr pokoju
                    .append($('<td/>', {text: stay.start_date || ''}))        //poczatek
                    .append($('<td/>', {text: end_date || ''}))      //koniec
                    .append(lek_nazw);    // nazwisko lekarza

                $('table tbody').append(row);

            })

        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert(data.description);
        }
    })

}

function getRoomById(id, hosp_id, oddzial) {
    fetch("/api/rooms?id=" + id).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var room = data.room;
            getHospitalSectionById(room.hospital_section_id, hosp_id, oddzial);
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
        }
    })
}

function getHospitalSectionById(id, hosp_id, oddzial) {
    fetch("/api/hospitalSection?id=" + id).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospitalSection = data.hospital_section;
            hosp_id.innerText = hospitalSection.hospital_id;
            oddzial.innerText = hospitalSection.name;
            getHospitalById(hospitalSection.hospital_id);
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
        }
    })
}

function getDoctorById(id, nameTd) {
    fetch("/api/doctorInfo?id=" + id).then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var doctor = data.doctor;
            nameTd.innerText = doctor.last_name;
        } else {
            //TODO ładniesze info o błędzie
            alert(data.description);
        }
    })
}
