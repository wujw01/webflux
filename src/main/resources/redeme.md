#Mono
   ###Mono 是什么？ 
    
        官方描述如下：A Reactive Streams Publisher with basic rx operators that completes successfully by emitting an element, or with an error.

        Mono 是响应流 Publisher 具有基础 rx 操作符，可以成功发布元素或者错误，如图所示：

   ###[mono图片地址]http://springforall.ufile.ucloud.com.cn/static/img/9e9fc4aec1e96acb7cdc942aad0967e21523363

   ###Mono 常用的方法有：
    
    Mono.create()：使用 MonoSink 来创建 Mono。
        
    Mono.justOrEmpty()：从一个 Optional 对象或 null 对象中创建 Mono。
        
    Mono.error()：创建一个只包含错误消息的 Mono。
        
    Mono.never()：创建一个不包含任何消息通知的 Mono。
        
    Mono.delay()：在指定的延迟时间之后，创建一个 Mono，产生数字 0 作为唯一值。
#Flux
   ### Flux 是什么？
    官方描述如下：A Reactive Streams Publisher with rx operators that emits 0 to N elements, and then completes (successfully or with an error).

    Flux 是响应流 Publisher 具有基础 rx 操作符，可以成功发布 0 到 N 个元素或者错误。Flux 其实是 Mono 的一个补充，如图所示：
   
   ###[flux图片地址]http://springforall.ufile.ucloud.com.cn/static/img/37dd113ad50858e41d17143911696e401523363
   
    所以要注意：如果知道 Publisher 是 0 或 1 个，则用 Mono。

    Flux 最值得一提的是 fromIterable 方法，fromIterable(Iterable<? extends T> it) 可以发布 Iterable 类型的元素。当然，Flux 也包含了基础的操作：map、merge、concat、flatMap、take，这里就不展开介绍了。