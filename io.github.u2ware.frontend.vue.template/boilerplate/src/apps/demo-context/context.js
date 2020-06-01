import Vue from "vue";
import axios from "axios";
import qs from 'qs';

////////////////////////////////////////////////////
//
////////////////////////////////////////////////////
const _api = process.env.VUE_APP_API_URI;

const _rest = {

    create(requestBody, uri) {
        return axios({
            method : 'post',
            url : _api + uri,
            data : requestBody
        });
    },

    read(requestUriOrBody) {
        var url = '';
        try{
            url = requestUriOrBody._links.self.href;
        }catch(e){
            url = _api + requestUriOrBody;
        }
        return axios({
            method : 'get',
            url : url
        });
    },
    update(requestBody, isPut) {
        return axios({
            method : isPut ? 'put' : 'patch',
            data : requestBody,
            url : requestBody._links.self.href
        });
    },
    delete(requestUriOrBody) {
        var url = '';
        try{
            url = requestUriOrBody._links.self.href;
        }catch(e){
            url = requestUriOrBody;
        }
        return axios({
            method : 'delete',
            url : url
        });
    }
};



const _context = {

    code : {
        types : ['A', 'B', 'C', 'D']
    },

    api : {
        create(uri, data){
            return axios({
                method : 'post',
                url : _api + uri,
                data :data,
            });
        },
        read(uri){
            return axios({
                method : 'get',
                url : _api + uri,
            });
        },
        update(uri, data, isPut){
            return axios({
                method : isPut == false ? 'patch' : 'put' ,
                url : _api + uri,
                data :data,
            });
        },
        delete(uri){
            return axios({
                method : 'delete',
                url : _api + uri,
            });
        },
        search(uri, data, params){
            return axios({
                method : 'post',
                url : _api + uri,
                headers: {
                    query: "true"
                },
                params : params,
                paramsSerializer: (params) => {
                    return qs.stringify(params, {arrayFormat: 'repeat'});
                },
                data :data,
            });
        }
    },

    users : {

        search(){
            return _rest.search;
        }
    }

}



const Context = {};
Context.install = function(Vue) {
    Vue.context = _context;
    window.context = _context;
    Object.defineProperties(Vue.prototype, {
        context: {
            get() {
                return _context;
            }
        },
        $context: {
            get() {
                return _context;
            }
        },
    });
};

Vue.use(Context)

export default Context;
