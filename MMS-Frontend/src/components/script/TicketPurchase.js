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
                showAlert: false,
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
                axiosTicketPurchase.get('/mms/getMms')
                .then(response => {
                    var newestPrice = response.data.ticketFee;
                    if(newestPrice == this.ticketFee) {
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
                       window.location.href = '/client/mytickets';
                    }else{
                        this.showAlert = true;
                    }
                })
                .catch(error => {
                    console.log(error);
                })
            },
            resetNewTicketPurchaseModal: function () {
                this.counter = 1;
                this.displayPrice = this.ticketFee;
            },
            handleSubmitNewTicketPurchase: async function () {
                this.createTicket(this.counter)
            },
            changeCounter: function (num) {
                this.counter += +num;
                !isNaN(this.counter) && this.counter > 0 ? this.counter : (this.counter = 1);
                this.displayTotal = this.ticketFee * this.counter;
            }
                
        }
    }  