



# **ArrayList底层结构和数据结构分析**

> 1. ArrayList中维护了一个Object数组elementData 。
> 2. 当创建ArrayList对象时，如果使用的是无参构造器，则elementData数组的长度为0，第一次添加元素，elementData数组容量扩展为10.
> 3. 如果容量不足则会进行扩容，newCapacity = oldCapacity + oldCapacity>>1。
> 

## ArrayList无参构造器开始分析源码

```java
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("a");
        }
        list.add("b");
        list.add("b");
        list.add("b");
        list.add("b");
```

### 第一步 查看`ArrayList`无参构造器源码

下面来分析在调用无参构造器时底层做了哪些？

```java
    public ArrayList() {
        //private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
        //将DEFAULTCAPACITY_EMPTY_ELEMENTDATA 赋值给  elementData
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```

### 第二步 查看`ArrayList`的`add(E e)`方法源码

```java
    //将指定的元素附加到此列表的末尾。
	public boolean add(E e) {
        //每次添加元素都需要确认内部Object数组容量  
        //第一次添加元素时，size=0进来
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
```

### 第三步 查看`ensureCapacityInternal(int minCapacity)`

```java
    private void ensureCapacityInternal(int minCapacity) {
        ////第一次添加元素时，minCapacity = size + 1 = 0 + 1 = 2
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
```

### 第三步 查看私有方法`ensureCapacityInternal(int minCapacity)`

```java
    private void ensureCapacityInternal(int minCapacity) {
        ////第一次添加元素时，minCapacity = size + 1 = 0 + 1 = 2
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }
```
### 第四步 查看私有方法`calculateCapacity(Object[] elementData, int minCapacity)`
```java
//第一次添加元素时 elementData = {},minCapacity = 1
private static int calculateCapacity(Object[] elementData, int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        //只有第一次添加元素的时候才会走到这里 
        //private static final int DEFAULT_CAPACITY = 10;
        //第一次添加元素当前方法会返回DEFAULT_CAPACITY与size + 1 中最大的那个
        return Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    //后面添加元素时，上面的if分支不会走进去。也就是说，后面calculateCapacity方法返回的数据就是minCapacity
    return minCapacity;
}
```

>  `calculateCapacity` `return`10,现在回到 `ensureCapacityInternal`方法，进一步看下`ensureExplicitCapacity`方法

### 第五步 查看私有方法`ensureExplicitCapacity(int minCapacity)`

```java
	//记录集合结构被修改的次数 迭代器抛出的ConcurrentModificationExceptions和这个变量有关系
    protected transient int modCount = 0;    
    //显而易见，第一次添加元素时，minCapacity=0
	private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        //protected transient int modCount = 0;
		//minCapacity大于elementData.length时就会执行grow()方法，也就是对集合进行扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
```

### 第六步 查看私有方法`grow(int minCapacity)`

```java
    /**
     *	增加集合容量，确保其能容纳minCapacity个元素
     * @param minCapacity the desired minimum capacity
     */
	private void grow(int minCapacity) {
        // 第一次添加元素，如果ArrayList是无参构造器创建的，elementData.length=0
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //如果newCapacity < minCapacity 则 newCapacity = minCapacity 
        //这里其实就是在解决如果第一次添加元素，elementData.length = 0,上面的算法存在漏洞的问题
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 这里对数组的最大大小做了限制，因为虚拟机要在数组中保存一些header words
            // private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
            newCapacity = hugeCapacity(minCapacity);
        // 经过上面一系列的神之操作之后，newCapacity已经确定了，下面是将数组中已经存在的值copy到新的length为newCapacity的新的数组中
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```

### 第七步 查看私有方法`hugeCapacity(int minCapacity)`

```java
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```

   这个可以思考下newCapacity能否为Integer.MAX_VALUE?

   > 可以,  但是需要满足两个条件。
   >
   > - newCapacity> MAX_ARRAY_SIZE
   > - minCapacity> MAX_ARRAY_SIZE
   >
   > 但是，根据源码中对MAX_ARRAY_SIZE字段的注释，
   >
   > ```
   > /**
   >  * The maximum size of array to allocate.
   >  * Some VMs reserve some header words in an array.
   >  * Attempts to allocate larger arrays may result in
   >  * OutOfMemoryError: Requested array size exceeds VM limit
   >  */
   > ```
   >
   > 可以看出，数组的最大容量最好为`MAX_ARRAY_SIZE`,如果超出这个值，很大可能会抛出   `OutOfMemoryError`



**==回到`grow`方法中，新的数组创建并copy已存在集合中的元素后，将新的数组指向`elementData`。==**

### 第八步 再次回到`add(E e) `

```java
	public boolean add(E e) {
        //经过上面一系列的操作之后，我们得出ensureCapacityInternal做了如下几件事情。
        //1. modCount 加 1
        //2. elementData Array的扩容
        ensureCapacityInternal(size + 1);  
        //现在只需要把要添加的元素添加到扩容后的新数组中即可
        elementData[size++] = e;
        return true;
    }
```




3. **为什么ArrayList里面的elementData为什么要用transient来修饰？**

   > 因为elementData里面不是所有的元素都有数据，因为容量的问题，elementData里面有一些元素是空的，这种是没有必要序列化的。
   > ArrayList的序列化和反序列化依赖writeObject和readObject方法来实现。这样可以避免序列化空的元素。