
const scheduller = {};

export default {
    startup : (name, timeout, func)=>{
        func();
        scheduller[name] = setInterval(func, timeout);
        return true;
    },
    shutdown : (name)=>{
        if(scheduller[name] != null && scheduller[name] != undefined){
            clearInterval(scheduller[name]);
            delete scheduller.name;
        }
        return false;
    },
    isRunning : (name)=>{
        return scheduller[name] != null && scheduller[name] != undefined;
    }
}