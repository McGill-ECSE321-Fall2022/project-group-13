/**
 * @author Lucy Zhang (Lucy-Zh)
 * 
 */

import axios from 'axios'
var config = require('../../../config')
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
                counter: 1,
                displayTotal: 0,
            }
        },
        created() {
                axiosTicketPurchase.get('/mms/getMms')
                .then(response => {
                    this.ticketFee = response.data.ticketFee;
                    this.displayTotal = this.ticketFee;
                })
                .catch(error => {
                    console.log(error);
                })
        },
        methods: {
            createTicket: function(numberOfTickets) {

                for(var i = 0; i < numberOfTickets; i++) {
                    axiosTicketPurchase.post('/ticket', {}, {
                        params: {
                            clientUsername: sessionStorage.getItem('loggedInClient'),
                        }
                    })
                    .then(response => {
                        console.log(response);
                    })
                    .catch(error => {
                        console.log(error);
                    })
                }
            },
            resetNewTicketPurchaseModal: function () {
                this.counter = 1;
                this.displayPrice = this.ticketFee;
            },
            // checkPriceValidity: function(displayPrice) {
            //     var response = axiosTicketPurchase.get('/mms/getMms')
            //     if(response.data.ticketFee == displayPrice) {
            //         return true;
            //     }else{
            //         return false;
            //     }
            // },
            handleSubmitNewTicketPurchase: async function () {
                this.newTicketPurchase = await this.createTicket()
                this.createTicket(this.counter)
                this.$nextTick(() => {
                    this.$bvModal.hide("new-ticket-purchase-modal")
                })
            },
            handleOkNewTicketPurchaseModal: function (bvModalEvent) {
                bvModalEvent.preventDefault()
                this.handleSubmitNewTicketPurchase()
            },
            // refreshTotal: function () {
            //     var numberOfTickets = document.getElementById('numberOfTickets').value;
            //     this.displayTotal = this.ticketFee * numberOfTickets;
            // },
            changeCounter: function (num) {
                this.counter += +num;
                console.log(this.counter);
                !isNaN(this.counter) && this.counter > 0 ? this.counter : (this.counter = 0);
            }
        }
    }  