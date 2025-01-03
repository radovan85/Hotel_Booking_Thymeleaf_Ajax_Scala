var current_page = 1;
var records_per_page = document.getElementById("recordsPerPage").value;
var l = document.getElementById("listingTable").rows.length;

function prevPage() {
	if (current_page > 1) {
		current_page--;
		changePage(current_page);
	}
}

function nextPage() {
	if (current_page < numPages()) {
		current_page++;
		changePage(current_page);
	}
}

function changePage(page) {
	var btn_next = document.getElementById("btn_next");
	var btn_prev = document.getElementById("btn_prev");
	var listing_table = document.getElementById("listingTable");
	var page_span = document.getElementById("page");

	// Validate page
	if (page < 1)
		page = 1;
	if (page > numPages())
		page = numPages();

	for (var i = 0; i < l; i++) {
		listing_table.rows[i].style.display = "none";
	}

	listing_table.rows[0].style.display = "table-row"; // displaying table
	// header

	for (var x = (page - 1) * records_per_page; x < (page * records_per_page); x++) {

		if (x === l - 1) {
			break;
		}
		listing_table.rows[x + 1].style.display = "table-row";
	}
	;

	page_span.innerHTML = page + "/" + numPages();

	if (page == 1) {
		btn_prev.style.visibility = "hidden";
	} else {
		btn_prev.style.visibility = "visible";
	}

	if (page == numPages()) {
		btn_next.style.visibility = "hidden";
	} else {
		btn_next.style.visibility = "visible";
	}
}

function numPages() {
	var returnValue;
	var arraySize = l - 1;

	if ((arraySize % records_per_page) === 0) {
		returnValue = arraySize / records_per_page;
	} else {
		returnValue = Math.ceil(arraySize / records_per_page);
	}

	return returnValue;
}

changePage(1);