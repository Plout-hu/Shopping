# 模拟电商购物平台
## 主要实现的功能

### 1、通过密码登录

![image-20230724235739614](https://github.com/Plout-hu/Shopping/assets/96678139/3d47f938-3d74-4535-9269-04401782ab9d)


### 2.忘记密码及注册新用户
![image-20230725143632392](https://github.com/Plout-hu/Shopping/assets/96678139/6f56d869-c355-4de5-9472-deb6f131258d)

### 2.修改密码及注册新用户
![image-20230725143524735](https://github.com/Plout-hu/Shopping/assets/96678139/cb54753d-6a97-48b0-b660-8b49d4b4b8e1)

### 3、修改密码

### ![image-20230725143802487](./ShoppingPages/shopping/src/assets/image-20230725143802487.png)

### 4、显示商品列表及购物

![image-20230725150337882](./ShoppingPages/shopping/src/assets/image-20230725150337882.png)

![image-20230725150538816](./ShoppingPages/shopping/src/assets/image-20230725150538816.png)

### 5、显示历史的订单

![image-20230725152046115](./ShoppingPages/shopping/src/assets/image-20230725152046115.png)
=======
![image-20230725143802487](https://github.com/Plout-hu/Shopping/assets/96678139/0b42beb6-4f39-4512-b395-f94afe09e46e)

### 4、显示商品列表及购物

![image-20230725150337882](https://github.com/Plout-hu/Shopping/assets/96678139/74dde2d3-9be3-43ae-9c91-fd660ec39d2d)

![image-20230725150538816](https://github.com/Plout-hu/Shopping/assets/96678139/ba8d1502-f852-4672-9ae9-42446f1413cc)


### 5、显示历史的订单

![image-20230725152046115](https://github.com/Plout-hu/Shopping/assets/96678139/3fd88320-330f-49a0-af6e-72ebf4050e7c)

### 6、显示用户列表（需要以管理员账户登录）

![image](https://github.com/Plout-hu/Shopping/assets/96678139/360ba6bc-a8fe-4c02-a0f1-44a676a8e486)

### 6、添加客户

![image](https://github.com/Plout-hu/Shopping/assets/96678139/dd0c0d31-f637-4ff3-9c94-2f0fe715b909)

### 7、添加商品

![image](https://github.com/Plout-hu/Shopping/assets/96678139/fde67f3f-8fa0-492f-ae66-93bcd57ca5de)

## 用例图
@startuml
!include https://unpkg.com/plantuml-style-c4@latest/core.puml
' uncomment the following line and comment the first to use locally
'!include core.puml
left to right direction
actor Customer as customer
actor Administrators as admin
actor NotLogin as notLogin
package Shopping {
    usecase "登录" as login
    usecase "注册新用户" as register
    usecase "忘记密码" as forgetPassword
    package "客户管理"{
        usecase "修改密码" as changePassword
        usecase "查看客户列表" as listcustomer
        usecase "新增客户" as addCustomer
        usecase "删除客户" as deleteCustomer
        usecase "批量删除客户" as deleteCustomers
        usecase "重置客户密码" as resetPassword
        usecase "根据条件查询客户" as queryCustomer
    }
    package "商品"{
        usecase "查看商品列表" as listgoods
        usecase "根据条件查询商品" as queryGoods
        usecase "添加商品" as addGoods
        usecase "删除商品" as deleteGoods
        usecase "批量删除商品" as deleteGoodses
        usecase "修改商品" as modifyGoods
        usecase "将商品添加到购物车" as addToShoppingcart
    }
    package "历史订单"{
        usecase "查看所有用户历史订单" as listAllorder
        usecase "根据条件查询订单" as queryOrders
    }
    package "购物车"{
        usecase "查看购物车" as listShoppingcart
        usecase "删除购物车中商品" as deleteShoppingcart
        usecase "批量删除购物车中商品" as deleteShoppingcarts
        usecase "购买购物车中所有商品" as buyShoppingcart
    }
}
notLogin --> login
notLogin --> register
notLogin --> forgetPassword
admin --> changePassword
admin --> listcustomer
admin --> addCustomer
admin --> deleteCustomer
admin --> deleteCustomers
admin --> resetPassword
admin --> listgoods
admin --> queryCustomer
admin --> queryGoods
admin --> addGoods
admin --> deleteGoods
admin --> deleteGoodses
admin --> modifyGoods
admin --> listAllorder
admin --> queryOrders

customer -->changePassword
customer -->listgoods
customer -->queryGoods
customer -->addToShoppingcart
customer -->queryOrders
customer -->listShoppingcart
customer -->deleteShoppingcart
customer -->deleteShoppingcarts
customer -->buyShoppingcart
@enduml