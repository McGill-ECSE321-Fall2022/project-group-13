<!-- @author Lucy Zhang (Lucy-Zh) -->
<template>
  <div class="myTickets">
      <head>
          <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
          <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
      </head>
      <div style="margin-top:10%; margin-left: 20%;">  
        <span class="title">MY TICKETS</span>
        <button class="styled-button bookTicketButton" onclick="window.location.href = '/client/ticketpurchase';">GET TICKETS</button>
      </div>
      <!-- ticket table, all tickets found for current client -->
      <table class="styled-table">
        <thead>
          <tr>
            <th>Ticket ID</th>
            <th>Ticket Fee</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
        <tr v-for="ticket in tickets">
          <td>{{ ticket.ticketId }}</td>
          <td>${{ ticket.fee }}</td>
          <td>{{ ticket.isActive }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script src="./script/MyTickets.js"></script>

<style>
.myTickets {
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
position: absolute;
font-family: Inter;
font-weight: 600;
font-size: 40px;
opacity: 1;
top: 40px;
left: 167px;
}

.bookTicketButton {
  position: absolute;
  top: 50px;
  right: 15px;
}

</style>

