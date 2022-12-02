import Vue from 'vue'
import Router from 'vue-router'
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'
import MyDonationRequests from '@/components/MyDonationRequests'
import MyLoanRequests from '@/components/MyLoanRequests'
import Client from '@/components/Client'

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
          name: 'clieant_mydonationrequests',
          component: MyDonationRequests
        },
        {
          path: 'myloanrequests',
          name: 'client_myloanrequests',
          component: MyLoanRequests
        }
      ]
    }
  ]
})


