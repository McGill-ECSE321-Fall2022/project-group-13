<template>
    <div class="myDonationRequests">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <button class="styledButton donateButton">DONATE</button>
        <span class="title">MY DONATION REQUESTS</span>
        <div class="bottomText">
        <span class="thankYou">Thank you for your wonderful donations!</span>
        </div>
        <table id="displayDonationRequests">
          <thead>
            <tr>
              <th>Donation ID</th>
              <th>Artifact Name</th>
              <th>Request Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="request in requests">
              <td>{{request.requestId}}</td>
              <td>{{request.artifact.name}}</td>
              <td>{{request.status}}</td>
              <td><button>View</button></td>
            </tr>
          </tbody>
        </table>
    </div>
</template>

<script src="./script/MyDonationRequests.js">
</script>

<style scoped>
.myDonationRequests {
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
  position: absolute;
  top: 50px;
  right: 15px;
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
