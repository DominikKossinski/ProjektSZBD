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

