let table = null;
let data = null;


function showReport() {

    $.ajax({
        url: "/api/reports/brand",
        type: "GET",
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            showdata(response);
            data = response;
        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function showdata(reports) {
    table.row().remove();
    reports.forEach(report => {
        table.row.add([report.name, report.category]);
    });
    table.draw();
}

function downloadData() {
    downloadAsTSV(data, "Brands_Report");
}

function initReport() {
    table = $("#main-table").DataTable();
    showReport();
}