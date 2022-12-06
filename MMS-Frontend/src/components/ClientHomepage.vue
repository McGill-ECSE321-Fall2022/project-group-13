<template>
    <div class="clientHomepage">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <div class="welcome">
        <p class="title">WELCOME TO THE CABINET OF CURIOSITIES </p>
        <p class="subtitle"> Fulfill your curiosity with us</p>
        </div>
        <div class="button-container">
            <a href="/client/ticketpurchase"> <button class="styledButton subtitle">Tickets</button> </a>
        </div>
    </div>
</template>

<style scoped>
.clientHomepage {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.title {
  color: #008573;
  position: relative;
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  top: 50%;
  left: 0%;
}

.subtitle {
  top: 55%;
  color: black;
  position: relative;
  font-family: Inter;
  font-weight: 450;
  font-size: 30px;
  opacity: 1;
}

.welcome {
  position: relative;
  top: 10%;
  width: 50%;
  height: 20%;
  margin-left: auto;
  margin-right: auto;
  background: rgba(255,255,255,1);
  opacity: 1;
}

.button-container {
  position: relative;
  top: 22%;
  margin-left: 37.5%;
  margin-right: 37.5%;
  width: 25%;
  display: block;
}

.styledButton {
  background: #008573;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
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
</style>
