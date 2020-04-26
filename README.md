# springboot-mini
这是一套基于spring boot 2.2.1、shiro、ehcache、swagger2、JPA 、thymeleaf、layui 后台管理系统， 权限控制的方式为 RBAC。代码通熟易懂 ，数据全程 ajax 获取，封装 ajax 工具类、菜单无线层级展示。
包含微信公众号相关配置以及微信小程序的相关配置
### 2. 具有如下特点
* 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
* 当角色或者菜单权限发生变化的时候能够自动刷新用户权限无需退出登录
* 友好的代码结构及注释，便于阅读及二次开发
* 页面交互使用thymeleaf+layui ，极大的提高了开发效率。
* 菜单支持无线层级展示、解决 layui.tree 树形组件数据回显错乱。
* 引入swagger文档支持，方便编写API接口文档。
* 实现单点登录功能
* 实现多列多图上传
* 使用art-template模板引擎
* 新增JWT验证功能。
* 新增websocket功能。
* 新增防XSS攻击。
