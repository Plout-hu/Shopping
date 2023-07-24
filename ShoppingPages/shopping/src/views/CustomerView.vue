<template>
    <div>
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home/welcome' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>客户管理</el-breadcrumb-item>
            <el-breadcrumb-item>客户列表</el-breadcrumb-item>
        </el-breadcrumb>

        <el-card>
            <el-form :inline="true" :model="searchInform" class="demo-form-inline">
                <el-form-item label="客户ID">
                    <el-input v-model="searchInform.id" placeholder="客户ID" clearable @clear="getCustomerList"
                        style="width: 201px;"></el-input>
                </el-form-item>
                <el-form-item label="客户名称">
                    <el-input v-model="searchInform.name" placeholder="客户名" clearable @clear="getCustomerList"></el-input>
                </el-form-item>
                <el-form-item label="注册日期">
                    <el-col :span="11">
                        <el-date-picker type="date" placeholder="选择日期" v-model="searchInform.begin"
                            value-format="yyyy-MM-dd"></el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="~">
                    <el-col :span="11">
                        <el-date-picker type="date" placeholder="选择日期" v-model="searchInform.end"
                            value-format="yyyy-MM-dd"></el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="getCustomerList">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="addCustomerVisible = true">添加用户</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="customers" border stripe @selection-change="handleSelectionChange">
                <!-- <el-table-column type="index"></el-table-column> -->
                <el-table-column align="center" type="selection"></el-table-column>
                <el-table-column align="center" label="客户名" prop="username"></el-table-column>
                <el-table-column align="center" label="邮箱" prop="email"></el-table-column>
                <el-table-column align="center" label="电话" prop="tel"></el-table-column>
                <el-table-column align="center" label="客户等级" prop="userLevel">
                    <template slot-scope="scope">
                        <span v-if="scope.row.userLevel === 0">铜牌用户</span>
                        <span v-else-if="scope.row.userLevel === 1">银牌用户</span>
                        <span v-else-if="scope.row.userLevel === 2">金牌用户</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="总消费金额" prop="consumptionAmount"></el-table-column>
                <el-table-column align="center" label="注册时间" prop="registerTime">
                    <template slot-scope="scope">
                        {{ scope.row.registerTime | formatDate }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="权限" prop="power">
                    <template slot-scope="scope">
                        <span v-if="scope.row.power === 1">管理员</span>
                        <span v-else>客户</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="管理" width="205px">
                    <template slot-scope="scope">
                        <!-- <el-button type="primary" icon="el-icon-edit" size="mini"
                            @click="showEiditDialog(scope.row.userId)">修改</el-button> -->
                        <el-button type="danger" icon="el-icon-delete" size="mini"
                            @click="deleteItem(scope.row.userId)">删除</el-button>
                        <el-button type="primary" icon="el-icon-refresh" size="mini"
                            @click="passwordInputShow(scope.row.userId)">重置密码</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="searchInform.page" :page-sizes="[5, 10, 50, 100]" :page-size="searchInform.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="this.total" style="text-align: center;">
            </el-pagination>
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item>
                    <el-button type="primary" @click="deleteByIds">批量删除</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-dialog title="添加客户" :visible.sync="addCustomerVisible" width="50%">
            <el-form :model="addInform" label-width="80px" :rules="addRules" ref="addCustomerRef">
                <el-form-item label="客户名称" prop="username">
                    <el-input v-model="addInform.username" placeholder="请输入客户名" :maxlength="16" :minlength="5"></el-input>
                </el-form-item>
                <el-form-item label="客户等级" prop="userLevel">
                    <el-select v-model="addInform.userLevel" placeholder="请选择用户等级">
                        <el-option label="金牌用户" value="2"></el-option>
                        <el-option label="银牌用户" value="1"></el-option>
                        <el-option label="铜牌用户" value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="累计消费" prop="consumptionAmount">
                    <el-input v-model="addInform.consumptionAmount" placeholder="请输入累计消费金额" type="number"></el-input>
                </el-form-item>
                <el-form-item label="电话号码" prop="tel">
                    <el-input v-model="addInform.tel" placeholder="请输入客户电话号码"></el-input>
                </el-form-item>
                <el-form-item label="电子邮箱" prop="email">
                    <el-input v-model="addInform.email" placeholder="请输入客户邮箱地址"></el-input>
                </el-form-item>
                <el-form-item label="用户权限" prop="power">
                    <el-select v-model="addInform.power" placeholder="请选择用户权限">
                        <el-option label="管理员" value="1"></el-option>
                        <el-option label="普通用户" value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelAdd">取 消</el-button>
                <el-button type="primary" @click="addCustomer">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="该操作需要验证您的管理员密码" :visible.sync="passwordInputVisible" width="50%" center>
            <el-form :model="inform" ref="passwordRef">
                <el-form-item>
                    <el-input type="password" v-model="inform.password"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="resetCancel">取 消</el-button>
                <el-button type="primary" @click="resetPassWord">确 定</el-button>
            </span>
        </el-dialog>
        <!--  <el-dialog title="添加客户" :visible.sync="editCustomerVisible" width="50%">
            <el-form :model="editInform" label-width="80px" :rules="addRules" ref="eiditCustomerRef">
                <el-form-item label="客户名称" prop="username">
                    <el-input v-model="editInform.username" placeholder="请输入客户名" :maxlength="16" :minlength="5"></el-input>
                </el-form-item>
                <el-form-item label="客户等级" prop="userLevel">
                    <el-select v-model="editInform.userLevel" placeholder="请选择用户等级">
                        <el-option label="金牌用户" :value="2"></el-option>
                        <el-option label="银牌用户" :value="1"></el-option>
                        <el-option label="铜牌用户" :value="0"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="累计消费" prop="consumptionAmount">
                    <el-input v-model="editInform.consumptionAmount" placeholder="请输入累计消费金额" type="number"></el-input>
                </el-form-item>
                <el-form-item label="电话号码" prop="tel">
                    <el-input v-model="editInform.tel" placeholder="请输入客户电话号码"></el-input>
                </el-form-item>
                <el-form-item label="电子邮箱" prop="email">
                    <el-input v-model="editInform.email" placeholder="请输入客户邮箱地址"></el-input>
                </el-form-item>
                <el-form-item label="用户权限" prop="power">
                    <el-select v-model="editInform.power" placeholder="请选择用户权限">
                        <el-option label="管理员" :value="1"></el-option>
                        <el-option label="普通用户" :value="0"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelEidit">取 消</el-button>
                <el-button type="primary" @click="updateCustomer">确 定</el-button>
            </span>
        </el-dialog> -->
    </div>
</template>

<script>
export default {
    data() {
        return {
            searchInform: {
                "id": null,
                "page": null,
                "pageSize": null,
                "name": null,
                "begin": null,
                "end": null
            },
            addInform: {
                "username": null,
                "userLevel": null,
                "registerTime": null,
                "consumptionAmount": null,
                "tel": null,
                "email": null,
                "password": null,
                "power": null
            },
            addRules: {
                username: [
                    { required: true, message: "请输入客户名", trigger: "blur" },
                    { min: 5, max: 16, message: "用户名长度在5~16之间", trigger: "blur" }
                ],
                userLevel: [{ required: true, message: "请选择用户等级", trigger: "blur" }],
                tel: [
                    { required: true, message: '请输入客户电话号码', trigger: 'blur' },
                    { pattern: /^[0-9]{10,12}$/, message: '电话号码必须由10到12个数字组成', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入客户邮箱地址', trigger: 'blur' },
                    { pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/, message: '请输入有效的邮箱地址', trigger: 'blur' }
                ]
            },
            inform: {
                username: "",
                password: ""
            },
            resetInform: {
                userId: "",
                password: "12345678"
            },
            total: null,
            customers: [],
            selectedIds: [],
            multipleSelection: [],
            addCustomerVisible: false,
            passwordInputVisible: false
        }
    },
    mounted() {
        this.getCustomerList();
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
    },
    methods: {
        async getCustomerList() {
            const { data: res } = await this.$http.get("customer", { params: this.searchInform });
            console.log(res);
            if (res.code === 0) return this.$message.error("获取用户列表失败");
            this.customers = res.data.rows;
            this.total = res.data.total;
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleSizeChange(newSize) {
            console.log(newSize);
            this.searchInform.pageSize = newSize;
            this.getCustomerList();
        },
        handleCurrentChange(newPage) {
            console.log(newPage);
            this.searchInform.page = newPage;
            this.getCustomerList();
        },
        async deleteById(id) {
            console.log(id);
            const { data: res } = await this.$http.delete("customer/" + id);
            if (res.code === 1) {
                this.$message.success("删除成功");
            } else {
                this.$message.error("删除失败！");
            }
            this.getCustomerList();
            return res;
        },
        cancelAdd() {
            this.addCustomerVisible = false;
            this.$refs.addCustomerRef.resetFields();
        },
        deleteItem(id) {
            this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                this.deleteById(id);
            }).catch(() => {
                this.$message.info("已取消删除");
            })
        },
        addCustomer() {
            this.$refs.addCustomerRef.validate(async valid => {
                if (!valid) {
                    this.$message.error("输入数据不符合要求，添加失败");
                    return;
                }
                const { data: res } = await this.$http.post("customer", this.addInform);

                if (res.code === 1) {
                    this.$message.success("添加用户成功");
                    this.getCustomerList();
                } else {
                    this.$message.error("添加用户失败");
                }

            });
            this.addCustomerVisible = false;
            this.$refs.addCustomerRef.resetFields();
        },
        deleteByIds() {
            this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {

                //用户点击确认按钮
                //1. 创建id数组, 从 this.multipleSelection 获取即可
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    this.selectedIds[i] = this.multipleSelection[i].userId;
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
        /* showEiditDialog(id) {
            console.log(id)
            const { data: res } = await this.$http.get("customer/" + id);
            if (res.code !== 1) {
                this.$message.error("查询数据失败");
                return;
            }
            this.editInform = res.data;
            this.editCustomerVisible = true;
        }, */
        /* ,
        cancelEidit() {
            this.editCustomerVisible = false;
            this.$refs.eiditCustomerRef.resetFields();
        },
        updateCustomer() {
            this.$refs.eiditCustomerRef.validate(async valid => {
                if(!valid){
                    this.$message.error("输入数据不符合要求，添加失败");
                    return;
                    }
                const{data:res} =await this.$http.put("")
            })
        } */
        resetCancel() {
            this.passwordInputVisible = false;
            this.inform.password = "";
        },
        passwordInputShow(id) {
            this.passwordInputVisible = true;
            this.resetInform.userId = id;
        },
        async resetPassWord() {
            this.inform.username = this.username;
            console.log(this.inform);
            const { data: res } = await this.$http.post("/login", this.inform);
            if (res.code === 1) {
                this.$confirm('将该用户的密码重置为"12345678", 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    localStorage.setItem("token", res.data);
                    const { data: result } = await this.$http.put("/customer/resetPassword", this.resetInform);
                    if (result.code === 1) {
                        this.$message.success("重置密码成功，密码已经被重置为12345678！");
                    } else {
                        this.$message.error("密码重置失败")
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消重置密码'
                    });
                });
            } else {
                this.$message.error("验证管理员密码错误，修改失败");
            }
            this.passwordInputVisible = false;
            this.inform.password = "";
        }
    },
    props: ["username"]
}
</script>

<style lang="less" scoped></style>