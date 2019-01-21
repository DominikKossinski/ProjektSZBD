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
                if(end_date === "null"){
                    end_date = "-";
                }

                var row = $('<tr/>')
                    .append($('<td/>', {text: stay.id || ''}))      //ID pobytu
                    .append($('<td/>', {text: stay.room_id || ''}))             // nr pokoju
                    .append($('<td/>', {text: stay.start_date || ''}))        //poczatek
                    .append($('<td/>', {text: end_date || ''}))      //koniec
                    .append($('<td/>', {text: stay.doctor_id || ''}));    // id lekarza

                $('table tbody').append(row);

            })

        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert(data.description);
        }
    })
}