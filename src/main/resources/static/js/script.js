window.onload = redirectHome;

function redirectHome() {
	redirectUrlPath("/home");
}

function redirectContact() {
	redirectUrlPath("/contactInfo");
}

function redirectLogin() {
	redirectUrlPath("/login");
}

function redirectRegister() {
	redirectUrlPath("/register");
}

function redirectAllGuests() {
	redirectUrlPathWithScript("/admin/allGuests");
}

function redirectGuestDetails(guestId) {
	redirectUrlPath("/admin/guestDetails/" + guestId);
}

function redirectAllCategories() {
	redirectUrlPathWithScript("/admin/allRoomCategories");
}

function redirectAddCategory() {
	redirectUrlPath("/admin/createRoomCategory");
}

function redirectCategoryDetails(categoryId) {
	redirectUrlPath("/admin/categoryDetails/" + categoryId);
}

function redirectUpdateCategory(categoryId) {
	redirectUrlPath("/admin/updateRoomCategory/" + categoryId);
}

function redirectAllRooms() {
	redirectUrlPathWithScript("/admin/allRooms");
}

function redirectAddRoom() {
	redirectUrlPath("/admin/createRoom");
}

function redirectRoomDetails(roomId) {
	redirectUrlPath("/admin/roomDetails/" + roomId);
}

function redirectUpdateRoom(roomId) {
	redirectUrlPath("/admin/updateRoom/" + roomId);
}

function redirectUserReservations() {
	redirectUrlPathWithScript("/guests/allUserReservations");
}

function redirectAllReservations() {
	redirectUrlPathWithScript("/admin/allReservations");
}

function redirectAllActiveReservations() {
	redirectUrlPathWithScript("/admin/allActiveReservations");
}

function redirectAllExpiredReservations() {
	redirectUrlPathWithScript("/admin/allExpiredReservations");
}

function redirectReservationDetails(reservationId) {
	redirectUrlPath("/admin/reservationDetails/" + reservationId);
}

function redirectBookReservation() {
	redirectUrlPath("/guests/bookReservation");
}

function redirectAllNotes() {
	redirectUrlPathWithScript("/admin/allNotes");
}

function redirectAllNotesFromToday() {
	redirectUrlPathWithScript("/admin/allNotesToday");
}

function redirectNoteDetails(noteId) {
	redirectUrlPath("/admin/noteDetails/" + noteId);
}

function redirectSwitchRoom(reservationId) {
	redirectUrlPath(
		"/admin/switchReservationRoom/" + reservationId);
}

function redirectAccountDetails() {
	redirectUrlPath("/guests/accountDetails");
}

function redirectRoomError() {
	redirectUrlPath(`/admin/roomNumberError`);
}

function redirectRegistrationCompleted() {
	redirectUrlPath(`/registerComplete`);
}

function redirectRegistrationError() {
	redirectUrlPath(`/registerFail`);
}


function confirmLoginPass() {
	axios.post(`http://localhost:8080/loginPassConfirm`)
		.then(() => {
			window.location.href = `/`;
		})

		.catch(() => {
			redirectUrlPath(`/loginErrorPage`);
		})
};

function login() {
	var formData = new FormData(document.getElementById("loginForm"));
	var serializedFormData = new URLSearchParams(formData).toString();

	document.cookie = "JSESSIONID=" + Math.random() + "; SameSite=None; Secure";

	axios.post('http://localhost:8080/login', serializedFormData, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		withCredentials: true
	})
		.then(() => {
			confirmLoginPass();
		})
		.catch(() => {
			alert("Failed!");
		});
}


function redirectLogout() {
	axios.post(`http://localhost:8080/loggedout`)
		.then(() => {
			window.location.href = "/";
		})

		.catch(() => {
			alert("Logout error!");
		})
}

