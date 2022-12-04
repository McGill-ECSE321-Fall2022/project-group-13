<template>
    <div class="mySchedule">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">MY SCHEDULE</span>
        <table class="styled-table">
        <thead>
          <tr>
            <th>Day</th>
            <th>Start Time</th>
            <th>End Time</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in shifts" :key="s.shiftId">
                <td>{{s.specificWeekDay.dayType}}</td>
                <td>{{s.startTime}}</td>
                <td>{{s.endTime}}</td>
            </tr>
        </tbody>
    </table>   
    </div>
</template>

<script>
import axios from 'axios';

var config = require('../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosEmployee = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'MySchedule',
  data(){
    return {
      employeeUsername: '',
      shifts: [], 
      mondayShifts: '',
    }
  }, 
  created(){
    this.employeeUsername = sessionStorage.getItem('loggedInEmployee');
    console.log(this.employeeUsername);
    axiosEmployee.get('/shift/employee/', {
      params: {
        employeeUsername: this.employeeUsername
      }
    })
    .then(response => {
            console.log(response)
            this.shifts = response.data
        })
    .catch(e => {
          console.log('Error in GET /shift/employee')
          console.log(e)
        })
  }
}

</script>

<style scoped>
.mySchedule {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.styled-table {
    border-collapse: collapse;
    margin: 200px 60px;
    font-size: 25px;
    font-family: sans-serif;
    min-width: 90%;
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

</style>