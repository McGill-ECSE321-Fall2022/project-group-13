<template>
    <div class="ticketPurchase">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <div style="margin-top:10%; margin-left: 20%;">        
            <span class="slogan">Fulfill your curiosity with us.</span>
            <span class="ticketprice">Regular Admission: CA ${{ticketFee}}</span>
            <button class="styled-button bookTicketButton" @click="openTicketPurchaseForm()">Book Tickets</button>
        </div>
        <hr class="line">
    </div>
</template>

<script>
    import axios from 'axios'
    var config = require('../../config')
    var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
    var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

    var axiosTicketPurchase = axios.create({
        baseURL: backendUrl,
        headers: { 'Access-Control-Allow-Origin': frontendUrl }
    })
    export default {
        name: 'TicketPurchase',
        data() {         
            return {
                ticketFee: 0,
            }
        },
        created() {
            axiosTicketPurchase.get('/mms/getMms')
                .then(response => {
                    this.ticketFee = response.data.ticketFee;
                })
                .catch(error => {
                    console.log(error);
                })
        }
    }
</script>

<style>
    .ticketPurchase {
    width: 100%;
    height: 1024px;
    background: rgba(255,255,255,1);
    opacity: 1;
    position: relative;
    top: 0px;
    left: 0px;
    overflow: hidden;
    }

    .slogan {
    width: 1089px;
    color: rgba(0,0,0,1);
    font-family: Inter;
    font-weight: 800;
    font-size: 40px;
    opacity: 1;
    text-align: left;
    line-height: normal;
    float: left;
    position: 25;
    }

    .ticketprice {
    width: 1089px;
    color: rgba(0,0,0,1);
    font-family: Inter;
    font-weight: 800;
    font-size: 30px;
    opacity: 1;
    text-align: left;
    line-height: 2.5;
    float: left;
    }

    .bookTicketButton {
        float: left;
    }  
    .line{
        margin-top:25%; 
        width:90%; 
        margin-left:5%; 
        size: 5; 
        color: black;
    }  
</style>