<template>
    <div class="accountSignup">
      <b-alert :show="showErrorAlert" variant="danger">{{this.errorData}}</b-alert>

    <div class = "dinoPicture">
        <img src="../assets/dinoLogo.png">
    </div>

    <form @submit="submitSignInForm">
        <h1>Cabinet of Curiosities</h1>
        <hr class="solid"

        <div style="position:relative; margin:auto; left:40%;">
        <div class="form-group">
            <input style="width: 20%;" class="form-control" type = "text" v-model="username" placeholder="Username" />
        </div>

        <div class="form-group" style="margin:20;">
            <input style="width: 20%; bottom = 20px;" type = "nameInput" class="form-control" v-model="name" placeholder="Name" />
        </div>

        <div class="form-group" style="margin:20;">
            <input style="width: 20%;" type = "text" class="form-control" v-model="password" placeholder="Password" />
        </div>
        
        </div>

        <div>
        <button class="styledButton">Create Account</button>
        </div>
        
        <div class = "label1">
        <h6>Already have an account?</h6>
        </div>

        <div class = "loginLink">
            <a href="/" style = "color: #008573">Login</a>
        </div>
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


export default {
    name: 'AccountSignUp',
    data() {
      return {
        username: '',
        name: '',
        password: '',
        showErrorAlert: false,
        errorData: ''
      };
    },

    methods: {
      submitSignInForm(event) {
          event.preventDefault();
          this.handleSubmit(this.username,this.password);
        },

        // http://localhost:8080/client?username=naziaC&name=Nazia Chowdhury&password=abcdhello&systemId=1
        
      handleSubmit(username,password) {
        const self = this;
        let nameOfClient = self.name;
        console.log(nameOfClient);

        axiosClient.post("/client", {}, {
          params: {
              "username": username,
              "name": nameOfClient,
              "password":password
          }
      }).then((response) => {
        self.$router.push({ path: '/' });
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
      
      }
    }
  }
</script>

<style>
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

.accountSignup {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.styledButton {
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 0px;
  background: #008573;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  transition: 0.2s;
}

.styledButton:hover {
  background: white;
  border: solid;
  border-color: #008573;
  color: #008573;
}

.label1 {
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 20px;
}

.loginLink {
  width: 20%;
  position:relative; 
  margin:auto; 
  top: 20px;
}

</style>