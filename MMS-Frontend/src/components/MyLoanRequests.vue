<template>
    <div class="myLoanRequests">
        <head>
              <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
              <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">MY LOAN REQUESTS</span>
        <div class="bottomText">
                        <span class="noOtherRequests">No other requests made.<br> Would you like to browse the catalog?<br></span>
                        <a href="/client/browsecatalog"> <button class="styled-button">View Catalog</button> </a>
        </div>
        <table class="styled-table">
              <thead>
                <tr>
                  <th>Loan Request ID</th>
                  <th>Artifact Name</th>
                  <th>Request Status</th>
                  <th> </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="request in requests" :key="request.requestId">
                  <td>{{request.requestId}}</td>
                  <td>{{request.artifact.name}}</td>
                  <td>{{request.status}}</td>
                  <td><button class="styled-button" v-b-modal.view-request-modal @click="sendInfo(request)">View</button></td>
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
              <span class="modalContent"><strong>Status: </strong> {{this.requestStatus}}<br></span>
      </b-modal>
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
    name: 'MyLoanRequests',
    data() {
        return {
            clientId: '',
            requests: [],
            artifactTitle: '',
            artifactDescription: '',
            artifactWorth: '',
            artifactCondition: '',
            artifactLoanFee: '',
            artifactLoanDuration: '',
            requestStatus: '',
        }
    },
    created: function () {
        // Get clientId from session storage
        let username = sessionStorage.getItem('loggedInClient')
        // Initializing donation requests of the client from the backend
        axiosClient.get('loanRequests/' + username)
            .then(response => {
                console.log(response)
                this.requests = response.data
            })
            .catch(e => {
                console.log('Error in GET loanRequest/' + username)
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
      },
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
</style>
