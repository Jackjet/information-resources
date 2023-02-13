###对外controller
我们把controller按服务对象分类(每种对象对应一种用户)，每个子目录对应一种对象比如:

    - app: 表示这个目录里的web api为手机端服务
    - web：表示这个目录里的web api为PC网站服务
    - webadmin：表示这个目录里的web api为管理网站服务
    - thirdparty： 表示这个目录里的web api为其它第三方提供服务间的调用
    - base: 表示这个目录里的web api为基础通用服务，可以为任何对象服务

通过加@Auth("对象类型")来标识一个controller类或类里某个方法能否被某种对象用户访问,如果为@Auth("noauth")表示无需验证

>注:我们不应该在controller里写太多太复杂的逻辑，业务逻辑都封装在service里