function checkStatus(){
    mytab = document.getElementById("mainTable");
    for (let row of mytab.rows)
    {
        if (row.cells.item(10).innerHTML == "") row.cells.item(10).innerHTML = "W transporcie"
        if (row.cells.item(10).innerHTML == 1) row.cells.item(10).innerHTML = "W domu"
        if (row.cells.item(10).innerHTML == 2){
         row.cells.item(10).innerHTML = "Pobranie"
         row.cells.item(11).style.display = "block";
        }
        if (row.cells.item(10).innerHTML == 3) row.cells.item(10).innerHTML = "Sprzedano"
    }
}