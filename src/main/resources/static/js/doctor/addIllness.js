function addIllness() {
    var name = document.getElementById("name-input").value;
    var description = document.getElementById("description-textarea").value;
    var data = JSON.stringify({
        "id": 0,
        "name": name,
        "description": description
    });
    console.log(data);
    if (name !== "") {
        var http = new XMLHttpRequest();
        var url = "/api/addIllness";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    //TODO ładniejsze info
                    var illness = response.illness;
                    alert("Dodano Chorobę id:" + illness.id);
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
            }
        };
    }
}