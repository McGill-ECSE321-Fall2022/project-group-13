<!-- @author Samantha Perez Hoffman (samperezh) (+ inspo for pop ups Yu An Lu) -->

<template>
    <div class="editSchedule">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">EDIT SCHEDULE</span>

        <div class="table-container">
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
                <td><button class="styled-button" v-b-modal.update-shift-modal @click="sendInfo(s)">Edit</button> </td>
                <td><button class="styled-button deleteButton" @click="deleteShift(s.shiftId)">Delete</button></td>
            </tr>
        </tbody>
    </table>
  </div>
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

    <b-modal modal-class="popup" id="update-shift-modal" centered title="UPDATE SHIFT" ok-title="Save" ok-variant="light" cancel-variant="dark"
      @show="resetUpdateShiftModal"
      @hidden="resetUpdateShiftModal"
      @ok="handleOkUpdateShiftModal"
    >
      <form ref="updateShiftForm" @submit.stop.prevent="handleSubmitUpdateShift">
        <b-form-group
          label="Day"
          invalid-feedback="Day of the week is required"
        >
          <b-form-input
            type="text"
            :placeholder="this.oldShiftDay"
            v-model="updatedShiftDay"
            :state="updatedShiftDay.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="Start Time"
          invalid-feedback="Start time is required"
        >
          <b-form-textarea
            :placeholder="this.oldShiftStartTime"
            v-model="updatedShiftStartTime"
            :state="updatedShiftStartTime.trim().length > 0 ? true : false"
            required
          ></b-form-textarea>
        </b-form-group>

        <b-form-group
          label="End Time"
          invalid-feedback="End time is required"
        >
          <b-form-textarea
            :placeholder="this.oldShiftEndTime"
            v-model="updatedShiftEndTime"
            :state="updatedShiftEndTime.trim().length > 0 ? true : false"
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

        oldShiftDay: '',
        oldShiftStartTime: '',
        oldShiftEndTime: '',

        updatedShift: {},
        updatedShiftId: '',
        updatedShiftDay: '',
        updatedShiftStartTime: '',
        updatedShiftEndTime: ''
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
    updateShift : function() {
      const self = this
      let shiftId = self.updatedShiftId
      var fetchDateUrl = ({shiftId}) => `/shift/update/${shiftId}`
      return new Promise(function (resolve, reject) {
        axiosManager.put(fetchDateUrl({shiftId: shiftId}), {}, {
            params: {
              startTime: self.updatedShiftStartTime,
              endTime: self.updatedShiftEndTime,
              day: self.updatedShiftDay
            }
        })
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in PUT /shift/update/')
            console.log(e)
            console.log(self.updatedShiftId)
            console.log() //, self.startTime, self.endTime, self.day)
            reject(e)
        })
      })
    }, 
    sendInfo(s){
        this.updatedShiftId = s.shiftId
        this.oldShiftDay = s.specificWeekDay.dayType
        this.oldShiftStartTime = s.startTime
        this.oldShiftEndTime = s.endTime
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
            console.log('Error in POST /createShift')
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
        },
    
    resetUpdateShiftModal: function () {
            this.updateShiftId = ''
            this.updatedShift = {}
            this.updatedShiftDay = ''
            this.updatedShiftStartTime = ''
            this.updatedShiftEndTime = ''
        },
    checkUpdatedShiftFormValidity() {
            return this.updatedShiftDay.trim().length > 0 && 
            this.updatedShiftStartTime.trim().length > 0 && 
            this.updatedShiftEndTime.trim().length > 0
        },
    handleSubmitUpdateShift: async function () {
            if (!this.checkUpdatedShiftFormValidity()) {
                return
            }
            this.updatedShift = await this.updateShift() //need to pass here the shiftId
            this.$nextTick(() => {
                this.$bvModal.hide("update-shift-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkUpdateShiftModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitUpdateShift()
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

.table-container {
  width: 100%;
  position: relative;
  overflow: hidden;
}

.employeeDropdown {
  width: 20%;
  position:relative; 
  top: 50px;
  margin:auto; 
}

.deleteButton:hover {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}

.addButton:hover {
  background: #000000;
  border: solid;
  border-color: #008573;
  color: white;
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