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
            newDonationArtifact: {},
            newDonationArtifactName: '',
            newDonationArtifactImage: "MMS-backend/images/Donation.PNG",
            newDonationArtifactDescription: '',
            newDonationArtifactWorth: 0,
            newDonationArtifactIsDamaged: false
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
        createDonationArtifact: function () {
            // Export this function scope to promise
            const self = this
            return new Promise(function (resolve, reject) {
            axiosClient.post('donationArtifact', {}, {
                params: {
                    name: self.newDonationArtifactName,
                    image: self.newDonationArtifactImage,
                    description: self.newDonationArtifactDescription,
                    isDamaged: self.newDonationArtifactIsDamaged,
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
            // axiosClient.post('donationArtifact', {}, {
            //     params: {
            //         name: this.newDonationArtifactName,
            //         image: this.newDonationArtifactImage,
            //         description: this.newDonationArtifactDescription,
            //         isDamaged: this.newDonationArtifactIsDamaged,
            //         worth: this.newDonationArtifactWorth
            //     }
            // })
            //     .then(response => {
            //         console.log(response)
            //         this.newDonationArtifact = response.data.artifactId
            //     })
            //     .catch(e => {
            //         console.log('Error in POST createDonationArtifact')
            //         console.log(e)
            //     })
        },
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
            this.newDonationArtifactIsDamaged = false
        },
        checkNewArtifactFormValidity() {
            return this.newDonationArtifactName.trim().length > 0 && 
            this.newDonationArtifactDescription.trim().length > 0 && 
            parseInt(this.newDonationArtifactWorth) > 0 
        },
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
        handleOkNewArtifactModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitNewDonationArtifact()
        }
    }
}