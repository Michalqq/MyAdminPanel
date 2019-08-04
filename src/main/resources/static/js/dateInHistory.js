function checkDate(){
  alert("test")
  if (document.getElementById("startDate").value == ""){
    var currentdate = new Date();
    document.getElementById("startDate").value = currentdate.getDate();
  }
  if (document.getElementById("stopDate").value == ""){
    var currentdate = new Date();
    document.getElementById("stopDate").value = currentdate.getDate() - 1;
  }
}