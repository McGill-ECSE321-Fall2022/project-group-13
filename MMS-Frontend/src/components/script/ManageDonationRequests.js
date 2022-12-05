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
            requests: [],
            currentRequestId: '',
            currentDonationArtifactName: '',
            currentDonationArtifactDescription: '',
            currentDonationArtifactWorth: '',
            currentDonationArtifactIsDamaged: false,
            storageRoomId: '',
            displayedStatus: 'All',
            clientId: '',
            showDismissibleAlert: false
        }
    },
    created: function () {
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
                console.log('Error in PUT donationRequest/approveRequest/' + request.requestId)
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
                console.log('Error in DELETE donationRequest/delete/' + request.requestId)
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
        },
        statusOnChange: function (e) {
            this.displayedStatus = e.target.value
            if (this.displayedStatus == 'Pending') {
                axiosStaff.get('donationRequests/withRequestStatus/' + this.displayedStatus)
                    .then(response => {
                        console.log(response)
                        this.requests = response.data
                    })
                    .catch(e => {
                        console.log('Error in GET donationRequests/withRequestStatus/Pending')
                        console.log(e)
                    })
            } else if (this.displayedStatus == 'Approved') {
                axiosStaff.get('donationRequests/withRequestStatus/' + this.displayedStatus)
                    .then(response => {
                        console.log(response)
                        this.requests = response.data
                    })
                    .catch(e => {
                        console.log('Error in GET donationRequests/withRequestStatus/Approved')
                        console.log(e)
                    })
            } else if (this.displayedStatus == 'Rejected') {
                axiosStaff.get('donationRequests/withRequestStatus/' + this.displayedStatus)
                    .then(response => {
                        console.log(response)
                        this.requests = response.data
                    })
                    .catch(e => {
                        console.log('Error in GET donationRequests/withRequestStatus/Rejected')
                        console.log(e)
                    })
            } else if (this.displayedStatus == 'All') {
                axiosStaff.get('donationRequests')
                    .then(response => {
                        console.log(response)
                        this.requests = response.data
                    })
                    .catch(e => {
                        console.log('Error in GET getAllDonationRequests')
                        console.log(e)
                    })
            }
            this.clientId = ''
        },
        getAllDonationRequestsByClient: function () {
            axiosStaff.get('donationRequests/ofClient/' + this.clientId)
                .then(response => {
                    console.log(response)
                    this.requests = response.data
                    this.displayedStatus='All'
                })
                .catch(e => {
                    console.log('Error in GET donationRequests/ofClient/' + this.clientId)
                    console.log(e)
                    if(e.response.data == "This username is invalid" || e.response.data == "This client account does not exist")
                    {
                        this.showAlert()
                    }
                })
        },
        showAlert: function () {
            this.showDismissibleAlert = true
        }
    }
 }