<template>
    <div class="accountInformation">
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    
    <span class="title">MY ACCOUNT</span>

    <form @submit="submitEditForm">

        <div class = "textFields">
        <div style="position:relative; margin:auto; left:40%; top:50%;">

        <div class="form-group" style="margin:20;">
            <input style="width: 20%;" class="form-control" type = "text" v-model="username" value = getClientInfo()[username] disabled/>
        </div>

        <div class="form-group" style="margin:20;">
            <input style="width: 20%;" type = "firstNameInput" class="form-control" v-model="firstName" placeholder="First Name" />
        </div>

        <div class="form-group" style="margin:20;">
            <input style="width: 20%; bottom = 20px;" type = "lastNameInput" class="form-control" v-model="lastName" placeholder="Last Name" />
        </div>

        <div class="form-group" style="margin:20;">
            <input style="width: 20%;" type = "text" class="form-control" name = "password" v-model="password" placeholder="Password" />
        </div>
        
        </div>
        </div>

        <div class = "saveButton">
        <button class="styled-button">Save Changes</button>
        </div>

    </form>

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
        clientUsername: '',
        clientFirstName: '',
        clientUsername: '',
        clientPassword: '',
      }
    },
  created() {
    let username = sessionStorage.getItem('loggedInClient');
    axiosClient.get('client/' + username)
    .then(response => {
        this.clientUsername = response.data.username; 
        const myArray = response.data.name.split(" ");
        this.clientFirstName = myArray[0]; 
        this.clientLastName = myArray[1]; 
        this.clientPassword = response.data.password; 
    })
    },

    methods: {
      submitEditForm(event) {
          event.preventDefault();
        }
        
    },
        getClientInfo(){
            return {
            'username': this.clientUsername,
            'firstname': this.clientFirstName,
            'lastname': this.clientUsername,
            'password': this.clientPassword
            };
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

.saveButton{
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 30px;
}


.logoutButton{
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 50px;
}

input {
  text-align: center;
}

</style>