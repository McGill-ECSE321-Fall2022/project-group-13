<template>
  <div class="clientCatalog">
    <head>
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
            integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <span class="title">CATALOG</span>
    <button class="styled-button" v-b-modal.add-artifact-modal>+</button>
    <table class="styled-table">
      <thead>
      <tr>
        <th>Artifact ID</th>
        <th>Artifact Name</th>
        <th> </th>
        <th> </th>
        <th> </th>
        <th> </th>
        <th> </th>
        <th> </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="artifact in artifacts" :key="artifact.artifactId">
        <td>{{artifact.artifactId}}</td>
        <td>{{artifact.name}}</td>
        <td><button class="styled-button" v-b-modal.view-artifact-modal @click="sendInfo(artifact)">View</button></td>
        <td>
          <button class="styled-button" @click="approveButton(request)">Edit</button>
        </td>
        <td>
          <button class="delete-button" @click="deleteArtifact(artifact)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>

    <b-modal modal-class="popup" id="add-artifact-modal" centered title="Add Artifact"
             ok-title="Add" ok-variant="light" cancel-variant="dark"
             no-close-on-esc no-close-on-backdrop hide-header-close
             @show="resetNewArtifactModal"
             @hidden="resetNewArtifactModal"
             @ok="handleOkNewArtifactModal"
    >
      <form ref="newArtifactForm" @submit.stop.prevent="handleSubmitNewArtifact">
        <!-- Artifact Name -->
        <b-form-group
          label="Name"
          invalid-feedback="Artifact Name is required"
        >
          <b-form-input
            type="text"
            placeholder="Artifact name"
            v-model="newArtifactName"
            :state="newArtifactName.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <!-- Artifact Description -->
        <b-form-group
          label="Description"
          invalid-feedback="Description is required"
        >
          <b-form-textarea
            placeholder="This artifact is ..."
            v-model="newArtifactDescription"
            rows="3"
            max-rows="5"
            :state="newArtifactDescription.trim().length > 0 ? true : false"
            required
          ></b-form-textarea>
        </b-form-group>

        <!-- Artifact Loan Availability Status -->
        <b-form-group
          label="Loan Availability"
        >
          <select @change = "onChange($event)" class = "form-select form-control">
            <option value = "Available"> Available </option>
            <option value = "Unavailable"> Unavailable </option>
          </select>
        </b-form-group>

        <!-- Loan Fee -->
        <b-form-group
          label="Loan Fee"
        >
          <b-form-input
            type="number"
            placeholder="0"
            v-model="newArtifactFee"
            :state="parseInt(newArtifactFee) > 0 && newArtifactFee.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <!-- Artifact Worth -->
        <b-form-group
          label="Worth"
          invalid-feedback="Worth is required"
        >
          <b-form-input
            type="number"
            placeholder="0"
            v-model="newArtifactWorth"
            :state="parseInt(newArtifactWorth) > 0 && newArtifactWorth.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <!-- Artifact Preserved State -->
        <b-form-group
          label="Preserved State"
        >
          <select @change = "onChange($event)" class = "form-select form-control">
            <option value = "Good"> Good </option>
            <option value = "Damaged"> Damaged </option>
          </select>
        </b-form-group>

        <!-- Artifact Room -->
        <b-form-group
          label="Room"
          invalid-feedback="Room ID is required"
        >
          <b-form-input
            type="number"
            placeholder="0"
            v-model="newArtifactRoom"
            :state="parseInt(newArtifactRoom) > 0 && newArtifactRoom.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>
      </form>
    </b-modal>

    <!-- Popup for view specific donation request -->
    <b-modal modal-class="popup" id="view-artifact-modal" centered title="ARTIFACT INFO" hide-footer>
      <span class="view-artifact"><strong>Name: </strong> {{this.currentArtifactName}}<br></span>
      <span class="view-artifact"><strong>Description: </strong> {{this.currentArtifactDescription}}<br></span>
      <span class="view-artifact"><strong>Worth: </strong> {{this.currentArtifactWorth}}$<br></span>
      <span class="view-artifact"><strong>Condition: </strong>
        <span v-if="this.currentArtifactIsDamaged == false">Mint condition</span>
        <span v-else> There are damages to the item </span><br></span>
    </b-modal>

    <!-- Alert -->
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
  name: 'ManageCatalog',
  data() {
    return {
      artifacts: [],
      currentArtifactId: '',
      currentArtifactName: '',
      currentArtifactDescription: '',
      currentArtifactStatus: '',
      currentArtifactFee: '',
      currentArtifactWorth: '',
      currentArtifactIsDamaged: false,
      newArtifact: {},
      newArtifactName: '',
      newArtifactImage: '',
      newArtifactDescription: '',
      newArtifactStatus: 'Available',
      newArtifactFee: '',
      newArtifactWorth: 0,
      newArtifactIsDamaged: 'Good',
      newArtifactRoom: '',
    }
  },
  created: function() {
    axiosStaff.get('/artifacts')
      .then(response => {
        console.log(response)
        this.artifacts=response.data;
      })
      .catch(e => {
        console.log('Error in GET getAllArtifacts')
        console.log(e)
      })
  },
  methods:{
    createArtifact: function () {
      const self = this
      let state = false
      let status = 'Available'
      if (self.newArtifactIsDamaged == 'Damaged') {
        state = true
      }
      if (self.newArtifactStatus == 'Unavailable') {
        status = 'Unavailable'
      }
      axiosStaff.post('/artifact/createArtifact', {}, {
        params: {
          name: self.newArtifactName,
          description: self.newArtifactDescription,
          image: self.newArtifactImage,
          status: status,
          loanFee: self.newArtifactFee,
          isDamaged: state,
          worth: self.newArtifactWorth,
          roomId: self.newArtifactRoom,
        }
      })
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
          },
          error => {
            console.log('Error in POST createArtifact')
            console.log(error)
            reject(error)
          })

    },
    resetNewArtifactModal: function () {
      this.newArtifact = {}
      this.newArtifactName = ''
      this.newArtifactImage = "MMS-backend/images/Donation.PNG"
      this.newArtifactDescription = ''
      this.newArtifactStatus = 'Available',
        this.newArtifactFee = '',
        this.newArtifactWorth = 0
      this.newArtifactIsDamaged = 'Good',
        this.newArtifactRoom = ''
    },
    checkNewArtifactFormValidity() {
      return this.newArtifactName.trim().length > 0 &&
        this.newArtifactDescription.trim().length > 0 &&
        parseInt(this.newArtifactWorth) > 0
    },
    handleSubmitNewArtifact: async function () {
      if (!this.checkNewArtifactFormValidity()) {
        return
      }
      this.newArtifact = await this.createArtifact()
      this.$nextTick(() => {
        this.$bvModal.hide("add-artifact-modal")
      })
      window.location.reload();
    },
    handleOkNewArtifactModal: function (bvModalEvent) {
      bvModalEvent.preventDefault()
      this.handleSubmitNewArtifact()
    },
    onChange: function (e){
      this.newArtifactStatus = e.target.value
      this.newArtifactIsDamaged = e.target.value
    },
    deleteArtifact: function (artifact) {
      axiosStaff.delete('artifacts/' + artifact.artifactId)
      .then(response => {
        console.log(response)
        window.location.reload()
      })
      .catch(e=> {
        console.log('Error in DELETE artifacts/' + artifact.artifactId)
        console.log(e)
      })
    },
    sendInfo: function (artifact) {
      this.currentArtifactName = artifact.name
      this.currentArtifactDescription = artifact.description
      this.currentArtifactWorth = artifact.worth
      this.currentArtifactIsDamaged = artifact.isDamaged
    },
  }
}
</script>

<style>
.clientCatalog{
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 50px;
  left: 0px;
  overflow: hidden;
}

</style>
