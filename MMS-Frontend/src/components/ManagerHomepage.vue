<!-- @author Samantha Perez Hoffman (samperezh)-->

<template>
    <div class="managerHomepage">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>

        <div class="welcome">
        <p class="title">WELCOME TO THE CABINET OF CURIOSITIES </p>
        <p class="subtitle"> Fulfill your curiosity with us</p>
        </div>

        <div class="button-container">
            <button class="styled-button">EDIT TICKET FEE</button>
            <button class="styled-button">EDIT DAY STATUS</button>
            <button class="styled-button">EDIT OPENING HOURS</button>
        </div>
    </div>

</template>

<script>
</script>

<style scoped>
.managerHomepage {
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
  top: 0%;
  left: 0%;
}

.subtitle {
  top: 15%;
  color: #008573;
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
  margin-left: 25%;
  margin-right: 25%;
  background: rgba(255,255,255,1);
  opacity: 1;
}

.button-container {
  position: relative;
  top: 15%;
  margin-left: 37.5%;
  margin-right: 37.5%;
  width: 25%;
  display: block;  /*make the buttons appear below each other*/
}

.styled-button{
    width:100%;
    margin-bottom: 10%;

}

</style>