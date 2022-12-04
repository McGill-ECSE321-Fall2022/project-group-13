<!-- @author Samantha Perez Hoffman (samperezh)-->

<template>
    <div class="managerHomepage">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>

        <div class="welcome">
        <p class="title">WELCOME TO THE CABINET OF CURIOSITIES </p>
        <p class="subtitle"> Fulfill your curiosity with us</p>
        </div>

        <div class="button-container">
            <button class="styled-button">EDIT TICKET FEE</button>
            <button class="styled-button" v-b-modal.update-day-modal>EDIT DAY STATUS</button>
            <button class="styled-button" v-b-modal.update-hours-modal>EDIT OPENING HOURS</button>
        </div>

        <b-modal modal-class="popup" id="update-hours-modal" centered title="OPENING HOURS" ok-title="Save" ok-variant="light" cancel-variant="dark"
      @show="resetUpdateHoursModal"
      @hidden="resetUpdateHoursModal"
      @ok="handleOkUpdateHoursModal"
        >
      <form ref="updateHoursForm" @submit.stop.prevent="handleSubmitOpeningHours">
        <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>

        <b-form-group
          label="Open Time"
          invalid-feedback="Open time is required"
        >
          <b-form-input
            :placeholder="this.oldOpenTime"
            v-model="newOpenTime"
            :state="newOpenTime.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="Close Time"
          invalid-feedback="Close time is required"
        >
          <b-form-input
            :placeholder="this.oldCloseTime"
            v-model="newCloseTime"
            :state="newCloseTime.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>
      </form>
    </b-modal>


    <b-modal modal-class="popup" id="update-day-modal" centered title="DAY STATUS" ok-title="Save" ok-variant="light" cancel-variant="dark"
      @show="resetUpdateDayModal"
      @hidden="resetUpdateDayModal"
      @ok="handleOkUpdateDayModal"
        >
      <form ref="updateDayForm" @submit.stop.prevent="handleSubmitDayStatus">
        <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}
            <b-button class="styled-button delete-shifts" variant="light" @click="deleteShifts()">Delete Shifts on {{this.errorDay}}</b-button>
        </b-alert>

        <b-form-group
          label="Day"
          invalid-feedback="Day is required"
        >
        <select @change = "onChangeDay($event)" class ="form-select form-control">
          <option value="PickDay">Choose Day</option>
          <option value="Monday">Monday</option>
          <option value="Tuesday">Tuesday</option>
          <option value="Wednesday">Wednesday</option>
          <option value="Thursday">Thursday</option>
          <option value="Friday">Friday</option>
          <option value="Saturday">Saturday</option>
          <option value="Sunday">Sunday</option>
        </select>
        </b-form-group>

        <b-form-group
          label="Status"
          invalid-feedback="Status is required"
        >
        <select @change = "onChangeStatus($event)" class ="form-select form-control">
          <option value="PickStatus">Choose Status</option>
          <option value="true">Close</option>
          <option value="false">Open</option>
        </select>
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
  name: 'ManagerHomepage',
    data(){
        return {
            newOpenHours: [],
            newOpenTime: '',
            newCloseTime: '',
            oldOpenTime: '',
            oldCloseTime: '',

            updatedSpecificDay: [],
            selectedDay: 'PickDay',
            selectedStatus: 'PickStatus',
            errorDay: '',

            showErrorAlert: false,
            errorData: ''
        }
    }, 
    created(){
    },
    methods: {
    updateHours: function() {
        const self= this;
        return new Promise(function (resolve, reject) {
        axiosManager.put('/mms/setOpeningHours/', {},{
            params: {
                openTime: self.newOpenTime,
                closeTime: self.newCloseTime
            }
        })        
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in PUT /mms/setOpeningHours/')
            console.log(e)
            if(e.response.data == "Opening time cannot be after closing time."){
                self.errorData = e.response.data
                self.showErrorAlert = true
            }else if (e.response.status == 500 || e.response.status == 400) {
                self.errorData = "Wrong Input Format"
                self.showErrorAlert = true
            }
            reject(e)
        })
      })
    },
    resetUpdateHoursModal: function () {
            this.newOpenHours = []
            this.newOpenTime = ''
            this.newCloseTime = ''
            this.showErrorAlert = false
        },
    checkUpdateHoursFormValidity() {
            return this.newOpenTime.trim().length > 0 && 
            this.newCloseTime.trim().length > 0 
        },
    handleSubmitOpeningHours: async function () {
            if (!this.checkUpdateHoursFormValidity()) {
                return
            }
            this.showErrorAlert = false
            this.newOpenHours = await this.updateHours()
            this.$nextTick(() => {
                this.$bvModal.hide("update-hours-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkUpdateHoursModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitOpeningHours()
        },
    updateDayStatus: function() {
        const self= this;
        return new Promise(function (resolve, reject) {
            axiosManager.put('/specificWeekDay', {},{
                params: {
                    day: self.selectedDay,
                    isClosed: self.selectedStatus
                }
            })        
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in PUT /specificWeekDay/')
            console.log(e)
            if(e.response.data == "Cannot close day with that has scheduled shifts."){
                self.errorData = "Cannot close day that has scheduled shifts."
                self.errorDay = self.selectedDay
                self.showErrorAlert = true
            }
            reject(e)
        })

        })
    },
    onChangeDay(e){
          this.selectedDay = e.target.value;
    },
    onChangeStatus(e){
          this.selectedStatus = e.target.value;
    },
    resetUpdateDayModal: function () {
            this.updatedSpecificDay = []
            this.selectedDay = 'PickDay'
            this.selectedStatus = 'PickStatus'
            this.showErrorAlert = false
            this.errorDay = ''
        },
    checkUpdateDayFormValidity() {
            return this.selectedDay != "PickDay" && 
           this. selectedStatus != "PickStatus"
        },
    handleSubmitDayStatus: async function () {
            if (!this.checkUpdateDayFormValidity()) {
                return
            }
            this.showErrorAlert = false
            this.updatedSpecificDay = await this.updateDayStatus()
            this.$nextTick(() => {
                this.$bvModal.hide("update-day-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkUpdateDayModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitDayStatus()
        },
    deleteShifts: function(){
        axiosManager.delete('/shift/day', {
            params: {
                dayType: this.errorDay
            }
        })
        .then(response => {
            console.log(response)
        })
        .catch(e => {
            console.log('Error in DELETE /shift/day')
            console.log(e)
        })
        this.showErrorAlert = false
    }
    }
}
</script>

<style scoped>
.managerHomepage {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.title {
  color: #008573;
  position: relative;
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  top: 0%;
  left: 0%;
}

.subtitle {
  top: 15%;
  color: #008573;
  position: relative;
  font-family: Inter;
  font-weight: 450;
  font-size: 30px;
  opacity: 1;
}

.welcome {
  position: relative;
  top: 10%;
  width: 50%;
  height: 20%;
  margin-left: 25%;
  margin-right: 25%;
  background: rgba(255,255,255,1);
  opacity: 1;
}

.button-container {
  position: relative;
  top: 15%;
  margin-left: 37.5%;
  margin-right: 37.5%;
  width: 25%;
  display: block;  /*make the buttons appear below each other*/
}

.styled-button{
    width:100%;
    margin-bottom: 10%;

}

.delete-shifts {
    padding:0px;
    width:50%;
    margin-left: 25%;
    margin-top:5%;
    margin-bottom: 0px;
}

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}
/deep/ .modal-content {
  background: #008573;
}

</style>