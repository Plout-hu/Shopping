<template>
    <div class="outer-box">
        <el-card class="box-card">
            <el-form :model="passwordForm" :rules="passwordRules" ref="passwordRef">
                <el-form-item label="原密码" prop="original">
                    <el-input type="password" v-model="passwordForm.original" auto-complete="off"
                        placeholder="请输入原密码"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="new1">
                    <el-input type="password" v-model="passwordForm.new1" auto-complete="off"
                        placeholder="请输入新密码"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="new2">
                    <el-input type="password" v-model="passwordForm.new2" auto-complete="off"
                        placeholder="请输入再次输入新密码"></el-input>
                </el-form-item>
                <el-form-item class="buttons">
                    <el-button type="info" @click="reset">取消</el-button>
                    <el-button type="primary" @click="checkAndReset">确认</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
            passwordForm: {
                original: "",
                new1: "",
                new2: ""
            },
            loginForm: {
                username: "",
                password: ""
            },
            passwordRules: {
                original: [
                    { required: true, message: "请输入原密码", trigger: "blur" }
                ],
                new1: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 8, max: 16, message: '密码长度在8到16之间', trigger: 'blur' },
                    { validator: this.validatePassword, trigger: 'blur' }
                ],
                new2: [
                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
                    { validator: this.validatePassword, trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' }
                ]
            }
        }
    },
    props: ["username"],
    methods: {
        validatePassword(rule, value, callback) {
            const regex = /^(?![a-z]+$)(?![A-Z]+$)(?![\W_]+$)(?![0-9]+$)[a-zA-Z0-9\W_]{8,16}$/;
            if (regex.test(value)) {
                callback();
            } else {
                callback(new Error('密码必须包含至少两种组合：大写字母、小写字母、数字、标点符号'));
            }
        },
        validateConfirmPassword(rule, value, callback) {
            if (value === this.passwordForm.new1) {
                callback();
            } else {
                callback(new Error('两次输入的密码不一致'));
            }
        },
        reset() {
            this.$refs.passwordRef.resetFields();
            this.$message("已取消！");
        },
        async checkAndReset() {
            this.loginForm.username=this.username;
            this.loginForm.password = this.passwordForm.original;
            this.$refs.passwordRef.validate(async valid => {
                if (!valid) { this.$message.error("表达输入不合法，请重新输入"); return }
                const { data: res } = await this.$http.post("/login", this.loginForm);
                console.log(res)
                if (res.code === 1) {
                    localStorage.setItem("token", res.data);
                    this.$confirm('此操作将修改密码, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(async () => {
                        this.loginForm.password = this.passwordForm.new1;
                        const { data: result } = await this.$http.put('/customer/resetPassword', this.loginForm);
                        if (result.code === 1) {
                            this.$message({
                                type: 'success',
                                message: '修改密码成功'
                            });
                        } else {
                            this.$message({
                                type: "error",
                                message: "修改密码失败"
                            })
                        }
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消修改密码'
                        });
                    });
                } else {
                    this.$message({
                        type: "error",
                        message: "原密码错误，修改密码失败"
                    })
                }
            })
            this.$refs.passwordRef.resetFields();
        }
    }
}
</script>

<style lang="less" scoped>
.box-card {
    width: 550px;
    height: auto;

}

.outer-box {
    border-radius: 5px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 10px #ddd;
}

.buttons {
    text-align: center;
}
</style>