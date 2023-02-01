function saveBrand() {
    console.log("Saving Brand");
    let name = $("#name").val();
    let category = $("#category").val();
    let brand = { name, category };
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
            toast("success", "Brand Saved Successfully !");
            brand = response;
            $("#name").val("");
            $("#category").val("");
            $("#tablebody").prepend(
                `<tr id='${"brand" + brand.id}'>
                    <td>${brand.name}</td>
                    <td>${brand.category}</td>
                    <td><div class="btn btn-primary"
                    onclick="showUpdateForm(${brand.id},'${brand.name}','${brand.category
                }')">
                    Edit</div></td>
                    </tr>`
            );
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function loadAllBrands() {
    $.ajax({
        url: "/api/brands/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            $("#tablebody").html("");
            response.forEach((brand) => {
                $("#tablebody").append(
                    `<tr id='${"brand" + brand.id}'>
                    <td>${brand.name}</td>
                    <td>${brand.category}</td>
                    <td><div class="btn btn-primary"
                    onclick="showUpdateForm(${brand.id},'${brand.name}','${brand.category
                    }')">
                    Edit</div></td>
                    </tr>`
                );
            });
        },
        error: function (error) {
            console.log(error);
            console.log(error);
            toast(
                "error",
                "Error in Fetching All Brands : " + error.responseJSON.message
            );
        },
    });
}

function showUpdateForm(id, name, category) {
    $("#brand" + id).html(`<td>
        <input
            class="form-control"
            name=${"name" + id}
            id=${"name" + id}
            type="text"
            placeholder="Brand Name"
            value='${name}'
            autofocus
        /> 
        </td> <td>
        <input
            class="form-control"
            name=${"category" + id}
            id=${"category" + id}
            type="text"
            placeholder="Category"
            value='${category}'
        /> </td>
        <td>
        <div class="btn btn-primary" onclick="update(${id})">Edit </div>
        </td>
   `);
}

function update(id) {
    console.log("Updating " + id);
    let name = $(`#name${id}`).val();
    let category = $(`#category${id}`).val();
    let brand = {
        name,
        category,
    };
    console.log(brand);

    let data = JSON.stringify(brand);

    $.ajax({
        url: "/api/brands/" + id,
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            brand = response;
            $("#brand" + id).html(`
                    <td>${brand.name}</td>
                    <td>${brand.category}</td>
                    <td><div class="btn btn-primary"
                    onclick="showUpdateForm(${brand.id},'${brand.name}','${brand.category}')">
                    Edit</div></td>
                    
                `);
            toast("success", "Brand Updated Successfully !");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}
