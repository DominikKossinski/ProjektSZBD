function getDoctorData() {
    var id = document.getElementById("doctor-id-span").innerText;
    var firstNameLabel = document.getElementById("first-name-label");
    var lastNameLabel = document.getElementById("last-name-label");
    var idLabel = document.getElementById("id-label");
    var salaryLabel = document.getElementById("salary-label");
    var hospitalSectionLabel = document.getElementById("hospital-section-label");
    var positionLabel = document.getElementById("position-label");
    var url = "/api/" + id + "/doctor";
    fetch(url).then(function (value) {
        return value.json();
    }).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var doctor = data.doctor;
            firstNameLabel.innerText = doctor.first_name;
            lastNameLabel.innerText = doctor.last_name;
            idLabel.innerText = doctor.id;
            salaryLabel.innerText = doctor.salary;
            positionLabel.innerText = doctor.position;
            fetch("/api/hospitalSection?id=" + doctor.hospital_section_id).then(
                function (value) {
                    return value.json();
                }
            ).then(function (data) {
                if (data.resp_status === "ok") {
                    var hospitalSection = data.hospital_section;
                    hospitalSectionLabel.innerText = hospitalSection.name;
                } else {
                    //TODO wyświetlanie informacjii o błędzie
                    alert(data.description);
                }
            })
        } else {
            //TODO wyświetlanie informacjii o błędzie
            alert(data.description);
        }
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