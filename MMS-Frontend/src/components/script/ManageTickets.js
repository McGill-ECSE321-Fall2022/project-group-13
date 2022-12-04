/**
 * @author Lucy Zhang (Lucy-Zh)
 * 
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
     name: 'manageTickets',
     data() {
        return {
          tickets: [],
        }
    },
    created() {
        axiosMyTickets.get('/tickets')
                    .then(response => {
                        this.tickets = response.data
                    })
                    .catch(error => {
                        console.log(error);
                    })
    }
      
 }
 