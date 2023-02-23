let table;


function loadAllProducts() {
    $.ajax({
        url: "/api/products/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (products) {
            renderTable(products);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);

        },
    });
}

function renderTable(products) {
    $("#tablebody").html("");
    table = $("#main-table").DataTable();
    products.forEach(product => {
        prependProduct(product);
    });
    table.draw();
}

function prependProduct(product) {
    table.row.add([product.barcode, product.name, product.brand, product.category, product.mrp.toFixed(2),
    `<div class="btn btn-info" onclick="editProduct(${product.id},
         '${product.name}','${product.barcode}','${product.mrp}'
         ,'${product.brand}','${product.category}')">
         <i class="fa-regular fa-pen-to-square"></i> Edit
    </div>`
    ]).node().id = product.id;

}

function insertUpdatedProduct(product) {
    table.row(`#${product.id}`).remove();
    prependProduct(product);
    table.draw();
}

function createProduct() {
    console.log("create")
    let name = $("#name").val();
    let barcode = $("#barcode").val();
    let mrp = $("#mrp").val();
    let brand = $("#brand").val();
    let category = $("#category").val();
    const product = { name, barcode, mrp, brand, category };
    const data = JSON.stringify(product);
    console.log(data);
    $.ajax({
        url: "/api/products/",
        type: "POST",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            prependProduct(data);
            table.draw();
            $("#createModal").modal("hide");
            toast("success", "Product created");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function updateProduct() {
    let id = $("#u-id").val();
    let name = $("#u-name").val();
    let barcode = $("#u-barcode").val();
    let mrp = $("#u-mrp").val();
    let brand = $("#u-brand").val();
    let category = $("#u-category").val();
    const product = { name, barcode, mrp, brand, category };
    const data = JSON.stringify(product);
    $.ajax({
        url: "/api/products/" + id,
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            insertUpdatedProduct(data);
            $("#updateModal").modal("hide");
            toast("success", "Updated product saved");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function editProduct(id, name, barcode, mrp, brand, category) {
    console.log("update");
    $("#u-id").val(id);
    $("#u-name").val(name);
    $("#u-barcode").val(barcode);
    $("#u-mrp").val(mrp);
    $("#u-brand").val(brand);
    $("#u-category").val(category);
    $("#updateModal").modal("show");
}
function downloadSampleProduct() {
    console.log("yes");
    window.open("/sample/product", '_blank').focus();
}

function uploadProduct() {
    const data = new FormData();
    const file = $("#file")[0].files[0];
    data.append("file", file);
    if (file.type != "text/tab-separated-values") {
        toast("error", "Selected file must be a TSV (tab seperated value) file");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/api/products/upload",
        data: data,
        enctype: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data) {
            console.log("SUCCESS : ", data);
            $("#uploadModal").modal("hide");

        },
        error: function (e) {
            if (e.status == 400) {
                let text = e.responseJSON.message;
                $("#error-file").remove();
                let element = document.createElement('a');
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
                element.setAttribute('download', "errors.txt");
                element.setAttribute('id', "error-file");
                element.style.display = 'none';
                document.body.appendChild(element);
                $("#error-button").removeClass("d-none");

            }

            console.log("ERROR : ", e.responseJSON.message);
            toast("error", "Something went wrong with file, Download error file for more details")

        }
    });
}

function downloadErrorFile() {
    document.getElementById('error-file').click();
    $("#error-file").remove();
    $("#error-button").addClass("d-none");
}

function initProduct() {
    loadAllProducts();
    $("#showCreateModal").click(() => {
        $("#name").val("");
        $("#barcode").val("");
        $("#brand").val("");
        $("#category").val("");
        $("#mrp").val("");
        $("#createModal").modal("show");
    });

    $("#showUploadModal").click(() => {
        $("#name").val(null);
        $("#uploadModal").modal("show");
    })
}