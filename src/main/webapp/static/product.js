
const brands = {};

function getForm(id) {
    console.log("saving product");
    const product = {};
    product.name = $("#name" + id).val();
    product.barcode = $("#barcode" + id).val();
    product.mrp = $("#mrp" + id).val();
    product.brand = $("#brand" + id).val();
    product.category = $("#category" + id).val();
    return product;
}

function update(id) {
    let product = getForm(id);
    if (product.brand == '' || product.category == '') {
        toast("error", "Please select Brand and Category for product");
        return;
    }
    if (isNaN(product.mrp)) {
        toast("error", "MRP should be a Number");
        return;
    }

    product.brandId = brands[product.brand][product.category];
    console.log("product", product);
    const data = JSON.stringify(product);


    $.ajax({
        url: "/api/products/" + id,
        type: "PUT",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            toast("success", "Brand Updataed Successfully !");
            product = response;
            $("#product" + id).html(
                `   <td>${product.name}</td>
                    <td>${product.barcode}</td>
                    <td>${product.mrp}</td>
                    <td>${product.brandName}</td>
                    <td>${product.brandCategory}</td>
                    <td><div class="btn btn-primary"
                    onclick="showEdit(${product.id},'${product.name}','${product.barcode}','${product.mrp}')">
                    Edit</div></td>
                    `
            );
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function saveProduct1() {
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

        },
        error: function (e) {

            console.log("ERROR : ", e.responseJSON.message);

        }
    });

}

function cancelUpdate(id, name, barcode, mrp, brandName, brandCategory) {
    $("#product" + id).html(
        `   <td>${name}</td>
                    <td>${barcode}</td>
                    <td>${mrp}</td>
                    <td>${brandName}</td>
                    <td>${brandCategory}</td>
                    <td><div class="btn btn-primary"
                    onclick="showEdit(${id},'${name}','${barcode}','${mrp}','${brandName}','${brandCategory}')">
                    Edit</div></td>
                    `
    );
}


function saveProduct() {
    let product = getForm('');
    if (product.brand == '' || product.category == '') {
        toast("error", "Please select Brand and Category for product");
        return;
    }
    if (isNaN(product.mrp)) {
        toast("error", "MRP should be a Number");
        return;
    }
    product.brandId = brands[product.brand][product.category];
    const data = JSON.stringify(product);


    $.ajax({
        url: "/api/products/",
        type: "POST",
        data: data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            toast("success", "Brand Saved Successfully !");
            product = response;
            $("#name").val("");
            $("#brand").val("");
            $("#mrp").val("");
            $("#category").val("");
            $("#barcode").val("");
            $("#tablebody").prepend(
                `<tr id='${"product" + product.id}'>
                    <td>${product.name}</td>
                    <td>${product.barcode}</td>
                    <td>${product.mrp}</td>
                    <td>${product.brandName}</td>
                    <td>${product.brandCategory}</td>
                    <td><div class="btn btn-primary"
                    onclick="showEdit(${product.id},'${product.name}','${product.barcode}','${product.mrp}','${product.brandName}','${product.brandCategory}')">
                    Edit</div></td>
                    </tr>`
            );
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function showEdit(id, name, barcode, mrp, brandName, brandCategory) {
    $("#product" + id).
        html(`<td>
        <input
            class="form-control"
            name=${"name" + id}
            id=${"name" + id}
            type="text"
            placeholder="Brand Name"
            value='${name}'
            autofocus
        /> 
        </td> <td>
        <input
            class="form-control"
            name=${"barcode" + id}
            id=${"barcode" + id}
            type="text"
            placeholder="barcode"
            value='${barcode}'
        /> </td>
         <td>
        <input
            class="form-control"
            name=${"mrp" + id}
            id=${"mrp" + id}
            type="text"
            placeholder="mrp"
            value='${mrp}'
        /> </td>
         <td>
         <select class="form-control" id="brand${id}" name="brand${id}" onchange="showcategory(${id})" ></select>
         </td>
         <td>
         <select class="form-control" id="category${id}" name="category${id}" ></select>

          </td>
        <td style="display:inline-block;">
        <div class="btn btn-primary" onclick="update(${id})">Save </div>
        <div class="btn btn-secondary ml-2" onclick="cancelUpdate(${id},'${name}','${barcode}','${mrp}','${brandName}','${brandCategory}')">Cancel </div>
        </td>
    `);
    renderBrands(id);
    renderCatgory(id, brandName);
    $("#brand" + id).val(brandName);
    $("#category" + id).val(brandCategory);

}




function loadAllProducts() {
    $.ajax({
        url: "/api/products/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            $("#tablebody").html("");
            response.forEach((product) => {
                $("#tablebody").prepend(
                    `<tr id='${"product" + product.id}'>
                    <td>${product.name}</td>
                    <td>${product.barcode}</td>
                    <td>${product.mrp}</td>
                    <td>${product.brandName}</td>
                    <td>${product.brandCategory}</td>
                    <td><div class="btn btn-primary"
                    onclick="showEdit(${product.id},'${product.name}','${product.barcode}','${product.mrp}','${product.brandName}','${product.brandCategory}')">
                    Edit</div></td>
                    </tr>`
                );
            });
        },
        error: function (error) {
            console.log(error);
            console.log(error);
            toast(
                "error",
                "Error in Fetching All Brands : " + error.responseJSON.message
            );
        },
    });
}



function loadAllBrands() {
    $.ajax({
        url: "/api/brands/",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            response.forEach(brand => {
                if (brands[brand.name] == null) {
                    brands[brand.name] = {};
                }
                brands[brand.name][brand.category] = brand.id;
            });
            renderBrands("");
        },
        error: function (error) {
            console.log(error);
            console.log(error);
            toast(
                "error",
                "Error in Fetching All Brands : " + error.responseJSON.message
            );
        },
    });

}


function showcategory(id) {
    console.log(id, $("#brand" + id).val());
    renderCatgory(id, $("#brand" + id).val());
}

function renderBrands(id) {
    const list = getBrandList();
    $("#brand" + id).html("");
    $("#brand" + id).append("<option selected value=''>Select Brand</option>")
    list.forEach(b => {
        $("#brand" + id).append(`<option value ="${b}"> ${b} </option>`)
    })
}

function renderCatgory(id, brand) {
    const list = getCategoryList(brand);
    $("#category" + id).html("");
    $("#category" + id).append("<option selected value=''>Select Category</option>")
    list.forEach(b => {
        $("#category" + id).append(`<option value ="${b}"> ${b} </option>`)
    })
}

function getBrandList() {
    return Object.keys(brands);
}

function getCategoryList(name) {
    console.log(brands[name]);
    return Object.keys(brands[name]);
}


function initProduct() {
    loadAllBrands();
    loadAllProducts();
}