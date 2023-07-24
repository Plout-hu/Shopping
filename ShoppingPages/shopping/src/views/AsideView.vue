<template>
    <el-aside :width="collapse ? '64px' : '200px'" class="el-asider">
        <div class="fold_button">
            <i v-if="this.collapse == false" class="el-icon-s-unfold" @click="foldAndUnfold"></i>
            <i v-else class="el-icon-s-fold" @click="foldAndUnfold"></i>
        </div>
        <el-menu background-color="#333744" text-color="#fff" class="el-menu-vertical-demo" :collapse="this.collapse"
            @open="handleOpen" @close="handleClose" :collapse-transition="false" router
            :default-active="this.$router.history.current.path">
            <el-submenu index="1">
                <!-- 一级菜单的模板区 -->
                <template slot="title">
                    <!-- 一级菜单图标 -->
                    <i class="el-icon-user"></i>
                    <!-- 一级菜单文本 -->
                    <span v-if="right == 1">客户管理</span>
                    <span v-else>账户管理</span>
                </template>
                <!-- 二级菜单 -->
                <el-menu-item index="/customer/changePassword">
                    <!-- 二级菜单图标 -->
                    <i class="el-icon-edit"></i>
                    <!-- 二级菜单文本 -->
                    <span>修改密码</span>
                </el-menu-item>
                <el-menu-item v-if="right == 1" index="/customer">
                    <!-- 二级菜单图标 -->
                    <i class="el-icon-menu"></i>
                    <!-- 二级菜单文本 -->
                    <span>客户列表</span>
                </el-menu-item>
            </el-submenu>
            <el-menu-item index="/goods">
                <!-- 一级菜单的模板区 -->

                <!-- 一级菜单图标 -->
                <i class="el-icon-goods"></i>
                <!-- 一级菜单文本 -->
                <span slot="title">商品</span>

            </el-menu-item>
            <el-menu-item index="/shoppingcart" v-if="right == 0">

                <!-- 一级菜单图标 -->
                <i class="el-icon-shopping-cart-2"></i>
                <!-- 一级菜单文本 -->
                <span slot="title">购物车</span>

            </el-menu-item>
            <el-menu-item index="/orderhistory" v-if="right == 0">

                <!-- 一级菜单图标 -->
                <i class="el-icon-s-order"></i>
                <!-- 一级菜单文本 -->
                <span slot="title">历史订单</span>

            </el-menu-item>
        </el-menu>
    </el-aside>
</template>

<script>
export default {
    data() {
        return {
            collapse: false
        }
    },
    props: ["right"],
    methods: {
        foldAndUnfold() {
            this.collapse = !this.collapse;
        },
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        }
    }
}
</script>

<style lang="less" scoped>
.el-menu {
    border-right: 0px;
}

.el-asider {
    background-color: #333744;
}

i {
    margin-right: 10px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
}

.fold_button {
    text-align: right;
    margin-right: 15px;
    font-size: large;
    color: gray;
}
</style>