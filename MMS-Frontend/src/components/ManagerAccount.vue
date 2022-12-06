<template>
    <div class="account-information">
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    
    <span class="title">MY ACCOUNT</span>
  
      <div class = "account-info">

        <div class="usernameLabel">Username: {{this.oldManagerUsername}}</div>
        <div class="nameLabel">Name: {{this.oldManagerFullName}}</div>
        <div class="passwordLabel">Password: {{this.oldManagerPassword}}</div>

      </div>

    <div class = "account-button">
        <button class="styled-button" v-b-modal.edit-manager-account-modal>Edit Information</button>
        <a href="/"><button class="styled-button">Logout</button></a>
    </div>
        
    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="edit-manager-account-modal" centered title="EDIT YOUR INFORMATION" ok-title="Save Changes" ok-variant="light" cancel-variant="dark"
    hide-header-close
    @show="resetEditManagerModal"
    @hidden="resetEditManagerModal"
    @ok="handleOkEditManagerModal"
    >
    <form ref="editManagerInformationForm" @submit.stop.prevent="handleSubmitEditManagerAccount">
      <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>
        <b-form-group
          label="New Full Name"
          invalid-feedback="Full name is required"
        >
          <b-form-input
            type="text"
            :placeholder="this.oldManagerFullName"
            v-model="newManagerName"
            :state="(newManagerName.trim().length > 0)? true : false"
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
            v-model="newManagerPassword"
            :state="(newManagerPassword.trim().length >= 8 && newManagerPassword.trim().length <= 30 && newManagerPassword.indexOf(' ') < 0 )? true : false"
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
    name: 'ManagerAccount',
    data() {
      return {
        oldManagerUsername: '',
        oldManagerFullName: '',
        oldManagerPassword: '',
        newManagerName: '',
        newManagerPassword: '', 
        showErrorAlert: false,
        errorData: ''
      }
    },
  created() {
    axiosManager.get('/managers/')
    .then(response => {
        console.log('test1')
        this.oldManagerUsername= response.data.username; 
        this.oldManagerFullName = response.data.name; 
        this.oldManagerPassword = response.data.password; 
    })
    },

    methods: {
    // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
    resetEditManagerModal: function () {
      this.newManagerName = ''
      this.newManagerPassword =  ''
    },
    editManagerAccount: function(managerUsername){
        const self = this;
        var fetchDateUrl = ({managerUsername}) => `/manager/edit/${managerUsername}`;
        axiosManager.put(fetchDateUrl({managerUsername: managerUsername}), {}, {
          params: {
              "name": self.newManagerName,
              "password": self.newManagerPassword
          }})
        .then(response => {
            this.oldManagerFullName = response.data.name;
            this.oldManagerPassword = response.data.password; 
        })
    },
    checkEditManagerFormValidity() {
        return this.newManagerName.trim().length > 0 && 
        this.newManagerPassword.length  >= 8 &&
        this.newManagerPassword.length  <= 30 &&
        this.newManagerPassword.indexOf(' ') < 0 
        
    },
    handleSubmitEditManagerAccount: async function () {
      const self = this
        if (!this.checkEditManagerFormValidity()) {
            self.errorData = "Incorrect information was entered"
            self.showErrorAlert = true
            return
        }
        this.editManagerAccount(this.oldManagerUsername)
        this.$nextTick(() => {
            this.$bvModal.hide("edit-manager-account-modal")
        })
        //reload page to show updated table
        window.location.reload();
    },
    handleOkEditManagerModal: function (bvModalEvent) {

        bvModalEvent.preventDefault()
        this.handleSubmitEditManagerAccount()
    }
    }
  }

</script>

<style>
.account-information {
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