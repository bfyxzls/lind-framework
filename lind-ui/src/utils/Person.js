import modal from '../plugins/modal'

export default class Person {

  constructor(options) {
    this.id = options.id
    this.name = options.name
  }

  print() {
    modal.alert("操作成功");
  }
}
