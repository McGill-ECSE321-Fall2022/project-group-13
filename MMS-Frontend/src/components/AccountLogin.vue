<template>
    <div class="accountLogin">
    <form @submit="submitLoginForm">
        <h1>Cabinet of Curiosities</h1>
        <hr class="solid"
        <section>
        <input type="radio" name="test" value="value1"> Manager
        <input type="radio" name="test" value="value2"> Client
        <input type="radio" name="test" value="value3"> Employee
        </section>
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

export default {
  name: 'AccountLogin',
  data() {
    return {
      username: '',
      password: '',
    }
  },
  methods: {
    submitLoginForm(event) {
        event.preventDefault();
        this.handleSubmit(this.password);
      },
    handleSubmit(password) {
      const self = this;
      var fetchDateUrl = ({username}) => `/client/signin/${username}`;
      axiosClient.get(fetchDateUrl({username: this.username}), {
      params: {
        password
      }}).then((response) => {
            sessionStorage.setItem('loggedInClient', response.data.username);
            self.$router.push({ path: '/client' });
       })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
