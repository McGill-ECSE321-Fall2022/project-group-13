<!-- @author Yu An Lu (yu-an-lu) -->
<template>
  <div class="manageDonationsRequests">
    <head>
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <span class="title">ALL DONATION REQUESTS</span>

    <!-- Search & Filter bar -->
    <div class="search-bar">
      <div class="search-dropdown">
        <span><strong>Filters:  &nbsp</strong></span>
        <select @change = "statusOnChange($event)" class = "form-select dropdown" v-model="displayedStatus">
          <option value = "All"> All </option>
          <option value = "Pending"> Pending </option>
          <option value = "Approved"> Approved </option>
          <option value = "Rejected"> Rejected </option>
        </select>
      </div>
      <input class="search-input" type="text" placeholder="Enter client username" v-model="clientId"></b-form-input>
      <button v-if="clientId.trim().length > 0" class="styled-button" @click="getAllDonationRequestsByClient()">Search</button>
      <button v-else class="delete-button" disabled>Search</button>
    </div>

    <!-- Display table for all donation requests -->
    <table class="styled-table">
      <thead>
        <tr>
          <th>Donation ID</th>
          <th>Donator</th>
          <th>Artifact Name</th>
          <th>Request Status</th>
          <th> </th>
          <th> </th>
          <th> </th>
          <th> </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests" :key="request.requestId">
          <td>{{request.requestId}}</td>
          <td>{{request.client.username}}</td>
          <td>{{request.artifact.name}}</td>
          <td>{{request.status}}</td>
          <td><button class="styled-button" v-b-modal.view-request-modal @click="sendInfo(request)">View</button></td>
          <td>
              <button v-if="request.status == 'Pending'" class="styled-button" @click="approveButton(request)">Approve</button>
              <button v-else class="delete-button" disabled>Approve</button>
          </td>
          <td>
              <button v-if="request.status == 'Pending'" class="delete-button" @click="rejectDonationRequest(request)">Reject</button>
              <button v-else class="delete-button" disabled>Reject</button>
          </td>
          <td>
              <button v-if="request.status == 'Rejected'" class="delete-button" @click="deleteRejectedDonationRequest(request)">Delete</button>
              <button v-else class="delete-button" disabled>Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Popup for view specific donation request -->
    <b-modal modal-class="popup" id="view-request-modal" centered title="DONATION INFO" hide-footer>
      <span class="view-request"><strong>Name: </strong> {{this.currentDonationArtifactName}}<br></span>
      <span class="view-request"><strong>Description: </strong> {{this.currentDonationArtifactDescription}}<br></span>
      <span class="view-request"><strong>Worth: </strong> {{this.currentDonationArtifactWorth}}$<br></span>
      <span class="view-request"><strong>Condition: </strong>
        <span v-if="this.currentDonationArtifactIsDamaged == false">Mint condition</span>
        <span v-else> There are damages to the item </span><br></span>
    </b-modal>

    <!-- Alert -->
    <div class="bottomText">
      <b-alert v-model="showDismissibleAlert" variant="danger" dismissible fade in>
        Client with username does not exist!
      </b-alert>
    </div>
  </div>
</template>

<script src="./script/ManageDonationRequests.js">
</script>

<style scoped>
.manageDonationsRequests {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}

.delete-button {
  background: white;
  border-color: #008573;
  color: #008573;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  text-align: center;
  transition: 0.2s;
}

.delete-button:hover {
  background: black;
  border: none;
  color: white;
}

.delete-button:disabled,
button[disabled] {
  background: none;
  border: none;
  color: grey;
}

.search-bar {
  position: absolute;
  top: 6%;
  right: 15px;
}

.search-input {
  display: inline-block;
  padding: 12px 20px;
}

.search-dropdown {
  display: inline-block;
  border-right: 3px solid #008573;
  padding-right: 5px;
}

.dropdown {
  padding: 12px 20px;
}

.bottomText {
  position: fixed;
  text-align: center;
  padding-bottom: 40px;
  left: 0;
  bottom: 0;
  right: 0;
}

.alert {
  margin-left: auto; 
  margin-right: auto; 
  bottom:0;
  text-align: center;
  font-weight: bold;
  width: 40%;
}

/deep/ .popup {
  background: rgba(0, 0, 0, 0.5);
  color: white;
}

/deep/ .modal-content {
  background: #008573;
}

/deep/ .view-request {
  font-family: Inter;
  font-size: 20px;
}
</style>
