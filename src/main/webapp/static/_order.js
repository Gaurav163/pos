let table = null;
function initOrders() {
    showAll();
}

function showAll() {
    $.ajax({
        url: "/api/orders/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            showOrders(response);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function showOrders(orders) {
    $("#tablebody2").html("");
    table = $("#main-table").DataTable();
    orders.forEach(order => {
        console.log(order);
        appendOrder(order);
        // $("#tablebody2").append(`
        // <tr>
        // <td>${order.id}</td>
        // <td>${order.date}</td>
        // <td>${order.time}</td>
        // <td> 
        // <button class="btn btn-primary mx-2" onclick="getInvoice(${order.id},'download')"> Download </button>
        // </td>
        // </tr>
        // `);
    });
    table.draw();
}

function appendOrder(order) {
    table.row.add([order.id, order.date, order.time,
    getInvoiceButton(order.id, order.invoiced) + `<div class="cdiv" onclick="viewDetails(${order.id})"> View Details </div>`]).node().id = order.id;
}

function getInvoiceButton(id, invoiced) {
    if (invoiced) {
        return `<div class="cdiv" onclick="getInvoice(${id})"> Download Invoice </div>`;
    } else {
        return `<div class="cdiv" onclick="generateInvoice(${id})"> Generate Invoice </div>`;

    }
}

let productList = {};

function addProduct() {
    let barcode = $("#barcode").val();
    let quantity = $("#quantity").val();
    let price = $("#price").val();
    if (!isFloat(price)) {
        toast("error", "Selling Price should be a Postive number.");
        return;
    }
    if (!isNum(quantity)) {
        toast("error", "Quantity should be positive.");
        return;
    }

    const product = { barcode, quantity, price };
    productList[product.barcode] = product;
    renderProductList();
}

function renderProductList() {
    $("#tablebody").html("");
    Object.values(productList).forEach(product => {
        $("#tablebody").append(`
        <tr>=
            <td scope="col">${product.barcode}</td>
            <td scope="col">${product.quantity}</td>
            <td scope="col">${product.price}</td>
            <td scope="col"><div class="cdiv" onclick="editProduct('${product.barcode}')"> Edit</div> <div class="cdiv ms-4" onclick="removeProduct('${product.barcode}')"> Remove</div></td>
        </tr>
        `);
    })
}

function removeProduct(barcode) {
    delete productList[barcode];
    renderProductList();
}

function editProduct(barcode) {
    const product = productList[barcode];
    delete productList[barcode];
    renderProductList();

    $("#barcode").val(product.barcode);
    $("#quantity").val(product.quantity);
    $("#price").val(product.price);
}



function createOrder() {
    const itemList = Object.values(productList).map(item => {
        return {
            barcode: item.barcode,
            quantity: item.quantity,
            sellingPrice: item.price
        }
    });
    const data = JSON.stringify(itemList);
    console.log(data);
    $.ajax({
        url: "/api/orders/",
        type: "POST",
        data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (order) {
            console.log("order created");
            toast("success", "Order created");
            appendOrder(order);
            table.draw();
            productList = {};
            renderProductList();
            $("#createModal").modal("hide");

        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function generateInvoice(id) {
    $.ajax({
        url: "/api/orders/invoice/" + id,
        type: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (order) {
            table.row("#" + order.id).remove();
            appendOrder(order);
            table.draw();
            toast("success", "Invoice created");

        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}


function getInvoice(id) {
    $.ajax({
        url: "/api/orders/invoice/" + id,
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            downloadInvoice(response, id);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function downloadInvoice(data, id) {
    const linkSource = `data:application/pdf;base64,${data}`;
    const downloadLink = document.createElement("a");
    const fileName = "invoice-" + id + ".pdf";
    downloadLink.href = linkSource;
    downloadLink.download = fileName;
    downloadLink.click();
}

function renderDetails(order) {
    console.log(order);
    $("#orderid").text("Order ID: " + order.id);
    $("#orderdate").text("Date: " + order.date);
    $("#ordertime").text("Time: " + order.time);
    let total = 0;
    let count = 0;
    $("#details-table").html("");
    order.items.forEach(item => {
        total += item.sellingPrice * item.quantity;
        count += item.quantity;
        $("#details-table").append(`
        <tr>
        <td>${item.barcode}</td>
        <td>${item.name}</td>
        <td>${item.brand}</td>
        <td>${item.category}</td>
        <td>${item.quantity}</td>
        <td>${item.sellingPrice}</td>
        <td>${item.quantity * item.sellingPrice}</td>
        </tr>
        `)
    });

    $("#total-items").text("Total Items: " + count);
    $("#total-bill").text("Total Bill: Rs. " + total);

    $("#detailModal").modal("show");




}



function viewDetails(id) {
    console.log(id);
    $.ajax({
        url: "/api/orders/" + id,
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (order) {
            renderDetails(order);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}


