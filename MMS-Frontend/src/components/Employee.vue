<template>
  <div id="employee">
    <router-view></router-view>
    <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <div class="employeeHome">
        <a href="/employee/myaccount">Hi, {{employeeName}}!</a>
    </div>
    <div class="topnav">
        <a href="/employee/managecatalog">Manage Catalog</a>
        <a href="/employee/myschedule">My Schedule</a>
        <a href="/employee/manageloanrequests">Manage Loan Requests</a>
        <a href="/employee/managedonationrequests">Manage Donation Requests</a>
        <a href="/employee/managetickets">Manage Tickets</a> 
    </div>
    <button class="bookmark">
        <a href="/employee/myhomepage">
          <img src="../assets/dinoLogo.png" alt="Home" style="width:70px;height:120;position: absolute;left: 0;bottom: 0;">
        </a>
    </button>
    <footer class="text-white text-center text-lg-start" style="background-color: #01695a;">
    <!-- Grid container -->
    <div class="container p-4">
      <!--Grid row-->
      <div class="row mt-4">
        <!--Grid column-->
        <div class="col-lg-4 col-md-12 mb-4 mb-md-0">
          <h5 class="text-uppercase mb-4">About Cabinet of Curiosities</h5>

          <p>
            We are here to inspire and empower you to fulfill your curiosity through our exhibitions and events.
          </p>
        </div>
        <!--Grid column-->
        <div class="col-lg-4 col-md-6 mb-4 mb-md-0">
          <h5 class="text-uppercase mb-4 pb-1">Main Entrance</h5>
              <span class="ms-2">3630 Rue University,<br>Montréal, QC H3A 2B3<br><br></span>
              <span class="ms-2">contact@cabinetofcuriosities.com<br><br></span>
              <span class="ms-2">(514) 398-7878</span>
        </div>
        <!--Grid column-->

        <!--Grid column-->
        <div class="col-lg-4 col-md-6 mb-4 mb-md-0">
          <h5 class="text-uppercase mb-4">Opening hours</h5>

          <table class="table text-center text-white">
            <tbody class="fw-normal">
              <tr>
                <td>Monday:</td>
                <td v-if="monClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Tuesday:</td>
                <td v-if="tueClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Wednesday:</td>
                <td v-if="wedClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Thursday:</td>
                <td v-if="thuClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Friday:</td>
                <td v-if="friClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Saturday:</td>
                <td v-if="satClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
              <tr>
                <td>Sunday:</td>
                <td v-if="sunClosed == false">{{openTime}}-{{closeTime}}</td>
                <td v-else>CLOSED</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!--Grid column-->
      </div>
      <!--Grid row-->
    </div>

    <!-- Copyright -->
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2);">
      ©2022 Montreal Cabinet of Curiosities
    </div>
    <!-- Copyright -->
  </footer>
  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosEmployee = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'Employee',
  data() {
      return {
        employeeName: '',
        openTime: '',
        closeTime: '',
        monClosed: false,
        tueClosed: false,
        wedClosed: false,
        thuClosed: false,
        friClosed: false,
        satClosed: false,
        sunClosed: false,
      }
    },
  created() {
        let username = sessionStorage.getItem('loggedInEmployee');
        const response = axiosEmployee.get('employee/' + username);
        this.employeeName = username;

        //set opening and closing times
        axiosEmployee.get('/mms/getMms')
        .then(response => {
                this.openTime = response.data.openingTime;
                this.closeTime = response.data.closingTime;
        })
        .catch(error => {
            console.log(error);
        })
        //get monday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: 'Monday',
            }
        })
        .then(response => {
                console.log(response)
                this.monClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get tuesday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: 'Tuesday',
            }
        })
        .then(response => {
                console.log(response.isClosed)
                this.tueClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get wednesday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: "Wednesday",
            }
        })
        .then(response => {
                this.wedClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get thursday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: "Thursday",
            }
        })
        .then(response => {
                this.thuClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get friday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: "Friday",
            }
        })
        .then(response => {
                this.friClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get saturday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: "Saturday",
            }
        })
        .then(response => {
                this.satClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
        //get sunday closing status
        axiosEmployee.get('mms/DayByDayType', {
            params: {
                    day: "Sunday",
            }
        })
        .then(response => {
                this.sunClosed = response.data.isClosed;
        })
        .catch(error => {
            console.log(error);
        })
    }
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

.bookmark {
  width: 73px;
  height: 129px;
  border: none;
  background: #D6D2D2;
  opacity: 1;
  position: absolute;
  top: 0%;
  left: 5%;
  overflow: hidden;
}

.title {
  color: #008573;
  position: absolute;
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  top: 5%;
  left: 12%;
}

.styled-button {
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

.styled-button:hover {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}

.styled-table {
    border-collapse: collapse;
    text-align: center;
    margin-top: 10%;
    margin-left: auto; 
    margin-right: auto; 
    font-size: 18px;
    font-family: sans-serif;
    width: 80%;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.styled-table th,
.styled-table td {
    padding: 12px 15px;
}

.styled-table thead tr {
    background-color: #008573;
    color: #ffffff;
    text-align: middle;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #009879;
}
</style>