function getIllnesses() {
    var url = "/api/illness";
    fetch(url).then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            mapIllnesses(data.illnesses);
        } else {
            var illnessesDiv = document.getElementById("illnesses-div");
            var label = document.createElement("label");
            label.className = "error-description-label";
            label.innerText = "Natąpił błąd podczas ładowania chorób";
            illnessesDiv.appendChild(label);
        }
    });
}

function mapIllnesses(illnesses) {
    var i = 0;
    var illnessesDiv = document.getElementById("illnesses-div");
    illnessesDiv.innerHTML = "";
    if (illnesses.length === 0) {
        var label = document.createElement("label");
        label.className = "error-description-label";
        label.innerText = "Nie znaleziono chorób.";
        illnessesDiv.appendChild(label);
    }
    illnesses.map(function (illness) {
        if (i < 10) {
            var div = document.createElement("div");
            div.className = "illness-div";

            var label = document.createElement("label");
            label.className = "illness-name-label";
            label.innerText = illness.name;
            div.appendChild(label);

            var p = document.createElement("p");
            p.className = "illness-description-label";
            if (illness.description !== "null") {
                p.innerText = illness.description;
            } else {
                p.innerText = "Przypadek nie opisany";
            }
            div.appendChild(p);

            illnessesDiv.appendChild(div);
        }
        i++;
    });
}

function getIllnessesByPattern() {
    var pattern = document.getElementById("pattern-input").value;
    var url = "/api/illness?pattern=" + pattern;
    fetch(url).then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            mapIllnesses(data.illnesses);
        } else {
            alert(data.description);
        }
    });
}

function logout() {
    var url = "/api/logout";
    fetch(url).then(function (value) {
        return value.json();
    }).then(function (data) {
        if (data.resp_status === "ok") {
            if (data.logout.logout === true) {
                console.log("Logged out");
                window.location.assign("/home");
            }
        }
    });

}

