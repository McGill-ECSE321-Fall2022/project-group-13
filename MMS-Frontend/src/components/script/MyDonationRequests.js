/**
 * @author Yu An Lu (yu-an-lu)
 * This class handles front-end and http requests for the My Donation Requests page.
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
            currentRequestId: '',
            currentDonationArtifactName: '',
            currentDonationArtifactDescription: '',
            currentDonationArtifactWorth: '',
            currentDonationArtifactIsDamaged: false,
            newDonationArtifact: {},
            newDonationArtifactName: '',
            newDonationArtifactImage: "MMS-backend/images/Donation.PNG",
            newDonationArtifactDescription: '',
            newDonationArtifactWorth: 0,
            newDonationArtifactIsDamaged: 'Good',
        }
    },
    created: function () {
        // Get clientId from session storage
        let username = sessionStorage.getItem('loggedInClient')
        // Initializing donation requests of the client from the backend
        axiosClient.get('donationRequests/ofClient/' + username)
            .then(response => {
                console.log(response)
                this.requests = response.data
            })
            .catch(e => {
                console.log('Error in GET donationRequests/ofClient/' + username)
                console.log(e)
            })
        this.clientId = username
    },
    methods: {
        // Creates a new donation artifact
        createDonationArtifact: function () {
            // Export this function scope to promise
            const self = this

            return new Promise(function (resolve, reject) {
            let state = false
            // Cases in preserved state
            if (self.newDonationArtifactIsDamaged == 'Damaged') {
                state = true
            }

            axiosClient.post('donationArtifact', {}, {
                params: {
                    name: self.newDonationArtifactName,
                    image: self.newDonationArtifactImage,
                    description: self.newDonationArtifactDescription,
                    isDamaged: state,
                    worth: self.newDonationArtifactWorth
                }
            })
                .then(response => {
                    var result = response.data
                    console.log(response)
                    resolve(result)
                },
                    error => {
                        console.log('Error in POST createDonationArtifact')
                        console.log(error)
                        reject(error)
                    })
            })
        },
        // Creates a new donation request
        createDonationRequest: function () {
            axiosClient.post('donationRequest', {}, {
                params: {
                    clientUsername: this.clientId,
                    artifactId: this.newDonationArtifact.artifactId
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
        },
        // Template for Pop up b-modal from https://bootstrap-vue.org/docs/components/modal
        resetNewArtifactModal: function () {
            this.newDonationArtifact = {}
            this.newDonationArtifactName = ''
            this.newDonationArtifactImage = "MMS-backend/images/Donation.PNG"
            this.newDonationArtifactDescription = ''
            this.newDonationArtifactWorth = 0
            this.newDonationArtifactIsDamaged = 'Good'
        },
        // Check if all inputs to create a new donation artifact are valid
        checkNewArtifactFormValidity() {
            return this.newDonationArtifactName.trim().length > 0 && 
            this.newDonationArtifactDescription.trim().length > 0 && 
            parseInt(this.newDonationArtifactWorth) > 0 
        },
        // Create donation request if all inputs are valid
        handleSubmitNewDonationArtifact: async function () {
            if (!this.checkNewArtifactFormValidity()) {
                return
            }
            this.newDonationArtifact = await this.createDonationArtifact()
            this.createDonationRequest()
            this.$nextTick(() => {
                this.$bvModal.hide("new-artifact-donation-modal")
            })
        },
        // Wrapper function when Donate button is clicked
        handleOkNewArtifactModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitNewDonationArtifact()
        },
        // Handles dropdown menu for preserved state
        onChange: function (e){
            this.newDonationArtifactIsDamaged = e.target.value
        },
        // Gets details of the selected donation request and donated artifact
        sendInfo: function (request) {
            this.currentRequestId = request.currentRequestId
            this.currentDonationArtifactName = request.artifact.name
            this.currentDonationArtifactDescription = request.artifact.description
            this.currentDonationArtifactWorth = request.artifact.worth
            this.currentDonationArtifactIsDamaged = request.artifact.isDamaged
        }
    }
}