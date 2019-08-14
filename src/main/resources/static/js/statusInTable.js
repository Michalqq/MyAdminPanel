function check(){
    checkDate();
    checkStatus();
}
function checkDate(){
    mytab = document.getElementById("mainTable");
    var oneDay = 24*60*60*1000;
    var today = new Date();
    for (let row of mytab.rows)
    {
        var date = row.cells.item(6).innerHTML.split('-');
        var mydate = new Date(date[0], date[1] - 1, date[2]);
        var diffDays = Math.round(Math.abs((mydate.getTime() - today.getTime())/(oneDay)));
        if (diffDays > 6 && row.cells.item(10).innerHTML == 2) {
            row.style.backgroundColor = "#FFF0CA";
        }
    }
}
function checkStatus(){
    mytab = document.getElementById("mainTable");
    for (let row of mytab.rows)
    {
        if (row.cells.item(10).innerHTML == "") row.cells.item(10).innerHTML = "W transp"
        if (row.cells.item(10).innerHTML == 1) row.cells.item(10).innerHTML = "W domu"
        if (row.cells.item(10).innerHTML == 2){
         row.cells.item(10).innerHTML = "Pobranie"
         row.cells.item(11).style.visibility = "visible";
        }
        if (row.cells.item(10).innerHTML == 3) row.cells.item(10).innerHTML = "Sprzedano"
    }
}