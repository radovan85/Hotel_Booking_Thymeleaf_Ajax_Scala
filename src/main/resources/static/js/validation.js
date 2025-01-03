function validateRegForm() {

	var firstName = document.getElementById("firstName").value.trim();
	var lastName = document.getElementById("lastName").value.trim();
	var email = document.getElementById("email").value.trim();
	var password = document.getElementById("password").value.trim();
	var confirmpass = document.getElementById("confirmpass").value.trim();
	var idNumber = document.getElementById("idNumber").value.trim();
	var phoneNumber = document.getElementById("phoneNumber").value.trim();

	var firstNameError = document.getElementById("firstNameError");
	var lastNameError = document.getElementById("lastNameError");
	var emailError = document.getElementById("emailError");
	var passwordError = document.getElementById("passwordError");
	var idNumberError = document.getElementById("idNumberError");
	var phoneNumberError = document.getElementById("phoneNumberError");

	var regEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/g;
	var returnValue = true;

	if (password !== confirmpass) {
		returnValue = false;
		alert(`Password not match!`)
	}

	if (firstName === "" || firstName.length > 30) {
		firstNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		firstNameError.style.visibility = "hidden";
	}


	if (lastName === "" || lastName.length > 30) {
		lastNameError.style.visibility = "visible";
		returnValue = false;
	} else {
		lastNameError.style.visibility = "hidden";
	}


	if (email === "" || !regEmail.test(email) || email.length > 40) {
		emailError.style.visibility = "visible";
		returnValue = false;
	} else {
		emailError.style.visibility = "hidden";
	}


	if (password.length < 6 || password.length > 30) {
		passwordError.style.visibility = "visible";
		returnValue = false;
	} else {
		passwordError.style.visibility = "hidden";
	}

	if (idNumber.length < 6 || idNumber.length > 12) {
		idNumberError.style.visibility = "visible";
		returnValue = false;
	} else {
		idNumberError.style.visibility = "hidden";
	}

	if (phoneNumber.length < 9 || phoneNumber.length > 15) {
		phoneNumberError.style.visibility = "visible";
		returnValue = false;
	} else {
		phoneNumberError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateReservation() {

	var checkInDateStr = document.getElementById("checkInDateStr").value.trim();
	var numberOfNights = document.getElementById("numberOfNights").value.trim();

	var checkInDateStrError = document.getElementById("checkInDateStrError");
	var numberOfNightsError = document.getElementById("numberOfNightsError");

	var numberOfNightsNum = Number(numberOfNights);
	var returnValue = true;

	if (checkInDateStr === "") {
		checkInDateStrError.style.visibility = "visible";
		returnValue = false;
	} else {
		checkInDateStrError.style.visibility = "hidden";
	}


	if (numberOfNights === "" || numberOfNightsNum < 1
		|| numberOfNightsNum > 30) {
		numberOfNightsError.style.visibility = "visible";
		returnValue = false;
	} else {
		numberOfNightsError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateRoomCategory() {
	var name = document.getElementById("name").value.trim();
	var price = document.getElementById("price").value.trim();
	var wc = document.getElementById("wc").value.trim();
	var wifi = document.getElementById("wifi").value.trim();
	var tv = document.getElementById("tv").value.trim();
	var bar = document.getElementById("bar").value.trim();

	var nameError = document.getElementById("nameError");
	var priceError = document.getElementById("priceError");
	var wcError = document.getElementById("wcError");
	var wifiError = document.getElementById("wifiError");
	var tvError = document.getElementById("tvError");
	var barError = document.getElementById("barError");

	var priceNum = Number(price);
	var returnValue = true;

	if (name === "" || name.length > 30) {
		nameError.style.visibility = "visible";
		returnValue = false;
	} else {
		nameError.style.visibility = "hidden";
	}


	if (price === "" || priceNum <= 0) {
		priceError.style.visibility = "visible";
		returnValue = false;
	} else {
		priceError.style.visibility = "hidden";
	}


	if (wc === "") {
		wcError.style.visibility = "visible";
		returnValue = false;
	} else {
		wcError.style.visibility = "hidden";
	}


	if (wifi === "") {
		wifiError.style.visibility = "visible";
		returnValue = false;
	} else {
		wifiError.style.visibility = "hidden";
	}


	if (tv === "") {
		tvError.style.visibility = "visible";
		returnValue = false;
	} else {
		tvError.style.visibility = "hidden";
	}


	if (bar === "") {
		barError.style.visibility = "visible";
		returnValue = false;
	} else {
		barError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateRoom() {

	var roomNumber = document.getElementById("roomNumber").value.trim();
	var roomCategory = document.getElementById("roomCategory").value.trim();

	var roomNumberError = document.getElementById("roomNumberError");
	var roomCategoryError = document.getElementById("roomCategoryError");

	var roomNumberNum = Number(roomNumber);
	var returnValue = true;

	if (roomNumber === "" || roomNumberNum <= 0) {
		roomNumberError.style.visibility = "visible";
		returnValue = false;
	} else {
		roomNumberError.style.visibility = "hidden";
	}


	if (roomCategory === "" || roomCategory.length > 30) {
		roomCategoryError.style.visibility = "visible";
		returnValue = false;
	} else {
		roomCategoryError.style.visibility = "hidden";
	}

	return returnValue;
};

function validateRoomId() {
	var roomId = document.getElementById("roomId").value.trim();
	var roomIdError = document.getElementById("roomId");
	var returnValue = true;

	if (roomId === "") {
		roomIdError.style.visibility = "visible";
		returnValue = false;
	} else {
		roomIdError.style.visibility = "hidden";
	}

	return returnValue;
}

function validateNumber(event) {
	if (!/^[\d.]$/.test(event.key) && !['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(event.key)) {
		event.preventDefault();
	}
};

