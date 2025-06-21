import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SuperAgentView from '../views/SuperAgentView.vue'
import LoveMasterView from '../views/LoveMasterView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/super-agent',
    name: 'superAgent',
    component: SuperAgentView
  },
  {
    path: '/love-master',
    name: 'loveMaster',
    component: LoveMasterView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router