export default [
{
    path: '/samples',
    name: '/samples',
    component: () => import(/* webpackChunkName: "/samples" */ '@/samples/App.vue'),
    children : [
        {
            path: '/samples',
            name: '/samples/home',
            component: () => import(/* webpackChunkName: "/samples/home" */ '@/samples/views/Home.vue')
        },
        {
            path: '/samples/about',
            name: '/samples/about',
            component: () => import(/* webpackChunkName: "/samples/about" */ '@/samples/views/About.vue')
        },
        {
            path: '/samples/i18n',
            name: '/samples/i18n',
            component: () => import(/* webpackChunkName: "/samples/i18n" */ '@/samples/views/i18n.vue')
        },
        {
            path: '/samples/axios',
            name: '/samples/axios',
            component: () => import(/* webpackChunkName: "/samples/axios" */ '@/samples/views/axios.vue')
        },
        {
            path: '/samples/moment',
            name: '/samples/moment',
            component: () => import(/* webpackChunkName: "/samples/moment" */ '@/samples/views/moment.vue')
        },
        {
            path: '/samples/env',
            name: '/samples/env',
            component: () => import(/* webpackChunkName: "/samples/env" */ '@/samples/views/env.vue')
        }
    ]
},
]
  