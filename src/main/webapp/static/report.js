function showReport() {
    let startDate = new Date($("#startDate").val());
    let endDate = new Date($("#endDate").val());
    endDate.setDate(endDate.getDate() + 1);
    let brand = $("#brand").val();
    let category = $("#category").val();
    let data = { startDate, endDate, brand, category };
    let jsonData = JSON.stringify(data);
    console.log(data, jsonData);

    $.ajax({
        url: "/api/reports/",
        type: "POST",
        data: jsonData,
        headers: {
            "Content-Type": "application/json",
        },
        success: function (response) {
            console.log(response);
            showdata(response);
            toast("success", "Report fetched success");

        },
        error: function (error) {
            console.log(error);
            toast("error", "Error : " + error.responseJSON.message);
        },
    });

}

function showdata(reports) {
    $("#tablebody").html("");
    reports.forEach(report => {
        $("#tablebody").append(`
        <tr>
        <td>${report.brand}</td>
        <td>${report.category}</td>
        <td>${report.quantity}</td>
        <td>${report.revenue}</td>
        </tr>
        `);
    });
}