===============准备工作=================
1. start.spring.io初始化gradle项目
2. 用gradlew来统一版本,用的是3.5版本,服务器在国外,需要翻墙
3. 用阿里云的maven仓库镜像加速
4. 导入eclipse
===============编码工作==================
1. 先写一个hello world项目,编写一个mockmvc的测试
===============说明=====================
1. 当springboot项目对jpa进行了封装整合,当我们工程引用到此项目的时候,如果没有数据库的配置,会报错.
2. 这时候我们可以引入运行时的h2内存数据库.
3. 只要配置了数据源,h2内存数据库就不再被使用了
==================restful===============
因为接口是参考了restful风格,所以返回对象要作一个统一的封装(封装成对象)
==================测试===================
blog-user项目测试:
1. localhost:8080/    ===> 首页index 测试页面
2. localhost:8080/admins


3. 写博客 ====> localhost:8080/u/{username}/blogs/edit
4. 发布 =====> localhost:8080/u/{username}/blogs/{id}
5. 删除 ===> localhost:8080/u/{username}/blogs

6. 输入:localhost:8080/u/{username} ===> localhost:8080/u/{username}/blogs
7. localhost:8080/u/{username}/blogs 为用户的主页
===================改进===================
1. 登录界面增加非空校验等等    controller增加@valid注解等等
2. 权限优化,很多界面应该没有授权不能访问等等
===================增加点赞模块=============
首先先对点赞来建模,建立Vote实体
@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER) 看黑马视频
====================运行===================
gradle builder   ===>build/libs
推荐用gradlew build , java -jar builder/libs/demo.jar
==========================================
ssh ubuntu@111.230.227.31   /p: 12345hadoop
==========================================
安装并启动 FTP 服务
sudo apt install vsftpd -y	 || sudo yum install vsftpd -y
安装完成后，启动 FTP 服务：
service vsftpd start
启动后，可以看到系统已经监听了 21 端口
netstat -nltp | grep 21
此时，访问 ftp://139.199.227.57 可浏览机器上的 /var/ftp 目录了。
==========================================
王尼玛没烦恼