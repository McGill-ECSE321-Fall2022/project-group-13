<!-- @author Lucy Zhang (Lucy-Zh) -->
<template>
    <div class="viewTickets">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">ALL TICKETS</span>
        <!-- table to display all tickets ever bought -->
        <table class="styled-table">
            <thead>
            <tr>
                <th>Ticket ID</th>
                <th>Purchaser Name</th>
                <th>Purchaser Username</th>
                <th>Ticket Fee</th>
                <th>Status</th>
                <th>Edit Status</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="ticket in tickets">
                    <td>{{ ticket.ticketId }}</td>
                    <td>{{ ticket.client.name }}</td>
                    <td>{{ ticket.client.username }}</td>
                    <td>${{ ticket.fee }}</td>
                    <td>{{ ticket.isActive }}</td>
                    <td>
                        <button v-if="ticket.isActive == 'Inactive'" class="make-active-button" @click="makeActive(ticket)">Make Active</button>
                        <button v-else class="styled-button" @click="makeInactive(ticket)">Make Inactive</button>
                     </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script src="./script/ManageTickets.js"></script>

<style>
.viewTickets {
  width: 100%;
  height: 1024px;
  background: rgba(255,255,255,1);
  opacity: 1;
  position: relative;
  top: 0px;
  left: 0px;
  overflow: hidden;
}
.make-active-button {
  background: black;
  border: none;
  border-radius: 12px;
  padding: 15px;
  opacity: 1;
  font-family: Inter;
  font-weight: 600;
  color: white;
  text-align: center;
  transition: 0.2s;
}
.make-active-button:hover {
  background: white;
  border: solid;
  border-color: black;
  color: black;
}
</style>