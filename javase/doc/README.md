分布式锁设计思想
对于单进程的并发场景，我们可以使用synchronized关键字和Reentranlock类等。对于分布式场景，我们可以使用分布式锁
创建锁
多个jvm服务器之间，同时在zookeeper上创建相同的一个临时节点，因为临时检点路径保证唯一，只要谁能够创建节点成功，谁就能够获取到锁，
没有创建成功节点，只能注册个监听器监听这个锁并进行等待，当释放锁的时候，采用事件通知给其他客户端重新获取锁的资源。这时候客户端使用
事件监听，如果该临时节点被删除的话，重新进入获取锁的步骤。
释放锁
zookeeper使用直接关闭临时节点session绘画连接，因为临时节点生命周期与session绘画绑定在一起，如果session绘画连接关闭的话，该
临时节点也会被删除，这时候客户端使用事件监听，如果该临时节点被删除的话，重新进入到获取锁的步骤。
------------------------------------------------------------------------------------------------------------------------
















