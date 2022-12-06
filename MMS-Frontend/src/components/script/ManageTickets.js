/**
 * @author Lucy Zhang (Lucy-Zh)
 * 
 * This class handles front-end and http requests for the Manage Tickets page.
 */
 import axios from 'axios'
 var config = require('../../../config')
 var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
 var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
 
 var axiosManageTickets = axios.create({
     baseURL: backendUrl,
     headers: { 'Access-Control-Allow-Origin': frontendUrl }
 })
 
 export default {
     name: 'manageTickets',
     data() {
        return {
          tickets: [],
        }
    },
    created() {
        //gets all tickets ever bought and attributes
        axiosManageTickets.get('/tickets')
                    .then(response => {
                        var response = response.data;
                        for (var i = 0; i < response.length; i++) {
                            if(response[i].isActive) {
                                response[i].isActive = "Active";
                            }else{
                                response[i].isActive = "Inactive";
                            }
                        }
                        this.tickets = response
                    })
                    .catch(error => {
                        console.log(error);
                    })
    },
    methods: {
        //methods to change status of tickets for staff
        makeActive: function (ticket) {
            axiosManageTickets.put('/ticket/' + ticket.ticketId,{},{params: {
                isActive: true
            }
            }).then(response => {
                console.log(response)
                window.location.reload();
            })
            .catch(error => {
                console.log(error);
            })
        },
        makeInactive: function (ticket) {
            axiosManageTickets.put('/ticket/' + ticket.ticketId,{},{params: {
                isActive: false
            }
            }).then(response => {
                console.log(response)
                window.location.reload();
            })
            .catch(error => {
                console.log(error);
            })
        }
    }
      
 }
 