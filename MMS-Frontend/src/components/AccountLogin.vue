<template>
    <div class="accountLogin">
    
    <form @submit="submitLoginForm">
        <h1>Cabinet of Curiosities</h1>
        <hr class="solid"

        <div class = "userTypeDropdown">
        <section>
        <select name = "userID" @change = "onChange($event)" class ="form-select form-control">
          <option value = "Client"> Cient </option>
          <option value = "Employee"> Employee </option>
          <option value = "Manager"> Manager </option>
        </select>
        </section>
        </div>

        <div style="position:relative; margin:auto; left:40%;">
        <div class="form-group">
            <input style="width: 20%;" class="form-control" type = "text" v-model="username" placeholder="Username" />
        </div>

        <div class="form-group" style="margin:auto;">
            <input style="width: 20%;" type = "password" class="form-control" v-model="password" placeholder="Password" />
        </div>
        
        </div>
            <button class="btn btn-primary btn-block">Login</button>
        <h6>Don't have an account?</h6>

        <p>
            <router-link to="/signup">Sign Up</router-link>
        </p>

    </form>
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

var axiosEmployee = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

var axiosManager = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'AccountLogin',
    data() {
      return {
        username: '',
        password: '',
        //The accountType is set as Client bcs it only updates if the dropdown menu is touched.. 
        accountType: 'Client', 
        
        //This method gets the current value of the dropdown menu
        onChange(e){
          this.accountType = e.target.value;
        }
  
      }
    },
    methods: {
      submitLoginForm(event) {
          event.preventDefault();
          this.handleSubmit(this.password);
        },
        
      handleSubmit(password) {
        const self = this;
        var fetchDateUrl; 
        // Case where the account type is a client 
        if (this.accountType === 'Client'){
            fetchDateUrl = ({username}) => `/client/signin/${username}`;
            axiosClient.get(fetchDateUrl({username: this.username}), {
                params: {
                  password
                }}).then((response) => {
                      sessionStorage.setItem('loggedInClient', response.data.username);
                      self.$router.push({ path: '/client' });
                 })
        }

        // Case where the account type is an employee 
        else if (this.accountType === "Employee"){
            fetchDateUrl = ({username}) => `/employee/signin/${username}`;
            axiosEmployee.get(fetchDateUrl({username: this.username}), {
                params: {
                  password
                }}).then((response) => {
                      sessionStorage.setItem('loggedInEmployee', response.data.username);
                      self.$router.push({ path: '/employee' });
                 })
        }
        
        //Case where the account type is a manager 
        else if (this.accountType === "Manager"){
            fetchDateUrl = ({username}) => `/manager/signin/${username}`;
            axiosEmployee.get(fetchDateUrl({username: this.username}), {
                params: {
                  password
                }}).then((response) => {
                      sessionStorage.setItem('loggedInManager', response.data.username);
                      self.$router.push({ path: '/manager' });
                 })
        }
      }
    }
  }
</script>

<style scoped>
h1 {
  font-weight: bold;
  margin-top: 5vw;
  margin-bottom: 3vw;
}
h2 {
  font-size: 150%;
  padding-bottom: 1.5rem;
}

.accountLogin {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}
</style>
