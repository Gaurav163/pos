function addQuantity() {
    let barcode = $("#barcode").val();
    let quantity = parseInt($("#quantity").val());
    if (isNaN(quantity)) {
        toast("error", "Quantity should be number.");
        return;
    }
    if (Math.abs(quantity) != parseInt(quantity, 10) || parseInt(quantity, 10) <= 0) {
        toast("error", "Quantity should should be a positive number.");
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
        success: function (response) {
            console.log(response);
            toast("success", "Items Added to Inventory.")
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    })

}

function initInventory() {

    $.ajax({
        url: "/api/inventories/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            showTable(response);
            toast("success", "Data loaded");
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    })
}

function showTable(data) {
    $("#tablebody").html("");
    data.forEach(inv => {
        $("#tablebody").append(`
        <tr>
        <td>${inv.barcode}</td>
        <td>${inv.name}</td>
        <td>${inv.quantity}</td>
        </tr>
        `);
    });
}