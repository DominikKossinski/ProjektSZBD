function addPesel() {
    var pesel = document.getElementById("pesel-span").innerText;
    if (pesel !== "") {
        document.getElementById("pesel-input").value = pesel;
    }
}

function addStay() {
    //TODO sprawdznie poprawności danych i fech do api
    var pesel = document.getElementById("pesel-input").value;
    var id = document.getElementById("id-span").value;
    var date = document.getElementById("date-input").value;
    var data = JSON.stringify({
        "id": 0,
        "start_date": date,
        "end_date": null,
        "room_id": 0,
        "doctor_id": id,
        "pesel": pesel
    })

    console.log(data);
}