const state = {
  count: 10
}
const mutations = {
  CHANGE_SETTING: (state, {key, value}) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  // 修改布局设置
  changeSetting({commit}, data) {
    commit('CHANGE_SETTING', data)//commit是一个函数，第一个参数是mutations的方法名，第二个参数是传递的参数
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}
