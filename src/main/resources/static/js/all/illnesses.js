function getIllnesses() {
    var url = "/api/illness";
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

function mapIllnesses(illnesses) {
    var ul = document.getElementById("illnesses-ul");
    ul.innerHTML = "";
    var i = 0;
    illnesses.map(function (illness) {
        var li = document.createElement("li");
        li.innerText = i + ". " + illness.name;
        ul.appendChild(li);
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