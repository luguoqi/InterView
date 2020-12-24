# Kettle(Pentaho Data Integration)

## 简介

​		Kettle是一款免费开源的、可视化的、功能强大的ETL工具集，它允许你管理来自不同数据库的数据。他是基于java的免费开源的软件，对商业用户也没有限制，可以在Window、Linux、Unix上运行，绿色无需安装，数据抽取高效稳定。有另种脚本文件，transformation和job，transformation完成针对数据的基础转换，job则完成整个工作流的控制，并且可以通过图形界面设计实现做什么业务，而无需写代码去实现。在Job下的start模块，有一个定时功能，可以每日，每周等方式进行定时。Kettle家族有四大工具，分别为SPOON: 允许你通过图形界面来设计ETL转换过程（Transformation）；PAN:Pan是一个后台执行的程序，没有图形界面，类似于时间调度器；CHEF:任务通过允许每个转换，任务，脚本等等，更有利于自动化更新数据仓库的复杂工作；KITCHEN:批量使用由Chef设计的任务。现已更名为Pentaho Data Integration



