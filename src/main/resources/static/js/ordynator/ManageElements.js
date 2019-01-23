function getElements() {
    var elementsDiv = document.getElementById("elements-div");
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    fetch("/api/elements?hospitalSectionId=" + hospitalSectionId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var elements = data.elements;
            elementsDiv.innerHTML = "";
            if (elements.length > 0) {

                var table = document.createElement("table");
                var thead = document.createElement("thead");
                var tr = document.createElement("tr");

                var nameTh = document.createElement("th");
                nameTh.innerText = "Nazwa";
                tr.appendChild(nameTh);

                var countTh = document.createElement("th");
                countTh.innerText = "Liczba";
                tr.appendChild(countTh);

                var priceTh = document.createElement("th");
                priceTh.innerText = "Cena";
                tr.appendChild(priceTh);

                var manageTh = document.createElement("th");
                manageTh.innerText = "Edytuj";
                tr.appendChild(manageTh);

                var deleteTh = document.createElement("th");
                deleteTh.innerText = "Usuń";
                tr.appendChild(deleteTh);

                thead.appendChild(tr);

                table.appendChild(thead);

                var tbody = document.createElement("tbody");
                table.appendChild(tbody);


                elementsDiv.appendChild(table);

                elements.map(function (element) {
                    var row = document.createElement("tr");

                    var nameTd = document.createElement("td");

                    var nameLabel = document.createElement("label");
                    nameLabel.className = "table-text-label";
                    nameLabel.innerText = element.name;
                    nameTd.appendChild(nameLabel);

                    var nameInput = document.createElement("input");
                    nameInput.className = "text-input";
                    nameInput.value = element.name;
                    nameInput.type = "text";
                    nameInput.style.display = "none";
                    nameTd.appendChild(nameInput);

                    row.appendChild(nameTd);

                    var countTd = document.createElement("td");

                    var countLabel = document.createElement("label");
                    countLabel.className = "table-text-label";
                    countLabel.innerText = element.count;
                    countTd.appendChild(countLabel);


                    var countInput = document.createElement("input");
                    countInput.className = "text-input";
                    countInput.value = element.count;
                    countInput.type = "number";
                    countInput.min = "0";
                    countInput.step = "1";
                    countInput.style.display = "none";
                    countTd.appendChild(countInput);

                    row.appendChild(countTd);

                    var priceTd = document.createElement("td");

                    var priceLabel = document.createElement("label");
                    priceLabel.className = "table-text-label";
                    priceLabel.innerText = Math.round(parseFloat(element.price * 100)) / 100;
                    priceTd.appendChild(priceLabel);

                    var priceInput = document.createElement("input");
                    priceInput.className = "text-input";
                    priceInput.value = element.count;
                    priceInput.type = "number";
                    priceInput.step = "0.01";
                    priceInput.style.display = "none";
                    priceTd.appendChild(priceInput);

                    row.appendChild(priceTd);

                    var manageTd = document.createElement("td");

                    var acceptButton = document.createElement("input");
                    acceptButton.className = "accept-input";
                    acceptButton.value = "Akceptuj";
                    acceptButton.type = "submit";
                    acceptButton.style.display = "none";
                    acceptButton.onclick = function () {
                        changeElement(element.id, nameInput.value, countInput.value, priceInput.value);
                    };
                    manageTd.appendChild(acceptButton);

                    var manageButton = document.createElement("input");
                    manageButton.className = "manage-input";
                    manageButton.type = "submit";
                    manageButton.value = "Edytuj";
                    manageButton.onclick = function () {
                        if (manageButton.value === "Edytuj") {
                            manageButton.value = "Anuluj";
                            nameInput.value = element.name;
                            countInput.value = element.count;
                            priceInput.value = element.price;
                            nameLabel.style.display = "none";
                            priceLabel.style.display = "none";
                            countLabel.style.display = "none";
                            nameInput.style.display = "block";
                            countInput.style.display = "block";
                            priceInput.style.display = "block";
                            acceptButton.style.display = "block";
                            nameLabel.style.display = "none";
                        } else {
                            manageButton.value = "Edytuj";
                            nameInput.style.display = "none";
                            countInput.style.display = "none";
                            priceInput.style.display = "none";
                            acceptButton.style.display = "none";
                            nameLabel.style.display = "block";
                            priceLabel.style.display = "block";
                            countLabel.style.display = "block";
                        }

                    };
                    manageTd.appendChild(manageButton);

                    row.appendChild(manageTd);

                    var deleteTd = document.createElement("td");

                    var deleteButton = document.createElement("input");
                    deleteButton.className = "delete-input";
                    deleteButton.type = "submit";
                    deleteButton.value = "Usuń";
                    deleteButton.onclick = function () {
                        deleteElement(element.id);
                    };
                    deleteTd.appendChild(deleteButton);

                    row.appendChild(deleteTd);

                    tbody.appendChild(row);
                })
            } else {
                var errorLabel = document.createElement("label");
                errorLabel.className = "error-description-label";
                errorLabel.innerText = "Ten oddział nie ma jeszcze elementów wyposażenia!";
                elementsDiv.appendChild(errorLabel);
            }
        } else {
            var errorLabel1 = document.createElement("label");
            errorLabel1.className = "error-description-label";
            errorLabel1.innerText = "Nastąpił błąd podczas ładowania elementów wyposażenia";
            elementsDiv.appendChild(errorLabel1);

        }
    })
}

