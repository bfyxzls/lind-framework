import Cookies from "js-cookie";

const jsCookie = {
  set(key, value) {
    Cookies.set(key, value)
  },
  get(key) {
    return Cookies.get(key);
  },
  remove(key) {
    Cookies.remove(key);
  }
}
export default jsCookie;
