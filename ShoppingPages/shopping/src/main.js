import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import "./assets/css/global.css"
import axios from 'axios'
axios.defaults.baseURL='http://localhost:8081/'
Vue.prototype.$http=axios

Vue.config.productionTip = false
axios.interceptors.request.use(config=>{
  //1.1 获取到浏览器里面一直存储的token，并将它放到
  let token =  localStorage.getItem("token");
  if(token){
  //1.2 注意：给请求头里面添加u-token（后台判断就是取的这个请求头）请求头，并把随机数的token值也设置进去
      config.headers['token']=token;
  }
  //1.3 必须要返回这个，才能执行后面代码
  return config;
},error => {
  Promise.reject(error);
});

axios.interceptors.response.use(result=>{
    let data = result.data;
    //只要前台被拦截的请求里面含这两个参数，那么就跳转到登录界面
    if(!data.success && data.msg==="NOT_LOGIN")
        this.$router.push('/login')
    return result;
},error => {
    Promise.reject(error);
});
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
