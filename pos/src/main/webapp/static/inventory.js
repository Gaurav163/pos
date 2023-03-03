let table;

function showAll() {
    $.ajax({
        url: "/api/inventories/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            renderTable(data);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error while loading list : " + error.responseJSON.message);
        },
    })
}

function renderTable(inventories) {
    $("#tablebody").html("");
    table = $("#main-table").DataTable();
    inventories.forEach(inv => {
        appendInventory(inv);
    });
    table.draw();
}

function appendInventory(inv) {
    table.row.add([inv.barcode, inv.name, inv.brand, inv.category, inv.quantity,
    `<div class="btn btn-info" onclick="editInventory('${inv.barcode}','${inv.quantity}')">
        <i class="fa-regular fa-pen-to-square"></i> Edit</div>`
    ]).node().id = inv.barcode;
}

function updateRow(inv) {
    table.row(`#${inv.barcode}`).remove();
    appendInventory(inv);
    table.draw();
}

function editInventory(barcode, quantity) {
    $("#u-id").val(barcode);
    $("#u-barcode").val(barcode);
    $("#u-quantity").val(quantity);
    $("#updateModal").modal("show");
}

function addQuantity() {
    let barcode = $("#barcode").val();
    let quantity = parseInt($("#quantity").val());
    if (!isString(barcode)) {
        toast("error", "Barcode must not be empty");
        return;
    }
    if (!isNum(quantity)) {
        toast("error", "Quantity should be number");
        return;
    }

    const form = { barcode, quantity }
    const data = JSON.stringify(form);
    console.log(data);

    $.ajax({
        url: "/api/inventories/increase",
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (inv) {
            console.log(inv);
            toast("success", "Quantity added to inventory")
            $("#u-barcode").val("");
            $("#u-quantity").val("");
            $("#createModal").modal("hide");
            updateRow(inv);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    })

}

function updateInventory() {
    let barcode = $("#u-barcode").val();
    let quantity = $("#u-quantity").val();
    if (!isString(barcode)) {
        toast("error", "Barcode must not be empty");
        return;
    }
    if (!isNum(quantity)) {
        toast("error", "Quantity should be number");
        return;
    }

    const form = { barcode, quantity }
    const data = JSON.stringify(form);
    console.log(data);

    $.ajax({
        url: "/api/inventories/",
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (inv) {
            console.log(inv);
            toast("success", "Inventory Updated")
            $("#u-barcode").val("");
            $("#u-quantity").val("");
            $("#updateModal").modal("hide");
            updateRow(inv);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    })
}

function downloadSampleInventory() {
    console.log("yes");
    window.open("/sample/sample-inventory.tsv", '_blank').focus();
}


function uploadQuantity() {
    const data = new FormData();
    const file = $("#file")[0].files[0];
    data.append("file", file);
    if (file.type != "text/tab-separated-values") {
        toast("error", "Selected file must be a TSV (tab seperated value) file");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/api/inventories/upload",
        data: data,
        enctype: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        success: function (data) {
            toast("success", "Inventory uploaded successfully");
            $("#uploadModal").modal("hide");
            showAll();

        },
        error: function (e) {
            if (e.status == 400) {
                let text = e.responseJSON.message;
                $("#error-file").remove();
                let element = document.createElement('a');
                element.setAttribute('href', 'data:text/tsv;charset=utf-8,' + encodeURIComponent(text));
                element.setAttribute('download', "errors" + file.name);
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

function initInventory() {
    showAll();

    $("#showCreateModal").click(() => {
        $("#barcode").val("");
        $("#quantity").val("");
        $("#createModal").modal("show");
    });

    $("#showUploadModal").click(() => {
        $("#name").val(null);
        $("#uploadModal").modal("show");
    })
}
