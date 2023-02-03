
function initInventory() {
    showAll();
}

function showAll() {
    $.ajax({
        url: "/api/inventories/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (data) {
            console.log(data);
            showTable(data);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error while loading list : " + error.responseJSON.message);
        },
    })
}

function showTable(data) {
    $("#tablebody").html("");
    data.forEach(inv => {
        $("#tablebody").append(`
        <tr id="${inv.id}">
        <td>${inv.barcode}</td>
        <td>${inv.name}</td>
        <td>${inv.quantity}</td>
        <td><div class="cdiv" onclick="editInventory('${inv.barcode}','${inv.quantity}')">Edit</div></td>
        </tr>
        `);
    });
}

function editInventory(barcode, quantity) {
    $("#u-id").val(barcode);
    $("#u-barcode").val(barcode);
    $("#u-quantity").val(quantity);
    $("#updateModal").modal("show");
}

function addQuantity() {
    console.log("add");
}
function uploadQuantity() {
    console.log("upload");
}
function updateInventory() {
    console.log("update");
}