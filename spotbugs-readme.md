1. EC_UNRELATED_TYPES
Bug: Call to equals() comparing different types Pattern id: EC_UNRELATED_TYPES, type: EC, category: CORRECTNESS
解释：
两个不同类型的对象调用equals方法，如果equals方法没有被重写，那么调用object的==，永远不会相等；如果equals方法被重写，而且含有instanceof逻辑，那么还是不会相等。
解决方法：
应该改为str.toString()
2. IM_BAD_CHECK_FOR_ODD
Bug: Check for oddness that won't work for negative numbers Pattern id: IM_BAD_CHECK_FOR_ODD, type: IM, category: STYLE
解释：
如果row是负奇数，那么row % 2 == -1，
解决方法：
考虑使用x & 1 == 1或者x % 2 != 0
3. NP_ALWAYS_NULL
Pattern: Null pointer dereference id: NP_ALWAYS_NULL, type: NP, category: CORRECTNESS
A null pointer is dereferenced here. This will lead to a NullPointerException when the code is executed.
4. RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE
Bug: Redundant nullcheck of bean1, which is known to be non-null Pattern id: RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE, type: RCN, category: STYLE
This method contains a redundant check of a known non-null value against the constant null.
这种方法包含了一个称为非空对空值的不断重复检查。
修改为：
5. SS_SHOULD_BE_STATIC
Bug: Unread field: ADDRESS_KEY; should this field be static? Pattern id: SS_SHOULD_BE_STATIC, type: SS, category: PERFORMANCE
This class contains an instance final field that is initialized to a compile-time static value. Consider making the field static.
解释：
final成员变量表示常量，只能被赋值一次，赋值后值不再改变。
这个类包含的一个final变量初始化为编译时静态值。考虑变成静态常量
解决方法：
增加static关键字
6. EQ_COMPARETO_USE_OBJECT_EQUALS
Bug: RsInterface defines compareTo(Object) and uses Object.equals() Pattern id: EQ_COMPARETO_USE_OBJECT_EQUALS, type: Eq, category: BAD_PRACTICE
解释：
第一段代码，没有使用instanceof判断就直接转型，有抛出classcastexception异常的可能。
这个BUG主题是，遵守约定(x.compareTo(y)==0) == (x.equals(y))，强烈建议，但不严格要求。
在return 0的时候，调用equals方法返回true，因为在PriorityQueue.remove方法中，1.5使用的是compareTo方法，而1.6使用的是equals方法，保证环境升级的时候，受影响最小。
解决方法：
在return 0的时候，调用equals方法返回true
7. NM_METHOD_NAMING_CONVENTION
Bug: The method name MsmPlanDAOTest.TestViewMsmPlanList() doesn't start with a lower case letter Pattern id: NM_METHOD_NAMING_CONVENTION, type: Nm, category: BAD_PRACTICE
Methods should be verbs, in mixed case with the first letter lowercase, with the first letter of each internal word capitalized.
解释：
方法应该是动词，与第一个字母小写混合的情况下，与每个单词的首字母大写的内部。
解决方法：
方法名称小写就通过了。
8. HE_EQUALS_USE_HASHCODE
Bug: PerfmSingleGraphPanel$RSCategory defines equals and uses Object.hashCode() Pattern id: HE_EQUALS_USE_HASHCODE, type: HE, category: BAD_PRACTICE
解释：
重载了equals方法，却没有重载hashCode方法，如果使用object自己的hashCode，我们可以从JDK源代码可以看到object的hashCode方法是native的，它的值由虚拟机分配（某种情况下代表了在虚拟机中的地址或者唯一标识），每个对象都不一样。所以这很可能违反“Equals相等，hashcode一定相等；hashcode相等，equals不一定相等。”除非你保证不运用到HashMap/HashTable等运用散列表查找值的数据结构中。否则，发生任何事情都是有可能的。
关于何时改写hashcode，请参考：在重写了对象的equals方法后，还需要重写hashCode方法吗?
关于编写高质量的equals方法：
1.先使用==操作符检查是否是同一个对象，==都相等，那么逻辑相等肯定成立；
2.然后使用instanceof操作符检查“参数是否为正确的类型”；
3.把参数转换成正确的类型；
4.对于该类中的非基本类型变量，递归调用equals方法；
5.变量的比较顺序可能会影响到equals方法的性能，应该最先比较最有可能不一致的变量，或者是开销最低的变量。
当你编写完成equals方法之后，应该问自己三个问题：它是否是对称的、传递的、一致的？
解决方法：
除非你保证不运用到HashMap/HashTable等运用散列表查找值的数据结构中，请重写hashcode方法。
9. NM_CONFUSING
Bug: Confusing to have methods xxx.SellerBrandServiceImpl.getAllGrantSellerBrandsByBrandId(long) and xxx.DefaultSellerBrandManager.getALLGrantSellerBrandsByBrandId(long) Pattern id: NM_CONFUSING, type: Nm, category: BAD_PRACTICE
The referenced methods have names that differ only by capitalization.
解释：
同一个包两个类中有一模一样的两个方法（包括参数）
解决方法：
最好可以修改为不一样的方法名称
10. MF_CLASS_MASKS_FIELD
Bug: Field PDHSubCardInstanceDialogCommand.m_instance masks field in superclass ViewNEProperity Pattern id: MF_CLASS_MASKS_FIELD, type: MF, category: CORRECTNESS
This class defines a field with the same name as a visible instance field in a superclass. This is confusing, and may indicate an error if methods update or access one of the fields when they wanted the other.
解释：
这是什么意思呢？想要字段也能够具有多态性吗？太迷惑了。
当你想要更新一个m_instance时，你要更新哪个？你用到它时，你知道哪个又被更新了？
解决方法：
要么去掉其中一个字段，要么重新命名。
11. NM_CLASS_NAMING_CONVENTION
Bug: The class name crossConnectIndexCollecter doesn't start with an upper case letter
解释： Pattern id: NM_CLASS_NAMING_CONVENTION, type: Nm, category: BAD_PRACTICE
看到这样的命名方式，我第一个反映就是有点晕车！
解决方法：
类名第一个字符请大写。
12. RE_POSSIBLE_UNINTENDED_PATTERN
Bug: "." used for regular expression Pattern id: RE_POSSIBLE_UNINTENDED_PATTERN, type: RE, category: CORRECTNESS
解释：
String的split方法传递的参数是正则表达式，正则表达式本身用到的字符需要转义，如：句点符号“.”，美元符号“$”，乘方符号“^”，大括号“{}”，方括号“[]”，圆括号“()” ，竖线“|”，星号“*”，加号“+”，问号“?”等等，这些需要在前面加上“\\”转义符。
解决方法：
在前面加上“\\”转义符。
13.IA_AMBIGUOUS_INVOCATION_OF_INHERITED_OR_OUTER_METHOD
外部类：
内部类：
……
Bug: Ambiguous invocation of either an outer or inherited method JExtendDialog.onOK() Pattern id: IA_AMBIGUOUS_INVOCATION_OF_INHERITED_OR_OUTER_METHOD, type: IA, category: STYLE
解释：
TargetSetupDialog是JExtendDialog的子类，JExtendDialog有一个onOK方法，但是JExtendDialog的外部类也有一个onOK方法，到底这个onOK方法调用的是它父类onOK方法还是调用它外部类onOK方法呢，这不免让人误解。
当然这并没有编译错误，实际上优先调用的是父类JExtendDialog的onOK方法，如果把JExtendDialog的onOK方法去掉，它调用的就是外部类onOK方法，这个时候不能写成this.onOK，因为此时的this并不代表外部类对象。
解决方法：
如果要引用外部类对象，可以加上“outclass.this”。
如果要引用父类的onOK方法，请使用super.onOK()。
14. DM_FP_NUMBER_CTOR
Bug: Method OnlineLicenseDAOTest.testUpdateOnlineLicenseByOnlineMerchantId() invokes inefficient Double.valueOf(double) constructor; use OnlineLicenseDAOTest.java:[line 81] instead Pattern id: DM_FP_NUMBER_CTOR, type: Bx, category: PERFORMANCE
Using new Double(double) is guaranteed to always result in a new object whereas Double.valueOf(double) allows caching of values to be done by the compiler, class library, or JVM. Using of cached values avoids object allocation and the code will be faster.
Unless the class must be compatible with JVMs predating Java 1.5, use either autoboxing or the valueOf() method when creating instances of Double and Float.
解释：
采用new Ddouble(double)会产生一个新的对象，采用Ddouble.valueOf(double)在编译的时候可能通过缓存经常请求的值来显著提高空间和时间性能。
解决方法：
采用Ddouble.valueOf方法
类似的案例
15. CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE
Bug:AlarmSoundManager$SoundProperty defines clone() but doesn't implement Cloneable Pattern id: CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE, type: CN, category: BAD_PRACTICE
解释：
SoundProperty类实现了clone方法，但是没有实现Cloneable接口，当然这没有任何问题，但是你应该知道你为什么这么做。
解决方法：
最好实现Cloneable接口
16. STCAL_INVOKE_ON_STATIC_DATE_FORMAT_INSTANCE
Bug: Call to method of static java.text.DateFormat Pattern id: STCAL_INVOKE_ON_STATIC_DATE_FORMAT_INSTANCE, type: STCAL, category: MT_CORRECTNESS
private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");format.format(new Date());同一个format多次调用会导致线程不安全，可以改为：private static final String COMMON_DATE = "dd/MM/yyyy";SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE);format.format(new Date());解释：
TIME_FORMAT是一个DateFormat静态变量，文档中DateFormat不是线程安全（多个线程访问一个类时，这些线程执行顺序没有统一的调度和约定，如果这个类的行为仍然是正确的，那么这个类就是线程安全的。考虑vector的实现）的，如果多个线程同时访问，会出现意料不到的情况，详情参见Sun Bug #6231579和Sun Bug #6178997。
因此对于DateFormat、SimpleDateFormat、Calendar类对象不建议定义成静态成员字段使用，同时对它们在多线程环境下的使用请一定要保证同步。
另外，多说一句，java为我们提供了很多的封装手段，比如private关键字、内部类、全限定包名等等，我们要充分利用这些手段封装信息，对外尽量提供最小集。关于静态变量也是如此，就算是vector这种线程安全的类，在无状态类中也可能存在并发的问题，参见：无状态类在并发环境中绝对安全吗？
解决方法：
修改类字段为对象字段，然后改为private，同时提供get方法，最后对get方法实现同步机制。
最好连对象字段也去掉，直接在方法里使用，就不存在同步的问题了（不必考虑性能问题，而且DateFormat本身就不必作为对象的字段，我想这也是sun为什么不把它实现为线程安全的了）。
17. SE_NO_SERIALVERSIONID
Bug: WindowHandlerManager$MySingleSelectionModel is Serializable; consider declaring a serialVersionUID Pattern id: SE_NO_SERIALVERSIONID, type: SnVI, category: BAD_PRACTICE
This class implements the Serializable interface, but does not define a serialVersionUID field. A change as simple as adding a reference to a .class object will add synthetic fields to the class, which will unfortunately change the implicit serialVersionUID (e.g., adding a reference to String.class will generate a static field class$java$lang$String). Also, different source code to bytecode compilers may use different naming conventions for synthetic variables generated for references to class objects or inner classes. To ensure interoperability of Serializable across versions, consider adding an explicit serialVersionUID.
解释：
实现了Serializable接口，却没有实现定义serialVersionUID字段，序列化的时候，我们的对象都保存为硬盘上的一个文件，当通过网络传输或者其他类加载方式还原为一个对象时，serialVersionUID字段会保证这个对象的兼容性，考虑两种情况：
1. 新软件读取老文件，如果新软件有新的数据定义，那么它们必然会丢失。
2. 老软件读取新文件，只要数据是向下兼容的，就没有任何问题。
序列化会把所有与你要序列化对象相关的引用（包括父类，特别是内部类持有对外部类的引用，这里的例子就符合这种情况）都输出到一个文件中，这也是为什么能够使用序列化能进行深拷贝。这种序列化算法给我们的忠告是，不要把一些你无法确定其基本数据类型的对象引用作为你序列化的字段（比如JFrame），否则序列化后的文件超大，而且会出现意想不到的异常。
解决方法：
定义serialVersionUID字段
18.SE_COMPARATOR_SHOULD_BE_SERIALIZABLE
Bug: ToStringComparator implements Comparator but not Serializable Pattern id: SE_COMPARATOR_SHOULD_BE_SERIALIZABLE, type: Se, category: BAD_PRACTICE
解释：
ToStringComparator类实现了Comparator接口却没有实现Serializable接口，因为像TreeMap这种可序列化数据结构（它实现了Serializable接口）只有当比较器继承了Serializable接口时，它才能被序列化。
解决方法：
实现Serializable接口并定义serialVersionUID字段
19. ES_COMPARING_STRINGS_WITH_EQ
Bug: Comparison of String objects using == or != Pattern id: ES_COMPARING_STRINGS_WITH_EQ, type: ES, category: BAD_PRACTICE
解释：
你确定你已经了解string的全部了？
如果你不了解，请参考FX大神的博文：请别再拿“String s = new String("xyz");创建了多少个String实例”来面试了吧
那么，接下来我就开始剥皮了： Object和StringBuilder的toString方法都是返回一个new String()，跟””不相等。
如果你之前是这样的定义的：String name = “”;OK，它们处于同一个class常量池，跟””相等。
如果在这之前，你使用了String. Intern方法，你是高手，跟””相等。
如果你没有意识到这些问题，却仍然使用==和!=去比较字符串，那么请不要告诉我是你手滑了= =!
解决方法：
老实使用equals方法吧，至少为了保持代码的清晰性。
20. ES_COMPARING_STRINGS_WITH_EQ
Bug: Comparison of String parameter using == or != Pattern id: ES_COMPARING_PARAMETER_STRING_WITH_EQ, type: ES, category: BAD_PRACTICE
解释：
跟前面的例子差不多，你如果不能确保propertyName来源于常量池，那么用==比较没有一点意义，难不成你告诉我这能提高性能？ 如果有功夫为这点性能担惊受怕，还不如花点时间去找找性能瓶颈。
解决方法：
使用equals方法
21. IM_AVERAGE_COMPUTATION_COULD_OVERFLOW
Bug: Computation of average could overflow Pattern id: IM_AVERAGE_COMPUTATION_COULD_OVERFLOW, type: IM, category: STYLE
解释：
参照了Findbugs的解释，(low+high)/2当平均数过大的时候（难道是超过了int最大值？）会溢出，会出现一个负值，此问题出现在早期实现的二进制搜索和归并排序，但是已经被修复了。参见Joshua Bloch（google首席java架构师）widely publicized the bug pattern（需翻墙）.
解决方法：
建议使用无符号右移位运算符：use (low+high) >>> 1
22. SC_START_IN_CTOR
Bug: new AsyncCentral() invokes AsyncCentral$FireThread.start() Pattern id: SC_START_IN_CTOR, type: SC, category: MT_CORRECTNESS
解释：
构造方法里重启新的线程，我还是第一次见过这样写的。
首先说明三点：
1. 对象的创建一般分两步走，在堆上new对象操作，执行<init>方法（包含构造方法），为什么我们开发人员看见的只有一步，那是因为JVM不想让开发人员在这个过程中插上一脚，破坏对象的初始化流程。
2. 类的加载和初始化是由虚拟机保证同步的，但是对象的生成和初始化就没有任何同步机制来保证了。
3. 构造器不能加synchronized，是一项程序语言设计上的选择(见:JLS 8.8.3 Constructor Modifiers)，正常情况下，是不需要加上synchronized，但不代表所有的情况都不要加上synchronized，更不能认为一个构造器隐含的就是一个synchronized。
那什么时候构造方法需要同步呢？通常来说，<init>方法在生成对象的时候只被执行一次，一般new对象的操作可能因为JVM自身的关系保证原子性操作（自己臆测的，没有任何根据），所以我们经常不用关心构造方法同步的问题。但是上述情况就不一样了，在构造方法中新启线程，如果AsyncCentral是一个状态类，FireThread线程极有可能对AsyncCentral的状态进行反复读取和写入，更严重的一种情况是，AsyncCentral有父类，极有可能在父类的构造方法还没开始前，FireThread线程就已经开始执行并对AsyncCentral的状态进行“破坏”了。这个时候，就有两个线程来对AsyncCentral的状态进行操作了（一个是执行<init>方法的线程，一个是FireThread线程），自然而然，就会存在同步的问题了。
多数时候，我们没有发现，可能是AsyncCentral类没有状态，或者是时候未到，我想说的是，我们写的大部分程序都存在同步的问题，本例子就是其中一个，值得我们好好思考。
另一种理解（觉得更靠谱，来自于Java.Concurrency.in.Practice）叫做“对象逃逸”，意思就是说在构造方法里，this是可以访问的到的，同一时间，FireThread线程而是可以访问到this对象的，所以这时候this就从<init>方法线程逃逸到了FireThread线程中，这时候初始化就会存在并发问题。
解决方法：
不要再构造方法中新启线程，可以提供init方法，其他方法根据实际情况而定。
23. EQ_SELF_USE_OBJECT
Bug: ManageItem defines equals(ManageItem) method and uses Object.equals(Object) Pattern id: EQ_SELF_USE_OBJECT, type: Eq, category: CORRECTNESS
解释：
这是重载，不是覆盖，除非你能保证其他人调用这个方法传入的参数都是ManageItem 的，否则会调用Object的boolean equals(Object)方法，这样的话根本就不会跑到这个方法里来！！很多所谓的大牛都会犯这么一个错误，我坚信这是你手滑了。
解决方法：
如果你想覆盖父类的方法，请在上面加上@Override注解，它会防止这种错误的出现（透露一个小细节，JDK1.5覆盖接口方法时加上@Override编译器会报错，JDK1.6修正，这可能是当初实现者对@Override注解理解的问题）。
24.DLS_DEAD_LOCAL_STORE
案例二：
Bug: Dead store to date Pattern id: DLS_DEAD_LOCAL_STORE, type: DLS, category: STYLE
解释：
先看看，我们的程序有多少个这样的例子：
真是伤不起啊，不知道当时的作者这是神马意图？手滑？还是眼花？虽然说这不是神马问题，也不会对程序性能造成多大的影响，但是这就像一颗沙子，我们每个程序员对待程序都应该是眼里不能进沙子的态度，当然，你非要这么写，我也没神马可说的。
By the way：
对本地变量定义了之后未使用到，编译器能够做优化处理，也就是在编译之后的class文件中删除这些本地变量。方法是在eclipse的Preferences里将以下的钩去除：
解决方法：
大胆的去掉或者注释掉。
误报的案例：
上述案例二种： IntegralItemDO integralItem = new IntegralItemDO();
是一个局部的变量，不需要定义到外部去，定义在外部，可能会变成一个无效的变量。
25.FE_TEST_IF_EQUAL_TO_NOT_A_NUMBER
Bug: Doomed test for equality to NaN Pattern id: FE_TEST_IF_EQUAL_TO_NOT_A_NUMBER, type: FE, category: CORRECTNESS
解释：
我也开眼界了，照搬Findbugs的理解：
大概意思就是说Nan很特殊（表示未定义和不可表示的值），没有任何值跟它相等，包括它自身，所以x == Double.NaN永远返回false。
解决方法：
如果要检查x是特殊的，不是一个数值，请用Double.isNaN(x)方法。
26. FI_EMPTY
Bug: FilterIPConfigDialog.finalize() is empty and should be deleted Pattern id: FI_EMPTY, type: FI, category: BAD_PRACTICE
解释：
空的finalize方法，有什么用？
根据JDK文档， finalize() 是一个用于释放非 Java 资源的方法。但是， JVM 有很大的可能不调用对象的finalize() 方法，因此很难证明使用该方法释放资源是有效的。
解决方法：
删除掉finalize方法
27.REC_CATCH_EXCEPTION
Bug: Exception is caught when Exception is not thrown Pattern id: REC_CATCH_EXCEPTION, type: REC, category: STYLE
解释：
我觉得有点迷惑，有些catch (Exception e)并没有被Findbugs捕捉到，开始以为它的意思是try catch里没有任何异常的产生，包括RuntimeException，但是后来我写了例子证明并不是这么回事。
总之，它的意思应该是说JVM对RuntimeException有统一的捕获机制（一般都是打印异常栈信息，然后向外抛，没有遇到Exception线程就死掉，EDT线程除外），你搞一个catch (Exception e)这样也把RuntimeException就捕获了。但是如果你的处理机制中没有针对这些异常，那就可能有问题了。通常来说，很多应用程序都把异常记录在日志之中，但是我觉得也应该同时打印在调试屏幕中，这样有利于开发人员调试。
比如上面的程序，假如发生了空指针异常，你只有去日志中才能看到，这对我们调试人员来说很不方便的。
解决方法：
其实这样写也没有问题（除非你有意），有时候我们确实需要捕获RuntimeException，比如我们有一个批处理，这个任务很重要，必须保证某个任务出了问题不能影响其他的任务，这个时候就可以在for循环内捕获RuntimeException，出现了异常还可以continue。
不过上面的例子最好再把异常信息打印到调试屏幕上。
28. DM_GC
Bug: DBExportTask2.exportDBRecords(DBExportProperty, String) forces garbage collection; extremely dubious except in benchmarking code Pattern id: DM_GC, type: Dm, category: PERFORMANCE
解释：
有两点：
1. System.gc()只是建议，不是命令，JVM不能保证立刻执行垃圾回收。
2. System.gc()被显示调用时，很大可能会触发Full GC。
GC有两种类型：Scavenge GC和Full GC，Scavenge GC一般是针对年轻代区（Eden区）进行GC，不会影响老年代和永生代（PerGen），由于大部分对象都是从Eden区开始的，所以Scavenge GC会频繁进行，GC算法速度也更快，效率更高。但是Full GC不同，Full GC是对整个堆进行整理，包括Young、Tenured和Perm，所以比Scavenge GC要慢，因此应该尽可能减少Full GC的次数。
解决方法：
去掉System.gc()
28. DP_DO_INSIDE_DO_PRIVILEGED
Bug: com.taobao.sellerservice.core.test.BaseTestJunit.autoSetBean() invokes reflect.Field.setAccessible(boolean), which should be invoked from within a doPrivileged block Pattern id: DP_DO_INSIDE_DO_PRIVILEGED, type: DP, category: BAD_PRACTICE
This code invokes a method that requires a security resourcePermission check. If this code will be granted security resourcePermissions, but might be invoked by code that does not have security resourcePermissions, then the invocation needs to occur inside a doPrivileged block.
此代码调用一个方法，需要一个安全权限检查。如果此代码将被授予安全权限，但可能是由代码不具有安全权限调用，则需要调用发生在一个doPrivileged的块。
30. MS_SHOULD_BE_FINAL
Bug: IPv4Document.m_strInitString isn't final but should be Pattern id: MS_SHOULD_BE_FINAL, type: MS, category: MALICIOUS_CODE
解释：
使用public和protected，别的包可以轻易修改它，如果你不想它被修改，请使用final。
封装很重要，不管是从维护方面和技术方面来说，都很重要，我不明白为神马有那么多的人把变量都写成public的（就算要给别人共享，也要提供get方法），特别是在并发环境中，特别特别注意类变量的共享，而且特别特别特别注意共享的这个变量是否是线程安全的。
解决方法：
加上final
31. NM_FIELD_NAMING_CONVENTION
Bug: The field name TopoControlPaneII.SyncSelection doesn't start with a lower case letter Pattern id: NM_FIELD_NAMING_CONVENTION, type: Nm, category: BAD_PRACTICE
解释：
为神马字段是大写开头的？喂神马？喂神马啊？
解决方法：
建议按照sun规定的命名方式
Bug: Field only ever set to null: RaisecomStatus.infoURL Pattern id: UWF_NULL_FIELD, type: UwF, category: CORRECTNESS
解释：
字段infoURL整个过程中一直为null，但却被用来作为分支判断的条件，不知道作者何意？难道真的是传说中的手滑？
解决方法：
这个就要问作者的意图了，当时你究竟要干神马来着？
32. MS_PKGPROTECT
Bug: ActionPatternManager.m_This should be package protected Pattern id: MS_PKGPROTECT, type: MS, category: MALICIOUS_CODE
解释：
Findbugs说，静态字段m_oThis应该是包权限的，如果是protected的话，可以被其他包访问到，其实个人觉得仅仅是封装范围的话是一个“小问题”，毕竟很多人都没意识到public、protected等关键字的重要性。但是我接着往下看：
单例模式？？这是神马单例模式？字段不是private，还是单例模式吗？我在任何一个地方继承UserManager，然后直接m_oThis = new UserManager();这还是一个单例吗？
在看看Findbugs为我们找出了多少个：
另外，我很客观的说一点，我们后怕，因为知道了真相，在想想我们实际情况中遇到很多不能复现的问题，我们有理由去知道这一切。
解决方法：
修改protected为private，然后将单例模式实现方式改为恶汉，或者双重校验锁定。
33. FI_USELESS
Pattern: Finalizer does nothing but call superclass finalizer id: FI_USELESS, type: FI, category: BAD_PRACTICE
解释：
finalize() 是一个用于释放非 Java 资源的方法，这里的finalize直接用Object的finalize方法，无任何意义。
解决方法：
勇敢去掉finalize()
34. NP_NULL_ON_SOME_PATH
Bug: Possible null pointer dereference of busCatId Pattern id: NP_NULL_ON_SOME_PATH, type: NP, category: CORRECTNESS
There is a branch of statement that, if executed, guarantees that a null value will be dereferenced, which would generate a NullPointerException when the code is executed. Of course, the problem might be that the branch or statement is infeasible and that the null pointer exception can't ever be executed; deciding that is beyond the ability of FindBugs.
解释：
方法中存在空指针
解决方法：
增加字段busCatId为空的判断
35. NP_NULL_ON_SOME_PATH
Bug:.HierarchicalManagerImpl.isExistByName(String, long) forgets to throw new exception.HierarchicalServiceException(String, Throwable) Pattern id: RV_EXCEPTION_NOT_THROWN, type: RV, category: CORRECTNESS
This code creates an exception (or error) object, but doesn't do anything with it. For example, something like
if (x < 0)
new IllegalArgumentException("x must be nonnegative");
It was probably the intent of the programmer to throw the created exception:
if (x < 0)
throw new IllegalArgumentException("x must be nonnegative");
解释：
此代码创建了一个异常（或错误）的对象，但并不做任何事情。
可能作者是想继续抛出异常信息吧，可是却产生了一个对象，啥也不干。
解决方法：
抛出这个错误
36. FI_FINALIZER_NULLS_FIELDS
Bug: CustomerResTreeDialog.java:[line 67] is set to null inside finalize method Pattern id: FI_FINALIZER_NULLS_FIELDS, type: FI, category: BAD_PRACTICE
解释：
关于finalize方法，前面应该已经介绍过了，所以m_UniResTree = null，纯粹是多此一举，没有任何意义。
解决方法：
勇敢去掉finalize()
37. FI_PUBLIC_SHOULD_BE_PROTECTED
Bug: FilterIPConfigDialog.finalize() is public; should be protected Pattern id: FI_PUBLIC_SHOULD_BE_PROTECTED, type: FI, category: MALICIOUS_CODE
解释：
Finalize方法不是protected的，当然你写成public也没错，依然可以覆盖父类中的finalize方法。
解决方法：
勇敢去掉finalize()
38. IS2_INCONSISTENT_SYNC
Bug: Inconsistent synchronization of URLAlarmMonitor.m_Counter; locked 50% of time Pattern id: IS2_INCONSISTENT_SYNC, type: IS, category: MT_CORRECTNESS
解释：
m_Counter只锁住了50%，它还是处于线程不安全的状态，如果一个字段只被read，那么它是线程安全的，不需要提供额外的同步开销，可以定义为final的（参考不可变类的实现），如果既有read也有write，那么就必须保证每个get和set方法都同步，而不能像上面一样，只对set方法进行了同步。
解决方法：
对get和set方法都实行同步。
39. LI_LAZY_INIT_UPDATE_STATIC
Bug: Incorrect lazy initialization and update of static field MonitorRuleManager.m_This Pattern id: LI_LAZY_INIT_UPDATE_STATIC, type: LI, category: MT_CORRECTNESS
解释：
此问题的m_This也是protected的，这里就不再追究了。这里的问题是，当线程1执行到initMonitorRules方法时，线程2执行getInstance方法，它直接返回m_This，这时候它可以用m_This做任何事情，但是此时线程1的初始化动作还没有完成，如果initMonitorRules方法里有对对象状态进行更新的操作，那么很可能线程2得到的对象的状态是还没有初始化的，这就是一个多线程的BUG（多线程的问题之所以很严重，是因为我们很难复现解决它，但它又是的确存在的，它总是在关键时候爆发，让你感到很郁闷）！
当然就算没有initMonitorRules方法，这个单例模式也不是线程安全的，下面会讲到这个问
题。
解决方法：
将initMonitorRules方法放在构造方法里，然后将单例改成恶汉模式，或者使用双重校验锁。
40. LI_LAZY_INIT_STATIC
Bug: Incorrect lazy initialization of static field TopoController.m_This Pattern id: LI_LAZY_INIT_STATIC, type: LI, category: MT_CORRECTNESS
解释：
为什么它存在多线程的bug，比如线程1进入到if语句内，被线程2打断，线程2同样进入了if语句内然后生成了一个对象a，随即被线程1打断，线程1又生成了另外一个对象b，这还是一个单例么？
更详细的解释请看：双重检查锁定以及单例模式
另外，关于单例模式更多的资料，参见单例模式的七种写法
如果你并发功底相当好，请看这篇文章：用happen-before规则重新审视DCL
解决方法：
我比较钟情于恶汉，如果需要传递参数，我会使用双重校验锁。
41. WMI_WRONG_MAP_ITERATOR
案例一：
案例二：
Bug: Method JTAMainFrame.initView(JFrame) makes inefficient use of keySet iterator instead of entrySet iterator Pattern id: WMI_WRONG_MAP_ITERATOR, type: WMI, category: PERFORMANCE
This method accesses the value of a Map entry, using a key that was retrieved from a keySet iterator. It is more efficient to use an iterator on the entrySet of the map, to avoid the Map.get(key) lookup.
解释：
很多人都这样遍历Map，没错，但是效率很低，先一个一个的把key遍历，然后在根据key去查找value，这不是多此一举么，为什么不遍历entry（桶）然后直接从entry得到value呢？它们的执行效率大概为1.5:1（有人实际测试过）。
我们看看HashMap.get方法的源代码： 1. public V get(Object key) { 2. if (key == null) 3. return getForNullKey(); 4. int hash = hash(key.hashCode()); 5. for (Entry<K,V> e = table[indexFor(hash, table.length)]; 6. e != null; 7. e = e.next) { 8. Object k; 9. if (e.hash == hash && ((k = e.key) == key || key.equals(k))) 10. return e.value; 11. } 12. return null; 13. }
从这里可以看出查找value的原理，先计算出hashcode，然后散列表里取出entry，不管是计算hashcode，还是执行循环for以及执行equals方法，都是CPU密集运算，非常耗费CPU资源，如果对一个比较大的map进行遍历，会出现CPU迅速飚高的现象，直接影响机器的响应速度，在并发的情况下，简直就是一场灾难。
解决方法： 1. for (Map.Entry<String, JMenu> entry : menuList.entrySet()) { 2. mb.add(entry.getValue());
}
for(Map.Entry<String, List<BlackListDO>> tempEntiy: companyBlackItemsMap.entrySet()) {
String key = tempEntiy.getKey();
List<BlackListDO> eachCompanyBlackItems = tempEntiy.getValue();
42. BC_VACUOUS_INSTANCEOF
Bug: instanceof will always return true, since all TopoTreeNode are instances of TopoTreeNode Pattern id: BC_VACUOUS_INSTANCEOF, type: BC, category: STYLE
解释：
因为getSelectedTreeNode方法返回类型就是TopoTreeNode，所以if里的instanceof测试永远为true，除非它是null，确保你没有其他的逻辑上的误解，你这样写，会让其他人丈二和尚摸不着头脑。
解决方法：
去掉instanceof检测。
43. INT_BAD_REM_BY_1
Bug: Integer remainder modulo 1 computed Pattern id: INT_BAD_REM_BY_1, type: INT, category: STYLE
解释：
I % 1永远都为0，I / 1也为i，不知道作者想干嘛。
解决方法：
恕我愚昧，不明白作者的意图。
Bug: Load of known null value Pattern id: NP_LOAD_OF_KNOWN_NULL_VALUE, type: NP, category: STYLE
The variable referenced at this point is known to be null due to an earlier check against null. Although this is valid, it might be a mistake (perhaps you intended to refer to a different variable, or perhaps the earlier check to see if the variable is null should have been a check to see if it was nonnull).
解释：
Node为null，还进一步调用它上面的方法，除非你能保证当node为null的时候isDeleteSingleObject为false，否则很可能发生空指针异常，我估计作者是第二个if是想判断node != null吧。
解决方法：
努力找到原作者，当面询问其用意。
44. EI_EXPOSE_REP2
案例
DO类
Bug: SingleNePollConfigDialog.collectValues(Hashtable) may expose internal representation by storing an externally mutable object into SingleNePollConfigDialog.values Pattern id: EI_EXPOSE_REP2, type: EI2, category: MALICIOUS_CODE
解释：
参数values保存在当前线程的执行栈中，而this.values保存在堆上，它们同时指向同一个对象，对参数values的任何操作都会影响到this.values，如果你知道这一点，而且本意就是这样的，那么你可以忽略上面这些话，但是下面这些话你应该好好听听。
这是一段正确的代码，但不是一段可维护性强、可理解性强的代码，参数代表操作的条件，它们应该是只读的，我们不应该对它直接进行操作或者赋值。
解决方法：
如果把上面对参数values的操作都改成this.values，我相信你和你的同事都会觉得这样的代码更加清晰。
}
案例二
DO类
Bug: SingleNePollConfigDialog.collectValues(Hashtable) may expose internal representation by storing an externally mutable object into SingleNePollConfigDialog.values Pattern id: EI_EXPOSE_REP2, type: EI2, category: MALICIOUS_CODE
This code stores a reference to an externally mutable object into the internal representation of the object. If instances are accessed by untrusted code, and unchecked changes to the mutable object would compromise security or other important properties, you will need to do something different. Storing a copy of the object is better approach in many situations.
翻译愿意：
此代码存储到一个到对象的内部表示外部可变对象的引用。如果实例是由不受信任的代码，并以可变对象会危及安全或其他重要的属性选中更改访问，你需要做不同的东西。存储一个对象的副本，在许多情况下是更好的办法。
解释：
DO类实例产生之后，里面包含的Date不是原始数据类型，导致其gmtCrate属性不光是set方法可以改变其值，外部引用修改之后也可能导致gmtCreate 被改变，会引起可能的不安全或者错误。
这个是一个不好的实践，不过我们应用里面DO都是比较简单使用，不太会出现这种情况。
解决方法：
修改成：
public Date getGmtCreate() { return new Date(this.gmtCreate.getTime()); //正确值
}
45. EI_EXPOSE_REP
Bug: temsLoader.getItemsWithPriority() may expose internal representation by returning ItemsLoader.m_htItemsWithPriority Pattern id: EI_EXPOSE_REP, type: EI, category: MALICIOUS_CODE
解释：
刚开始一看挺纳闷的，这个方法有什么问题吗？后来仔细看一下，发现返回值都有一个特点，它们都是集合数组之类的，我想findBugs的本意是，某些数据集合不应该直接对外提供public返回方法，即使表面上提供了get方法，但实际上可以任意修改里面的数据。
解决方法1：
如果你确定这些数据集合不应该被外界修改，那么对于基本数据类型，你提供get方法即可，对于引用，get方法里的返回值应该是数据的拷贝。
解决方法2：
如果是Date类型的字段出现的这个问题，可以使用重写它的Getter方法，去判断它的空值，例如：
```$xslt
public void setBillDate(Date billDate) {
    this.billDate = billDate != null ? new Date(billDate.getTime()) : null;
}
```
46. NP_NULL_PARAM_DEREF
Bug: Method call passes null for nonnull parameter of queryScriptData(ObjService) Pattern id: NP_NULL_PARAM_DEREF, type: NP, category: CORRECTNESS
解释：
当getAllListFiles方法发生了任何异常（checked和unchecked），allFiles都为null，关键是在queryScriptData方法里，并没有对参数是否为null进行判断，它直接调用了参数对象上面的方法，这肯定会发生空指针异常。
一个优秀的程序员，在过马路时都要向两边看一下，在写一个方法时，首先要考虑的就是对方法参数的有效性判断。
解决方法：
在queryScriptData方法里对参数进行有效性判断。
46. SBSC_USE_STRINGBUFFER_CONCATENATION
Bug: Method InitDBPoolParaTask.execute() concatenates strings using + in a loop Pattern id: SBSC_USE_STRINGBUFFER_CONCATENATION, type: SBSC, category: PERFORMANCE
The method seems to be building a String using concatenation in a loop. In each iteration, the String is converted to a StringBuffer/StringBuilder, appended to, and converted back to a String. This can lead to a cost quadratic in the number of iterations, as the growing string is recopied in each iteration.
Better performance can be obtained by using a StringBuffer (or StringBuilder in Java 1.5) explicitly.
解释：
每次循环里的字符串+连接，都会新产生一个string对象，在java中，新建一个对象的代价是很昂贵的，特别是在循环语句中，效率较低。
解决方法：
利用StringBuffer或者StringBuilder重用对象。
47. RV_RETURN_VALUE_IGNORED_BAD_PRACTICE
Bug: NewScriptAction.actionPerformed(ActionEvent) ignores exceptional return value of java.io.File.delete() Pattern id: RV_RETURN_VALUE_IGNORED_BAD_PRACTICE, type: RV, category: BAD_PRACTICE
解释：
关于一个方法逻辑执行是否成功，有两种方式，一种是抛出异常，一种是提供boolean类型的返回值。举一个例子，用户登录，某些人将login方法的返回值定义为int，然后枚举出各个值的含义，比如0代表成功，1代表用户名不存在等等；而有些人，把这些枚举值看成是use case中的异常流，将它们定义为异常对象，遇到“异常”情况直接抛出异常从而实现分支的流程。第一种方式是典型的C语言面向过程风格，第二种方式，带有强烈的面向对象味道，特别是java提供了checked Exception，貌似偏离主题了。
java中很多方法的执行成功依赖于异常的分支实现，但也有提供返回值的实现，比如这里的File.delete方法，上面的写法忽略了返回值（如果调用某个方法却不使用其返回值要特别注意），删除一个文件很可能不成功，但是从代码里并没有看到这一层面的意思。
解决方法：
文件删除不成功该怎么办？现在能处理就处理，现在不能处理就把父类的方法也改成有返回值的，然后向上传递，这跟处理异常的道理是一样的，当然，你也可以把它封装成一个异常对象。
48. RV_RETURN_VALUE_IGNORED
Bug: BackupFileListPanel$PopupListener.maybeShowPopup(MouseEvent) ignores return value of String.trim() Pattern id: RV_RETURN_VALUE_IGNORED, type: RV, category: CORRECTNESS
解释：
String是一个不可变类，调用String上的任何操作都会返回新的String对象，虽然String是一个class，但实际上对它的任何操作都可以把它看成基本数据类型，比如s.trim方法是不会改变s值的。
解决方法：
S = s.trim
49. DM_BOOLEAN_CTOR
Bug: TopoCardManagerAction.processLocalCard(Hashtable) invokes inefficient Boolean constructor; use Boolean.valueOf(...) instead Pattern id: DM_BOOLEAN_CTOR, type: Dm, category: PERFORMANCE
解释：
不必创建一个新的Boolean对象，使用Boolean.valueOf方法可以重用Boolean.FALSE和Boolean.TRUE对象。
我们可以从API中可以看到public Boolean(boolean value)方法的解释：注：一般情况下都不宜使用该构造方法。若不需要新 的实例，则静态工厂 valueOf(boolean) 通常是一个更好的选择。这有可能显著提高空间和时间性能。
解决方法：
使用Boolean.valueOf方法或者直接返回Boolean.FALSE和Boolean.TRUE对象。
50. RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE
Pattern id: RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE,
解释：
StringBuffer连接更有効率，因此，建议使用StringBuffer
51. DM_NUMBER_CTOR
new Integer(int) 和 Integer.valueOf(int)
bug描述：
[Bx] Method invokes inefficient Number constructor; use static valueOf instead [DM_NUMBER_CTOR]
Using new Integer(int) is guaranteed to always result in a new object whereas Integer.valueOf(int) allows caching of values to be done by the compiler, class library, or JVM. Using of cached values avoids object allocation and the code will be faster.
说明:
[参考]http://www.cnblogs.com/hyddd/articles/1391318.html
FindBugs推荐使用Integer.ValueOf(int)代替new Integer(int)，因为这样可以提高性能。如果当你的int值介于-128～127时，Integer.ValueOf(int)的效率比Integer(int)快大约3.5倍。下面看看JDK的源码，看看到Integer.ValueOf(int)里面做了什么优化：public static Integer valueOf(int paramInt) {	assert (IntegerCache.high >= 127);	if ((paramInt >= -128) && (paramInt <= IntegerCache.high)) {		return IntegerCache.cache[(paramInt + 128)];	}	return new Integer(paramInt);}从源代码可以知道，ValueOf对-128～127这256个值做了缓存(IntegerCache)，如果int值的范围是：-128～127，在ValueOf(int)时，他会直接返回IntegerCache的缓存给你。所以你会看到这样的一个现象：public static void main(String []args) {	　　Integer a = 100;	　　Integer b = 100;	　　System.out.println(a==b);	　　Integer c = new Integer(100);	　　Integer d = new Integer(100);	　　System.out.println(c==d);}结果是：truefalse因为：java在编译的时候Integer a = 100;被翻译成：Integer a = Integer.valueOf(100);所以a和b得到都是一个Cache对象，并且是同一个！而c和d是新创建的两个不同的对象，所以c自然不等于d。再看看这段代码：public static void main(String args[]) throws Exception{	　　Integer a = 100;	　　Integer b = a;	　　a = a + 1; //或者a++;	　　System.out.println(a==b);}结果是：false因为在对a操作时(a=a+1或者a++)，a重新创建了一个对象，而b对应的还是缓存里的100，所以输出的结果为false。
52. INT_VACUOUS_BIT_OPERATION

bug描述
在整形进行位运算时，出现的错误，这是因为整形出现了溢出，最大值为0x7fffffff，例如：
```$xslt
int inc = inc & 0xFFFFFFFF;
```
解决方法
```$xslt
int inc = inc & 0x7FFFFFFF;
```