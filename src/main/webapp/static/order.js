
const productList = {};

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