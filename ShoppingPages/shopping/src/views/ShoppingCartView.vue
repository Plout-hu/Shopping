<template>
    <div v-loading="loading">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>购物车</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card>
            <el-table :data="shoppingCart" border stripe @selection-change="handleSelectionChange">
                <el-table-column align="center" type="selection"></el-table-column>
                <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
                <el-table-column align="center" label="售价" prop="goodsPrice"></el-table-column>
                <el-table-column align="center" label="选购数量" prop="amount"></el-table-column>
                <el-table-column align="center" label="选购时间" prop="orderTime">
                    <template slot-scope="scope">
                        {{ scope.row.orderTime | formatDate }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="总价">
                    <template slot-scope="scope">
                        {{ scope.row.goodsPrice * scope.row.amount }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <el-button type="danger" icon="el-icon-delete" size="mini"
                            @click="deleteOrder(scope.row.id)"></el-button>

                    </template>
                </el-table-column>
            </el-table>
            <el-form :inline="true" class="form-container">
                <el-form-item>
                    <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteByIds">批量删除</el-button>
                </el-form-item>
                <el-form-item class="right-align">
                    <span>总价: ￥{{ totalPrice }}</span>
                    <el-button type="primary" size="mini" style="margin-left: 60px;" @click="buy(totalPrice)">购买</el-button>
                </el-form-item>
            </el-form>

        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            shoppingCart: [],
            queryInform: {
                page: null,
                pageSize: null,
                userId: null,
                goodsId: null
            },
            multipleSelection: [],
            selectedIds: [],
            totalPrice: 0,
            loading: false,
            userInfrom: {
                username: "",
                userId: ""
            }
        }
    },
    props: ['username', 'power', 'userId'],
    mounted() {
        this.getOrderList()
    },
    methods: {
        buy(totalPrice) {
            this.userInfrom.username = this.username;
            this.userInfrom.userId = this.userId;
            console.log(this.userInfrom);
            this.$confirm('请选择支付方式', '支付', {
                confirmButtonText: '微信支付',
                cancelButtonText: '支付宝支付',
                type: 'primary',
                center: true
            }).then(async () => {
                this.loading = true;
                setTimeout(() => {
                    this.loading = false;
                }, 2000);
                const { data: res } = await thi.$http.post("/shoppingcart/buy", this.userInfrom)
                if (res.code === 1) {
                    this.$message({
                        type: 'success',
                        message: '微信支付成功，一共消费' + totalPrice + "元"
                    });
                    this.getOrderList();
                } else {
                    this.$message.error("支付失败")
                }
            }).catch(async () => {
                this.loading = true;
                setTimeout(() => {
                    this.loading = false;
                }, 2000);
                const { data: res } = await this.$http.post("/shoppingcart/buy", this.userInfrom)
                if (res.code === 1) {
                    this.$message({
                        type: 'success',
                        message: '支付宝成功，一共消费' + totalPrice + "元"
                    });
                    this.getOrderList();
                } else {
                    this.$message.error("支付失败")
                }
            });
            
        },
        async getOrderList() {
            const { data: res } = await this.$http.get("/shoppingcart")
            if (res.code !== 1) {
                this.$message.error("获取购物车列表失败")
                return
            }
            console.log(res)
            this.shoppingCart = res.data.rows;
            for (let i = 0; i < this.shoppingCart.length; i++) {
                this.totalPrice += this.shoppingCart[i].goodsPrice * this.shoppingCart[i].amount;
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        }, deleteOrder(id) {
            this.$confirm('此操作将删除该商品, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteById(id);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        async deleteById(id) {
            console.log(this.userInform);
            const { data: res } = await this.$http.delete("shoppingcart/" + id)
            if (res.code !== 1) {
                this.$message.error("删除失败")
            } else {
                this.$message.success("删除成功")
                this.getOrderList()
            }
        },
        deleteByIds() {
            this.$confirm("此操作将删除选中商品, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {

                //用户点击确认按钮
                //1. 创建id数组, 从 this.multipleSelection 获取即可
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    this.selectedIds[i] = this.multipleSelection[i].id;
                }
                // console.log(this.selectedIds)
                //2. 发送AJAX请求
                this.deleteById(this.selectedIds);/* .then((resp) => { */
                //     if (resp.data.code == "1") {
                //         //删除成功
                //         this.$message.success("恭喜你，删除成功");
                //         this.page();
                //     } else {
                //         this.$message.error(resp.data.msg);
                //     }
                // });
            }).catch(() => {
                //用户点击取消按钮
                this.$message.info("已取消删除");
            });
        }
    },
    filters: {
        formatDate(value) {
            if (value && Array.isArray(value)) {
                let [year, month, day, hour, minute] = value;
                let second = value[5] || "00";
                if (second.length === 1) {
                    second = '0' + second.toString(); // 如果秒数为一位数，在前面添加零
                }
                return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
            }
            return "";
        }
    }
}
</script>

<style lang="less" scoped>
.form-container {
    display: flex;
    justify-content: space-between;
}

.right-align {
    margin-left: auto;
}
</style>