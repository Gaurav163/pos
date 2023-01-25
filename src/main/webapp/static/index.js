function toast(type, message) {
	console.log("toast", message);
	let c = Date.now();
	$("#toaster").html("");
	$("#toaster").attr("class", "");
	$("#toaster").addClass("toast-" + type);
	$("#toaster").text(message);
	$("#toaster").fadeOut();
	$("#toaster").fadeIn(1000);
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
