<template>
    <div class="editSchedule">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">EDIT SCHEDULE</span>

        <table class="styled-table">
        <thead>
          <tr>
            <th>Day</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th></th>
            <th><button class="styled-button addButton" @click="addNewShift()">Add new shift</button></th>
          </tr>
        </thead>
        <tbody>
            <tr v-for="s in shifts" :class="{editing: s == editedShift}" v-cloak>
                <td>
                  <div class="view">
                    {{s.specificWeekDay.dayType}}
                  </div>
                  <div class="edit">
                    <input type="text" v-model="day"/>
                  </div>
                </td>

                <td>
                  <div class="view">
                    {{s.startTime}}
                  </div>
                  <div class="edit">
                    <input type="text" v-model="startTime"/>
                  </div>
                </td>

                <td>
                  <div class="view">
                    {{s.endTime}}
                  </div>
                  <div class="edit">
                    <input type="text" v-model="endTime"/>
                  </div>
                </td>

                <td>
                  <div class="view">
                    <button class="styled-button" @click="editData(s)">Edit</button>
                  </div>
                  <div class="edit">
                    <button class="styled-button" @click="saveData(s)">Save</button>
                  </div>
                </td>
                <td><button class="styled-button deleteButton" @click="deleteShift(s.shiftId)">Delete</button></td>
            </tr>
        </tbody>
    </table>
        <div class = "employeeDropdown">
        <section>
        <select v-model="selectedEmployee" @change = "onChange($event)" class ="form-select form-control">
          <option v-for="e in employees" :value="e.username">{{e.username}} </option>
        </select>
        </section>
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
  name: 'EditSchedule',
  data(){
    return {
        employees: [],
        shifts: [],
        selectedEmployee: '', 

        editMode: false,
        editedShift: null,
        day: '',
        startTime: '',
        endTime: ''
    }
  }, 
  created(){
    axiosManager.get('/employees')
    .then(response => {
        console.log(response)
        this.employees = response.data
    })
    .catch(e => {
        console.log('Error in GET /employees')
        console.log(e)
    })
  },
  methods: {
    onChange: function(){
        axiosManager.get('/shift/employee', {
            params: {
                employeeUsername: this.selectedEmployee
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
    }, 
    deleteShift: function(shiftId){
        const self = this;
        var fetchDateUrl = ({shiftId}) => `/deleteShift/${shiftId}`;
        axiosManager.delete(fetchDateUrl({shiftId: shiftId}))
        .then(response => {
            console.log(response)
        })
        .catch(e => {
            console.log('Error in DELETE /deleteShift' + shiftId)
            console.log(e)
        })
        //reload page to show updated table
        window.location.reload();
    }, 
    saveData: function(s){
        let shiftId = s.shiftId;
        const self = this;
        var fetchDateUrl = ({shiftId}) => `/shift/update/${shiftId}`;
        axiosManager.put(fetchDateUrl({shiftId: s.shiftId}), {
            params: {
              startTime: this.startTime,
              endTime: this.endTime,
              day: this.day,
            }
        })
        .then(response => {
            console.log(response)
        })
        .catch(e => {
            console.log('Error in PUT /shift/update/' + shiftId)
            console.log(e)
        })
        //reload page to show updated table
        window.location.reload();
    },
    editData (s) {
      this.beforEditCache = s
      this.editedUser = s
    }
  }

}

</script>

<style scoped>
.editSchedule {
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

.employeeDropdown {
  width: 20%;
  position:relative; 
  margin:auto; 
  bottom: 20px;
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

.addButton {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}

.deleteButton {
  background: #000000;
  border: solid;
  border-radius: 12px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  transition: 0.2s;
}

[v-cloak] {
      display: none;
    }
    .edit {
      display: none;
    }
    .editing .edit {
      display: block
    }
    .editing .view {
      display: none;
    }

</style>