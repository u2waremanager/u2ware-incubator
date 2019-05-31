<template>
<v-app id="inspire" dark>

    <v-navigation-drawer fixed app right
        v-model="drawerRight" 
        :clipped="$vuetify.breakpoint.lgAndUp" >
        <v-container grid-list-md text-xs-center>
            <v-layout row wrap>
                <v-flex xs12>
                    <v-card dark color="primary">
                        Right
                    </v-card>
                </v-flex>
            </v-layout>
        </v-container>
    </v-navigation-drawer>


    <v-navigation-drawer fixed app
        v-model="drawer"
        :clipped="$vuetify.breakpoint.lgAndUp">
        <v-container grid-list-md text-xs-center>
            <v-layout row wrap>
                <v-flex xs12>
                    <v-card dark color="primary">
                        Left
                    </v-card>
                </v-flex>
            </v-layout>
        </v-container>
    </v-navigation-drawer>


    <v-toolbar fixed app 
        :clipped-left="$vuetify.breakpoint.lgAndUp" 
        :clipped-right="$vuetify.breakpoint.lgAndUp">
        <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
        <v-toolbar-title>X1</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-side-icon> <v-icon>flash_on</v-icon></v-toolbar-side-icon>
        <v-toolbar-side-icon @click.stop="drawerRight = !drawerRight"></v-toolbar-side-icon>
    </v-toolbar>


    <v-content id="content">
      <v-container fluid fill-height >
            <v-layout justify-center align-center>
                <v-flex xs12>
                    <v-card dark color="primary">
                        Content
                    </v-card>
                </v-flex>
            </v-layout>
      </v-container>

        <v-speed-dial
            v-model="fab"

            :top="top"
            :right="right"
            :bottom="bottom"
            :left="left"

            :direction="direction"
            :open-on-hover="hover"
            :transition="transition"
        >
            <template v-slot:activator>
                <v-btn fab color="blue darken-2" dark>
                <v-icon>account_circle</v-icon>
                <!-- <v-icon>close</v-icon>-->
                </v-btn>
            </template>
            <v-btn fab dark color="green">
                <v-icon>edit</v-icon>
            </v-btn>
            <v-btn fab dark color="indigo">
                <v-icon>add</v-icon>
            </v-btn>
            <v-btn fab dark color="red">
                <v-icon>delete</v-icon>
            </v-btn>
        </v-speed-dial> 

    </v-content>
</v-app>  
</template>

<script>
export default {
    data: () => ({
        drawer: null,
        drawerRight: null,

        direction: 'bottom',
        fab: false,
        fling: false,
        hover: false,
        tabs: null,
        top: true,
        bottom: false,
        right: true,
        left: false,
        transition: 'slide-y-reverse-transition' 
    }),
}
</script>

<style>
  #content .v-speed-dial {
    position: absolute;
  }

  #content .v-btn--floating {
    position: relative;
  }
</style>
