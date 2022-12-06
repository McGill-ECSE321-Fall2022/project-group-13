<!-- @author Samantha Perez Hoffman (samperezh) -->
<template>
    <div class="viewSchedules">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">EMPLOYEE SCHEDULE</span>

        <div class = "dayDropdown">
        <section>
        <select @change = "onChange($event)" class ="form-select form-control">
          <option value="PickDay">Choose Day</option>
          <option value="Monday">Monday</option>
          <option value="Tuesday">Tuesday</option>
          <option value="Wednesday">Wednesday</option>
          <option value="Thursday">Thursday</option>
          <option value="Friday">Friday</option>
          <option value="Saturday">Saturday</option>
          <option value="Sunday">Sunday</option>
        </select>
        </section>
        </div>

        <div class="table-container">
        <table class="styled-table">
        <thead>
          <tr>
            <th>Employee Username</th>
            <th> Name </th>
            <th>{{selectedDay}}</th>
          </tr>
        </thead>
        <tbody>
            <tr v-for="s in shifts" :key="s.shiftId">
                <td>{{s.employee.username}}</td>
                <td>{{s.employee.name}}</td>
                <td>{{s.startTime}} - {{s.endTime}}</td>
            </tr>
        </tbody>
        </table>
        </div>


    </div>
</template>

<script>
import axios from 'axios';
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosManager = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'ViewSchedules',
    data(){
        return {
            shifts: [],
            selectedDay: 'PickDay',
        }
    }, 
    created(){
        
    },
    methods: {
        //Once selected day is changed, get the shifts for that day from the backend 
        onChange(e){
          this.selectedDay = e.target.value;
          if(this.selectedDay == "PickDay"){
            window.location.reload();
          }
          axiosManager.get('/shift/day', {
            params: {
                day: this.selectedDay
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
}
</script>

<style scoped>
.viewSchedules {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.dayDropdown {
  position: absolute;
  top: 6%;
  right: 15px;
}
</style>
