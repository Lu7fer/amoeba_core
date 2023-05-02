# Amoeba_core

## description

Amoeba是一个使用kotlin语言编写的基于Spring-Context的网页服务器框架. 该框架完全使用程序和注解配置, 无配置文件.
这是一个小巧的核心, 采用Jetty作为网页服务器, 使用H2数据库, 两者均为嵌入式程序, 模仿SpringBoot的启动方式.

## details

### technical stack

- Container: Spring-Context 6.0
- Server: Jetty 11.0
- DataSource: H2 Database 2.1
- ORM: Spring Data Jpa 2.7 (Hibernate 6.2)
- Logger: Custom Implemented Slf4j 2.0
- JsonConverter: Jackson 2.14

### SPI 加载

Core使用Java SPI加载 `cf.vbnm.amoeba.core.spi.Starter` 实现类, 并将该实现类作为Spring Configuration加载进入Spring容器以实现子模块加载.

### Amoeba Event Filter

该功能可以识别散落在Spring Component各处被相应注解标记的方法, 在发出事件之后执行, 并可以拦截该事件.

## TODO

前端页面

## 开源协议

![GPLv3](https://www.gnu.org/graphics/gplv3-or-later.svg)

[协议详情](https://www.gnu.org/licenses/gpl-3.0.txt)