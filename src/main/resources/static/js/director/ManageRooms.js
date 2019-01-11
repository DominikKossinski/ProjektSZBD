function getRooms() {
    var hospitalId = document.getElementById("hospital-id-span").innerText;
    var sectionsSelect = document.getElementById("hospital-section-select");
    var roomsUl = document.getElementById("rooms-ul");
    fetch("/api/hospitalSections?hospitalId=" + hospitalId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var sections = data.sections;
            var i = 0;
            sections.map(function (hospitalSection) {
                var op = document.createElement("option");
                op.value = hospitalSection.id;
                op.innerText = hospitalSection.id + "-" + hospitalSection.name;
                sectionsSelect.appendChild(op);

                var sectionLi = document.createElement("li");
                roomsUl.appendChild(sectionLi);
                var nameLabel = document.createElement("label");
                nameLabel.innerText = i + ". id: " + hospitalSection.id + " " + hospitalSection.name;
                sectionLi.appendChild(nameLabel);

                var ul = document.createElement("ul");
                sectionLi.appendChild(ul);

                fetch("/api/rooms?hospitalSectionId=" + hospitalSection.id).then(
                    function (value1) {
                        return value1.json();
                    }
                ).then(function (data1) {
                    console.log(data1);
                    if (data1.resp_status === "ok") {
                        var rooms = data1.rooms;
                        var j = 0;
                        rooms.map(function (room) {
                            var li = document.createElement("li");
                            li.innerText = j + ". " + room.id + " l miejsc : " + room.number_of_places +
                                " l zajętyhc: " + room.act_placed_count + " piętro: " + room.floor;
                            ul.appendChild(li);
                        })
                    }
                })
            })
        } else {
            //TODO ładniejsze wyświetlanie błędu
            alert("error");
        }
    })
}

//TODO dopisać łączenie do api
function addRoom() {

}