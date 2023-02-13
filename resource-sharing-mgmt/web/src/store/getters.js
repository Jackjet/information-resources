const getters = {
    name: state => state.user.name,
    token: state => state.user.token,
    userId: state => state.user.id,
    orgId: state => state.user.orgId,
}
export default getters
