/**
 * @author Yu An Lu (yu-an-lu)
 * 
 */
 import axios from 'axios'
 var config = require('../../../config')
 var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
 var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
 
 var axiosClient = axios.create({
     baseURL: backendUrl,
     headers: { 'Access-Control-Allow-Origin': frontendUrl }
 })

 export default {
    name: 'ManageDonationRequests',
    data() {
        return {
            employeeId: '',
            requests: [],
        }
    },
    created: function () {
        // Get employeeId from session storage
        let username = sessionStorage.getItem('loggedInEmployee')
        // Initializing all donation requests from the backend
        axiosClient.get('donationRequests')
            .then(response => {
                console.log(response)
                this.requests = response.data
            })
            .catch(e => {
                console.log('Error in GET getAllDonationRequests')
                console.log(e)
            })
        this.employeeId = username
    },
    methods: {
        
    }
 }