<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png" />
    <v-row>
      <v-col cols="12">
        <v-card class="mx-auto">
          <v-card-text>
            <custom-input v-model="custom1"></custom-input>
            <pre>{{custom1}}</pre>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      
      <v-col cols="6">
        <v-card class="mx-auto">
          <v-card-text>
            <div>Parent Area</div>
            <p>
              <v-btn @click="toChild">To Child</v-btn>
            </p>
            <p>fromChildEvent</p>
            <pre>{{fromChildEvent ? fromChildEvent.message : ''}}</pre>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="6">
        <v-card class="mx-auto">
          <v-card-text>
            <div>Child Area</div>
            <p>
              <custom-child ref="child" v-on:toParent="fromChild"></custom-child>
            </p>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>


<script>
import CustomInput from "./components/CustomInput.vue";
import CustomChild from "./components/CustomChild.vue";

export default {
  name: "demo-component",
  components: {
    CustomInput,
    CustomChild
  },
  data: () => ({
    custom1: "bbbbb",
    fromChildEvent: null
  }),
  methods: {
    toChild(event) {
      event.message = new Date();
      console.log("Parent", "toChild", event);
      this.$refs.child.fromParent(event);
    },
    fromChild(event) {
      console.log("Parent", "fromChild", event);
      this.fromChildEvent = event;
    }
  },
  created: function() {
    console.log(this.$options.name, "created");
  },
  mounted: function() {
    console.log(this.$options.name, "mounted");
  },
  updated: function() {
    console.log(this.$options.name, "updated");
  },
  destroyed: function() {
    console.log(this.$options.name, "destroyed");
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
