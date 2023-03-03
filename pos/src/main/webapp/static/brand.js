let table = null;


function loadAllBrands() {
    $.ajax({
        url: "/api/brands/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (brands) {
            renderTable(brands);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);

        },
    });
}



function renderTable(brands) {
    $("#tablebody").html("");
    table = $("#main-table").DataTable();
    brands.forEach(brand => {
        prependBrand(brand);
    });
    table.draw();

}

function prependBrand(brand) {
    table.row.add([brand.name, brand.category,
    `<div class="btn btn-info" onclick="editBrand(${brand.id},'${brand.name}','${brand.category}')"><i class="fa-regular fa-pen-to-square"></i> Edit</div>`
    ]).node().id = brand.id;
}

function insertUpdatedBrand(brand) {
    table.row(`#${brand.id}`).remove();
    prependBrand(brand);
    table.draw();
}



function editBrand(id, name, category) {
    $("#u-id").val(id);
    $("#u-name").val(name);
    $("#u-category").val(category);
    $("#updateModal").modal("show");
}

function createBrand() {
    let name = $("#name").val();
    let category = $("#category").val();
    const brand = { name, category };
    const data = JSON.stringify(brand);
    $.ajax({
        url: "/api/brands/",
        type: "POST",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            prependBrand(data);
            table.draw();
            $("#createModal").modal("hide");
            toast("success", "Brand created");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function updateBrand() {
    let id = $("#u-id").val();
    let name = $("#u-name").val();
    let category = $("#u-category").val();
    const brand = { name, category };
    const data = JSON.stringify(brand);
    $.ajax({
        url: "/api/brands/" + id,
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            insertUpdatedBrand(data);
            $("#updateModal").modal("hide");
            toast("success", "Brand updated");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function downloadSampleBrand() {
    console.log("yes");
    window.open("/sample/sample-brand.tsv", '_blank').focus();
}

function uploadBrand() {
    const data = new FormData();
    const file = $("#file")[0].files[0];
    console.log(file.name);
    data.append("file", file);
    if (file.type != "text/tab-separated-values") {
        toast("error", "Selected file must be a TSV (tab seperated value) file");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/api/brands/upload",
        data: data,
        enctype: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data) {
            toast("success", "Brands uploaded successfully");
            $("#uploadModal").modal("hide");
            loadAllBrands();
        },
        error: function (e) {
            if (e.status == 400) {
                let text = e.responseJSON.message;
                $("#error-file").remove();
                let element = document.createElement('a');
                element.setAttribute('href', 'data:text/tsv;charset=utf-8,' + encodeURIComponent(text));
                element.setAttribute('download', "errors_" + file.name);
                element.setAttribute('id', "error-file");
                element.style.display = 'none';
                document.body.appendChild(element);
                $("#error-button").removeClass("d-none")
            }

            console.log("ERROR : ", e.responseJSON.message);
            toast("error", "Invalid file, download error file for more info")

        }
    });
}

function downloadErrorFile() {
    document.getElementById('error-file').click();
    $("#error-file").remove();
    $("#error-button").addClass("d-none");
}


function initBrand() {
    loadAllBrands();
    $("#showCreateModal").click(() => {
        $("#name").val("");
        $("#category").val("");
        $("#createModal").modal("show");
    });

    $("#showUploadModal").click(() => {
        $("#name").val(null);
        $("#uploadModal").modal("show");
    })
}
