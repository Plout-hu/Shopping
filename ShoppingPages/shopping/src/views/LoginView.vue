<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="../assets/ynu.jpg" alt="">
      </div>
      <el-form :model="loginFrom" :rules="loginFromRules" ref="loginFormRef" label-width="00px" class="login_form">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" v-model="loginFrom.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="loginFrom.password" type="password"></el-input>
        </el-form-item>
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" @click="resetLoginFrom">重置</el-button>
        </el-form-item>
        <el-form-item class="link" style="display: flex; justify-content: space-between;">
          <el-link type="primary" class="left-link" @click="forget">忘记密码</el-link>
          <el-link type="primary" class="right-link" @click="registerVisible = true">注册</el-link>
        </el-form-item>
      </el-form>
    </div>
    <el-dialog title="忘记密码" :visible.sync="forgetPasswordVisible" width="50%" center>
      <el-form :model="forgetPassword" ref="forgetPasswordRef" :rules="forgetRules" label-width="80px">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="forgetPassword.username" :disabled="!checkCodeDisabled"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="forgetPassword.email" style="width: 81%; margin-right: 12px;"
                    :disabled="!checkCodeDisabled"></el-input>
          <el-button type="primary" @click="sendCode">发送验证码</el-button>
        </el-form-item>
        <el-form-item prop="checkCode" label="验证码">
          <el-input v-model="forgetPassword.checkCode" placeholder="请输入邮箱中收到的验证码"
                    :disabled="checkCodeDisabled"></el-input>
        </el-form-item>
        <el-form-item prop="newPassword1" label="新密码">
          <el-input v-model="forgetPassword.newPassword1" type="password"></el-input>
        </el-form-item>
        <el-form-item prop="newPassword2" label="确认密码">
          <el-input v-model="forgetPassword.newPassword2" type="password"></el-input>
        </el-form-item>
        <el-form-item style="text-align: center;">
          <template>
            <el-button @click="forgetCancel">取消</el-button>
            <el-button type="primary" @click="forgetConfirm">确认</el-button>
          </template>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog title="注册新用户" :visible.sync="registerVisible" width="50%" center @closed="handleRegisterClose">
      <el-form :model="registerForm" ref="registerRef" :rules="registerRule" label-width="80px">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="registerForm.username" :disabled="!checkCodeDisabled"
                    @blur="checkUsernameExists"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="registerForm.email" style="width: 81%; margin-right: 12px;"
                    :disabled="!checkCodeDisabled"></el-input>
          <el-button type="primary" @click="checkUsername">发送验证码</el-button>
        </el-form-item>
        <el-form-item prop="checkCode" label="验证码">
          <el-input v-model="registerForm.checkCode" :disabled="checkCodeDisabled"></el-input>
        </el-form-item>
        <el-form-item prop="tel" label="电话号码">
          <el-input v-model="registerForm.tel"></el-input>
        </el-form-item>
        <el-form-item prop="passwordNew1" label="密码">
          <el-input v-model="registerForm.passwordNew1" type="password"></el-input>
        </el-form-item>
        <el-form-item prop="passwordNew2" label="确认密码">
          <el-input v-model="registerForm.passwordNew2" type="password"></el-input>
        </el-form-item>
        <el-form-item label="注册身份">
          <el-radio-group v-model="registerForm.power">
            <el-radio :label="0">客户</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="key" label="密钥" v-if="registerForm.power === 1">
          <el-input v-model="registerForm.key" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="registerCancel">取消</el-button>
          <el-button type="primary" @click="registerConfirm">确定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data () {
    const validateUsername = async (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入用户名'))
        return
      }
      if (value.length < 5 || value.length > 16) {
        callback(new Error('用户名长度在5~16之间'))
        return
      }
      if (this.usernameExists) {
        callback(new Error('该用户名已存在'))
        return
      }
      callback()
    }

    return {
      loginFrom: {
        username: '',
        password: ''
      },
      loginFromRules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          },
          {
            min: 5,
            max: 16,
            message: '用户名长度在5~16之间',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            min: 5,
            max: 16,
            message: '密码长度在8~16之间',
            trigger: 'blur'
          }
        ]
      },
      registerRule: {
        username: [
          {
            required: true,
            message: '请输入客户名',
            trigger: 'blur'
          },
          {
            min: 5,
            max: 16,
            message: '用户名长度在5~16之间',
            trigger: 'blur'
          },
          {
            validator: validateUsername,
            trigger: 'blur'
          }
        ],
        tel: [
          {
            required: true,
            message: '请输入客户电话号码',
            trigger: 'blur'
          },
          {
            pattern: /^[0-9]{10,12}$/,
            message: '电话号码必须由10到12个数字组成',
            trigger: 'blur'
          }
        ],
        email: [
          {
            required: true,
            message: '请输入客户邮箱地址',
            trigger: 'blur'
          },
          {
            pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/,
            message: '请输入有效的邮箱地址',
            trigger: 'blur'
          }
        ],
        checkCode: [{
          required: true,
          message: '请输入验证码',
          trigger: 'blur'
        }],
        passwordNew1: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          },
          {
            min: 8,
            max: 16,
            message: '密码长度在8到16之间',
            trigger: 'blur'
          },
          {
            validator: this.validatePassword,
            trigger: 'blur'
          }
        ],
        passwordNew2: [
          {
            required: true,
            message: '请再次输入新密码',
            trigger: 'blur'
          },
          {
            validator: this.validatePassword,
            trigger: 'blur'
          },
          {
            validator: this.validateConfirmPassword1,
            trigger: 'blur'
          }
        ],
        key: [
          {
            required: true,
            message: '请输入管理员密钥',
            trigger: 'blur'
          }
        ]
      },

      forgetRules: {
        username: [
          {
            required: true,
            message: '请输入客户名',
            trigger: 'blur'
          },
          {
            min: 5,
            max: 16,
            message: '用户名长度在5~16之间',
            trigger: 'blur'
          }
        ],
        email: [
          {
            required: true,
            message: '请输入客户邮箱地址',
            trigger: 'blur'
          },
          {
            pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/,
            message: '请输入有效的邮箱地址',
            trigger: 'blur'
          }
        ],
        newPassword1: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          },
          {
            min: 8,
            max: 16,
            message: '密码长度在8到16之间',
            trigger: 'blur'
          },
          {
            validator: this.validatePassword,
            trigger: 'blur'
          }
        ],
        newPassword2: [
          {
            required: true,
            message: '请再次输入新密码',
            trigger: 'blur'
          },
          {
            validator: this.validatePassword,
            trigger: 'blur'
          },
          {
            validator: this.validateConfirmPassword,
            trigger: 'blur'
          }
        ]
      },
      userInform: {
        username: '',
        email: ''
      },
      changePassword: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        checkCode: '',
        email: '',
        password: '',
        passwordNew1: '',
        passwordNew2: '',
        tel: '',
        power: 0,
        key: ''
      },
      forgetPassword: {
        username: '',
        newPassword1: '',
        newPassword2: '',
        checkCode: '',
        email: ''
      },
      forgetPasswordVisible: false,
      checkCodeDisabled: true,
      registerVisible: false,
      usernameExists: false,
    }
  },
  methods: {
    registerConfirm () {
      this.$refs.registerRef.validate(async valid => {
        if (!valid) {
          return
        }

        if (this.usernameExists) {
          this.$message.error('用户名已存在')
          return
        }

        this.registerForm.password = this.registerForm.passwordNew1
        const { data: res } = await this.$http.post('/customer/register/' + this.registerForm.checkCode, this.registerForm)
        if (res.code === 1) {
          this.$message.success('注册成功')
          this.$refs.registerRef.resetFields()
          this.registerVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    async checkUsername () {
      if (this.registerForm.username !== null && this.registerForm.email !== null && this.registerForm.username !== '' && this.registerForm.email !== '') {
        this.userInform.username = this.registerForm.username
        this.userInform.email = this.registerForm.email
        console.log(this.userInform)
        const { data: res } = await this.$http.post('/checkUsername', this.userInform)
        if (res.code === 1) {
          this.$message.success('发送验证码成功')
          this.checkCodeDisabled = false
        } else {
          this.$message.error(res.msg)
        }
      }
    },
    registerCancel () {
      this.$refs.registerRef.resetFields()
      this.registerVisible = false
      this.usernameExists = false
      this.registerForm.power = 0
      this.registerForm.key = ''
      this.checkCodeDisabled = true
      this.$message('注册已取消')
    },
    handleRegisterClose() {
      this.$refs.registerRef.resetFields()
      this.usernameExists = false
      this.registerForm.power = 0
      this.registerForm.key = ''
      this.checkCodeDisabled = true
    },
    forgetConfirm () {
      this.$refs.forgetPasswordRef.validate(async valid => {
        if (valid && this.checkCode !== null && this.checkCode !== '') {
          this.changePassword.username = this.forgetPassword.username
          this.changePassword.password = this.forgetPassword.newPassword1
          const { data: res } = await this.$http.put('/customer/forgetPassWord/' + this.forgetPassword.checkCode, this.changePassword)
          if (res.code === 1) {
            this.$message.success('修改密码成功')
          } else {
            this.$message.error(res.msg)
          }
        } else {
          this.$message.error('输入不合法，请输入合法之后再发送验证码')
          return
        }
      })
      this.$refs.forgetPasswordRef.resetFields()
      this.checkCodeDisabled = true
      this.forgetPasswordVisible = false
    },
    forgetCancel () {
      this.$refs.forgetPasswordRef.resetFields()
      this.checkCodeDisabled = true
      this.forgetPasswordVisible = false
    },
    sendCode () {
      this.$refs.forgetPasswordRef.validate(async valid => {
          if (!valid) {
            this.$message.error('输入不合法，请输入合法之后再发送验证码')
            return
          }
          this.userInform.email = this.forgetPassword.email
          this.userInform.username = this.forgetPassword.username
          const { data: res } = await this.$http.post('/checkEmail', this.userInform)
          if (res.code === 1) {
            this.$message.success('发送验证码成功！')
            this.checkCodeDisabled = false
          } else {
            this.$message.error(res.msg)
          }
        }
      )
    },
    resetLoginFrom () {
      this.$refs.loginFormRef.resetFields()
    },
    forget () {
      this.forgetPasswordVisible = true
    },
    login () {
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/login', this.loginFrom)
        if (res.code === 1) {
          localStorage.setItem('token', res.data)
          console.log(res.data)
          this.$router.push('/home')
          return this.$message.success('登录成功')
        } else {
          return this.$message.error(res.msg)
        }
      })
    },
    tvalidatePassword (rule, value, callback) {
      const regex = /^(?![a-z]+$)(?![A-Z]+$)(?![\W_]+$)(?![0-9]+$)[a-zA-Z0-9\W_]{8,16}$/
      if (regex.test(value)) {
        callback()
      } else {
        callback(new Error('密码必须包含至少两种组合：大写字母、小写字母、数字、标点符号'))
      }
    },
    validateConfirmPassword (rule, value, callback) {
      if (value === this.forgetPassword.newPassword1) {
        callback()
      } else {
        callback(new Error('两次输入的密码不一致'))
      }
    },
    validateConfirmPassword1 (rule, value, callback) {
      if (value === this.registerForm.passwordNew1) {
        callback()
      } else {
        callback(new Error('两次输入的密码不一致'))
      }
    },
    async checkUsernameExists () {
      if (!this.registerForm.username) return

      try {
        const { data: res } = await this.$http.get('/customer/getByUsername/' + this.registerForm.username)
        this.usernameExists = !res.data

        this.$refs.registerRef.validateField('username')
      } catch (error) {
        console.error('检查用户名出错:', error)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.link {
  display: flex;
  justify-content: space-between;
}

.left-link {
  text-align: right;
}

.right-link {
  text-align: left;
}

.login_container {
  background-image: url('http://www.news.cn/local/2023-04/20/1129541562_16819611731871n.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center center;
  height: 100%;
}

.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns {
  display: flex;
  justify-content: center;
}

.login_box {
  width: 500px;
  height: 370px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 10px #ddd;

  .avatar_box {
    height: 130px;
    width: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 10px;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;

    img {
      height: 100%;
      width: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}

.el-radio-group {
  margin-top: 8px;
}

.el-radio {
  margin-right: 20px;
}
</style>
