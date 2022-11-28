<template>
    <div>
        <h1>Cabinet of Curiosities</h1>
        <hr class="solid">
        <div class = "usernameInput">
            <h5><input type = "text" v-model="username" placeholder="Username" /></h5>
        </div>
        <div class = "firstNameInput">
            <h5><input type = "text" v-model="firstNameInput" placeholder="First Name" /></h5>
        </div>
        <div class = "lastNameInput">
            <h5><input type = "text" v-model="lastNameInput" placeholder="Last Name" /></h5>
        </div>
        <div class = "passwordInput">
            <h5><input type = "password" v-model="password" placeholder="Password" /></h5>
        </div>
        <div class = "signUpButton">
            <h5><button type="button">Create Account</button></h5>
        </div>
        <h6>Already have an account?</h6>
        <p>
            <router-link to="/">Sign In</router-link>
        </p>
    </div>
</template>


<script>
</script>

<style>

/* Solid border */
hr.solid {
  border-top: 3px solid #bbb;
}

/* Centering the placeholders */
::placeholder {
   text-align: center; 
}
</style>