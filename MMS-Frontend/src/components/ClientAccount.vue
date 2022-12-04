<template>
    <div class="accountInformation">
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    
    <span class="title">MY ACCOUNT</span>
  
      <div class = "accountInfo">

        <div class="usernameLabel">Username: {{this.oldClientUsername}}</div>
        <div class="firstNameLabel">First Name: {{this.oldClientFirstName}}</div>
        <div class="lastNameLabel">Last Name: {{this.oldClientLastName}}</div>
        <div class="passwordLabel">Password: {{this.oldClientPassword}}</div>

      </div>

    <div class = "saveButton">
        <button class="styled-button EditClientButton" v-b-modal.edit-client-account-modal>Edit Information</button>
        </div>
        
    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="edit-client-account-modal" centered title="EDIT YOUR INFORMATION" ok-title="Save Changes" ok-variant="light" cancel-variant="dark"
    @show="resetEditClientModal"
    @hidden="resetEditClientModal"
    @ok="handleOkEditClientModal"
    >
    <form ref="editClientInformationForm" @submit.stop.prevent="handleSubmitEditClientAccount">

        <b-form-group
          label="New Full Name"
          invalid-feedback="Full name is required"
        >
          <b-form-input
            type="text"
            placeholder="Full Name"
            v-model="newClientName"
            :state="newClientName.trim().length > 0 ? true : false"
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
            :state="newClientPassword.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>
      </form>
    </b-modal>

        <div class = "logoutButton">
        <form action="/">
            <button class="styled-button">Logout</button>
        </form>
        </div>
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
        oldClientFirstName: '',
        oldClientLastName : '',
        oldClientPassword: '',
        newClientName: '',
        newClientPassword: '', 
      }
    },
  created() {
    let username = sessionStorage.getItem('loggedInClient');
    axiosClient.get('client/' + username)
    .then(response => {
        this.oldClientUsername= response.data.username; 
        const myArray = response.data.name.split(" ");
        this.oldClientFirstName = myArray[0]; 
        this.oldClientLastName = myArray[1]; 
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
            const myArray = response.data.name.split(" ");
            this.oldClientFirstName = myArray[0]; 
            this.oldClientLastName = myArray[1]; 
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
        this.newClientPassword.length  <= 30
        
    },
    handleSubmitEditClientAccount: async function () {
        if (!this.checkEditClientFormValidity()) {
            console.log('failed to edit client')
            return
        }
        this.editClientAccount(this.oldClientUsername)
        console.log('edited client')
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

.accountInfo{
  position:relative; 
  margin:auto; 
  top: 30%;
}

.saveButton{
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 40%;
}

.accountInfo{
  color: #196d60;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 50px;
}

.logoutButton{
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 42%;
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