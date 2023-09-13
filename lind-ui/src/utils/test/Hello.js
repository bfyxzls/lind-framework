import User from './User'

// 定义方法，使用类型为User
export function sayHello() {
  const user = new User(1, 'Tom');
  console.log('Hello' + user.name);
}
