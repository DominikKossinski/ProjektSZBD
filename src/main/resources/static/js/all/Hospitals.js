function getHospitals() {
    var hospitalsDiv = document.getElementById("hospitals-div");
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
                var hospitalDiv = document.createElement("div");
                hospitalDiv.className = "hospital-div";

                var hospitalNameLabel = document.createElement("label");
                hospitalNameLabel.className = "hospital-name-label";
                hospitalNameLabel.innerText = hospital.name;
                hospitalDiv.appendChild(hospitalNameLabel);

                var hospitalAddressLabel = document.createElement("label");
                hospitalAddressLabel.className = "hospital-address-label";
                hospitalAddressLabel.innerText = "Adres: " + hospital.address + " " + hospital.city;
                hospitalDiv.appendChild(hospitalAddressLabel);

                var hospitalDirectorLabel = document.createElement("label");
                hospitalDirectorLabel.className = "hospital-director-label";
                hospitalDirectorLabel.innerText = "";
                hospitalDiv.appendChild(hospitalDirectorLabel);
                fetch("/api/hospitalDirector?hospitalId=" + hospital.id).then(
                    function (value) {
                        return value.json();
                    }
                ).then(function (data) {
                    console.log(data);
                    if (data.resp_status === "ok") {
                        var director = data.director;
                        var doctor = director.doctor;
                        hospitalDirectorLabel.innerText = "Dyrektor: " + doctor.first_name + " " + doctor.last_name;

                    }
                });

                var hospitalSectionLabel = document.createElement("label");
                hospitalSectionLabel.className = "hospital-section-label";
                hospitalSectionLabel.innerText = "Oddziały:";
                hospitalDiv.appendChild(hospitalSectionLabel);

                var ul = document.createElement("ul");
                ul.className = "hospital-sections-ul";
                hospitalDiv.appendChild(ul);

                hospitalsDiv.appendChild(hospitalDiv);
                i++;
                fetch("/api/hospitalSections?hospitalId=" + hospital.id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var hospitalSections = data1.sections;
                        var j = 1;
                        hospitalSections.map(function (hospitalSection) {
                            var sectionLi = document.createElement("li");
                            sectionLi.className = "hospital-section-li";
                            sectionLi.innerText = hospitalSection.name;
                            ul.appendChild(sectionLi);
                            j++;
                        })
                    }
                })
            })
        } else {
            //TODO ładniejszy komunikat o błędzie
            alert(data.description)
        }
    })
}