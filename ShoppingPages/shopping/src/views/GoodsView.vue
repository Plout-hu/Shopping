<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>商品</el-breadcrumb-item>
        </el-breadcrumb>
        <el-card>
            <el-form :inline="true" :model="searchInform" class="demo-form-inline" :rules="searchRules">
                <el-form-item label="商品名">
                    <el-input v-model="searchInform.goodsName" placeholder="请输入商品名称" clearable @clear="getGoodsList"
                        style="width: 150px;" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="生产厂商">
                    <el-input v-model="searchInform.producer" placeholder="请输入生产厂商名称" @clear="getGoodsList" clearable
                        size="mini" style="width: 160px;"> </el-input>
                </el-form-item>
                <el-form-item label="价格区间">
                    <el-input v-model="searchInform.startPrice" placeholder="最低价格" size="mini"
                        style="width: 100px;"></el-input>
                </el-form-item>
                <el-form-item label="~" prop="endPrice">
                    <el-input v-model="searchInform.endPrice" placeholder="最高价格" size="mini"
                        style="width: 100px;"></el-input>
                </el-form-item>
                <el-form-item label="生产日期区间">
                    <el-date-picker v-model="tempDate" type="daterange" align="right" unlink-panels range-separator="至"
                        start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd" size="mini" style="width: 270px;" clearable @change="search">
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" size="mini">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" size="mini" @click="addGoods" v-if="power==1">添加商品</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="goods" border stripe @selection-change="handleSelectionChange">
                <el-table-column align="center" type="selection"></el-table-column>
                <el-table-column align="center" label="商品名称" prop="goodsName"></el-table-column>
                <el-table-column align="center" label="生产厂商" prop="producer"></el-table-column>
                <el-table-column align="center" label="生产日期" prop="produceDate">
                    <template slot-scope="scope">
                        {{ scope.row.produceDate | formatDate }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="分类" prop="type">
                    <template slot-scope="scope">
                        {{ typeMap[scope.row.type] }}
                    </template>
                </el-table-column>
                <el-table-column align="center" v-if="this.power == 1" label="进货价格" prop="buyingPrice"></el-table-column>
                <el-table-column align="center" label="售价" prop="price"></el-table-column>
                <el-table-column align="center" label="库存" prop="inventory"></el-table-column>
                <el-table-column align="center" label="选购数量">
                    <template slot-scope="scope">
                        <el-input-number v-model="pickNumber[scope.row.goodsId]" @change="handleChange" :min="0"
                            :max="scope.row.inventory" label="描述文字" size="mini"></el-input-number>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <el-tooltip class="item" effect="dark" content="删除该商品" placement="bottom" v-if="power == 1">
                            <el-button type="danger" @click="deleteGoods(scope.row.goodsId)" icon="el-icon-delete"
                                size="mini"></el-button>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" content="修改该商品" placement="bottom" v-if="power == 1">
                            <el-button type="primary" icon="el-icon-edit" size="mini"
                                @click="eiditGoods(scope.row.goodsId)"></el-button></el-tooltip>
                        <el-button type="primary" icon="el-icon-circle-plus" v-if="power == 0" size="mini"
                            @click="addToShoppingCart(scope.row)">添加到购物车</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="searchInform.page" :page-sizes="[5, 10, 50, 100]" :page-size="searchInform.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="this.total" style="text-align: center;">
            </el-pagination>
            <el-form :inline="true" class="demo-form-inline" style="text-align: right;">
                <el-form-item>
                    <el-button type="danger" icon="el-icon-delete" size="mini" v-if="power == 1"
                        @click="deleteByIds">批量删除</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-dialog title="添加商品" :visible.sync="addGoodsVisible" width="50%" center>
            <el-form :model="addGoodsInform" label-width="80px" :rules="goodsInputRule" ref="goodsInputRef">
                <el-form-item label="商品名" prop="goodsName">
                    <el-input v-model="addGoodsInform.goodsName" placeholder="请输入商品名称"></el-input>
                </el-form-item>
                <el-form-item label="厂家" prop="producer">
                    <el-input v-model="addGoodsInform.producer" placeholder="请输入厂家名"></el-input>
                </el-form-item>
                <el-form-item label="生产日期" prop="produceDate">
                    <el-date-picker v-model="addGoodsInform.produceDate" type="date" placeholder="选择生产日期"
                        value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="商品类型" prop="type">
                    <el-select v-model="addGoodsInform.type" placeholder="选择商品类型">
                        <el-option v-for="(label, value) in typeMap" :key="value" :label="label" :value="value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="进货价" prop="buyingPrice">
                    <el-input v-model="addGoodsInform.buyingPrice" placeholder="请输入商品进货价格"> </el-input>
                </el-form-item>
                <el-form-item label="售价" prop="price">
                    <el-input v-model="addGoodsInform.price" placeholder="请输入商品的售价"></el-input>
                </el-form-item>
                <el-form-item label="库存" prop="inventory">
                    <el-input-number v-model="addGoodsInform.inventory" :min="0" label="库存"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addCanceled">取 消</el-button>
                <el-button type="primary" @click="addConfirm">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="修改商品" :visible.sync="editGoodsVisible" width="50%" center>
            <el-form :model="addGoodsInform" label-width="80px" :rules="goodsInputRule" ref="goodsEditRef">
                <el-form-item label="商品名" prop="goodsName">
                    <el-input v-model="addGoodsInform.goodsName" placeholder="请输入商品名称"></el-input>
                </el-form-item>
                <el-form-item label="厂家" prop="producer">
                    <el-input v-model="addGoodsInform.producer" placeholder="请输入厂家名"></el-input>
                </el-form-item>
                <el-form-item label="生产日期" prop="produceDate">
                    <el-date-picker v-model="addGoodsInform.produceDate" type="date" placeholder="选择生产日期"
                        value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="商品类型" prop="type">
                    <el-select v-model="addGoodsInform.type" placeholder="选择商品类型">
                        <el-option v-for="(label, value) in typeMap" :key="value" :label="label" :value="value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="进货价" prop="buyingPrice">
                    <el-input v-model="addGoodsInform.buyingPrice" placeholder="请输入商品进货价格"> </el-input>
                </el-form-item>
                <el-form-item label="售价" prop="price">
                    <el-input v-model="addGoodsInform.price" placeholder="请输入商品的售价"></el-input>
                </el-form-item>
                <el-form-item label="库存" prop="inventory">
                    <el-input-number v-model="addGoodsInform.inventory" :min="0" label="库存"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editCanceled">取 消</el-button>
                <el-button type="primary" @click="editConfirm">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            searchInform: {
                goodsName: null,
                producer: null,
                startPrice: null,
                endPrice: null,
                order: null,
                startProduceTime: null,
                endProduceTime: null,
                page: null,
                pageSize: null
            },
            searchRules: {
                startPrice: [
                    { validator: this.validatePrice, trigger: 'blur' }
                ],
                endPrice: [
                    { validator: this.validatePrice, trigger: 'blur' }
                ]
            },
            pickNumber: [],
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
            tempDate: "",
            goods: [],
            total: null,
            typeMap: { "1": "食品", "2": "装饰品", "3": "饮品", "4": "服饰", "5": "日用品", '6': "家电" },
            addGoodsVisible: false,
            editGoodsVisible: false,
            addGoodsInform: {
                goodsId: "",
                goodsName: "",
                producer: "",
                produceDate: "",
                type: "",
                buyingPrice: "",
                price: "",
                inventory: ""
            },
            goodsInputRule: {
                goodsName: [{ required: true, message: "请输入商品名", trigger: "blur" }],
                producer: [{ required: true, message: "请输入生产厂家", trigger: "blur" }],
                produceDate: [{ required: true, message: "请输入生产日期", trigger: "blur" }],
                type: [{ required: true, message: "请选择商品类型", trigger: "blur" }],
                buyingPrice: [{ required: true, message: "请输入进货价格", trigger: "blur" }],
                price: [{ required: true, message: "请输入售价", trigger: "blur" }],
                inventory: [{ required: true, message: "请输入库存", trigger: "blur" }]
            },
            multipleSelection: [],
            selectedIds: [],
            shoppingCart: {
                id: "",
                userId: "",
                goodsId: "",
                goodsName: "",
                goodsPrice: "",
                goodsBuyingPrice: "",
                amount: ""
            }
        }
    },
    props: ['uesername', 'power', 'userId'],
    mounted() {
        this.getGoodsList();
    },
    filters: {
        formatDate(dateArray) {
            if (!dateArray || !Array.isArray(dateArray) || dateArray.length < 3) {
                return '';
            }

            const year = dateArray[0];
            const month = String(dateArray[1]).padStart(2, '0');
            const day = String(dateArray[2]).padStart(2, '0');

            return `${year}-${month}-${day}`;
        }
    },
    methods: {
        handleChange(value) {
            console.log(value);
        },
        validatePrice(rule, value, callback) {
            const startPrice = this.searchInform.startPrice;
            const endPrice = this.searchInform.endPrice;
            if ((!startPrice && endPrice) || (startPrice && !endPrice)) {
                callback(new Error('请输入价格区间'));
            } else if (startPrice && endPrice && endPrice <= startPrice) {
                callback(new Error('最高价格必须大于最低价格'));
            } else {
                callback();
            }
        },
        search() {
            console.log(this.tempDate)
            if (this.tempDate) {
                this.searchInform.startProduceTime = this.tempDate[0];
                this.searchInform.endProduceTime = this.tempDate[1];
            } else {
                this.searchInform.startProduceTime = null
                this.searchInform.endProduceTime = null
            }
            this.getGoodsList();
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        async getGoodsList() {
            const { data: res } = await this.$http.get("/goods", { params: this.searchInform });
            console.log(res)
            if (res.code === 0) {
                this.$message.error("请求商品列表失败");
                return;
            }
            this.goods = res.data.rows;
            this.total = res.data.total;
        }, handleSizeChange(newSize) {
            console.log(newSize);
            this.searchInform.pageSize = newSize;
            this.getGoodsList();
        },
        handleCurrentChange(newPage) {
            console.log(newPage);
            this.searchInform.page = newPage;
            this.getGoodsList();
        },
        addGoods() {
            this.addGoodsVisible = true;
        },
        addConfirm() {
            this.$refs.goodsInputRef.validate(async valid => {
                if (!valid) {
                    this.$message.error("输入不合法，添加失败")
                    return
                }
                const { data: res } = await this.$http.post('/goods', this.addGoodsInform)
                if (res.code !== 1) {
                    this.$message.error("添加失败");
                } else {
                    this.$message.success("添加商品成功");
                    this.getGoodsList();
                }
            });
            this.addGoodsVisible = false;
            this.$refs.goodsInputRef.resetFields()
        },
        addCanceled() {
            this.addGoodsVisible = false;
            this.$refs.goodsInputRef.resetFields()
        },
        deleteGoods(id) {
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
            const { data: res } = await this.$http.delete('/goods/' + id);
            if (res.code !== 1) {
                this.$message.error("删除失败")
                return;
            } else {
                this.$message.success("删除成功！")
                this.getGoodsList();
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
                    this.selectedIds[i] = this.multipleSelection[i].goodsId;
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
        },
        async eiditGoods(id) {
            const { data: res } = await this.$http.get("goods/" + id);
            if (res.code !== 1) {
                this.$message.error("获取商品原有数据失败！")
                return
            }
            this.addGoodsInform = res.data;
            this.editGoodsVisible = true;
        },
        editCanceled() {
            this.$refs.goodsEditRef.resetFields()
            this.editGoodsVisible = false;
            this.$message("修改已取消")
        },
        editConfirm() {
            this.$refs.goodsEditRef.validate(async valid => {
                if (!valid) {
                    this.$message.error("输入不合法，修改失败")
                    return
                }
                const { data: res } = await this.$http.put('/goods', this.addGoodsInform)
                if (res.code !== 1) {
                    this.$message.error("修改数据失败")
                } else {
                    this.$message.success("修改数据成功")
                    this.getGoodsList()
                }
            });
            this.$refs.goodsEditRef.resetFields()
            this.editGoodsVisible = false;
        },
        async addToShoppingCart(goods) {
            if (!this.pickNumber[goods.goodsId] > 0) {
                this.$message.error("无法将选购数量为0的商品添加到购物车");
                return
            }
            this.shoppingCart.amount = this.pickNumber[goods.goodsId];
            this.shoppingCart.userId = this.userId;
            this.shoppingCart.goodsId = goods.goodsId;
            /* this.shoppingCart.goodsName = goods.goodsName;
            this.shoppingCart.goodsPrice = goods.price;
            this.shoppingCart.goodsBuyingPrice = goods.buyingPrice; */
            const { data: res } = await this.$http.post('shoppingcart', this.shoppingCart);
            if (res.code !== 1) {
                this.$message.error('添加到购物车失败')
            } else {
                this.$message.success("添加得到购物车成功")
                this.getGoodsList()
            }
            this.pickNumber[goods.goodsId] = null;
        }
    }
}
</script>

<style></style>