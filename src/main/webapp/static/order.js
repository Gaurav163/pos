
const productList = {};


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

function showOrders(orders) {
    $("#tablebody2").html("");
    orders.forEach(order => {
        $("#tablebody2").append(`
        <tr>
        <td>${order.id}</td>
        <td>${order.date}</td>
        <td>${order.time}</td>
        <td> 
        <button class="btn btn-info" onclick="getInvoice(${order.id},'download')"> Download </button>
        <button class="btn btn-info" onclick="getInvoice(${order.id},'print')"> Print </button>
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
            sellingPrice: item.sellingPrice
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
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function addProduct() {
    let barcode = $("#barcode").val();
    let quantity = $("#quantity").val();
    let sellingPrice = $("#sellingPrice").val();
    if (isNaN(sellingPrice) || parseInt(sellingPrice, 10) <= 0) {
        toast("error", "Selling Price should be a Postive number.");
        return;
    }
    if (isNaN(quantity)) {
        toast("error", "Quantity should be number.");
        return;
    }
    if (Math.abs(quantity) != parseInt(quantity, 10) || parseInt(quantity, 10) <= 0) {
        toast("error", "Quantity should should be a positive number.");
        return;
    }



    $.ajax({
        url: "/api/inventories/" + barcode,
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            if (response.quantity < quantity) {
                toast("error", "There is only " + response.quantity + " product present in Inventory");
            }
            else {
                getProductByBarcode(barcode, response, quantity, sellingPrice);
            }
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function getProductByBarcode(barcode, inventory, quantity, sellingPrice) {
    $.ajax({
        url: "/api/products/barcode/" + barcode,
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (product) {
            addNewProduct(inventory, product, quantity, sellingPrice);

        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });
}

function addNewProduct(inventory, product, quantity, sellingPrice) {
    console.log(inventory, product);
    let newP = {
        name: product.name,
        barcode: product.barcode,
        brand: product.brandName,
        mrp: product.mrp,
        quantity,
        sellingPrice
    }

    if (productList[product.barcode]) {
        console.log(product.barcode);
        toast("error", "Product with given barcode already in cart");
    }
    else {
        productList[product.barcode] = newP;
        renderProductList();
    }

}


function renderProductList() {
    $("#tablebody").html("");
    Object.values(productList).forEach(product => {
        $("#tablebody").append(`
        <tr>
            <td scope="col">${product.name}</td>
            <td scope="col">${product.barcode}</td>
            <td scope="col">${product.brand}</td>
            <td scope="col">${product.mrp}</td>
            <td scope="col">${product.sellingPrice}</td>
            <td scope="col">${product.quantity}</td>
            <td scope="col"><button class="btn btn-warning" onclick="removeProduct('${product.barcode}')"> Remove</button></td>
        </tr>
        `);
    })
}

function removeProduct(barcode) {
    delete productList[barcode];
    renderProductList();
}