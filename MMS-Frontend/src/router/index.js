import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/login',
      name: 'Login',
      component: AccountLogin
    },
    {
      path: '/signup',
      name: 'SignUp',
      component: AccountSignUp
    }
  ]
})
