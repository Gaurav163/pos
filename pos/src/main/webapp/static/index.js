function toast(type, message) {
    let tid = Date.now()
    if (type == "success") {
        $("#toast").prepend(`
        <div class="toast align-items-center text-white bg-success"
        role="alert" aria-live="assertive" aria-atomic="true" id="${tid}"  >
        <div class="d-flex">
            <div class="toast-body">
            ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 mt-3" onclick="$('#${tid}').fadeOut(500).next().remove()" ></button>
        </div>
        </div>
       `);
        setTimeout(() => {
            $("#" + tid).fadeOut(500).next().remove();
        }, 4000);
    } else {
        $("#toast").prepend(`
        <div class="toast align-items-center text-white bg-danger"
         role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false" id="${tid}">
        <div class="d-flex">
            <div class="toast-body">
            ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 mt-3" onclick="$('#${tid}').fadeOut(500).next().remove()" ></button>
        </div>
        </div>
       `);
    }
    $('.toast').toast('show');

}

function login() {
    const email = $("#email").val();
    const password = $("#password").val();
    if (email.length == 0 || password.length == 0) {
        toast("error", "Enter email ans password");
        return;
    }
    const d = { email, password };
    const jsonData = JSON.stringify(d);
    $.ajax({
        url: `/api/users/login`,
        type: "POST",
        data: jsonData,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            setTimeout(() => {
                window.location.replace("/");
            }, 2000);
            toast("success", "Welcome to pos");


        },
        error: function (error) {
            toast("error", error.responseJSON.message);
        },
    });
}

function signup() {
    const name = $("#name").val()
    const email = $("#email").val();
    const password = $("#password").val();
    const user = { name, email, password };
    const data = JSON.stringify(user);
    $.ajax({
        url: `/api/users/signup`,
        type: "POST",
        data,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            setTimeout(() => {
                window.location.replace("/user/login");
            }, 2000);
            toast("success", "Sign up success!");

        },
        error: function (error) {
            toast("error", error.responseJSON.message);

        },
    });
}

function setCurrent(page) {
    $("#nav-" + page).addClass("active-link");
}

function isNum(x) {
    if (isNaN(x)) {
        return false;
    }
    return !(Math.abs(x) != parseInt(x, 10) || parseInt(x, 10) < 0);
}

function isFloat(x) {
    return !isNaN(x);
}

function isString(x) {
    return x.length > 0;
}

function downloadAsTSV(data, name) {
    const rows = [];
    rows.push((Object.keys(data[0])).join("\t"));
    data.forEach(e => {
        rows.push((Object.values(e).join("\t")));
    });
    const tsvData = rows.join("\n");
    const linkSource = `data:text/tsv;utf8,${tsvData}`;
    const downloadLink = document.createElement("a");
    const fileName = name + ".tsv";
    downloadLink.href = linkSource;
    downloadLink.download = fileName;
    downloadLink.click();
}