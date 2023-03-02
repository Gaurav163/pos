let table = null;
let data = null;


function showReport() {
    let startDate = new Date($("#startDate").val());
    let endDate = new Date($("#endDate").val());
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
    $("#d-report").removeClass("d-none");

}

function showdata(reports) {
    data = [];
    table.rows().remove();
    reports.forEach(report => {
        report.totalRevenue = report.totalRevenue.toFixed(2);
        data.push(report);
        table.row.add([report.date, report.invoicedOrdersCount, report.invoicedItemsCount, report.totalRevenue]);
    });
    table.order([[0, 'desc']]);
    table.draw();
}

function downloadData() {
    downloadAsTSV(data, "Daily_Sales_Report");
}

function initReport() {
    table = $("#main-table").DataTable();
    $("#startDate").val("2023-01-01");
    $("#endDate").val(new Date().toISOString().slice(0, 10).replace(":", "-"));
}