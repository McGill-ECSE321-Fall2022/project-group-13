<template>
    <div class="manageEmployees">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <button class="styled-button CreateEmployeeButton" v-b-modal.new-employee-account-modal>Create Employee Account</button>

        <div class = "viewSchedules">
        <form action="/manager/viewSchedules">
            <button class="styled-button">View Schedules</button>
        </form>
        </div>

        <div class = "editSchedules">
        <form action="/manager/editschedule">
            <button class="styled-button">Edit Schedules</button>
        </form>
        </div>

        <span class="title">MY SCHEDULE</span>
        <table class="styled-table">
        <thead>
          <tr>
            <th>Employee Username</th>
            <th>Full Name</th>
            <th>Action</th>

          </tr>
        </thead>
        <tbody>
          <tr v-for="s in employees" :key="s.username">
                <td>{{s.username}}</td>
                <td>{{s.name}}</td>
                <td><button class="styled-button deleteButton" @click="deleteEmployee(s.username)">Delete</button></td>
            </tr>
        </tbody>
    </table>   


    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <b-modal modal-class="popup" id="new-employee-account-modal" centered title="CREATE NEW EMPLOYEE" ok-title="Create" ok-variant="light" cancel-variant="dark"
    @show="resetNewEmployeeModal"
    @hidden="resetNewEmployeeModal"
    @ok="handleOkNewEmployeeModal"
    >

      <form ref="newEmployeeAccountForm" @submit.stop.prevent="handleSubmitNewEmployeeAccount">
        <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>

        <b-form-group
          label="Employee Username"
          invalid-feedback="Username is required"
        >
          <b-form-input
            type="text"
            placeholder="Username"
            v-model="newEmployeeUsername"
            :state="(newEmployeeUsername.trim().length > 0 && newEmployeeUsername.indexOf(' ') < 0)? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group
          label="Employee Full Name"
          invalid-feedback="Full name is required"
        >
          <b-form-input
            type="text"
            placeholder="Full Name"
            v-model="newEmployeeName"
            :state="(newEmployeeName.trim().length > 0) ? true : false"
            required
          ></b-form-input>
        </b-form-group>


        <b-form-group
          label="Employee Password"
          invalid-feedback="Password is required"
        >
          <b-form-input
            type="text"
            placeholder="Password"
            v-model="newEmployeePassword"
            :state="(newEmployeePassword.trim().length >= 8 && newEmployeePassword.trim().length <= 30 && newEmployeePassword.indexOf(' ') < 0 ) ? true : false"
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
  name: 'ManageEmployees',
  data(){
    return {
      employees: [],
      newEmployeeUsername: '', 
      newEmployeeName: '', 
      newEmployeePassword: '',
      showErrorAlert: false,
      errorData: ''
    }
  }, 
  created(){
    axiosManager.get('/employees')
    .then(response => {
            this.employees = response.data
        })
    .catch(e => {
          console.log('Error in GET /shift/employee')
          console.log(e)
        })
  },
  methods:{
    deleteEmployee: function(EmployeeUsername){
        const self = this;
        var fetchDateUrl = ({EmployeeUsername}) => `/employee/delete/${EmployeeUsername}`;
        axiosManager.delete(fetchDateUrl({EmployeeUsername: EmployeeUsername}))
        .then(response => {
            console.log(response)
        })
        .catch(e => {
            console.log('Error in DELETE /employee/delete/' + EmployeeUsername)
            console.log(e)
        })
        //reload page to show updated table
        window.location.reload();
    }, 
    createEmployeeAccount : function() {
      const self = this
      console.log(self.newEmployeeName)
      return new Promise(function (resolve, reject) {
        axiosManager.post('/employee',{}, {
            params: {
              username: self.newEmployeeUsername,
              name: self.newEmployeeName,
              password: self.newEmployeePassword
            }
        })
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
        e => {
          if (e.response.data == "Cannot have empty fields" || e.response.data == "The username cannot have spaces" || e.response.data == "Invalid name" 
          || e.response.data =="Invalid password" || e.response.data == "This username is already taken" ){
              self.errorData = e.response.data
              self.showErrorAlert = true
            }
          else if (e.response.status == 400 || e.response.status == 409){
                  self.errorData = "Wrong Input Format"
                  self.showErrorAlert = true
                }
                reject(e)
        })
      })
    },
    // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
    resetNewEmployeeModal: function () {
      this.newEmployeeUsername = ''
      this.newEmployeeName =  ''
      this.newEmployeePassword = ''
    },
    checkNewEmployeeFormValidity() {
        return this.newEmployeeUsername.trim().length > 0 && 
        this.newEmployeeName.length > 0 && 
        this.newEmployeePassword.length  > 0
        
    },
    handleSubmitNewEmployeeAccount: async function () {
      const self = this
        if (!this.checkNewEmployeeFormValidity()) {
            self.errorData = "Fields can't be empty"
            self.showErrorAlert = true
            return
        }
        this.newEmployeeAccount = await this.createEmployeeAccount()
        // console.log('created new employee')
        this.$nextTick(() => {
            this.$bvModal.hide("new-employee-account-modal")
        })
        //reload page to show updated table
        window.location.reload();
    },
    handleOkNewEmployeeModal: function (bvModalEvent) {

        bvModalEvent.preventDefault()
        this.handleSubmitNewEmployeeAccount()
    }

  }
}
</script>

<style scoped>
.manageEmployees {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.styled-table {
    border-collapse: collapse;
    margin: 200px 60px;
    font-size: 25px;
    font-family: sans-serif;
    min-width: 90%;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.styled-table th,
.styled-table td {
    padding: 12px 15px;
}

.styled-table thead tr {
    background-color: #008573;
    color: #ffffff;
    text-align: middle;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}

.CreateEmployeeButton {
  position: absolute;
  top: 50px;
  right: 15px;
}
.editSchedules {
  position: absolute;
  top: 50px;
  right: 270px;
}

.viewSchedules {
  position: absolute;
  top: 50px;
  right: 440px;
}


/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}

/deep/ .modal-content {
  background: #008573;
}
</style>