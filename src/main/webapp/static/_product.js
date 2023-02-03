function initProduct() {
    loadAllProducts();
}

const brands = {};

function loadAllBrands() {
    $.ajax({
        url: "/api/brands/",
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
    products.forEach(product => {
        prependProduct(product);
    });
}

function prependProduct(product) {
    $("#tablebody").prepend(`
    <tr id='${product.id}'>
    <td>${product.name}</td>
    <td>${product.barcode}</td>
    <td>${product.mrp}</td>
    <td>${product.brand}</td>
    <td>${product.category}</td>
    <td><div class="cdiv" onclick="editProduct(${product.id},
        '${product.name}','${product.barcode}','${product.mrp}'
        ,'${product.brand}','${product.category}')">Edit</div></td>
    </tr>
    `);
}

function insertUpdatedBrand(product) {
    $("#" + product.id).html(`
    <td>${product.name}</td>
    <td>${product.barcode}</td>
    <td>${product.mrp}</td>
    <td>${product.brand}</td>
    <td>${product.category}</td>
    <td><div class="cdiv" onclick="editProduct(${product.id},
        '${product.name}','${product.barcode}','${product.mrp}'
        ,'${product.brand}','${product.category}')">Edit</div>
    </td>
    `);
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
            insertUpdatedBrand(data);
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
            insertUpdatedBrand(data);
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

function uploadProduct() {
    const data = new FormData();
    data.append("file", $("#file")[0].files[0]);
    console.log($("#file")[0].files[0]);

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
                let element = document.createElement('a');
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
                element.setAttribute('download', "errors.txt");
                element.setAttribute('id', "error-file");
                element.style.display = 'none';
                document.body.appendChild(element);
                $("#upload-modal-body").append(`<button class="btn btn-warning ms-4" id="error-button" onclick="downloadErrorFile()">Download Errors</button>`)
            }

            console.log("ERROR : ", e.responseJSON.message);
            toast("error", "Something went wrong with file, Download error file for more details")

        }
    });
}