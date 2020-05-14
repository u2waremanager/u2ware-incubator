<template>
	<div id="app">
		<img alt="Vue logo" src="../../assets/logo.png" />

    <h1>Demo1</h1>
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

		<v-divider></v-divider>

    <h1>Demo2</h1>
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

		<v-divider></v-divider>
    <h1>Demo3</h1>

		<v-row>
      <custom-comp :myprop1="value1"></custom-comp>
      <v-btn @click="changeProperty()">changeProperty</v-btn>

    </v-row>
	</div>
</template>


<script>
import CustomInput from "./components/CustomInput.vue";
import CustomChild from "./components/CustomChild.vue";
import CustomComp from "./components/CustomComp.vue";

export default {
	name: "demo-component",
	components: {
		CustomInput,
		CustomChild,
		CustomComp,
	},
	data: () => ({
		custom1: "bbbbb",
    fromChildEvent: null,
    value1 : null
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
    },
    changeProperty(){
      this.value1 = new Date()+' ';
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
