// 一些全局的config配置
const modeUrlObj = {
    // 生产环境
    'production': {
        baseURL: 'http://10.113.0.20:5080/',
        authBaseURL: 'http://10.113.0.45:9001/auth/realms/tianjin/account/password'
    },
    // 开发环境
    'development': {
        baseURL: 'http://8.140.114.226:8085/',
        authBaseURL: 'http://8.140.114.226:9001/auth/realms/tianjin/account/password'
    },
    // 测试环境
    'test': {
        baseURL: 'http://8.140.114.226:8085/',
        authBaseURL: 'http://8.140.114.226:9001/auth/realms/tianjin/account/password'
    }
}
export default modeUrlObj[process.env.NODE_ENV]