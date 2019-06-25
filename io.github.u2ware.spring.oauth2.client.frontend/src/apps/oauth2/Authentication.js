import Vue from 'vue'

export const Authentication = {

    clear() {
        Vue.$log.debug('Authentication', 'clear');
        localStorage.removeItem('token');
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('clientRegistrationId');
        localStorage.removeItem('principalName');
    },

    load() {
        Vue.$log.debug('Authentication', 'load');
        return {
            'token' : localStorage.token,
            'jwtToken' : localStorage.jwtToken,
            'clientRegistrationId' : localStorage.clientRegistrationId,
            'principalName' : localStorage.principalName
        };
    },
    save(auth) {
        Vue.$log.debug('Authentication', 'save', auth);
        localStorage.setItem('token', auth.token);
        localStorage.setItem('jwtToken', auth.jwtToken);
        localStorage.setItem('clientRegistrationId', auth.clientRegistrationId);
        localStorage.setItem('principalName', auth.principalName);
    }
};



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