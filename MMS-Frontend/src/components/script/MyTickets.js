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
     name: 'myTickets',
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
 methods: {
    function getTickets() {
      var self = this
      axiosClient.get('/mms/getMms', {
        dataType: 'json',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        mode: 'no-cors',
        credentials: 'include'
      })
      .then(function (response) {
        console.log(JSON.stringify(response.data))
        self.courses = response.data
      })
      .catch(function (error) {
        console.log(error)
      })
    }
  }