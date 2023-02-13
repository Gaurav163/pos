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

function signup() {
    const name = $("#name").val()
    const email = $("#email").val();
    const password = $("#password").val();
    if (email.length == 0 || password.length == 0 || name.length == 0) {
        toast("error", "Enter email ans password");
        return;
    }
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
            toast("success", "Sign up success, Redirecting to signin page");

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
    return !(Math.abs(x) != parseInt(x, 10) || parseInt(x, 10) < 0);
}

function isFloat(x) {
    return !isNaN(x);
}

function isString(x) {
    return x.length > 0;
}

function downloadAsTSV(data) {
    const rows = [];
    rows.push((Object.keys(data[0])).join("\t"));
    data.forEach(e => {
        rows.push((Object.values(e).join("\t")));
    });
    const tsvData = rows.join("\n");
    const linkSource = `data:text/tsv;utf8,${tsvData}`;
    const downloadLink = document.createElement("a");
    const fileName = "report.tsv";
    downloadLink.href = linkSource;
    downloadLink.download = fileName;
    downloadLink.click();
}