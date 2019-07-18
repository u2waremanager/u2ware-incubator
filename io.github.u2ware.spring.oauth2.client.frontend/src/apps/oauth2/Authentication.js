"use strict";
import Vue from 'vue'

const Authentication = {

    clear() {
        Vue.$log.debug('Authentication', 'clear');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('idToken');
        localStorage.removeItem('clientRegistrationId');
        localStorage.removeItem('principalName');
    },

    load() {
        Vue.$log.debug('Authentication', 'load');
        return {
            'accessToken' : localStorage.accessToken,
            'idToken' : localStorage.idToken,
            'clientRegistrationId' : localStorage.clientRegistrationId,
            'principalName' : localStorage.principalName
        };
    },

    save(auth) {
        Vue.$log.debug('Authentication', 'save', auth);
        localStorage.setItem('accessToken', auth.accessToken);
        localStorage.setItem('idToken', auth.idToken);
        localStorage.setItem('clientRegistrationId', auth.clientRegistrationId);
        localStorage.setItem('principalName', auth.principalName);
    },

    isAuthenticated(){
        Vue.$log.debug('Authentication', 'isAuthenticated', "");
        return localStorage.accessToken != null;
    }
};

export default Authentication;





// export default {

//     removeStorage(){
//         alert('removeStorage');
//         Vue.$log.debug(this.$options.name, 'removeStorage');
//     },
//     loadStorage(){
//     },
//     savedStorage(){

//     }
// };