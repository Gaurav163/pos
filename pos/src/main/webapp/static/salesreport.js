let table = null;
let data;

function showReport() {
    let startDate = new Date($("#startDate").val());
    let endDate = new Date($("#endDate").val());
    if ($("#startDate").val().length == 0) {
        startDate = new Date(0);
    }

    if ($("#endDate").val().length == 0) {
        endDate = new Date();
    }
    let brand = $("#brand").val();
    let category = $("#category").val();
    let form = { startDate, endDate, brand, category };
    let jsonform = JSON.stringify(form);
    console.log(form, jsonform);

    $.ajax({
        url: "/api/reports/sales",
        type: "POST",
        data: jsonform,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            showdata(response);
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function showdata(reports) {
    data = [];
    table.row().remove();
    reports.forEach(report => {
        report.revenue = report.revenue.toFixed(2);
        data.push(report);
        table.row.add([report.brand, report.category, report.quantity, report.revenue]);
    });
    table.draw();
}

function downloadData() {
    console.log(data);
    downloadAsTSV(data);
}

function initReport() {
    table = $("#main-table").DataTable();
    showReport();
}
