<template>
  <div class="browseCatalog">
    <head>
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <span class="title">Artifacts</span>

    <table class="styled-table">
              <thead>
                <tr>
                  <th>Artifact Name</th>
                  <th>Loan Status</th>
                  <th> </th>
                  <th> </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="artifact in artifacts" :key="artifact.artifactId">
                  <td>{{artifact.name}}</td>
                  <td>{{artifact.loanStatus}}</td>
                  <td><button class="styled-button" v-b-modal.view-artifact-modal @click="sendInfo(artifact)">View</button></td>
                  <td><button v-if="artifact.loanStatus == 'Available'" class="styled-button" v-b-modal.make-loan-request-modal @click="sendInfo(artifact)">Make Loan Request</button></td>
                </tr>
              </tbody>
        </table>

    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="view-artifact-modal" centered title="REQUEST" ok-only ok-variant="dark" ok-title="Back" hide-header-close>
                  <span class="modalContent"><strong>Title: </strong> {{this.artifactName}}<br></span>
                  <span class="modalContent"><strong>Description: </strong> {{this.artifactDescription}}<br></span>
                  <span class="modalContent"><strong>Worth: </strong> {{this.artifactWorth}}$<br></span>
                  <span class="modalContent"><strong>Condition: </strong>
                    <span v-if="this.artifactCondition == false">Mint condition</span>
                    <span v-else> There are damages to the item</span><br>
                  </span>
                  <span class="modalContent"><strong>Loan Status: </strong> {{this.artifactStatus}}<br></span>
    </b-modal>

    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="make-loan-request-modal" centered title="REQUEST" ok-title="Confirm Request"
    ok-variant="light" cancel-variant="dark"
    hide-header-close
    @show="resetLoanRequestModal"
    @hidden="resetLoanRequestModal"
    @ok="handleOkLoanRequestModal">
        <span class="modalContent"><strong>Title: </strong> {{this.artifactName}}<br></span>
        <span class="modalContent"><strong>Description: </strong> {{this.artifactDescription}}<br></span>
        <span class="modalContent"><strong>Worth: </strong> {{this.artifactWorth}}$<br></span>
        <span class="modalContent"><strong>Condition: </strong>
        <span v-if="this.artifactCondition == false">Mint condition</span>
        <span v-else> There are damages to the item </span><br>
        </span>
        <span class="modalContent"><strong>Loan Fee: </strong> {{this.artifactLoanFee}}$/day<br></span>

        <b-form-group label="Please Indicate Loan Duration" invalid-feedback="Loan Duration is required">
        <b-form-input type="text" v-model="loanDuration" :state="loanDuration > 0 ? true : false" required></b-form-input>
        </b-form-group>
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
    name: 'BrowseCatalog',
    data() {
        return {
            artifacts: [],
            artifactName: '',
            artifactDescription: '',
            artifactWorth: '',
            artifactCondition: '',
            artifactLoanFee: '',
            artifactStatus: '',
            clientUsername: '',
            artifactId: '',
            loanDuration: '',
            newLoanArtifact: {},
        }
    },
    created: function () {
        axiosClient.get('artifacts/')
           .then(response => {
              console.log(response)
              for (let index in response.data) {
                if (response.data[index].roomLocation != null) {
                  this.artifacts.push(response.data[index])
                }
              }
           }).catch(e => {
              console.log('Error in GET artifacts')
              console.log(e)
        })
    },
    methods: {
        createLoanRequest: function () {
          // Export this function scope to promise
          const self = this
          let username = sessionStorage.getItem('loggedInClient')
          return new Promise(function (resolve, reject) {
          // create a loan request
          axiosClient.post('loanRequest', {}, {
             params: {
               loanDuration: self.loanDuration,
               artifactId: self.artifactId,
               username: username,
             }
          }).then(response => {
                var result = response.data
                console.log(response)
                resolve(result)
          }, error => {
             console.log('Error in POST loanRequest/')
             console.log(error)
             reject(error)
          })
         })
        },
        // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
        resetLoanRequestModal: function () {
            this.loanDuration = ''
        },
        checkLoanRequestFormValidity() {
            return parseInt(this.loanDuration) > 0
        },
        handleSubmitNewLoanRequest: async function () {
            if (!this.checkLoanRequestFormValidity()) {
                return
            }
            // loan request is created and we hide the modal
            this.createLoanRequest()
            this.$nextTick(() => {
                this.$bvModal.hide("make-loan-request-modal")
            })
        },
        handleOkLoanRequestModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitNewLoanRequest()
        },
        // updating data to be displayed in the view modal
        sendInfo(artifact){
           this.artifactName = artifact.name
           this.artifactDescription = artifact.description
           this.artifactWorth = artifact.worth
           this.artifactCondition = artifact.isDamaged
           this.artifactLoanFee = artifact.loanFee
           this.artifactStatus = artifact.loanStatus
           this.artifactId = artifact.artifactId
        }
    }
}
</script>

<style scoped>
.browseCatalog {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}

/deep/ .modal-content {
  background: #008573;
}

/deep/ .view-request {
  font-family: Inter;
  font-size: 20px;
}
</style>
