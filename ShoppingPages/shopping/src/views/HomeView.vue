<template>
    <el-container class="home-container">
        <el-header>
            <span class="title">易购之家购物平台</span>
            <el-button type="info" @click="logout">退出</el-button>
        </el-header>
        <el-container>
            <AsideView :right="this.inform['power']" />
            <el-main>
                <router-view :username="this.inform['username']" :power="this.inform['power']"
                    :userId="this.inform['id']"></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import AsideView from './AsideView'
export default {
    components: { AsideView },
    data() {
        return {
            inform: {}
        }
    },
    mounted() {
        this.getRight();
    }
    ,
    methods: {
        logout() {
            window.localStorage.clear();
            this.$router.push("/login");
        },
        async getRight() {
            const { data: res } = await this.$http.get("customer/inform");
            console.log(res.data)
            if (res.data) {
                this.inform = res.data;
            }
        }
    }
}
</script>

<style lang="less" scoped>
.home-container {
    height: 100%;
}

.el-header {
    background-color: #373d41;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #fff;
    font-size: 20px;

    >div {
        display: flex;
        align-items: center;

        span {
            margin-left: 15px;
        }
    }
}

.el-main {
    background-color: #eaedf1;
}
</style>
