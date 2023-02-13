import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
export const constantRoutes = [
    {
        path: '/',
        component: Layout,
        redirect: '/test',
        meta: { title: '', icon: 'example' },
        children: [
            {
                path: 'test',
                name: 'test',
                component: () => import('@/views/test'),
                meta: { title: '菜单1', icon: 'user' },
            },
            {
                path: 'test2',
                name: 'test2',
                component: () => import('@/views/test2'),
                meta: { title: '菜单2', icon: 'user' },
            },
            {
                path: 'test3',
                name: 'test3',
                component: () => import('@/views/test3'),
                meta: { title: '菜单3', icon: 'user' },
            }
        ]
    },
    {
        path: 'external-link',
        component: Layout,
        children: [{
            path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
            meta: { title: 'External Link', icon: 'link' }
        }]
    },

    // 404 page must be placed at the end !!!
    // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
