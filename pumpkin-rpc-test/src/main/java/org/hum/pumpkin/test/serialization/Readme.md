### 序列化
##### 1.功能性
###### 考虑各种基本数据类型（8大数据类型）
###### 考虑如何序列化数组、集合等类型
###### 考虑如何序列化内部对象
###### 考虑如何序列化继承
###### 其他杂项：例如不序列化transient、static修饰符；
###### 考虑特殊对象：Date、BigDecimal、BigInteger、枚举
###### 考虑字段变动：1-顺序改变；2-新增字段；3-删除字段；4-字段类型改变
###### 相互引用问题：例如A对象引用B对象，B对象引用A对象
###### 比较serializableID
###### 处理Null值

##### 2.性能优化
###### 考虑序列化速度快慢
###### 观察序列化后的字节大小
###### 采用缓存区write，不要每次write
###### 采用NIO模型
###### 优化细节
	1.整型：
	2.字符串：取分字符集？ascii和utf8



Kryo 
	1.字段变动很不稳定，由于内部采用字典排序，因此稳定性与改动字段名称有关
	2.
	
Hessian
	1.序列化包含类型，即便反序列化时不需要传入ClassType，也可以正确解析出原始对象。
	2.新增/删除字段不会报错；改变类型会报错；改变字段顺序没有问题

#####原始对象
######EmployeeModel [employeeNo=EMP1551170588749, salary=5000.0, tasks=null, toString()=HumanModel [id=1551170588749, name=zhangsan, birthday=2019-02-26, sex=false]]
######Kryo:2019-02-2�EMP155117058874���Ə�Zzhangsa�@��       
######Hessian:Mt 6org.hum.pumpkin.test.serialization.model.EmployeeModelS salaryD@��     S employeeNoS EMP1551169102469S idL  i(�"�S nameS zhangsanS birthdayS 2019-02-26S sexFS tasksNz
######JDK:�� sr 6org.hum.pumpkin.test.serialization.model.EmployeeModel�R^�?�' L employeeNot Ljava/lang/String;L salaryt Ljava/lang/Double;L taskst Ljava/util/List;xr 3org.hum.pumpkin.test.serialization.model.HumanModel���^��� L birthdayq ~ L idt Ljava/lang/Long;L nameq ~ L sext Ljava/lang/Boolean;xpt 2019-02-26sr java.lang.Long;��̏#� J valuexr java.lang.Number������  xp  i(�/Gt zhangsansr java.lang.Boolean� r�՜�� Z valuexp t EMP1551170809671sr java.lang.Double���J)k� D valuexq ~ @��     p

##### 其他序列化：jackson/gson/xml，相比上面的Kryo、Hessian，Fst等也不是一无是处，json和xml类型的序列化协议在编码后的数据具备更加的可读性。
	
	

v2
 使用byte[] 替换DataInputStream/DataOutputStream

