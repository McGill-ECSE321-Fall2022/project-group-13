/**
 * @author Lucy Zhang (Lucy-Zh)
 * 
 * This class handles front-end and http requests for the My Tickets page.
 */
 import axios from 'axios'
 var config = require('../../../config')
 var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
 var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
 
 var axiosMyTickets = axios.create({
     baseURL: backendUrl,
     headers: { 'Access-Control-Allow-Origin': frontendUrl }
 })
 
 export default {
     name: 'myTickets',
     data() {
        return {
          tickets: [],
        }
    },
    created() {
        let username = sessionStorage.getItem('loggedInClient');
        axiosMyTickets.get('/tickets/' + username) //gets all tickets by logged in client
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
    }    
 }