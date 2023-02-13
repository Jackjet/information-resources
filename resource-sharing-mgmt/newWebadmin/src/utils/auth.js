import Cookies from 'js-cookie'

export function getCookies(key) {
    return Cookies.get(key)
}

export function setCookies(key, value, expiresTime) {
    let day = expiresTime
    let expires = new Date(new Date() * 1 + 24 * 60 * 60 * 1000 * day)
    return Cookies.set(key, value, { expires: expires })
}

export function removeCookies(key) {
    return Cookies.remove(key)
}
