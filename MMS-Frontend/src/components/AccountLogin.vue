<template>
    <div>
        <h1>Cabinet of Curiosities</h1>
        <hr class="solid">
        
        <section>
        <input type="radio" name="test" value="value1"> Manager
        <input type="radio" name="test" value="value2"> Client
        <input type="radio" name="test" value="value3"> Employee
        <br />
        </section>

        <div class = "usernameInput">
            <h5><input type = "text" v-model="username" placeholder="Username" /></h5>
        </div>
        <div class = "passwordInput">
            <h5><input type = "password" v-model="password" placeholder="Password" /></h5>
        </div>
        <div class = "loginButton">
            <h5><button type="button">Login</button></h5>
        </div>
        <h6>Don't have an account?</h6>
        <p>
            <router-link to="/signup">Sign Up</router-link>
        </p>
    </div>
</template>


<script>
</script>

<style>
/* Centering the placeholders */
::placeholder {
   text-align: center; 
}

/* Solid border */
hr.solid {
  border-top: 3px solid #bbb;
}
</style>