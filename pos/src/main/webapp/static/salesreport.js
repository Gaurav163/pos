let table = null;
let data;

function showReport() {
    let startDate = new Date($("#startDate").val());
    let endDate = new Date($("#endDate").val());
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
    $("#d-report").removeClass("d-none");

}

function showdata(reports) {
    data = [];
    table.rows().remove();
    reports.forEach(report => {
        report.revenue = report.revenue.toFixed(2);
        data.push(report);
        table.row.add([report.brand, report.category, report.quantity, report.revenue]);
    });
    table.draw();
}

function downloadData() {
    console.log(data);
    downloadAsTSV(data, "Sales_Report");
}

function initReport() {
    table = $("#main-table").DataTable();
    $("#startDate").val("2023-01-01");
    $("#endDate").val(new Date().toISOString().slice(0, 10).replace(":", "-"));
}
