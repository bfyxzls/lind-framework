# 说明
正确情况下，我们购买一下商品时，找到最佳的折扣方案就行了，不能让买家一次享受多个折扣的累加

# 算法说明
handler只使用一个
* 按最高优先级去使用，符合就用，不符合就走下一个策略
* 具体链条
  * VipHandler >10000
    * BigGiftHandler >1000
      * DiscountHandler >200
        * CouponHandler >100
handler可能会有多个，但根据条件处理多个
  *  VipHandler >10000
    * BigGiftHandler >1000
      * DiscountHandler >200
        * CouponHandler >100
    * DiscountHandler >200
      * CouponHandler >100
    * CouponHandler >100
