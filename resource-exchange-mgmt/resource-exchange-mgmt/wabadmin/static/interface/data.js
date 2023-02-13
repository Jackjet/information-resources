// const PortUrl = './'
// const PortUrl = 'http://8.140.127.114:8072/'
const PortUrl = 'http://47.105.96.207:8080'

const ApiDocUrl = 'http://47.93.42.226/portalservice/'
// const ApiDocUrl = 'http://47.105.96.207/portalservice/'
const keycloakAuth = 'http://8.140.114.226:9001/auth/'
const sso = { realm: 'tianjin', clientId: 'resource_exchange_management' }

//windows
// const FileSeparator="\\"

//linux
const FileSeparator = '/'

const constApi = {
  // 组件接口
  componentFindAll: 'http://47.105.96.207/portalservice/web/component/center/findAll',
  /**
   * @login 用户登录
   * @PutPassWord  修改用户密码
   */
  UserLogin: {
    auth: keycloakAuth,
    login: PortUrl + '/webadmin/webAdminUser/signIn',
    PutAdminPwd: PortUrl + '/webadmin/webAdminUser/webadminUpdate',
    PutTenantsPwd: PortUrl + '/webadmin/webAdminUser/tenantsUserUpdate',
    PutIntegPwd: PortUrl + '/webadmin/webAdminUser/integrationUserUpdate',
    logOut: keycloakAuth + 'realms/' + sso.realm + '/protocol/openid-connect/logout?redirect_uri=' + PortUrl + '/main.html',
    signOut: PortUrl + '/webadmin/webAdminUser/signOut'
  },
  /**
   *  管理员系统接口
   */
  // 系统服务模板管理
  SysPluginInstallPack: {
    findAll: PortUrl + 'webadmin/sysPluginInstallPack/findAll',
    insert: PortUrl + 'webadmin/sysPluginInstallPack/insert',
    update: PortUrl + 'webadmin/sysPluginInstallPack/update',
    upload: PortUrl + 'webadmin/sysPluginInstallPack/upload'
  },
  // 其他服务配置
  managementConfig: {
    findAll: PortUrl + 'webadmin/managementConfig/findAll',
    update: PortUrl + 'webadmin/managementConfig/update'
  },
  // 租户列表
  tenants: {
    findAll: PortUrl + 'webadmin/tenants/findAll',
    findById: PortUrl + 'webadmin/tenants/findById',
    insert: PortUrl + 'webadmin/tenants/insert',
    update: PortUrl + 'webadmin/tenants/update',
    resetPassword: PortUrl + 'webadmin/tenants/resetPassword'
  },
  /**
   *  租户系统接口
   */
  // 集成项目列表
  integrationProjectManage: {
    findAll: PortUrl + 'tenants/integrationProjectManage/findAll',
    findById: PortUrl + 'tenants/integrationProjectManage/findById',
    insert: PortUrl + 'tenants/integrationProjectManage/insert',
    update: PortUrl + 'tenants/integrationProjectManage/update',
    resetPassword: PortUrl + 'tenants/integrationProjectManage/resetPassword'
  },
  /**
   *  项目管理系统接口
   */
  SysServiceInstallPack: {
    findAll: PortUrl + 'integration/sysServiceInstallPack/findAll',
    findAllVersion: PortUrl + 'integration/sysServiceInstallPack/findAllVersion',
    insert: PortUrl + 'integration/sysServiceInstallPack/insert',
    update: PortUrl + 'integration/sysServiceInstallPack/update',
    upload: PortUrl + 'integration/sysServiceInstallPack/upload'
  },
  // 项目系统请求节点
  runNode: {
    isOnline: PortUrl + 'integration/runNode/isOnline',
    findAll: PortUrl + 'integration/runNode/findAll',
    findById: PortUrl + 'integration/runNode/getById',
    delete: PortUrl + 'integration/runNode/deleteById',
    insert: PortUrl + 'integration/runNode/insert',
    update: PortUrl + 'integration/runNode/update'
  },
  // 项目系统 系统服务列表
  sysService: {
    find: PortUrl + 'integration/sysService/findAll',
    findById: PortUrl + 'integration/sysService/findById',
    findST: PortUrl + 'integration/sysService/findAllServiceTemplate',
    delete: PortUrl + 'integration/sysService/deleteById',
    insert: PortUrl + 'integration/sysService/insert',
    update: PortUrl + 'integration/sysService/update',
    reboot: PortUrl + 'integration/sysService/reboot',
    upgradeConfig: PortUrl + 'integration/sysService/upgradeConfig',
    upgradeScript: PortUrl + 'integration/sysService/upgradeScript',
    isOnline: PortUrl + 'integration/sysService/isOnline',
    install: PortUrl + 'integration/sysService/install',
    isInstalled: PortUrl + 'integration/sysService/isInstalled',
    info: PortUrl + 'integration/sysService/versionAndStatus'
  },
  // 项目系统 操作记录列表
  operation: {
    find: PortUrl + 'integration/sysService/operation/findAllByAppId',
    findActive: PortUrl + 'integration/sysService/operation/operationType'
  },
  // Topic信息
  topics: {
    findAll: PortUrl + 'integration/topics/findAll',
    deleteById: PortUrl + 'integration/topics/deleteById',
    insert: PortUrl + 'integration/topics/insert',
    syncTopicPermission: PortUrl + 'integration/topicConfig/syncTopicPermission'
  },
  // Topic配置信息
  topicConfig: {
    findAll: PortUrl + 'integration/topicConfig/findAll',
    insert: PortUrl + 'integration/topicConfig/insert',
    deleteById: PortUrl + 'integration/topicConfig/deleteById'
  },
  // emq开发者管理
  emqUser: {
    findAll: PortUrl + 'integration/emqUser/findAll',
    deleteById: PortUrl + 'integration/emqUser/deleteById',
    insert: PortUrl + 'integration/emqUser/insert',
    syncEmqUser: PortUrl + 'integration/emqUser/syncEmqUser'
  },
  // Emq 管理
  emq: {
    install: PortUrl + 'integration/emq/install',
    start: PortUrl + 'integration/emq/start',
    stop: PortUrl + 'integration/emq/stop',
    config: {
      find: PortUrl + 'integration/emq/config/findAll',
      update: PortUrl + 'integration/emq/config/update',
      sync: PortUrl + 'integration/emq/config/sync'
    }
  },
  // api 管理
  api: {
    install: PortUrl + 'integration/api/install',
    start: PortUrl + 'integration/api/start',
    stop: PortUrl + 'integration/api/stop',
    config: {
      find: PortUrl + 'integration/api/config/findAll',
      update: PortUrl + 'integration/api/config/update',
      sync: PortUrl + 'integration/api/config/sync'
    }
  },
  // 脚本运行服务 管理
  webapi: {
    start: PortUrl + 'integration/base/start',
    stop: PortUrl + 'integration/base/stop'
  },
  // webapi打包
  cmdBuild: {
    find: PortUrl + 'integration/build/webapiAndCmd/findAll',
    delete: PortUrl + 'integration/build/webapiAndCmd/deleteById',
    insert: PortUrl + 'integration/build/webapiAndCmd/insert',
    update: PortUrl + 'integration/build/webapiAndCmd/updateById',
    upgradeApp: PortUrl + 'integration/build/webapiAndCmd/upgradeApp',
    failedInfo: PortUrl + 'integration/build/webapiAndCmd/failedInfo',
    version: PortUrl + 'integration/build/webapiAndCmd/version'
  },
  // 官方api打包
  apiBuild: {
    find: PortUrl + 'integration/dcApiGateway/findAll',
    delete: PortUrl + 'integration/dcApiGateway/deleteById',
    insert: PortUrl + 'integration/dcApiGateway/insert',
    update: PortUrl + 'integration/dcApiGateway/updateById'
  },
  // 项目系统 服务配置
  sysServiceConfig: {
    find: PortUrl + 'integration/config/findAll',
    update: PortUrl + 'integration/config/update',
    version: PortUrl + 'integration/config/version',
    upgradeConfig: PortUrl + 'integration/config/upgrade'
  },
  // 项目系统 自定义配置列表
  customSysServiceConfig: {
    find: PortUrl + 'integration/custom/config/findAll',
    version: PortUrl + 'integration/config/version',
    update: PortUrl + 'integration/custom/config/update',
    upgradeConfig: PortUrl + 'integration/config/upgrade'
  },
  // 开发
  scriptFile: {
    version: PortUrl + 'integration/script/version',
    find: PortUrl + 'integration/script/file/find',
    findDebug: PortUrl + 'integration/script/findDebug',
    new: PortUrl + 'integration/script/file/new',
    updateDebug: PortUrl + 'integration/script/updateDebug',
    uploadScript: PortUrl + 'integration/script/uploadScript',
    delete: PortUrl + 'integration/script/file/delete',
    rename: PortUrl + 'integration/script/rename',
    read: PortUrl + 'integration/script/read',
    save: PortUrl + 'integration/script/save',
    wsInfo: PortUrl + 'integration/script/wsInfo',
    findByAppid: PortUrl + 'integration/script/findByAppid',
    getComponentsHints: PortUrl + 'integration/script/getComponentsHints'
  },
  // 项目系统 组件列表
  components: {
    findAllByAppid: PortUrl + 'integration/components/findAllByAppid',
    findAll: PortUrl + 'integration/components/findAll',
    delete: PortUrl + 'integration/components/delete',
    insert: PortUrl + 'integration/components/insert',
    custom: PortUrl + 'integration/components/custom'
  },
  // api开发者管理
  apiRouteUser: {
    syncApiRouteUser: PortUrl + 'integration/apiRouteUser/syncApiRouteUser',
    find: PortUrl + 'integration/apiRouteUser/findAll',
    delete: PortUrl + 'integration/apiRouteUser/deleteById',
    insert: PortUrl + 'integration/apiRouteUser/insert'
  },
  // 路由管理
  apiRoute: {
    syncRoutes: PortUrl + 'integration/apiRoute/syncRoutes',
    find: PortUrl + 'integration/apiRoute/findAll',
    findById: PortUrl + 'integration/apiRoute/find',
    insert: PortUrl + 'integration/apiRoute/insert',
    update: PortUrl + 'integration/apiRoute/update',
    delete: PortUrl + 'integration/apiRoute/deleteById'
  },
  // 访问控制
  accessStrategy: {
    find: PortUrl + 'integration/accessStrategy/findAll',
    delete: PortUrl + 'integration/accessStrategy/deleteById',
    insert: PortUrl + 'integration/accessStrategy/insert',
    update: PortUrl + 'integration/accessStrategy/update',
    findById: PortUrl + 'integration/accessStrategy/findById'
  },
  // 流量控制
  flowStrategy: {
    find: PortUrl + 'integration/flowStrategy/findAll',
    delete: PortUrl + 'integration/flowStrategy/deleteById',
    insert: PortUrl + 'integration/flowStrategy/insert',
    update: PortUrl + 'integration/flowStrategy/update',
    findById: PortUrl + 'integration/flowStrategy/findById'
  },
  // 系统服务开发者
  sysUser: {
    findAll: PortUrl + 'integration/sysUser/findAll',
    findById: PortUrl + 'integration/sysUser/findById',
    insert: PortUrl + 'integration/sysUser/insert',
    update: PortUrl + 'integration/sysUser/update',
    deleteById: PortUrl + 'integration/sysUser/deleteById'
  },
  // key定义
  keys: {
    findAll: PortUrl + 'integration/keys/findAll',
    findById: PortUrl + 'integration/keys/findById',
    insert: PortUrl + 'integration/keys/insert',
    update: PortUrl + 'integration/keys/update',
    delete: PortUrl + 'integration/keys/delete',
    sendGet: PortUrl + 'integration/keys/debug/sendGet',
    sendSet: PortUrl + 'integration/keys/debug/sendSet'
  },
  // key配置信息
  keyConfig: {
    findAll: PortUrl + 'integration/key/config/findAll',
    insert: PortUrl + 'integration/key/config/insert',
    update: PortUrl + 'integration/key/config/update',
    delete: PortUrl + 'integration/key/config/delete',
    syncKeys: PortUrl + 'integration/key/config/syncKeys'
  },
  datacacheConfig: {
    findAll: PortUrl + 'integration/datacache/config/findAll',
    update: PortUrl + 'integration/datacache/config/update'
  },
  dataCache: {
    install: PortUrl + 'integration/data/cache/install',
    start: PortUrl + 'integration/data/cache/start',
    stop: PortUrl + 'integration/data/cache/stop'
  },
  dataResource: {
    find: PortUrl + '/webadmin/dataSourceInfo/findAll',
    findById: PortUrl + '/webadmin/dataSourceInfo/findById',
    insert: PortUrl + '/webadmin/dataSourceInfo/insert',
    update: PortUrl + '/webadmin/dataSourceInfo/update',
    delete: PortUrl + '/webadmin/dataSourceInfo/deleteById',
    toLink: PortUrl + '/webadmin/dataSourceInfo/testConnection',
    subToLink: PortUrl + '/webadmin/dataSourceInfo/testConnectionButton',
  },
  container: {
    find: PortUrl + '/webadmin/containerInfo/findAll',
    findById: PortUrl + '/webadmin/containerInfo/findById',
    insert: PortUrl + '/webadmin/containerInfo/insert',
    update: PortUrl + '/webadmin/containerInfo/update',
    delete: PortUrl + '/webadmin/containerInfo/deleteById',
    toLink: PortUrl + '/webadmin/containerInfo/testConnection',
    subToLink: PortUrl + '/webadmin/containerInfo/testController',
  },
  groupInfo: {
    getList: PortUrl + '/webadmin/groupInfo/getGroupList',
    getTree: PortUrl + '/webadmin/groupInfo/getTree',
    insert: PortUrl + '/webadmin/groupInfo/insert',
    update: PortUrl + '/webadmin/groupInfo/update',
    delete: PortUrl + '/webadmin/groupInfo/delete',
  },
  analysis: {
    getDay: PortUrl + '/webadmin/task/analysis/findAnalysisByDay',
    getMonth: PortUrl + '/webadmin/task/analysis/findAnalysisByMonth',
    getTaskLogs: PortUrl + '/webadmin/task/logs/findAnalysisInfo'
  },
  group: {
    getTree: PortUrl + '/webadmin/task/group/getTree',
    insert: PortUrl + '/webadmin/task/group/insert',
    update: PortUrl + '/webadmin/task/group/update',
    delete: PortUrl + '/webadmin/task/group/deleteById',
  },
  task: {
    findLogs: PortUrl + '/webadmin/task/logs/findAll',
    findLogInfo: PortUrl + '/webadmin/task/logs/findInfo',
    find: PortUrl + '/webadmin/task/findAll',
    insert: PortUrl + '/webadmin/task/insert',
    update: PortUrl + '/webadmin/task/update',
    delete: PortUrl + '/webadmin/task/deleteById',
    run: PortUrl + '/webadmin/task/run',
    stop: PortUrl + '/webadmin/task/stop',
    getStatus: PortUrl + '/webadmin/task/getStatus'
  },
  kettleFiles: {
    upload: PortUrl + '/webadmin/task/kettleFiles/uploadFile',
    download: PortUrl + '/webadmin/task/kettleFiles/downloadFile',
    delete: PortUrl + '/webadmin/task/kettleFiles/deleteFile',
    findById: PortUrl + '/webadmin/task/kettleFiles/findByTaskId',
    findByType: PortUrl + '/webadmin/task/kettleFiles/findByTaskIdAndType',
    findByName: PortUrl + '/webadmin/task/kettleFiles/findByTaskIdAndName',
  },
  backups: {
    find: PortUrl + '/integration/backup/findAll',
    delete: PortUrl + '/integration/backup/deleteById',
    insert: PortUrl + 'integration/sysService/codeBackup',
  },
  logs: {
    operationLogfindAll: PortUrl + '/webadmin/log/operationLog/findAll',
    operationLogfind: PortUrl + '/webadmin/log/operationLog/find',
    download: PortUrl + '/webadmin/log/operationLog/export',
  },
  apiDoc: ApiDocUrl + 'web/page/api/index.html'
}

export default {
  constApi,
  PortUrl,
  FileSeparator,
  sso
}
