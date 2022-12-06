<template>
    <div class="account-information">
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    
    <span class="title">MY ACCOUNT</span>
  
      <div class = "account-info">

        <div class="usernameLabel">Username: {{this.oldEmployeeUsername}}</div>
        <div class="nameLabel">Name: {{this.oldEmployeeFullName}}</div>
        <div class="passwordLabel">Password: {{this.oldEmployeePassword}}</div>

      </div>

    <div class = "account-button">
        <button class="styled-button" v-b-modal.edit-employee-account-modal>Edit Information</button>
        <a href="/"><button class="styled-button">Logout</button></a>
    </div>
        
    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="edit-employee-account-modal" centered title="EDIT YOUR INFORMATION" ok-title="Save Changes" ok-variant="light" cancel-variant="dark"
    hide-header-close
    @show="resetEditEmployeeModal"
    @hidden="resetEditEmployeeModal"
    @ok="handleOkEditEmployeeModal"
    >
    <form ref="editEmployeeInformationForm" @submit.stop.prevent="handleSubmitEditEmployeeAccount">
      <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>
        <b-form-group
          label="New Full Name"
          invalid-feedback="Full name is required"
        >
          <b-form-input
            type="text"
            :placeholder="this.oldEmployeeFullName"
            v-model="newEmployeeName"
            :state="(newEmployeeName.trim().length > 0)? true : false"
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
            v-model="newEmployeePassword"
            :state="(newEmployeePassword.trim().length >= 8 && newEmployeePassword.trim().length <= 30 && newEmployeePassword.indexOf(' ') < 0 )? true : false"
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

var axiosEmployee = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'EmployeeAccount',
    data() {
      return {
        oldEmployeeUsername: '',
        oldEmployeeFullName: '',
        oldEmployeePassword: '',
        newEmployeeName: '',
        newEmployeePassword: '', 
        showErrorAlert: false,
        errorData: ''
      }
    },
  created() {
    let username = sessionStorage.getItem('loggedInEmployee');
    axiosEmployee.get('employee/' + username)
    .then(response => {
        this.oldEmployeeUsername= response.data.username; 
        this.oldEmployeeFullName = response.data.name; 
        this.oldEmployeePassword = response.data.password; 
    })
    },

    methods: {
    // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
    resetEditEmployeeModal: function () {
      this.newEmployeeName = ''
      this.newEmployeePassword =  ''
    },
    editEmployeeAccount: function(employeeUsername){
        const self = this;
        var fetchDateUrl = ({employeeUsername}) => `/employee/edit/${employeeUsername}`;
        axiosEmployee.put(fetchDateUrl({employeeUsername: employeeUsername}), {}, {
          params: {
              "name": self.newEmployeeName,
              "password": self.newEmployeePassword
          }})
        .then(response => {
            this.oldEmployeeFullName = response.data.name;
            this.oldEmployeePassword = response.data.password; 

        })
    },
    checkEditEmployeeFormValidity() {
        return this.newEmployeeName.trim().length > 0 && 
        this.newEmployeePassword.length  >= 8 &&
        this.newEmployeePassword.length  <= 30 &&
        this.newEmployeePassword.indexOf(' ') < 0 
        
    },
    handleSubmitEditEmployeeAccount: async function () {
      const self = this
        if (!this.checkEditEmployeeFormValidity()) {
            self.errorData = "Incorrect information was entered"
            self.showErrorAlert = true
            return
        }
        this.editEmployeeAccount(this.oldEmployeeUsername)
        // console.log('edited employee')
        this.$nextTick(() => {
            this.$bvModal.hide("edit-employee-account-modal")
        })
        //reload page to show updated table
        window.location.reload();
    },
    handleOkEditEmployeeModal: function (bvModalEvent) {

        bvModalEvent.preventDefault()
        this.handleSubmitEditEmployeeAccount()
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