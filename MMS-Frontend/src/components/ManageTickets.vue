<template>
    <div class="viewTickets">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <span class="title">ALL TICKETS</span>
        <table class="styled-table">
            <thead>
            <tr>
                <th>Ticket ID</th>
                <th>Purchaser Name</th>
                <th>Purchaser Username</th>
                <th>Ticket Fee</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="ticket in tickets">
                    <td>{{ ticket.ticketId }}</td>
                    <td>{{ ticket.client.name }}</td>
                    <td>{{ ticket.client.username }}</td>
                    <td>{{ ticket.fee }}</td>
                    <td>{{ ticket.isActive }}</td>
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
.styled-table {
    border-collapse: collapse;
    margin: 200px 60px;
    font-size: 30px;
    font-family: sans-serif;
    min-width: 1400px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.styled-table th,
.styled-table td {
    padding: 12px 15px;
}

.styled-table thead tr {
    background-color: #008573;
    color: #ffffff;
    text-align: middle;
}
.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #009879;
}
</style>