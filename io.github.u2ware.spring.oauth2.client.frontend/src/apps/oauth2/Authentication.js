"use strict";
import Vue from 'vue'

const Authentication = {

    clear() {
        Vue.$log.debug('Authentication', 'clear');
        localStorage.removeItem('clientRegistrationId');
        localStorage.removeItem('principalName');
        localStorage.removeItem('accessToken');
        localStorage.removeItem('idToken');
    },

    load() {
        Vue.$log.debug('Authentication', 'load');
        return {
            'clientRegistrationId' : localStorage.clientRegistrationId,
            'principalName' : localStorage.principalName,
            'accessToken' : localStorage.accessToken,
            'idToken' : localStorage.idToken,
        };
    },

    save(auth) {
        Vue.$log.debug('Authentication', 'save', auth);
        localStorage.setItem('clientRegistrationId', auth.clientRegistrationId);
        localStorage.setItem('principalName', auth.principalName);
        localStorage.setItem('accessToken', auth.accessToken);
        localStorage.setItem('idToken', auth.idToken);
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