# 装饰器设计模式
* 被增强类、增强类实现同一个接口
* 增强类持有被增强类的引用
* 被增强的方法调用增强类的方法，其他方法保持原有的继续使用被增强类的旧方法
```java
public abstract class Drink {
  private String name;
  private double price;
}
public class Coffee extends Drink {
  public Coffee() {
    super.setName("咖啡");
    super.setPrice(7);
  }

  /**
   * 计算价格.
   *
   * @return
   */
  @Override
  public double cost() {
    return this.getPrice();
  }
}
/**
 * 饮料装饰器基类,所有装饰饮料的decorate都需要继承它.
 */
public abstract class AbstractDrinkDecorate extends Drink {
}
public class MilkAbstractDrinkDecorate extends AbstractDrinkDecorate {
  public MilkAbstractDrinkDecorate(Drink drink) {
    super(drink);
    super.setName("牛奶");
    super.setPrice(5);
  }
}
public class Test{
 @Test
 public void drink() {
        Drink coffee = new Coffee();
        AbstractDrinkDecorate milk = new MilkAbstractDrinkDecorate(coffee);
 }
}

```