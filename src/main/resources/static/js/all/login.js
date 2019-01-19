function login() {
    var id = document.getElementById("id-input").value;
    var password = document.getElementById("password-input").value;
    var errorLabel = document.getElementById("error-label");
    if (id !== "" && password !== "") {
        errorLabel.style.display='none';
        var data = JSON.stringify({"id": id, "password": password});
        var http = new XMLHttpRequest();
        var url = "/api/login";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                if (response.resp_status === "ok") {
                    if (response.login.login === true) {
                        console.log("Logged in");
                        errorLabel.innerText = "";
                        window.location.assign("/home");
                    } else {
                        document.getElementById("id-input").value = "";
                        document.getElementById("password-input").value = "";
                        errorLabel.innerText = "Podano złe id lub zły pesel lob złe hasło";
                        errorLabel.style.display='block';
                    }
                } else {
                    errorLabel.innerText = "Logowanie nie powiodło się";
                    errorLabel.style.display='block';

                }
            }
        };
    }
    console.log(data);
}