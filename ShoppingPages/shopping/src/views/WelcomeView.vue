<template>
  <div class="jianbian">

    <router-view></router-view>
    <h1 v-if="power == 1">欢迎您进入后台管理系统</h1>
    <h1 v-if="power == 0">欢迎您进入云大文创购物系统</h1>
    <!-- 内容区域 -->
    <!-- <img src="../assets/src=http___p4.itc.cn_q_70_images01_20210506_628477c0733b44ac898ed640b2e473c1.jpeg&refer=http___p4.itc.webp" alt class="tupian-img" /> -->
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  props: ["power"],
  created() { },
  mounted() { },
  methods: {},
};
</script>

<style scoped>
h1 {
  overflow: hidden;
  font-size: 80px;
  width: 0;
  height: 150px;
  line-height: 150px;
  margin: 100px auto;
  color: #fff;
  /* background: lightcoral; */
  white-space: nowrap;
  animation: w 4s steps(13) forwards;
  text-align: center;
}

@keyframes w {
  0% {
    width: 0;
  }

  100% {
    width: 1100px;
  }
}

.tupian-img {
  width: 1100px;
  height: 500px;
  /* margin-top:20px; */
}

.el-card {
  width: 100%;
  height: 500px;
}

.jianbian {
  height: 100%;
  width: 100%;
  /* margin: 0;
            padding: 0; */
  font-family: "montserrat";
  background-image: linear-gradient(45deg,
      #2c3e50,
      #27ae60,
      #2980b9,
      #e74c3c,
      #8e44ad);
  background-size: 400%;
  animation: bganimation 15s infinite;
  margin-top: -100px;
}

@keyframes bganimation {
  0% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }

  100% {
    background-position: 0% 50%;
  }
}</style>

