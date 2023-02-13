const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  rebackdeptid: state => state.app.rebackdeptid,
  infoReBackDeptId: state => state.app.infoReBackDeptId,
  OSReBackDeptId: state => state.app.OSReBackDeptId,
  breadcrumb: state => state.app.breadcrumb,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  portalUrl: state => state.user.portalUrl,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  status: state => state.user.status,
  roles: state => state.user.roles,
  userid: state => state.user.userid,
  propertyId: state => state.user.propertyId,
  perms: state => state.user.perms,
  setting: state => state.user.setting,
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
