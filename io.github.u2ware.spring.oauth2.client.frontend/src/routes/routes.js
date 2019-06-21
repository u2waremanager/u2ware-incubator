export default [
    {
        path: '/oauth2/login',
        name: 'oauth2login',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "oauth2" */ '../apps/oauth2/OAuth2Login.vue'), 
    },
    {
        path: '/oauth2/callback',
        name: 'oauth2callback',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "oauth2callback" */ '../apps/oauth2/OAuth2Callback.vue'), 
    },
    {
        path: '/oauth2/logon',
        name: 'oauth2logon',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "oauth2callback" */ '../apps/oauth2/OAuth2Logon.vue'), 
    },
    
]
