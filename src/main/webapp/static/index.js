function toast(type, message) {
	console.log("toast", message);
	let c = Date.now();
	$("#toaster").html("");
	$("#toaster").attr("class", "");
	$("#toaster").addClass("toast-" + type);
	$("#toaster").text(message);
	$("#toaster").fadeIn(500);
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
		3000,
		time
	);
}
