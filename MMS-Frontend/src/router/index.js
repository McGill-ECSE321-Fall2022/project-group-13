import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'
import MyDonations from '@/components/MyDonations'

Vue.use(Router)

export default new Router({
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
    }
    ,
    {
      path: '/client/mydonations',
      name: 'MyDonations',
      component: MyDonations
    }
  ]
})
