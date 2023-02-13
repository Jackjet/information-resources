const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  id: state => state.user.id,
  avatar: state => state.user.avatar,
  name: state => state.user.name
}
export default getters
