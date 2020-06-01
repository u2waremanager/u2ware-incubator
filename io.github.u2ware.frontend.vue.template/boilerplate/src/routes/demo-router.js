import Home from '../apps/demo-router/views/Home.vue'

export default [

    {
        path: '/demo-router',
        name: 'App',
        component: () => import(/* webpackChunkName: "demo-router" */ '../apps/demo-router/App.vue'),
        children : [

            {
                path: '/demo-router',
                name: 'Home',
                component: Home
              },
              {
                path: '/demo-router/about',
                name: 'About',
                // route level code-splitting
                // this generates a separate chunk (about.[hash].js) for this route
                // which is lazy-loaded when the route is visited.
                component: () => import(/* webpackChunkName: "demo-router-about" */ '../apps/demo-router/views/About.vue')
              }
            
        ]
   },
    
    

]