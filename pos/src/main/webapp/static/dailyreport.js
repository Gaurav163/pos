let table = null;
let data = null;


function showReport() {
    let startDate = new Date($("#startDate").val());
    let endDate = new Date($("#endDate").val());
    if ($("#startDate").val().length == 0) {
        startDate = new Date(0);
    }
    if ($("#endDate").val().length == 0) {
        endDate = new Date();
    }
    let query = `?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`
    console.log(query);
    $.ajax({
        url: "/api/reports/daily" + query,
        type: "GET",
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
        report.totalRevenue = report.totalRevenue.toFixed(2);
        data.push(report);
        table.row.add([report.date, report.invoicedItemsCount, report.invoicedOrdersCount, report.totalRevenue]);
    });
    table.draw();
}

function downloadData() {
    downloadAsTSV(data);
}

function initReport() {
    table = $("#main-table").DataTable();
    showReport();
}