function getPrescriptions() {
    var ul = document.getElementById("prescriptions-ul");
    var pesel = document.getElementById("pesel-span").innerText;
    fetch("/api/patient/" + pesel + "/prescriptions").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var prescriptions = data.prescriptions;
            prescriptions.map(function (prescription) {
                var li = document.createElement("li");
                fetch("/api/illness?id=" + prescription.illness_id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var illness = data1.illness;
                        //TODO ładniejsze wyświetlanie danych
                        li.innerText = "id: " + prescription.id + " termin: " + prescription.date
                            + " dawkowanie: " + prescription.dosage + " nazwa choroby: " + illness.name;
                        ul.appendChild(li);
                    } else {
                        //TODO ładniejsze wyświetlanie błędu
                        alert(data.description);
                    }
                })

            })
        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert(data.description);
        }
    })
}