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
                openTime: '',
                closeTime: '',
                monClosed: false,
                tueClosed: false,
                wedClosed: false,
                thuClosed: false,
                friClosed: false,
                satClosed: false,
                sunClosed: false,
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
                //set opening and closing times
                axiosTicketPurchase.get('/mms/getMms')
                .then(response => {
                        this.openTime = response.data.openingTime;
                        this.closeTime = response.data.closingTime;
                })
                .catch(error => {
                    console.log(error);
                })
                //get monday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: 'Monday',
                    }
                })
                .then(response => {
                        console.log(response)
                        this.monClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get tuesday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: 'Tuesday',
                    }
                })
                .then(response => {
                        console.log(response.isClosed)
                        this.tueClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get wednesday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: "Wednesday",
                    }
                })
                .then(response => {
                        this.wedClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get thursday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: "Thursday",
                    }
                })
                .then(response => {
                        this.thuClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get friday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: "Friday",
                    }
                })
                .then(response => {
                        this.friClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get saturday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: "Saturday",
                    }
                })
                .then(response => {
                        this.satClosed = response.data.isClosed;
                })
                .catch(error => {
                    console.log(error);
                })
                //get sunday closing status
                axiosTicketPurchase.get('mms/DayByDayType', {
                    params: {
                            day: "Sunday",
                    }
                })
                .then(response => {
                        this.sunClosed = response.data.isClosed;
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