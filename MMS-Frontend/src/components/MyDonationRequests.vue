<template>
    <div class="myDonations">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <button class="donateButton">DONATE</button>
        <span class="title">MY DONATION REQUESTS</span>
        <div class="bottomText">
        <span class="thankYou">Thank you for your wonderful donations!</span>
        </div>
    </div>
</template>

<style scoped>
.myDonations {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.donateButton {
  background: #008573;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  position: absolute;
  top: 50px;
  right: 15px;
}

.title {
  color: #008573;
  position: absolute;
  font-family: Inter;
  font-weight: 600;
  font-size: 40px;
  opacity: 1;
  top: 0;
  left: 167px;
}

.bottomText {
  position: fixed;
  text-align: center;
  padding-bottom: 40px;
  left: 0;
  bottom: 0;
  right: 0;
}

.thankYou {
  width: 796px;
  color: rgba(0,0,0,1);
  font-family: Inter;
  font-weight: Bold;
  font-size: 40px;
  opacity: 1;
  text-align: center;
}
</style>

