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
            <button class="styled-button">EDIT DAY STATUS</button>
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
            console.log('Error in PUT /shift/update/')
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

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}
/deep/ .modal-content {
  background: #008573;
}

</style>