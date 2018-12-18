function getHello() {

    var textLabel = document.getElementById("text-label");
    var text = document.getElementById("text-input").value;
    var url = "/api/hello?text=" + text;
    fetch(url).then(function (responce) {
        return responce.text();
    }).then(function (data) {
        textLabel.innerText = data;
    })
}