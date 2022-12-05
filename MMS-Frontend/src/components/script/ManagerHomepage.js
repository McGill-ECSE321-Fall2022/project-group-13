import axios from 'axios';
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var axiosManager = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'ManagerHomepage',
    data(){
        return {
            oldTicketFee: '',
            newTicketFee: '',

            newOpenHours: [],
            newOpenTime: '',
            newCloseTime: '',
            oldOpenTime: '',
            oldCloseTime: '',

            updatedSpecificDay: [],
            selectedDay: 'PickDay',
            selectedStatus: 'PickStatus',
            errorDay: '',

            showErrorAlert: false,
            errorData: ''
        }
    }, 
    created(){
        axiosManager.get('/mms/getMms')
                .then(response => {
                    this.oldTicketFee = response.data.ticketFee;
                })
                .catch(error => {
                    console.log(error);
        })
    },
    methods: {
    updateTicketFee: function() {
        axiosManager.put('/mms/fee',{},{params: {
            ticketFee: this.newTicketFee
        }
        }).then(response => {
            console.log(response)
        })
        .catch(error => {
            console.log(error);
        })
    },
    resetEditTicketFeeModal: function () {
        this.newTicketFee = 0
    },
    handleOkEditTicketFeeModal: function (bvModalEvent) {
        bvModalEvent.preventDefault()
        this.handleSubmitTicketFee()
    },
    handleSubmitTicketFee: async function () {
        if (!this.checkTicketEditFormValidity()) {
            return
        }
        await this.updateTicketFee()
        this.$nextTick(() => {
            this.$bvModal.hide("edit-ticket-fee-modal")
        })
        window.location.reload();
    },
    checkTicketEditFormValidity() {
        return this.newTicketFee.trim().length > 0 && parseInt(this.newTicketFee) >= 0
    },
    updateHours: function() {
        const self= this;
        return new Promise(function (resolve, reject) {
        axiosManager.put('/mms/setOpeningHours/', {},{
            params: {
                openTime: self.newOpenTime,
                closeTime: self.newCloseTime
            }
        })        
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in PUT /mms/setOpeningHours/')
            console.log(e)
            if(e.response.data == "Opening time cannot be after closing time."){
                self.errorData = e.response.data
                self.showErrorAlert = true
            }else if (e.response.status == 500 || e.response.status == 400) {
                self.errorData = "Wrong Input Format"
                self.showErrorAlert = true
            }
            reject(e)
        })
      })
    },
    resetUpdateHoursModal: function () {
            this.newOpenHours = []
            this.newOpenTime = ''
            this.newCloseTime = ''
            this.showErrorAlert = false
        },
    checkUpdateHoursFormValidity() {
            return this.newOpenTime.trim().length > 0 && 
            this.newCloseTime.trim().length > 0 
        },
    handleSubmitOpeningHours: async function () {
            if (!this.checkUpdateHoursFormValidity()) {
                return
            }
            this.showErrorAlert = false
            this.newOpenHours = await this.updateHours()
            this.$nextTick(() => {
                this.$bvModal.hide("update-hours-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkUpdateHoursModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitOpeningHours()
        },
    updateDayStatus: function() {
        const self= this;
        return new Promise(function (resolve, reject) {
            axiosManager.put('/specificWeekDay', {},{
                params: {
                    day: self.selectedDay,
                    isClosed: self.selectedStatus
                }
            })        
        .then(response => {
            var result = response.data
            console.log(response)
            resolve(result)
        },
          e => {
            console.log('Error in PUT /specificWeekDay/')
            console.log(e)
            if(e.response.data == "Cannot close day with that has scheduled shifts."){
                self.errorData = "Cannot close day that has scheduled shifts."
                self.errorDay = self.selectedDay
                self.showErrorAlert = true
            }
            reject(e)
        })

        })
    },
    onChangeDay(e){
          this.selectedDay = e.target.value;
    },
    onChangeStatus(e){
          this.selectedStatus = e.target.value;
    },
    resetUpdateDayModal: function () {
            this.updatedSpecificDay = []
            this.selectedDay = 'PickDay'
            this.selectedStatus = 'PickStatus'
            this.showErrorAlert = false
            this.errorDay = ''
        },
    checkUpdateDayFormValidity() {
            return this.selectedDay != "PickDay" && 
           this. selectedStatus != "PickStatus"
        },
    handleSubmitDayStatus: async function () {
            if (!this.checkUpdateDayFormValidity()) {
                return
            }
            this.showErrorAlert = false
            this.updatedSpecificDay = await this.updateDayStatus()
            this.$nextTick(() => {
                this.$bvModal.hide("update-day-modal")
            })
            //reload page to show updated table
            window.location.reload();
        },
    handleOkUpdateDayModal: function (bvModalEvent) {
            bvModalEvent.preventDefault()
            this.handleSubmitDayStatus()
        },
    deleteShifts: function(){
        axiosManager.delete('/shift/day', {
            params: {
                dayType: this.errorDay
            }
        })
        .then(response => {
            console.log(response)
        })
        .catch(e => {
            console.log('Error in DELETE /shift/day')
            console.log(e)
        })
        this.showErrorAlert = false
    }
    }
}