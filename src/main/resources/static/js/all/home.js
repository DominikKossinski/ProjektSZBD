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

function getHospitals() {
    var hospitalsUl = document.getElementById("hospitals-ul");
    fetch("/api/allHospitals").then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var hospitals = data.hospitals;
            var i = 0;
            hospitals.map(function (hospital) {
                var li = document.createElement("li");
                li.innerText = i + ". Id: " + hospital.id + " Nazwa: " + hospital.name + " Adres: " + hospital.address +
                    " Miasto: " + hospital.city + " Oddziały:";
                var ul = document.createElement("ul");
                ul.style.marginLeft = 20 + "px";
                li.appendChild(ul);
                hospitalsUl.appendChild(li);
                i++;
                fetch("/api/hospitalSections?hospitalId=" + hospital.id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var hospitalSections = data1.sections;
                        var j = 0;
                        hospitalSections.map(function (hospitalSection) {
                            var sectionLi = document.createElement("li");
                            sectionLi.innerText = j + ". Nazwa: " + hospitalSection.name;
                            ul.appendChild(sectionLi);
                            j++;
                        })
                    } else {
                        //ToDO ładniejsze info obłędzie
                        alert(data1.description);
                    }
                })
            })
        } else {
            //TODO ładniejszy komunikat o błędzie
            alert(data.description)
        }
    })
}