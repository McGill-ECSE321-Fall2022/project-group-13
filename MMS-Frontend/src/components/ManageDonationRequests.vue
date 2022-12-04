<template>
    <div class="myDonations">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">ALL DONATION REQUESTS</span>
    <span class="title">ALL DONATION REQUESTS</span>
    <table class="styled-table">
        <thead>
            <tr>
                <th>Donation ID</th>
                <th>Donator</th>
                <th>Artifact Name</th>
                <th>Request Status</th>
                <th> </th>
            </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests" :key="request.requestId">
          <td>{{request.requestId}}</td>
          <td>{{request.client.username}}</td>
          <td>{{request.artifact.name}}</td>
          <td>{{request.status}}</td>
          <td><button class="styled-button">View</button></td>
        </tr>
      </tbody>
    </table>
    </div>
</template>

<script src="./script/ManageDonationRequests.js">
</script>

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
</style>
