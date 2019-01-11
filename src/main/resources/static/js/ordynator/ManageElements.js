function getElements() {
    var ul = document.getElementById("elements-ul");
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    fetch("/api/elements?hospitalSectionId=" + hospitalSectionId).then(
        function (value) {
            return value.json();
        }
    ).then(function (data) {
        console.log(data);
        if (data.resp_status === "ok") {
            var elements = data.elements;
            var j = 0;
            ul.innerHTML = "";
            elements.map(function (element) {
                var elementLi = document.createElement("li");
                elementLi.setAttribute("id", element.id + "-element-li");

                var nameLabel = document.createElement("label");
                nameLabel.innerText = j + ". Nazwa: " + element.name + " Ilość: " + element.count + " cena: " +
                    Math.round(parseFloat(element.price * 100)) / 100;
                elementLi.appendChild(nameLabel);

                var deleteButton = document.createElement("input");
                deleteButton.type = "submit";
                deleteButton.value = "Usuń Element";
                deleteButton.onclick = function () {
                    deleteElement(element.id);
                };
                elementLi.appendChild(deleteButton);

                var nameInput = document.createElement("input");
                nameInput.value = element.name;
                nameInput.type = "text";
                nameInput.style.display = "none";
                elementLi.appendChild(nameInput);

                var countInput = document.createElement("input");
                countInput.value = element.count;
                countInput.type = "number";
                countInput.style.display = "none";
                elementLi.appendChild(countInput);

                var priceInput = document.createElement("input");
                priceInput.value = element.count;
                priceInput.type = "number";
                priceInput.step = "0.01";
                priceInput.style.display = "none";
                elementLi.appendChild(priceInput);

                var acceptButton = document.createElement("input");
                acceptButton.value = "Akceptuj";
                acceptButton.type = "submit";
                acceptButton.style.display = "none";
                acceptButton.onclick = function () {
                    changeElement(element.id, nameInput.value, countInput.value, priceInput.value);
                };
                elementLi.appendChild(acceptButton);

                var manageButton = document.createElement("input");
                manageButton.type = "submit";
                manageButton.value = "Edytuj";
                manageButton.onclick = function () {
                    if (manageButton.value === "Edytuj") {
                        manageButton.value = "Anuluj";
                        nameInput.value = element.name;
                        countInput.value = element.count;
                        priceInput.value = element.price;
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
                    }

                };
                elementLi.appendChild(manageButton);

                ul.appendChild(elementLi);
                j++;
            })
        } else {
            //TODO ładniejsze wyświetlanie informacjii o błedzie

        }
    })
}

function countPrice() {
    var count = document.getElementById("count-input").value;
    var price = document.getElementById("price-input").value;
    var priceLabel = document.getElementById("price-label");
    priceLabel.innerText = "Łączna cena: " + price * count + " zł";
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
                    //TODO ładniejsze info
                    alert("Dodano element");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getElements();
            }
        };
    }

}

function deleteElement(id) {
    //TODO jakieś potwierdzenie chęci
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
                //TODO ładniejsze info
                alert("Usunięto element");
            } else {
                //TODO lepsze wyswietlanie errora
                alert(response.description);
            }
            getElements();
        }
    };
}

function changeElement(id, name, count, price) {
    var hospitalSectionId = document.getElementById("hospital-section-id-span").innerText;
    //TODO jakieś potwierdzenie chęci
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
                    //TODO ładniejsze info
                    alert("Zmodyfikowano element");
                } else {
                    //TODO lepsze wyswietlanie errora
                    alert(response.description);
                }
                getElements();
            }
        };
    } else {
        //TODO ładna informcja o błędzie
    }
}