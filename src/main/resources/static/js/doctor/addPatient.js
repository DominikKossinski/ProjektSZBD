var globalPesel = "";

function addPatient() {
    var pesel = document.getElementById("pesel-input").value;
    var firstName = document.getElementById("first-name-input").value;
    var lastName = document.getElementById("last-name-input").value;
    var password = document.getElementById("password-input").value;

    if (parseInt(pesel) >= 10000000000 && parseInt(pesel) < 100000000000 && password !== ""
        && firstName !== "" && lastName != "") {
        var data = JSON.stringify({
            "pesel": parseInt(pesel),
            "first_name": firstName,
            "last_name": lastName,
            "password": password
        });
        var http = new XMLHttpRequest();
        var url = "/api/addPatient";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        console.log(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    globalPesel = pesel;
                    alert("Dodano Pacjenta");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
            }
        };
    } else {
        //TODO ladniej wyswietlac informacje
        alert("error")
    }
}

function addStay() {
    var id = document.getElementById("id-span").innerText;
    if (globalPesel === "") {
        window.location.assign("/" + id + "/addStay");
    } else {
        window.location.assign("/" + id + "/addStay?pesel=" + globalPesel)
    }
}