注意事项

1、该项目为一体机/APP企业端公用项目;

2、布局文件夹区分布局：layout-land为一体机，layout-port为APP，values文件夹同理；

3、接入的打印机SDK有三款，其中usb有两款用于一体机打印标签功能，在Constants.java文件中可配置新旧打印机，蓝牙打印机用于APP打印标签功能；

4、项目请求地址和端口可动态配置，同时也可在Constants.java配置默认值；

5、在打包时注意修改AndroidManifest.xml文件：

    (1) APP：搜索 landscape，替换成 portrait；

    (2) 一体机： 搜索 portrait，替换成 搜索 landscape；

 6、打包时注意修改app/build.gradle文件，versionCode/versionName往上加版本：

 7、在PC管理端上传版本：
    请求地址：http://192.168.3.209:9999/web-waste
    账号信息：admin / gdgf123zxc!@!

 8、PC企业端：
    请求地址：http://192.168.3.209:9998/web-waste-qy
    账号信息：gzcsqy01 / gdgf123zxc!@!
    swagger地址： http://192.168.3.209:9998/ent-waste-qy/login


 项目功能

 1、登录模块
    （1）登录模式：账号密码登录/卡片登录；
    （2）登录检查更新功能；
    （3）切换访问IP、端口、协议功能；
    （4）debug模式
