<template>
    <div class="myLoanRequests">
        <head>
              <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
              <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <div style="margin-top:7%;">
           <b-alert v-model="showAlert" variant="info">Client has reached maximum number of loans. Please check active requests.</b-alert>
        </div>
        <span class="title">ALL LOAN REQUESTS</span>
        <div class="statusTypeDropdown">
              <select @change = "statusOnChange($event)" class = "form-select form-control">
                <option value = "All"> All </option>
                <option value = "Active"> Active </option>
                <option value = "Pending"> Pending </option>
                <option value = "Approved"> Approved </option>
                <option value = "Rejected"> Rejected </option>
                <option value = "Returned"> Returned </option>
              </select>
        </div>
        <table class="styled-table">
              <thead>
                <tr>
                  <th>Loan Request ID</th>
                  <th>Artifact Name</th>
                  <th>Request Status</th>
                  <th> </th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="request in requests" :key="request.requestId">
                  <td>{{request.requestId}}</td>
                  <td>{{request.artifact.name}}</td>
                  <td>{{request.status}}</td>
                  <td><button class="styled-button" v-b-modal.view-request-modal @click="sendInfo(request)">View</button></td>
                  <td>
                  <span v-if="request.status == 'Pending'">
                  <button class="styled-button" @click="approveRequest(request)">Approve</button>
                  <button class="reject-button" @click="rejectRequest(request.requestId)">Reject</button>
                  </span>
                  <span v-if="request.status == 'Approved'">
                  <button class="styled-button" @click="returnedRequest(request.requestId)">Confirm Return</button>
                  </span>
                  </td>
                </tr>
              </tbody>
        </table>
      <b-modal modal-class="popup" id="view-request-modal" centered title="REQUEST" ok-only ok-variant="dark" ok-title="Back">
              <span class="modalContent"><strong>Title: </strong> {{this.artifactTitle}}<br></span>
              <span class="modalContent"><strong>Description: </strong> {{this.artifactDescription}}<br></span>
              <span class="modalContent"><strong>Worth: </strong> {{this.artifactWorth}}$<br></span>
              <span class="modalContent"><strong>Condition: </strong>
                <span v-if="this.artifactCondition == false">Mint condition</span>
                <span v-else> There are damages to the item </span><br>
              </span>
              <span class="modalContent"><strong>Loan Fee: </strong> {{this.artifactLoanFee}}$/day<br></span>
              <span class="modalContent"><strong>Loan Duration: </strong> {{this.artifactLoanDuration}} days<br></span>
              <span class="modalContent"><strong>Client: </strong> {{this.clientName}}<br></span>
              <span class="modalContent"><strong>Status: </strong> {{this.requestStatus}}<br></span>
      </b-modal>
    </div>
</template>

