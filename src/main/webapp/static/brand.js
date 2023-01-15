function saveBrand() {
    console.log("Saving Brand");
    let name = $("#name").val();
    let category = $("#category").val();
    const brand = { name, category };
    console.log(brand);
    let data = JSON.stringify(brand);

    $.ajax({
        url: "/api/brands/",
        type: "POST",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
        },
        error: function (error) {
            console.log(error);
        },
    });
}

function loadAllBrands() {
    $.ajax({
        url: "/api/brands/all",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            $("#tablebody").html("");
            response.forEach((brand) => {
                $("#tablebody").append(
                    `<tr>
                    <th scope="row">${brand.id}</th>
                    <td>${brand.name}</td>
                    <td>${brand.category}</td>
                    <td><div class="btn btn-warning"
                    onclick="showUpdateForm(${brand.id},'${brand.name}','${brand.category}')">
                    Update</div></td>
                    </tr>`
                );
            });
        },
        error: function (error) {
            console.log(error);
        },
    });
}

function searchById() {
    let id = $("#ids").val();
    id = parseInt(id);
    if (id > 0) {
        $.ajax({
            url: "/api/brands/brand/" + id,
            type: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            success: function (response) {
                console.log("response");
                showUpdateForm(response.id, response.name, response.category);
            },
            error: function (error) {
                console.log(error);
            },
        });
    } else {
        console.log("Id is a number greater than 0.");
    }
}

function showBrandFrom() {
    console.log("hide-update show-save");
    $("#updateBrand").hide();
    $("#saveBrand").show();
    $("#searchBrandId").hide();
}

function showGetIdForm() {
    $("#updateBrand").hide();
    $("#saveBrand").hide();
    $("#searchBrandId").show();
}

function showUpdateForm(id, name, category) {
    $("#idc").val(id);
    $("#namec").val(name);
    $("#categoryc").val(category);
    console.log("hide-save show-update");
    $("#updateBrand").show();
    $("#saveBrand").hide();
    $("#searchBrandId").hide();
    $("html, body").animate(
        {
            scrollTop: $("#updateBrand").offset(),
        },
        1000
    );
}
