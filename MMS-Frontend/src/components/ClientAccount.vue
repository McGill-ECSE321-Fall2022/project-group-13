<template>
  <div class="accountInformation">
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    
    <span class="title">MY ACCOUNT</span>
  
      <div class = "account-info">

        <div class="usernameLabel">Username: {{this.oldClientUsername}}</div>
        <div class="fullNameLabel">Name: {{this.oldClientFullName}}</div>
        <div class="passwordLabel">Password: {{this.oldClientPassword}}</div>

      </div>

    <div class = "account-button">
        <button class="styled-button" v-b-modal.edit-client-account-modal>Edit Information</button>
        <a href="/"><button class="styled-button">Logout</button></a>
    </div>
        
    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="edit-client-account-modal" centered title="EDIT YOUR INFORMATION" ok-title="Save Changes" ok-variant="light" cancel-variant="dark"
    @show="resetEditClientModal"
    @hidden="resetEditClientModal"
    @ok="handleOkEditClientModal"
    >
    <form ref="editClientInformationForm" @submit.stop.prevent="handleSubmitEditClientAccount">
      <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>
        <b-form-group
          label="New Full Name"
          invalid-feedback="Full name is required"
        >
          <b-form-input
            type="text"
            :placeholder="this.oldClientFullName"
            v-model="newClientName"
            :state="(newClientName.trim().length > 0) ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="New Password"
          invalid-feedback="Password is required"
        >
          <b-form-input
            type="text"
            placeholder="Password"
            v-model="newClientPassword"
            :state="(newClientPassword.trim().length >=8 && newClientPassword.trim().length <= 30 && newClientPassword.indexOf(' ') < 0) ? true : false"
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

var axiosClient = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'ClientAccount',
    data() {
      return {
        oldClientUsername: '',
        oldClientFullName: '',
        oldClientPassword: '',
        newClientName: '',
        newClientPassword: '', 
        showErrorAlert: false,
        errorData: ''
      }
    },
  created() {
    let username = sessionStorage.getItem('loggedInClient');
    axiosClient.get('client/' + username)
    .then(response => {
        this.oldClientUsername= response.data.username; 
        this.oldClientFullName = response.data.name;
        this.oldClientPassword = response.data.password; 
    })
    },

    methods: {
    // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
    resetEditClientModal: function () {
      this.newClientName = ''
      this.newClientPassword =  ''
    },
    editClientAccount: function(clientUsername){
        const self = this;
        var fetchDateUrl = ({clientUsername}) => `/client/edit/${clientUsername}`;
        axiosClient.put(fetchDateUrl({clientUsername: clientUsername}), {}, {
          params: {
              "name": self.newClientName,
              "password": self.newClientPassword
          }})
        .then(response => {
            this.oldClientFullName = response.data.name;
            this.oldClientPassword = response.data.password; 

        })
        .catch(e => {
            console.log('Error in PUT /client/edit/' + clientUsername)
            console.log(e)
        })
    },
    checkEditClientFormValidity() {
        return this.newClientName.trim().length > 0 && 
        this.newClientPassword.length  >= 8 &&
        this.newClientPassword.length  <= 30 && this.newClientPassword.indexOf(' ') < 0 
        
    },
    handleSubmitEditClientAccount: async function () {
      const self = this
        if (!this.checkEditClientFormValidity()) {
            self.errorData = "Incorrect information was entered"
            self.showErrorAlert = true
            return
        }
        this.editClientAccount(this.oldClientUsername)
        // console.log('edited client')
        this.$nextTick(() => {
            this.$bvModal.hide("edit-client-account-modal")
        })
        //reload page to show updated table
        window.location.reload();
    },
    handleOkEditClientModal: function (bvModalEvent) {

        bvModalEvent.preventDefault()
        this.handleSubmitEditClientAccount()
    }
    }
  }

</script>

<style>
.accountInformation {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.textFields{
    height: 400px;
    position: relative;
    top: 0px;
    left: 0px;
}

.account-info{
  position:relative; 
  margin:auto; 
  top: 20%;
  color: #196d60;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 50px;
}

.account-button{
  position: absolute;
  top: 50px;
  right: 15px;
}

input {
  text-align: center;
}

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}

/deep/ .modal-content {
  background: #008573;
}


</style>