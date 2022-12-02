import Vue from 'vue'
import Router from 'vue-router'
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'
import MyDonationRequests from '@/components/MyDonationRequests'
import MyLoanRequests from '@/components/MyLoanRequests'
import MySchedule from '@/components/MySchedule'
import ViewSchedules from '@/components/ViewSchedules'
import Client from '@/components/Client'
import Employee from '@/components/Employee'
import Manager from '@/components/Manager'


Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'Login',
      component: AccountLogin
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: AccountSignUp
    },
    {
      path: '/client',
      name: 'Client',
      component: Client,
      children: [
        {
          path: 'mydonationrequests',
          name: 'client_mydonationrequests',
          component: MyDonationRequests
        },
        {
          path: 'myloanrequests',
          name: 'client_myloanrequests',
          component: MyLoanRequests
        }
      ]
    }, 
    {
      path: '/employee',
      name: 'Employee',
      component: Employee,
      children: [
        {
          path: 'myschedule',
          name: 'employee_myschedule',
          component: MySchedule
        }
      ]
    }, 
    {
      path: '/manager',
      name: 'Manager',
      component: Manager,
      children: [
        {
          path: 'viewschedules',
          name: 'manager_viewschedules',
          component: ViewSchedules
        }
      ]
    }
  ]
})


