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
    name: 'MyDonationRequests',
    data() {
        return {
            clientId: '',
            requests: [],
            newDonationArtifact: ''
        }
    },
    created: function () {
        // Get clientId from session storage
        this.clientId = sessionStorage.getItem('loggedInClient')

        // Initializing donation requests of the client from the backend
        axiosClient.get('donationRequests/ofClient/' + clientId)
            .then(response => {
                console.log(response)
                this.requests = response.data
            })
            .catch(e => {
                console.log('Error in GET donationRequests/ofClient/' + clientId)
                console.log(e)
            })
    },
    methods: {
        createDonationArtifact: function (name, image, description, isDamaged, worth) {
            axiosClient.post('donationArtifact', {}, {
                params: {
                    name: name,
                    image: image,
                    description: description,
                    isDamaged: isDamaged,
                    worth: worth
                }
            })
                .then(response => {
                    console.log(response)
                    this.newDonationArtifact = response.data
                })
                .catch(e => {
                    console.log('Error in POST createDonationArtifact')
                    console.log(e)
                })
        },
        createDonationRequest: function (clientUsername, artifactId) {
            axiosClient.post('donationRequest', {}, {
                params: {
                    clientUsername: clientUsername,
                    artifactId: artifactId
                }
            })
                .then(response => {
                    console.log(response)
                    this.requests.push(response.data)
                })
                .catch(e => {
                    console.log('Error in POST createDonationRequest')
                    console.log(e)
                })
        }
    }
}