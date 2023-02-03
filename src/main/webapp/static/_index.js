function toast(type, message) {
    console.log("toast", message);
    let c = Date.now();
    $("#toaster").html("");
    $("#toaster").attr("class", "");
    $("#toaster").addClass("toast-" + type);
    $("#toaster").text(message);
    $("#toaster").fadeOut();
    $("#toaster").fadeIn(200);
    toastId = c;
    fadeToast(c);
}

let toastId = 0;

function fadeToast(time) {
    setTimeout(
        () => {
            if (time == toastId) {
                $("#toaster").fadeOut(1000);
            }
        },
        7000,
        time
    );
}

function login() {
    const email = $("#email").val();
    const password = $("#password").val();
    if (email.length == 0 || password.length == 0) {
        toast("error", "Enter email ans password");
        return;
    }
    $.ajax({
        url: `/login?username=${email}&password=${password}`,
        type: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            setTimeout(() => {
                window.location.replace("/");
            }, 2000);
            toast("success", "Signin success");


        },
        error: function (error) {
            toast("error", error);

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
    return !(Math.abs(x) != parseInt(x, 10) || parseInt(x, 10) <= 0);
}

function isFloat(x) {
    return !isNaN(x);
}

function isString(x) {
    return x.length > 0;
}