import Vue from 'vue'
import Router from 'vue-router'
import Client from '@/components/Client'
import Employee from '@/components/Employee'
import Manager from '@/components/Manager'
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'
import ClientHomepage from '@/components/ClientHomepage'
import EmployeeHomepage from '@/components/EmployeeHomepage'
import ManagerHomepage from '@/components/ManagerHomepage'
import MyDonationRequests from '@/components/MyDonationRequests'
import MyLoanRequests from '@/components/MyLoanRequests'
import MyTickets from '@/components/MyTickets'
import TicketPurchase from '@/components/TicketPurchase'
import BrowseCatalog from '@/components/BrowseCatalog'
import ClientSpecificArtifact from '@/components/ClientSpecificArtifact'
import ClientAccount from '@/components/ClientAccount'
import EmployeeAccount from '@/components/EmployeeAccount'
import ManageCatalog from '@/components/ManageCatalog'
import ManageLoanRequests from '@/components/ManageLoanRequests'
import ManageTickets from '@/components/ManageTickets'
import ManagerAccount from '@/components/ManagerAccount'
import ManageEmployees from '@/components/ManageEmployees'
import EditSchedule from '@/components/EditSchedule'
import MySchedule from '@/components/MySchedule'
import ViewSchedules from '@/components/ViewSchedules'
import ManageDonationRequests from '@/components/ManageDonationRequests'


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
          path: 'myhomepage',
          name: 'client_myhomepage',
          component: ClientHomepage
        },
        {
          path: 'myaccount',
          name: 'client_myaccount',
          component: ClientAccount
        },
        {
          path: 'mydonationrequests',
          name: 'client_mydonationrequests',
          component: MyDonationRequests
        },
        {
          path: 'myloanrequests',
          name: 'client_myloanrequests',
          component: MyLoanRequests
        },
        {
          path: 'mytickets',
          name: 'client_mytickets',
          component: MyTickets
        },
        {
          path: 'ticketpurchase',
          name: 'client_ticketpurchase',
          component: TicketPurchase
        },
        {
          path: 'browsecatalog',
          name: 'client_browsecatalog',
          component: BrowseCatalog
        },
        {
          path: 'specificartifact',
          name: 'client_specificartifact',
          component: ClientSpecificArtifact
        }
      ]
    }, 
    {
      path: '/employee',
      name: 'Employee',
      component: Employee,
      children: [
        {
          path: 'myhomepage',
          name: 'employee_myhomepage',
          component: EmployeeHomepage
        },
        {
          path: 'myaccount',
          name: 'employee_myaccount',
          component: EmployeeAccount
        },
        {
          path: 'myschedule',
          name: 'employee_myschedule',
          component: MySchedule
        },
        {
          path: 'managedonationrequests',
          name: 'employee_managedonationrequests',
          component: ManageDonationRequests
        },
        {
          path: 'managecatalog',
          name: 'employee_managecatalog',
          component: ManageCatalog
        },
        {
          path: 'manageloanrequests',
          name: 'employee_manageloanrequests',
          component: ManageLoanRequests
        },
        {
          path: 'managetickets',
          name: 'employee_managetickets',
          component: ManageTickets
        },
      ]
    }, 
    {
      path: '/manager',
      name: 'Manager',
      component: Manager,
      children: [
        {
          path: 'myhomepage',
          name: 'manager_myhomepage',
          component: ManagerHomepage
        },
        {
          path: 'myaccount',
          name: 'manager_myaccount',
          component: ManagerAccount
        },
        {
          path: 'viewschedules',
          name: 'manager_viewschedules',
          component: ViewSchedules
        },
        {
          path: 'managedonationrequests',
          name: 'manager_managedonationrequests',
          component: ManageDonationRequests
        },
        {
          path: 'managecatalog',
          name: 'manager_managecatalog',
          component: ManageCatalog
        },
        {
          path: 'manageloanrequests',
          name: 'manager_manageloanrequests',
          component: ManageLoanRequests
        },
        {
          path: 'managetickets',
          name: 'manager_managetickets',
          component: ManageTickets
        },
        {
          path: 'manageemployees',
          name: 'manager_manageemployees',
          component: ManageEmployees
        },
        {
          path: 'editschedule',
          name: 'manager_editsschedule',
          component: EditSchedule
        }
      ]
    }
  ]
})