<script>
import axios from 'axios'
var config = require('../../config')
var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosStaff = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'ManageLoanRequests',
    data() {
        return {
            clientId: '',
            requests: [],
            clientName: '',
            artifactTitle: '',
            artifactDescription: '',
            artifactWorth: '',
            artifactCondition: '',
            artifactLoanFee: '',
            artifactLoanDuration: '',
            requestStatus: '',
            showAlert: false,
        }
    },
    created: function () {
        // Get clientId from session storage
        let username = sessionStorage.getItem('loggedInClient')
        // Initializing donation requests of the client from the backend
        axiosStaff.get('/loanRequests')
            .then(response => {
                console.log(response)
                this.requests = response.data
            })
            .catch(e => {
                console.log('Error in GET loanRequest/{username}')
                console.log(e)
            })
        this.clientId = username
    },
    methods: {
      sendInfo(request){
              this.artifactTitle = request.artifact.name
              this.artifactDescription = request.artifact.description
              this.artifactWorth = request.artifact.worth
              this.artifactCondition = request.artifact.isDamaged
              this.artifactLoanFee = request.artifact.loanFee
              this.artifactLoanDuration = request.loanDuration
              this.requestStatus = request.status
              this.clientName = request.client.username
      },
    approveRequest : function(request) {
      const self = this
      if (request.client.currentLoanNumber != request.client.museumManagementSystem.maxLoanNumber) {
      return new Promise(function (resolve, reject) {
        axiosStaff.put('loanRequest/approveRequest/' + request.requestId)
        .then(response => {
            var result = response.data;
            console.log(response);
            resolve(result);
            window.location.reload();
        }, e => {
            console.log('Error in PUT loanRequest/approveRequest/{requestId}');
            console.log(e);
            reject(e);
        })
      })
      } else {
       this.showAlert = true;
      }
    },
    rejectRequest : function(requestId) {
          const self = this
          return new Promise(function (resolve, reject) {
            axiosStaff.put('loanRequest/rejectRequest/' + requestId)
            .then(response => {
                var result = response.data
                console.log(response)
                resolve(result)
                window.location.reload();
            },
              e => {
                console.log('Error in PUT loanRequest/rejectRequest/{requestId}')
                console.log(e)
                reject(e)
            })
          })
    },
    returnedRequest : function(requestId) {
          const self = this
          return new Promise(function (resolve, reject) {
            axiosStaff.put('loanRequest/loanReturn/' + requestId)
            .then(response => {
                var result = response.data
                console.log(response)
                resolve(result)
                window.location.reload();
            },
              e => {
                console.log('Error in PUT loanRequest/loanReturn/{requestId}')
                console.log(e)
                reject(e)
     })
    })
   },
    statusOnChange: function (e) {
       this.displayedStatus = e.target.value
       if (this.displayedStatus == 'Pending') {
          axiosStaff.get('loanRequests/Status/' + this.displayedStatus).then(response => {
            console.log(response)
            this.requests = response.data
          }).catch(e => {
            console.log('Error in GET loanRequests/Status/Pending')
            console.log(e) })
          } else if (this.displayedStatus == 'Approved') {
            axiosStaff.get('loanRequests/Status/' + this.displayedStatus).then(response => {
              console.log(response)
              this.requests = response.data })
          .catch(e => {
            console.log('Error in GET loanRequests/Status/Approved')
            console.log(e) })
           } else if (this.displayedStatus == 'Rejected') {
              axiosStaff.get('loanRequests/Status/' + this.displayedStatus).then(response => {
                console.log(response)
                this.requests = response.data })
           .catch(e => {
              console.log('Error in GET loanRequests/Status/Rejected')
              console.log(e) })
           } else if (this.displayedStatus == 'Active') {
              axiosStaff.get('/activeLoanRequests').then(response => {
              console.log(response)
              this.requests = response.data })
              .catch(e => {
              console.log('Error in GET getAllActiveLoanRequests')
              console.log(e) })
          } else if (this.displayedStatus == 'Returned') {
              axiosStaff.get('loanRequests/Status/' + this.displayedStatus).then(response => {
                console.log(response)
                this.requests = response.data })
           .catch(e => {
              console.log('Error in GET loanRequests/Status/Returned')
              console.log(e) })
            } else if (this.displayedStatus == 'All') {
                axiosStaff.get('loanRequests/').then(response => {
                console.log(response)
                this.requests = response.data
            }).catch(e => {
                console.log('Error in GET getAllLoanRequests')
                console.log(e)
            })
          }
        }
  },
}
</script>

<style scoped>
.myLoanRequests {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.noOtherRequests {
  width: 796px;
  color: rgba(0,0,0,1);
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  text-align: center;
}

.bottomText {
  text-align: center;
  position:fixed;
  padding-bottom: 40px;
  left: 0;
  bottom: 0;
  right: 0;
}

.modalContent {
  font-family: Inter;
  font-size: 20px;
}

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}

/deep/ .modal-content {
  background: #006356;
}

.approve-button {
  background: white;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: black;
  text-align: center;
  transition: 0.2s;
  width: 110px;
}

.approve-button:hover {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}

.reject-button {
  background: #2A2B2A;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  transition: 0.2s;
  width: 110px;
}

.reject-button:hover {
  background: white;
  border: solid;
  border-color: red;
  color: red;
}

.statusTypeDropdown {
  position: absolute;
  top: 6%;
  right: 15px;
}
</style>
