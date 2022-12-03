<template>
  <div class="myTickets">
      <head>
          <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
          <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
      </head>
      <span class="title">MY TICKETS</span>
      <table class="styled-table">
        <thead>
          <tr>
            <th>Ticket ID</th>
            <th>Ticket Fee</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
            <tr>
                <td>10</td>
                <td>$10.00</td>
                <td>Active</td>
            </tr>
            <tr class="active-row">
                <td>11</td>
                <td>$11.99</td>
                <td>Active</td>
            </tr>
            <tr class="active-row">
                <td>12</td>
                <td>$11.99</td>
                <td>Active</td>
            </tr>
        </tbody>
    </table>
  </div>
</template>

<style scoped>
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