function countPrice() {
    var count = document.getElementById("count-input").value;
    var price = document.getElementById("price-input").value;
    var priceLabel = document.getElementById("price-label");
    priceLabel.innerText = "Łączna cena: " + Math.round(parseFloat(price * count * 100)) / 100 + " zł";
}

function addElement() {
    var name = document.getElementById("name-input").value;
    var count = document.getElementById("count-input").value;
    var price = document.getElementById("price-input").value;
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    if (name !== "" && count >= 0 && price >= 0) {
        var data = JSON.stringify({
            "id": 0,
            "name": name,
            "count": parseInt(count),
            "price": parseFloat(price),
            "hospital_section_id": parseInt(hospitalSectionId)
        });
        console.log(data);
        var http = new XMLHttpRequest();
        var url = "/api/addElement";
        http.open("Put", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    alert("Dodano element");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getElements();
            }
        };
    } else {
        alert("Podano złe dane, sprawdź czy cena oraz ilość są większe bądź równe 0 " +
            "lub czy nazwa nie jest pusta");
    }

}

function deleteElement(id) {
    alert("delete: " + id);
    var http = new XMLHttpRequest();
    var url = "/api/deleteElement?id=" + id;
    http.open("Delete", url, true);
    http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    http.send("");
    http.onreadystatechange = function (e) {
        if (http.readyState === 4) {
            var response = JSON.parse(http.responseText);
            console.log(response);
            if (response.resp_status === "ok") {
                alert("Usunięto element");
            } else {
                alert("Wystąpił błąd podczas usuwania elementu, " +
                    "odśwież strone i dokonaj ponownej próby")

            }
            getElements();
        }
    };
}

function changeElement(id, name, count, price) {
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    if (name !== "" && count >= 0 && price >= 0) {
        var data = JSON.stringify({
            "id": parseInt(id),
            "name": name,
            "count": parseInt(count),
            "price": parseFloat(price),
            "hospital_section_id": parseInt(hospitalSectionId)
        });
        console.log(data);
        var http = new XMLHttpRequest();
        var url = "/api/updateElement";
        http.open("Post", url, true);
        http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
        http.send(data);
        http.onreadystatechange = function (e) {
            if (http.readyState === 4) {
                var response = JSON.parse(http.responseText);
                console.log(response);
                if (response.resp_status === "ok") {
                    alert("Zmodyfikowano element");
                } else {
                    alert("Wystąpił błąd podczas aktualizowania elementu," +
                        " odśwież strone i dokonaj ponownej próby")
                }
                getElements();
            }
        };
    } else {
        alert("Podano złe dane, sprawdź czy cena oraz ilość są większe bądź równe 0 " +
            "lub czy nazwa nie jest pusta");
    }
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

