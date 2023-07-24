import Vue from 'vue'
import VueRouter from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from '../views/HomeView.vue'
import WelcomeView from '../views/WelcomeView.vue'
import CustomerView from '../views/CustomerView.vue'
import PasswordChangeView from '../views/PasswordChangeView.vue'
import GoodsView from '../views/GoodsView.vue'
import ShoppingCartView from '../views/ShoppingCartView.vue'
import OrderHistoryView from '../views/OrderHistoryView.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    component: LoginView
  },
  {
    path: '/',
    redirect: '/login'
  }, {
    path: '/home',
    component: HomeView,
    redirect: '/home/welcome',
    children: [{
      path: 'welcome',
      component: WelcomeView
    }, {
      path: '/customer',
      component: CustomerView
    }, {
      path: '/customer/changePassword',
      component: PasswordChangeView
    }, {
      path: '/goods',
      component: GoodsView
    }, {
      path: '/shoppingcart',
      component: ShoppingCartView
    }, {
      path: '/orderhistory',
      component: OrderHistoryView
    }]
  }
]

const router = new VueRouter({
  routes
})

export default router
