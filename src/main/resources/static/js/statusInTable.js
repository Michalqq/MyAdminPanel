function checkStatus(){
    mytab = document.getElementById("mainTable");
    for (let row of mytab.rows)
    {
        if (row.cells.item(9).innerHTML == "") row.cells.item(9).innerHTML = "W transporcie"
        if (row.cells.item(9).innerHTML == 1) row.cells.item(9).innerHTML = "W domu"
        if (row.cells.item(9).innerHTML == 2) row.cells.item(9).innerHTML = "Pobranie"
        if (row.cells.item(9).innerHTML == 3) row.cells.item(9).innerHTML = "Sprzedano"
    }
}