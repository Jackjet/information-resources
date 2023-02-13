import Cookies from 'js-cookie'

const theme = {
  state: {
    themeColor: Cookies.get('theme') || 'blackGreen'
  },
  mutations: {
    setThemeColor (state, curcolor) {
      this.state.themeColor = curcolor
    }
  }
}
export default theme
