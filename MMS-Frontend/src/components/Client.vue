<template>
  <div id="client">
    <router-view></router-view>
    <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <div class="clientHome">
        <a href="/client/myaccount">Hi, {{clientName}}!</a>
    </div>
    <div class="topnav">
        <a href="/client/browsecatalog">Browse Catalog</a>
        <a href="/client/myloanrequests">My Loans</a>
        <a href="/client/mydonationrequests">My Donations</a>
        <a href="/client/mytickets">My Tickets</a>
    </div>
    <button class="bookmark">
      <a href="/client/myhomepage">
        <img src="../assets/dinoLogo.png" alt="Home" style="width:70px;height:120;position: absolute;left: 0;bottom: 0;">
      </a>
    </button>
  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosClient = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'Client',
  data() {
      return {
        clientName: '',
      }
    },
  created() {
        let username = sessionStorage.getItem('loggedInClient');
        const response = axiosClient.get('client/' + username);
        this.clientName = username;
    }
  }
</script>

<style>
.clientHome {
  overflow: hidden;
  position: absolute;
  right: 0;
  top: 0;
  padding-top: 15px;
  padding-bottom: 0px;
}

.clientHome a {
  color: #008573;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
}

.clientHome a:hover {
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
