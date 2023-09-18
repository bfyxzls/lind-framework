// 快速排序
export function quickSort(arr) {
  if (arr.length < 2) {
    return arr;
  }

  let pivot = arr[0];
  let left = [];
  let right = [];

  for (let i = 1; i < arr.length; i++) {
    if (arr[i] < pivot) {
      left.push(arr[i]);
    } else {
      right.push(arr[i]);
    }
  }

  return [...quickSort(left), pivot, ...quickSort(right)];
}

// 高阶函数
export function higherOrderFunction(func) {
  return function (num) {
    return func(num);
  };
}

export function double(num) {
  return num * 2;
}

//const doubleFunc = higherOrderFunction(double);
//console.log(doubleFunc(10)); // 20

// 柯里化
export function curry(func) {
  return function curried(...args) {
    if (args.length >= func.length) {
      return func.apply(this, args);
    } else {
      return function (...args2) {
        return curried.apply(this, [...args, ...args2]);
      };
    }
  };
}

export function sum(a, b, c) {
  return a + b + c;
}

//对象的遍历
this.$set(this.dic, "id", "1");
this.$set(this.dic, "name", "lind");
this.$set(this.dic, "gender", "男");
this.$set(this.dic, "info", "一个简单的开发人员");

const keys = Object.keys(this.dic);
for (const key of keys) {
  const value = this.dic[key];
  console.log(`${key}: ${value}`);
}

// Map和Set,Map数据结构通常用于存储键值对，它可以使用任意类型作为键和值。Set数据结构用于存储唯一值的集合。
// 创建Map对象
const map = new Map();
// 设置键值对
map.set('name', 'Tom');
map.set('age', 20);
// 获取键值对
console.log(map.get('name')); // 'Tom'
console.log(map.get('age')); // 20

// 创建Set对象
const set = new Set();
// 添加元素
set.add(10);
set.add(20);
set.add(30);
// 删除元素
set.delete(20);
// 判断元素是否存在
console.log(set.has(10)); // true
console.log(set.has(20)); // false

// 堆和栈是常用的内存分配方式。栈是一种后进先出的数据结构，堆是一种动态分配的内存结构。队列是一种先进先出的数据结构，它通常用于缓存和并发编程中。
// 使用数组模拟堆
const arr = [1, 2, 3, 4];
arr.push(5); // 入堆
console.log(arr.pop()); // 出堆

// 使用数组模拟栈
const stack = [1, 2, 3, 4];
stack.push(5); // 入栈
console.log(stack.pop()); // 出栈

// 使用数组模拟队列
const queue = [1, 2, 3, 4];
queue.push(5); // 入队
console.log(queue.shift()); // 出队

let arrSort = quickSort([1, 3, 2, 5, 4]);
console.log(arrSort);

const curriedSum = curry(sum);
console.log(curriedSum(1)(2)(3)); // 6

// 函数式编程
// map
const arr1 = [1, 2, 3];
const mapArr = arr1.map((item) => item * 2);
console.log(mapArr); // [2, 4, 6]

// filter
const filterArr = arr1.filter((item) => item > 1);
console.log(filterArr); // [2, 3]

// reduce
const reduceArr = arr1.reduce((sum, curr) => sum + curr, 0);
console.log(reduceArr); // 6
