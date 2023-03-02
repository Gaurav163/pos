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
    table.order([0, "desc"]);
    table.draw();
}

function appendOrder(order) {
    let datetime = new Date(Date.parse(order.datetime));
    table.row.add([order.id, datetime.toDateString().slice(4), datetime.toLocaleTimeString(),
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
    let quantity = parseInt($("#quantity").val());
    let price = parseFloat($("#price").val());
    const product = { barcode, quantity, price };
    const key = barcode + "_" + price.toFixed(2);
    if (productList[key]) productList[key].quantity = productList[key].quantity + quantity;
    else productList[key] = product;
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
            <td scope="col"><div class="btn btn-info" onclick="editProduct('${product.barcode}_${product.price.toFixed(2)}')">
            <i class="fa-regular fa-pen-to-square"></i> Edit</div>
             <div class="btn btn-warning ms-2" onclick="removeProduct('${product.barcode}_${product.price.toFixed(2)}')">
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
                let errs = error.responseJSON.message.replaceAll("\n", "<br>");
                console.log(errs);
                toast("error", errs);
            }
        },
    });

}

function generateInvoice(id) {
    $.ajax({
        url: "/api/orders/" + id + "/invoice",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (invoice) {
            downloadInvoice(invoice, id);
            let rowData = table.row(`#${id}`).data();
            let downloadbtn = `<div class=\"btn btn-primary\" onclick=\"getInvoice(${id})\">
            <i class=\"fa-solid fa-file-arrow-down\"></i> Download Invoice </div><div class=\"btn btn-success ms-4\" onclick=\"viewDetails(${id})\">
            <i class=\"fa-regular fa-eye\"></i> View Details </div>"`;
            rowData[3] = downloadbtn;
            table.row(`#${id}`).remove();
            table.row.add(rowData).node.id = id;
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
        url: "/api/orders/" + id + "/invoice",
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

    $("#total-items").html("<strong>Total Items: </strong>" + count);
    $("#total-bill").html("<strong>Total Amount: </strong>" + total.toFixed(2));

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



