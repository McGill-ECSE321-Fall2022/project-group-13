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
    name: 'myDonationRequests',
    data() {
        return {
            requests: [],
        }
    },
    // created() {
    //     axiosClient.get('/donationRequest/ofClient/' + this.$route.params.clientId)
    //         .then(response => {
    //             console.log(response)
    //             this.requests = response.data
    //         })
    //         .catch(e => {
    //             console.log('Error in GET /donationRequests/ofClient')
    //             console.log(e)
    //         })
    // },
    methods: {

    }
}