function addRoomCategory() {
	var formData = new FormData(document.getElementById(`roomCategoryForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	if (validateRoomCategory()) {
		axios.post(`http://localhost:8080/admin/createRoomCategory`, serializedFormData)
			.then(() => {
				redirectAllCategories();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function addRoom() {
    if (validateRoom()) {
        var formData = new FormData(document.getElementById(`roomForm`));
        var serializedFormData = new URLSearchParams(formData).toString();
        axios.post(`http://localhost:8080/admin/createRoom`, serializedFormData)
            .then(() => {
                redirectAllRooms();
            })
            .catch((error) => {
                if (error.response.status === 409) {
                    redirectRoomError();
                } else if (error.response.status === 422) {
                    alert(error.response.data);
                } else {
                    alert(`Failed!`);
                }
            });
    }
}


function registerUser(){
	if(validateRegForm()) {
		var formData = new FormData(document.getElementById(`registrationForm`));
		var serializedFormData = new URLSearchParams(formData).toString();
		axios.post(`http://localhost:8080/register`, serializedFormData)
			.then(() => {
				redirectRegistrationCompleted()
			})
			
			.catch((error) => {
				if(error.response.status === 409){
					redirectRegistrationError();
				}else {
					alert(`Failed!`);
				}
			})
	} 
};

function bookReservation() {
	if(validateReservation()) {
		var checkInDate = document.getElementById("checkInDateStr").value;
		var numberOfNights = document.getElementById("numberOfNights").value;
		redirectUrlPath(`/guests/createReservation/${checkInDate}/${numberOfNights}`);
	}
};

function addReservation(roomId){
	var formData = new FormData(document.getElementById(`reservationForm`));
	var serializedFormData = new URLSearchParams(formData).toString();
	axios.post(`http://localhost:8080/guests/createReservation/${roomId}`, serializedFormData)
		.then(() => {
			redirectUserReservations();
		})
		
		.catch(() => {
			alert(`Failed!`);
		})
};


function deleteCategory(categoryId) {
	if (confirm("Are you sure you want to clear this category?\nThis will affect all rooms and reservations related!")) {
		axios.get(`http://localhost:8080/admin/deleteRoomCategory/${categoryId}`)
			.then(() => {
				redirectAllCategories();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};

function deleteRoom(roomId) {
	if (confirm("Are you sure you want to clear this room?\nThis will affect all reservations related!")) {
		axios.get(`http://localhost:8080/admin/deleteRoom/${roomId}`)
			.then(() => {
				redirectAllRooms();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};


function deleteReservation(reservationId) {
	if (confirm("Are you sure you want to cancel this reservation?")) {
		axios.get(`http://localhost:8080/guests/deleteReservation/${reservationId}`)
			.then(() => {
				redirectUserReservations();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};

function deleteReservationAdmin(reservationId) {
	if (confirm("Are you sure you want to remove this reservation?")) {
		axios.get(`http://localhost:8080/admin/deleteReservation/${reservationId}`)
			.then(() => {
				redirectAllReservations();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};

function deleteNote(noteId) {
	if (confirm("Are you sure you want to remove this note?")) {
		axios.get(`http://localhost:8080/admin/deleteNote/${noteId}`)
			.then(() => {
				redirectAllNotes();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};


function deleteAllNotes() {
	if (confirm("Are you sure you want to clear all notes?")) {
		axios.get(`http://localhost:8080/admin/deleteAllNotes`)
			.then(() => {
				redirectAllNotes();
			})

			.catch(() => {
				alert("Failed!");
			})
	}
};

function deleteGuest(guestId) {
	if (confirm("Are you sure you want to remove this guest?\nIt will affect all related reservations!")) {
		axios.get(`http://localhost:8080/admin/deleteGuest/${guestId}`)
			.then(() => {
				redirectAllGuests();
			})

			.catch(() => {
				alert(`Failed!`);
			})
	}
};

function updateReservation(){
	var reservationId = document.getElementById(`reservationId`).value;
	var formData = new FormData(document.getElementById("reservationForm"));
	var serializedFormData = new URLSearchParams(formData).toString();
	if(validateRoomId()){
		axios.post(`http://localhost:8080/admin/updateReservation/${reservationId}`,serializedFormData)
			.then(() => {
				redirectAllReservations();
			})
			
			.catch(() => {
				alert(`Failed!`);
			})
	}
}

function redirectUrlPath(path) {
	axios.get(path)
		.then(response => {
			document.getElementById(`axiosLoadedContent`).innerHTML = response.data;
		})
		.catch(error => {
			console.error(`Error loading home page:`, error);
		});
};

function redirectUrlPathWithScript(path) {
	axios.get(path)
		.then(response => {
			var contentDiv = document.getElementById(`axiosLoadedContent`);
			contentDiv.innerHTML = response.data;

			var scripts = contentDiv.querySelectorAll(`script`);
			scripts.forEach((oldScript) => {
				var newScript = document.createElement(`script`);
				newScript.type = `text/javascript`;
				if (oldScript.src) {
					newScript.src = oldScript.src;
				} else {
					newScript.textContent = oldScript.textContent;
				}
				document.body.appendChild(newScript);
				oldScript.parentNode.removeChild(oldScript);
			});
		})
		.catch(error => {
			console.error(`Error loading login page:`, error);
		});
};




