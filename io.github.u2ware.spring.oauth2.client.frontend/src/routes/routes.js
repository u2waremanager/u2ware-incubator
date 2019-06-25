export default [
    {
        path: '/',
        name: 'oauth2login',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "oauth2" */ '../apps/oauth2/OAuth2Login.vue'), 
    },
    {
        path: '/callback',
        name: 'oauth2callback',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "oauth2callback" */ '../apps/oauth2/OAuth2Callback.vue'), 
    },

]
