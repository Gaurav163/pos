let table = null;
let productList = {};

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
    });
    table.draw();
}

function appendOrder(order) {
    table.row.add([order.id, order.date, order.time,
    getInvoiceButton(order.id, order.invoiced) + `<div class="btn btn-success ms-4" onclick="viewDetails(${order.id})"><i class="fa-regular fa-eye"></i> View Details </div>`]).node().id = order.id;
}

function getInvoiceButton(id, invoiced) {
    if (invoiced) {
        return `<div class="btn btn-primary" onclick="getInvoice(${id})"><i class="fa-solid fa-file-arrow-down"></i> Download Invoice </div>`;
    } else {
        return `<div class="btn btn-info" onclick="generateInvoice(${id})"> <i class="fa-solid fa-receipt"></i> Generate Invoice </div>`;

    }
}


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
    price = parseFloat(price);

    const product = { barcode, quantity, price };
    productList[product.barcode] = product;
    renderProductList();
    $("#barcode").val("");
    $("#quantity").val("");
    $("#price").val("");
}

function renderProductList() {
    $("#tablebody").html("");
    Object.values(productList).forEach(product => {
        $("#tablebody").append(`
        <tr>=
            <td scope="col">${product.barcode}</td>
            <td scope="col">${product.quantity}</td>
            <td scope="col">${product.price.toFixed(2)}</td>
            <td scope="col"><div class="btn btn-info" onclick="editProduct('${product.barcode}')">
            <i class="fa-regular fa-pen-to-square"></i> Edit</div>
             <div class="btn btn-warning ms-2" onclick="removeProduct('${product.barcode}')">
             <i class="fa-solid fa-xmark"></i> Remove</div></td>
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
    const orderForm = { items: itemList }
    const data = JSON.stringify(orderForm);
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
            if (error.status == 400) {
                let errs = error.responseJSON.message.split("\n");
                console.log(errs);
                errs.forEach(err => toast("error", err));
            }
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
    $("#orderid").text("Order No.: " + order.id);
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
        <td>${item.sellingPrice.toFixed(2)}</td>
        <td>${(item.quantity * item.sellingPrice).toFixed(2)}</td>
        </tr>
        `)
    });

    $("#total-items").append("<strong>Total Items: </strong>" + count);
    $("#total-bill").append("<strong>Total Bill: </strong>" + total.toFixed(2));

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

function initOrders() {
    showAll();
    $("#showCreateModal").click(() => {
        $("#barcode").val("");
        $("#quantity").val("");
        $("#price").val("");
        productList = {};
        renderProductList();
        $("#createModal").modal("show");
    });
}



