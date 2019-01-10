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
}