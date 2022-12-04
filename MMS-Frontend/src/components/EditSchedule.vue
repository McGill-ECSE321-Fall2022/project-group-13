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
            <th><button class="styled-button addButton" v-b-modal.new-shift-modal>Add new shift</button></th>
          </tr>
        </thead>
        <tbody>
            <tr v-for="s in shifts" :key="s.shiftId">
                <td>{{s.specificWeekDay.dayType}}</td>
                <td>{{s.startTime}}</td>
                <td>{{s.endTime}}</td>
                <td><button class="styled-button" @click="editShift(s.shiftId)">Edit</button> </td>
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


    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="new-shift-modal" centered title="NEW SHIFT" ok-title="Save" ok-variant="light" cancel-variant="dark"
      @show="resetNewShiftModal"
      @hidden="resetNewShiftModal"
      @ok="handleOkNewShiftModal"
    >
      <form ref="newShiftForm" @submit.stop.prevent="handleSubmitNewShift">
        <b-form-group
          label="Day"
          invalid-feedback="Day of the week is required"
        >
          <b-form-input
            type="text"
            placeholder="Monday"
            v-model="newShiftDay"
            :state="newShiftDay.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="Start Time"
          invalid-feedback="Start time is required"
        >
          <b-form-textarea
            placeholder="9:00:00"
            v-model="newShiftStartTime"
            :state="newShiftStartTime.trim().length > 0 ? true : false"
            required
          ></b-form-textarea>
        </b-form-group>

        <b-form-group
          label="End Time"
          invalid-feedback="End time is required"
        >
          <b-form-textarea
            placeholder="17:00:00"
            v-model="newShiftEndTime"
            :state="newShiftEndTime.trim().length > 0 ? true : false"
            required
          ></b-form-textarea>
        </b-form-group>
      </form>
    </b-modal>

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
        selectedEmployee: sessionStorage.getItem('selectedEmployeeSchedule'), 

        newShift: {},
        newShiftDay: '',
        newShiftStartTime: '',
        newShiftEndTime: '',

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
    if(this.selectedEmployee != ''){
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
    }
  },
  methods: {
    onChange: function(){
        sessionStorage.setItem('selectedEmployeeSchedule', this.selectedEmployee)
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
    editShift() {

    }, 
    addNewShift : function() {
      const self = this
      return new Promise(function (resolve, reject) {
        axiosManager.post('/createShift',{}, {
            params: {
              employeeUsername: self.selectedEmployee,
              day: self.newShiftDay,
              startTime: self.newShiftStartTime,
              endTime: self.newShiftEndTime
            }
        })
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in POST /shift/create')
            console.log(e)
            reject(e)
        })
      })
    },
    // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
    resetNewShiftModal: function () {
            this.newShift = {}
            this.newShiftDay = ''
            this.newShiftStartTime = ''
            this.newShiftEndTime = ''
        },
    checkNewShiftFormValidity() {
            return this.newShiftDay.trim().length > 0 && 
            this.newShiftStartTime.trim().length > 0 && 
            this.newShiftEndTime.trim().length > 0
        },
    handleSubmitNewShift: async function () {
            if (!this.checkNewShiftFormValidity()) {
                return
            }
            this.newShift = await this.addNewShift()
            this.$nextTick(() => {
                this.$bvModal.hide("new-shift-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkNewShiftModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitNewShift()
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

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}
/deep/ .modal-content {
  background: #008573;
}

</style>