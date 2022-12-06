<!-- @author Yu An Lu (yu-an-lu) -->
<template>
  <div class="myDonationRequests">
    <head>
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
    </head>
    <button class="styled-button donateButton" v-b-modal.new-artifact-donation-modal>DONATE</button>
    <span class="title">MY DONATION REQUESTS</span>
    <div class="bottomText">
      <span class="thankYou">Thank you for your wonderful donations!</span>
    </div>

    <!-- Display table for all donation requests of client-->
    <table class="styled-table">
      <thead>
        <tr>
          <th>Donation ID</th>
          <th>Artifact Name</th>
          <th>Request Status</th>
          <th> </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests" :key="request.requestId">
          <td>{{request.requestId}}</td>
          <td>{{request.artifact.name}}</td>
          <td>{{request.status}}</td>
          <td><button class="styled-button" v-b-modal.view-request-modal @click="sendInfo(request)">View</button></td>
        </tr>
      </tbody>
    </table>

    <!-- Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal -->
    <!-- Popup for create donation request -->
    <b-modal modal-class="popup" id="new-artifact-donation-modal" centered title="MAKE YOUR DONATION" 
      ok-title="Donate" ok-variant="light" cancel-variant="dark"
      hide-header-close
      @show="resetNewArtifactModal"
      @hidden="resetNewArtifactModal"
      @ok="handleOkNewArtifactModal"
    >
      <form ref="newDonationArtifactForm" @submit.stop.prevent="handleSubmitNewDonationArtifact">
        <!-- Donation Image --> <!-- Doesn't work -->
        <!--b-form-group-->
          <!--img src="../../../MMS-Backend/images/Donation.PNG" class="rounded-circle border border-light"/-->
        <!--/b-form-group-->

        <!-- Artifact Name -->
        <b-form-group
          label="Name"
          invalid-feedback="Artifact Name is required"
        >
          <b-form-input
            type="text"
            placeholder="Artifact name"
            v-model="newDonationArtifactName"
            :state="newDonationArtifactName.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <!-- Artifact Description -->
        <b-form-group
          label="Description"
          invalid-feedback="Description is required"
        >
          <b-form-textarea
            placeholder="This artifact is ..."
            v-model="newDonationArtifactDescription"
            rows="3"
            max-rows="5"
            :state="newDonationArtifactDescription.trim().length > 0 ? true : false"
            required
          ></b-form-textarea>
        </b-form-group>

        <!-- Artifact Worth -->
        <b-form-group
          label="Worth"
          invalid-feedback="Worth is required"
        >
          <b-form-input
            type="number"
            placeholder="0"
            v-model="newDonationArtifactWorth"
            :state="parseInt(newDonationArtifactWorth) > 0 && newDonationArtifactWorth.trim().length > 0 ? true : false"
            required
          ></b-form-input>
        </b-form-group>

        <!-- Artifact Preserved State -->
        <b-form-group
          label="Preserved State"
        >
          <select @change = "onChange($event)" class = "form-select form-control">
            <option value = "Good"> Good </option>
            <option value = "Damaged"> Damaged </option>
          </select>
        </b-form-group>
      </form>
    </b-modal>

    <!-- Popup for view specific donation request -->
    <b-modal modal-class="popup" id="view-request-modal" centered title="DONATION INFO" hide-footer>
      <span class="view-request"><strong>Name: </strong> {{this.currentDonationArtifactName}}<br></span>
      <span class="view-request"><strong>Description: </strong> {{this.currentDonationArtifactDescription}}<br></span>
      <span class="view-request"><strong>Worth: </strong> {{this.currentDonationArtifactWorth}}$<br></span>
      <span class="view-request"><strong>Condition: </strong>
        <span v-if="this.currentDonationArtifactIsDamaged == false">Mint condition</span>
        <span v-else> There are damages to the item </span><br></span>
    </b-modal>
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
  background-color: rgba(0, 133, 115, 0.2);
  width: 796px;
  color: rgba(0,0,0,1);
  font-family: Inter;
  font-weight: Bold;
  font-size: 35px;
  opacity: 1;
  text-align: center;
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
