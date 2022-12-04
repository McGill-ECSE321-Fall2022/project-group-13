/**
 * @author Yu An Lu (yu-an-lu)
 * 
 */
 import axios from 'axios'
 var config = require('../../../config')
 var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
 var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
 
 var axiosStaff = axios.create({
     baseURL: backendUrl,
     headers: { 'Access-Control-Allow-Origin': frontendUrl }
 })

 export default {
    name: 'ManageDonationRequests',
    data() {
        return {
            employeeId: '',
            requests: [],
            currentRequestId: '',
            currentDonationArtifactName: '',
            currentDonationArtifactDescription: '',
            currentDonationArtifactWorth: '',
            currentDonationArtifactIsDamaged: false,
            storageRoomId: ''
        }
    },
    created: function () {
        // Get employeeId from session storage
        let username = sessionStorage.getItem('loggedInEmployee')
        // Initializing all donation requests from the backend
        axiosStaff.get('donationRequests')
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
        getStorageRoom: function () {
            // Export this function scope to promise
            const self = this

            return new Promise(function (resolve, reject) {
                axiosStaff.get('mms/rooms/type/Storage')
                    .then(response => {
                        var result = response.data[0].roomId
                        console.log(result)
                        console.log(response)
                        resolve(result)
                    },
                        e => {
                        console.log('Error in GET mms/rooms/type/Storage')
                        console.log(e)
                        reject(e)
                    })
            })
        },
        approveDonationRequest: function (request) {
            axiosStaff.put('donationRequest/approveRequest/' + request.requestId, {}, {
                params: {
                    roomId: this.storageRoomId
                }
            })
            .then(response => {
                console.log(response)
                window.location.reload()
            })
            .catch(e => {
                console.log('Error in PUT approveDonationRequest')
                console.log(e)
            })
        },
        rejectDonationRequest: function (request) {
            axiosStaff.put('donationRequest/rejectRequest/' + request.requestId)
            .then(response => {
                console.log(response)
                window.location.reload()
            })
            .catch(e => {
                console.log('Error in PUT donationRequest/rejectRequest/' + request.requestId)
                console.log(e)
            })
        },
        deleteRejectedDonationRequest: function (request) {
            axiosStaff.delete('donationRequest/delete/' + request.requestId)
            .then(response => {
                console.log(response)
                window.location.reload()
            })
            .catch(e=>  {
                console.log('Error in DELETE deleteRejectedDonationRequest')
                console.log(e)
            })
        },
        approveButton: async function (request) {
            this.storageRoomId = await this.getStorageRoom()
            this.approveDonationRequest(request)
        },
        sendInfo: function (request) {
            this.currentRequestId = request.currentRequestId
            this.currentDonationArtifactName = request.artifact.name
            this.currentDonationArtifactDescription = request.artifact.description
            this.currentDonationArtifactWorth = request.artifact.worth
            this.currentDonationArtifactIsDamaged = request.artifact.isDamaged
        }
    }
 }