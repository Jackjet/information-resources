import Vue from 'vue';
import Router from 'vue-router';
Vue.use(Router);

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
    // mode: 'history',
    routes: [{
        path: '/',
        name: 'Home',
        redirect: '/home',
        component: () => import('@/views/home')
    }, {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home')
    }, {
        path: '/catalogue',
        name: 'Catalogue',
        component: () => import('@/views/catalogue')
    }, {
        path: '/catalogue/details',
        name: 'CatalogueDetails',
        component: () => import('@/views/catalogue/details')
    }, {
        path: '/catalogue/apiApplay',
        name: 'CatalogueApiApplay',
        component: () => import('@/views/catalogue/apiApplay')
    }, {
        path: '/catalogue/apiDetails',
        name: 'CatalogueApiDetails',
        component: () => import('@/views/catalogue/apiDetails')
    }, {
        path: '/demand',
        name: 'Demand',
        component: () => import('@/views/demand')
    }, {
        path: '/relevant',
        name: 'Relevant',
        component: () => import('@/views/relevant')
    },
    {
        path: '/personalDateil',
        name: 'PersonalDateil',
        component: () => import('@/views/personal/dateil.vue')
    },
    {
        path: '/personal',
        name: 'Personal',
        redirect: '/personal/info',
        component: () => import('@/views/personal'),
        children: [

            {
                path: '/personal/modifyInfo',
                name: 'modifyInfo',
                component: () => import('@/views/personal/admin/modifyInfo.vue')
            }, {
                path: '/personal/info',
                name: 'PersonalInfo',
                component: () => import('@/views/personal/admin/info.vue')
            }, {
                path: '/personal/follow',
                name: 'PersonalFollow',
                component: () => import('@/views/personal/admin/follow.vue')
            }, {
                path: '/personal/demand',
                name: 'PersonalDemand',
                component: () => import('@/views/personal/admin/demand.vue')
            }, {
                path: '/personal/demandDetails',
                name: 'PersonalDemandDetails',
                component: () => import('@/views/personal/admin/demandDetails.vue')
            }, {
                path: '/personal/apiList',
                name: 'PersonalApiList',
                component: () => import('@/views/personal/admin/apiList.vue')
            }, {
                path: '/personal/apiListDetails',
                name: 'PersonalApiListDetails',
                component: () => import('@/views/personal/admin/apiListDetails.vue')
            }, {
                path: '/personal/agency',
                name: 'PersonalAgency',
                component: () => import('@/views/personal/admin/agency.vue')
            }, {
                path: '/personal/weApply',
                name: 'PersonalWeApply',
                component: () => import('@/views/personal/admin/weApply.vue')
            }, {
                path: '/personal/editPassword',
                name: 'PersonalEditPassword',
                component: () => import('@/views/personal/admin/editPassword.vue')
            },]
    }, {
        path: '/download',
        name: 'Download',
        component: () => import('@/views/download')
    },],
    scrollBehavior(to, from, save) {
        if (save) {
            return save
        } else {
            return { x: 0, y: 0 }
        }
    }
})

export default router;