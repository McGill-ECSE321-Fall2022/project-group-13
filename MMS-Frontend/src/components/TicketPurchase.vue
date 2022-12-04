<template>
    <div class="ticketPurchase">
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter|Inter:600">
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
        </head>
        <div style="margin-top:7%;">
            <b-alert v-model="showAlert" variant="info">Price has changed, please refresh the page and try again.</b-alert>  
        </div>
        <div style="margin-top:2%; margin-left: 20%;">      
            <span class="slogan">Fulfill your curiosity with us.</span>
            <span class="ticketprice">Regular Admission: CA ${{ticketFee}}</span>
            <button class="styled-button bookTicketButton" v-b-modal.new-ticket-purchase-modal>Book Tickets</button>
        </div>
        <div style="margin-top:10%;"><hr class="line"></div>
        <b-modal modal-class="popup" id="new-ticket-purchase-modal" centered title="RESERVE YOUR SPOT" ok-only ok-title="Reserve" ok-variant="dark"
        @show="resetNewTicketPurchaseModal"
        @ok="handleSubmitNewTicketPurchase"
        @hidden="resetNewTicketPurchaseModal"
        >
            <form ref="newTicketPurchaseForm">
                <b-form-group>  
                    <span class="modalPrice">Regular Ticket - CA ${{ticketFee}} each</span><br /><br />
                    <button class="minus" @click="changeCounter('-1')" type="button" name="button">-</button>
					<input class="numberOfTickets" type="text" name="name" :value="counter">
					<button class="plus" @click="changeCounter('1')" type="button" name="button">+</button>
                </b-form-group>
                <b-form-group> 
                    <span class="total">Total: CA ${{displayTotal}}</span>
                </b-form-group>
            </form>
        </b-modal>
    </div>
</template>

<script src="./script/TicketPurchase.js"></script>

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
    margin-bottom: 5px;
    }

    .ticketprice {
    width: 1089px;
    color: rgba(0,0,0,1);
    font-family: Inter;
    font-weight: 800;
    font-size: 20px;
    opacity: 1;
    text-align: left;
    line-height: 2.5;
    float: left;
    margin-bottom: 10px;
    }

    .bookTicketButton {
        float: left;
    }  

    .line{
        margin-top:22%; 
        width:90%; 
        margin-left:5%; 
        background-color: black;
        height: 1px;
        position: relative;
    }
    .modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0,0,0); /* Fallback color */
        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    }
    .modal-content {
        background-color: #fefefe;
        margin: auto;
        padding: 20px;
        border: 1px solid #888;
        width: 80%;
    }  
    .close {
        color: #aaaaaa;
        float: right;
        font-size: 28px;
        font-weight: bold;
    }
    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }
    span {cursor:pointer; }
		.number{
			margin:100px;
		}
		.minus{
			width:32px;
			height:38px;
			background:black;
			border-radius:4px;
			padding:8px 5px 8px 5px;
			border:1px solid #ddd;
            display: inline-block;
            vertical-align: middle;
            text-align: center;
            color: white;
		}
        .plus{
			width:32px;
			height:38px;
			background:#008573;
			border-radius:4px;
			padding:8px 5px 8px 5px;
			border:1px solid #ddd;
            display: inline-block;
            vertical-align: middle;
            text-align: center;
            color: white;
		}
	    input{
            height:38px;
            width: 100px;
            text-align: center;
            font-size: 26px;
            border:1px solid #ddd;
            border-radius:4px;
            display: inline-block;
            vertical-align: middle;
        }
        .total{
            font-weight: 600;
            float: right;
        }
</style>