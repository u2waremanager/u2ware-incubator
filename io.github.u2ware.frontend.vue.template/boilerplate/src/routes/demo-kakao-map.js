export default [
    {
        path: '/demo-kakao-map',
        name: 'demo-kakao-map',
        component: () => import(/* webpackChunkName: "demo-kakao-map" */ '../apps/demo-kakao-map/App.vue')
    },
]