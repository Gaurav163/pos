
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
    orders.forEach(order => {
        $("#tablebody2").append(`
        <tr>
        <td>${order.id}</td>
        <td>${order.date}</td>
        <td>${order.time}</td>
        <td> 
        <button class="btn btn-primary mx-2" onclick="getInvoice(${order.id},'download')"> Download </button>
        <button class="btn btn-primary mx-2" onclick="getInvoice(${order.id},'print')"> Print </button>
        </td>
        </tr>
        `);
    });
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
        success: function (response) {
            console.log("order created");
            toast("success", "Order created")
            printInvoice(response);
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


function getInvoice(id, task) {
    $.ajax({
        url: "/api/orders/invoice/" + id,
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            if (task == "print") {
                printInvoice(response);
            } else {
                downloadInvoice(response, id);
            }
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function printInvoice(data) {
    let winparams = 'dependent=yes,locationbar=no,scrollbars=yes,menubar=yes,' +
        'resizable,screenX=50,screenY=50,width=1200,height=1050';

    let htmlPop = '<embed width=100% height=100%'
        + ' type="application/pdf"'
        + ' src="data:application/pdf;base64,'
        + data
        + '"></embed>';

    let printWindow = window.open("", "PDF", winparams);
    printWindow.document.write(htmlPop);
}

function downloadInvoice(data, id) {
    const linkSource = `data:application/pdf;base64,${data}`;
    const downloadLink = document.createElement("a");
    const fileName = "invoice-" + id + ".pdf";
    downloadLink.href = linkSource;
    downloadLink.download = fileName;
    downloadLink.click();
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

