<template>
  <div id="employee">
    <router-view></router-view>
    <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <div class="employeeHome">
        <a href="/employee/myaccount">Hi, Employee!</a>
    </div>
    <div class="topnav">
        <a href="/employee/managetickets">Manage Tickets</a> 
        <a href="/employee/myschedule">My Schedule</a>
        <a href="/employee/manageloanrequests">Manage Loan Requests</a>
        <a href="/employee/managedonationrequests">Manage Donation Requests</a>
        <a href="/employee/managecatalog">Manage Catalog</a>
    </div>
    <form action="/employee/myhomepage">
    <button class="bookmark">
        <img src="../assets/dinoLogo.png" alt="Home" style="width:70px;height:120;position: absolute;left: 0;bottom: 0;">
    </button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'employee'
}
</script>

<style>
.employeeHome {
  overflow: hidden;
  position: absolute;
  right: 0;
  top: 0;
  padding-top: 15px;
  padding-bottom: 0px;
}

.employeeHome a {
  color: #008573;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.employeeHome a:hover {
  text-decoration: underline;
}

.topnav {
  overflow: hidden;
  position: absolute;
  top: 50px;
  right: 0;
  border-bottom: solid;
  border-bottom-color:#008573;
  padding-top: 0px;
  padding-bottom: 15px;
}

.topnav a {
  color: #008573;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.bookmark{
  width: 73px;
  height: 129px;
  border: none;
  background: #D6D2D2;
  opacity: 1;
  position: absolute;
  top: 0px;
  left: 70px;
  overflow: hidden;
}

.title {
  color: #008573;
  position: absolute;
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  top: 50px;
  left: 167px;
}

.styledButton {
  background: #008573;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  transition: 0.2s;
}

.styledButton:hover {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}
</style>