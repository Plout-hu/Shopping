<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>历史订单</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card>
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="订单起止日期">
                    <el-date-picker v-model="time" type="daterange" align="right" unlink-panels range-separator="至"
                        start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <el-table :data="orderList" border stripe>
                <el-table-column label="用户名" v-if="this.power == 1" prop="customerName"></el-table-column>
                <el-table-column label="商品名" align="center" prop="goodsName"></el-table-column>
                <el-table-column label="单价" align="center" prop="price"></el-table-column>
                <el-table-column label="数量" align="center" prop="goodsNumber"></el-table-column>
                <el-table-column label="总价" align="center">
                    <template slot-scope="scope">
                        {{ scope.row.price * scope.row.goodsNumber }}
                    </template>
                </el-table-column>
                <el-table-column label="订购时间" align="center" prop="orderTime">
                    <template slot-scope="scope">
                        {{ scope.row.orderTime | formatDate }}
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="searchInform.page" :page-sizes="[5, 10, 50, 100]" :page-size="searchInform.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="total" style="text-align: center;">
            </el-pagination>
        </el-card>
    </div>
</template>

<script>
export default {
    methods: {
        async getOrderList() {
            if (this.power === 0) {
                this.searchInform.username = this.username;
            }
            this.searchInform.start = this.time[0];
            this.searchInform.end = this.time[1];
            const { data: res } = await this.$http.get("/OrderHistory", { params: this.searchInform })
            if (res.code !== 1) {
                this.$message.error("请求历史订单失败")
                return
            }
            console.log(res)
            this.orderList = res.data.rows
        },
        handleSizeChange(newSize) {
            console.log(newSize);
            this.searchInform.pageSize = newSize;
            this.getOrderList();
        },
        handleCurrentChange(newPage) {
            console.log(newPage);
            this.searchInform.page = newPage;
            this.getOrderList();
        }
    },
    mounted() {
        this.getOrderList();
    },
    data() {
        return {
            orderList: [],
            searchInform: {
                page: null,
                pageSize: null,
                username: null,
                start: null,
                end: null
            },
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            time: [],
            total: null
        }
    }, filters: {
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
    },
    props: ['power', "username"]
}
</script>

<style></style>